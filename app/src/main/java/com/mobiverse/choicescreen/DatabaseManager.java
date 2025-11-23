package com.zai.choicescreen;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private FirebaseFirestore db;
    private String userId;

    public DatabaseManager() {
        db = FirebaseFirestore.getInstance();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
    }

    // Save Launcher Settings
    public void saveLauncherSettings(LauncherSettings settings) {
        if (userId == null) return;
        db.collection("launcherSettings").document(userId)
            .set(settings)
            .addOnSuccessListener(aVoid -> Log.d("DB", "Settings saved"))
            .addOnFailureListener(e -> Log.e("DB", "Error saving settings", e));
    }

    // Add a new AI-generated Task
    public void addTask(Task task) {
        if (userId == null) return;
        task.userId = userId;
        db.collection("tasks")
            .add(task)
            .addOnSuccessListener(documentReference -> Log.d("DB", "Task added with ID: " + documentReference.getId()));
    }

    // Real-time Sync Listener for Notes
    public void syncNotes(NotesAdapter adapter) {
        if (userId == null) return;
        db.collection("notes")
            .whereEqualTo("userId", userId)
            .addSnapshotListener((snapshots, e) -> {
                if (e != null) {
                    Log.w("DB", "Listen failed.", e);
                    return;
                }

                List<Note> notesList = new ArrayList<>();
                for (QueryDocumentSnapshot doc : snapshots) {
                    Note note = doc.toObject(Note.class);
                    notesList.add(note);
                }
                adapter.updateData(notesList); // Update UI
            });
    }
}

interface NotesAdapter {
    void updateData(List<Note> notesList);
}
