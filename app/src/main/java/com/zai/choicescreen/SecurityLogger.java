package com.zai.choicescreen;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SecurityLogger {

    private FirebaseFirestore db;

    public SecurityLogger() {
        db = FirebaseFirestore.getInstance();
    }

    public void logSecurityEvent(String event, String actionTaken) {
        Map<String, Object> log = new HashMap<>();
        log.put("event", event);
        log.put("timestamp", new Date());
        log.put("actionTaken", actionTaken);

        db.collection("securityLogs").add(log);
    }
}
