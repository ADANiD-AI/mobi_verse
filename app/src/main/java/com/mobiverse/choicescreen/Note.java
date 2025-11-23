package com.zai.choicescreen;

import java.util.List;

public class Note {
    public String userId;
    public String content;
    public String type;
    public List<String> mediaUrls;
    public List<String> tags;
    public boolean isEncrypted;
    public String folder;

    public Note() {
        // Default constructor required for calls to DataSnapshot.getValue(Note.class)
    }
}
