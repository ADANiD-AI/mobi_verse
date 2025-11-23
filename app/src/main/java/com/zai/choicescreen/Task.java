package com.zai.choicescreen;

import java.util.Date;

public class Task {
    public String userId;
    public String title;
    public String description;
    public Date dueDate;
    public String priority;
    public String status;
    public String category;
    public String source;

    public Task() {
        // Default constructor required for calls to DataSnapshot.getValue(Task.class)
    }
}
