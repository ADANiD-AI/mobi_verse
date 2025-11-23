package com.zai.choicescreen;

import android.content.Context;

public class NotesSystem {

    private DatabaseManager databaseManager;

    public NotesSystem(Context context) {
        databaseManager = new DatabaseManager();
    }

    public void syncNotes(NotesAdapter adapter) {
        databaseManager.syncNotes(adapter);
    }
}
