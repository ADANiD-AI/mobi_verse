// app/api/ai/paint/route.ts (Next 14+ server route)
import { NextResponse } from "next/server";
import admin from "@/lib/firebaseAdmin";

// A mock function to simulate enqueuing a job for a background worker
const enqueuePaintJob = async (job: { projectId: string; prompt: string; style?: string; uid: string; }) => {
  console.log("✅ Job enqueued for project:", job.projectId);
  // In a real application, this would add the job to a queue system like Cloud Tasks or Pub/Sub.
  // The worker would then pick up this job, call the AI provider, and update the Firestore document.
  // For this simulation, we'll just log it.
  return Promise.resolve();
};

export async function POST(req: Request) {
  try {
    const idToken = req.headers.get("authorization")?.split("Bearer ")[1];
    if (!idToken) {
      return NextResponse.json({ error: "No authorization token provided." }, { status: 401 });
    }
    const decodedToken = await admin.auth().verifyIdToken(idToken);
    const uid = decodedToken.uid;

    const body = await req.json();
    const { prompt, style } = body;
    if (!prompt) {
      return NextResponse.json({ error: "Prompt is a required field." }, { status: 400 });
    }

    // Optional: Add moderation here
    // const mod = await moderatePrompt(prompt); if (mod.blocked) return NextResponse.json({ error: "Prompt blocked by moderation." }, { status: 403 });

    const cost = 5; // Cost for one image generation
    const db = admin.firestore();
    const projRef = db.collection("ai_projects").doc();

    // Atomic transaction to reserve credits and create the project
    await db.runTransaction(async (tx) => {
      const userRef = db.collection("users").doc(uid);
      const userSnap = await tx.get(userRef);

      if (!userSnap.exists) {
        throw new Error("User not found.");
      }

      const user = userSnap.data()!;
      const available = (user.total_ai_credits || 0) - (user.credits_used || 0);
      if (available < cost) {
        throw new Error("Insufficient credits.");
      }

      // 1. Create the project document with a 'queued' status
      tx.set(projRef, {
        user_id: uid,
        title: prompt.slice(0, 80),
        tool_used: "ADANID-Paint",
        prompt_text: prompt,
        status: "queued",
        credits_reserved: cost, // Reserve credits
        created_at: admin.firestore.FieldValue.serverTimestamp(),
        updated_at: admin.firestore.FieldValue.serverTimestamp(),
      });

      // 2. Atomically increment the credits_used on the user document
      tx.update(userRef, { 
        credits_used: admin.firestore.FieldValue.increment(cost),
        updated_at: admin.firestore.FieldValue.serverTimestamp(),
      });

      // 3. Create a ledger entry for the transaction
      const txRef = db.collection("credits_transactions").doc();
      tx.set(txRef, {
        user_id: uid,
        change: -cost,
        reason: `ADANID-Paint (reserved for project ${projRef.id})`,
        created_at: admin.firestore.FieldValue.serverTimestamp(),
      });
    });

    // If transaction is successful, enqueue the background job
    await enqueuePaintJob({ projectId: projRef.id, prompt, style, uid });

    // Return the project ID to the client immediately
    return NextResponse.json({ success: true, projectId: projRef.id });

  } catch (err: any) {
    console.error("API Error:", err);
    return NextResponse.json({ error: "server_error", detail: err.message }, { status: 500 });
  }
}
