package com.mobiverse.launcher.backup;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.mobiverse.launcher.utils.FirebaseUtils;

import java.util.HashMap;
import java.util.Map;

public class BackupManager {

    private static final String TAG = "BackupManager";
    private static final String COLLECTION_BACKUPS = "launcher_backups";
    private static final String FIELD_LAYOUT_CONFIG = "layout_config";
    private static final String FIELD_WIDGET_CONFIG = "widget_config";
    private static final String FIELD_GESTURE_CONFIG = "gesture_config";

    private final FirebaseFirestore db;
    private final Gson gson;
    private final Context context;

    public BackupManager(Context context) {
        this.context = context.getApplicationContext();
        this.db = FirebaseFirestore.getInstance();
        this.gson = new Gson();
    }

    public void backupSettings(final BackupCallback callback) {
        String userId = FirebaseUtils.getUserId();
        if (userId == null) {
            callback.onFailure("User not logged in");
            return;
        }

        // Gather all settings into a Map
        Map<String, String> settings = new HashMap<>();
        settings.put(FIELD_LAYOUT_CONFIG, getLayoutConfigJson());
        settings.put(FIELD_WIDGET_CONFIG, getWidgetConfigJson());
        settings.put(FIELD_GESTURE_CONFIG, getGestureConfigJson());

        db.collection(COLLECTION_BACKUPS).document(userId)
                .set(settings)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess("Settings backed up successfully");
                    } else {
                        callback.onFailure(task.getException().getMessage());
                    }
                });
    }

    public void restoreSettings(final RestoreCallback callback) {
        String userId = FirebaseUtils.getUserId();
        if (userId == null) {
            callback.onFailure("User not logged in");
            return;
        }

        DocumentReference docRef = db.collection(COLLECTION_BACKUPS).document(userId);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    restoreLayoutConfig(document.getString(FIELD_LAYOUT_CONFIG));
                    restoreWidgetConfig(document.getString(FIELD_WIDGET_CONFIG));
                    restoreGestureConfig(document.getString(FIELD_GESTURE_CONFIG));
                    callback.onSuccess("Settings restored successfully");
                } else {
                    callback.onFailure("No backup found");
                }
            } else {
                callback.onFailure(task.getException().getMessage());
            }
        });
    }

    // Dummy methods to get JSON from different managers (replace with actual implementation)
    private String getLayoutConfigJson() {
        return "{}"; // Replace with actual call to LayoutManager
    }

    private String getWidgetConfigJson() {
        return "{}"; // Replace with actual call to WidgetManager
    }

    private String getGestureConfigJson() {
        return "{}"; // Replace with actual call to GestureManager
    }

    // Dummy methods to restore from JSON (replace with actual implementation)
    private void restoreLayoutConfig(String json) {
        // Use LayoutManager to apply settings
    }

    private void restoreWidgetConfig(String json) {
        // Use WidgetManager to apply settings
    }

    private void restoreGestureConfig(String json) {
        // Use GestureManager to apply settings
    }

    public interface BackupCallback {
        void onSuccess(String message);
        void onFailure(String error);
    }

    public interface RestoreCallback {
        void onSuccess(String message);
        void onFailure(String error);
    }
}
