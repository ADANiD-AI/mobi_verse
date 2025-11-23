package com.zai.choicescreen;

import android.content.Context;

public class TaskManager {

    private DatabaseManager databaseManager;

    public TaskManager(Context context) {
        databaseManager = new DatabaseManager();
    }

    public void createTask(Task task) {
        databaseManager.addTask(task);
    }
}
