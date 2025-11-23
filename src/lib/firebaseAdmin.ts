import admin from "firebase-admin";

if (!admin.apps.length) {
  const svc = process.env.FIREBASE_SERVICE_ACCOUNT_BASE64!;
  const parsed = JSON.parse(Buffer.from(svc, "base64").toString());
  admin.initializeApp({
    credential: admin.credential.cert(parsed),
    storageBucket: process.env.FIREBASE_STORAGE_BUCKET
  });
}
export default admin;
