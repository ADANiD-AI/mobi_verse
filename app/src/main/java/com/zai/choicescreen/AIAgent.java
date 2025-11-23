package com.zai.choicescreen;

import android.content.Context;

import java.util.Date;

public class AIAgent {

    private Context context;
    private TaskManager taskManager;

    public AIAgent(Context context) {
        this.context = context;
        this.taskManager = new TaskManager(context);
    }

    public void generateAndAddTask(String prompt) {
        // In a real app, this would use a generative AI model
        Task task = new Task();
        task.title = "AI Generated Task";
        task.description = prompt;
        task.dueDate = new Date();
        task.priority = "High";
        task.status = "New";
        task.category = "AI";
        task.source = "AIAgent";

        taskManager.createTask(task);
    }
}
