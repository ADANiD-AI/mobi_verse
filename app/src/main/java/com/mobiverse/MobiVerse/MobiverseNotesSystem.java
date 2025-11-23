package com.zai.mobiverse;

import java.util.ArrayList;
import java.util.List;

public class NotesSystem {
    private List<String> notes;

    public NotesSystem() {
        this.notes = new ArrayList<>();
    }

    public void addNote(String note) {
        this.notes.add(note);
    }

    public List<String> getNotes() {
        return notes;
    }
}
