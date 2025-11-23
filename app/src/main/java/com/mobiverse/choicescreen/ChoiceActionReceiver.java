package com.zai.choicescreen;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

/**
 * BroadcastReceiver for handling choice actions in Z-AI Choice Screen
 * Processes user selections and manages notifications
 * 
 * @author Muhammad Adnan Ul Mustafa
 * @version 1.0.0
 */
public class ChoiceActionReceiver extends BroadcastReceiver {
    
    private static final String TAG = "ChoiceActionReceiver";
    private static final String CHANNEL_ID = "zai_choice_channel";
    private static final String PREFS_NAME = "ZAIChoicePrefs";
    
    // Action constants
    public static final String ACTION_CHOICE_SELECTED = "com.zai.choicescreen.CHOICE_SELECTED";
    public static final String ACTION_CHOICE_CONFIRMED = "com.zai.choicescreen.CHOICE_CONFIRMED";
    public static final String ACTION_CHOICE_CANCELLED = "com.zai.choicescreen.CHOICE_CANCELLED";
    public static final String ACTION_RESET_CHOICES = "com.zai.choicescreen.RESET_CHOICES";
    
    // Extra keys
    public static final String EXTRA_CHOICE_ID = "choice_id";
    public static final String EXTRA_CHOICE_TEXT = "choice_text";
    public static final String EXTRA_TIMESTAMP = "timestamp";
    public static final String EXTRA_USER_DATA = "user_data";
    
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || intent.getAction() == null) {
            Log.w(TAG, "Received null intent or action");
            return;
        }
        
        String action = intent.getAction();
        Log.d(TAG, "Received action: " + action);
        
        try {
            switch (action) {
                case ACTION_CHOICE_SELECTED:
                    handleChoiceSelected(context, intent);
                    break;
                case ACTION_CHOICE_CONFIRMED:
                    handleChoiceConfirmed(context, intent);
                    break;
                case ACTION_CHOICE_CANCELLED:
                    handleChoiceCancelled(context, intent);
                    break;
                case ACTION_RESET_CHOICES:
                    handleResetChoices(context, intent);
                    break;
                default:
                    Log.w(TAG, "Unknown action received: " + action);
                    break;
            }
        } catch (Exception e) {
            Log.e(TAG, "Error processing action: " + action, e);
        }
    }
    
    /**
     * Handles choice selection action
     */
    private void handleChoiceSelected(Context context, Intent intent) {
        String choiceId = intent.getStringExtra(EXTRA_CHOICE_ID);
        String choiceText = intent.getStringExtra(EXTRA_CHOICE_TEXT);
        long timestamp = intent.getLongExtra(EXTRA_TIMESTAMP, System.currentTimeMillis());
        
        Log.i(TAG, "Choice selected - ID: " + choiceId + ", Text: " + choiceText);
        
        // Save selection to preferences
        saveChoiceSelection(context, choiceId, choiceText, timestamp);
        
        // Show notification
        showNotification(context, 
            context.getString(R.string.notification_title),
            context.getString(R.string.choice_selected) + ": " + choiceText,
            createMainActivityIntent(context));
        
        // Broadcast selection to other components
        broadcastChoiceUpdate(context, choiceId, choiceText, "SELECTED");
    }
    
    /**
     * Handles choice confirmation action
     */
    private void handleChoiceConfirmed(Context context, Intent intent) {
        String choiceId = intent.getStringExtra(EXTRA_CHOICE_ID);
        String choiceText = intent.getStringExtra(EXTRA_CHOICE_TEXT);
        
        Log.i(TAG, "Choice confirmed - ID: " + choiceId + ", Text: " + choiceText);
        
        // Update preferences with confirmation
        updateChoiceStatus(context, choiceId, "CONFIRMED");
        
        // Show confirmation notification
        showNotification(context,
            context.getString(R.string.notification_title),
            context.getString(R.string.choice_confirmed),
            createMainActivityIntent(context));
        
        // Process the confirmed choice
        processConfirmedChoice(context, choiceId, choiceText);
        
        // Broadcast confirmation
        broadcastChoiceUpdate(context, choiceId, choiceText, "CONFIRMED");
    }
    
    /**
     * Handles choice cancellation action
     */
    private void handleChoiceCancelled(Context context, Intent intent) {
        String choiceId = intent.getStringExtra(EXTRA_CHOICE_ID);
        
        Log.i(TAG, "Choice cancelled - ID: " + choiceId);
        
        // Update preferences
        updateChoiceStatus(context, choiceId, "CANCELLED");
        
        // Clear any pending notifications
        clearNotifications(context);
        
        // Broadcast cancellation
        broadcastChoiceUpdate(context, choiceId, null, "CANCELLED");
    }
    
    /**
     * Handles reset choices action
     */
    private void handleResetChoices(Context context, Intent intent) {
        Log.i(TAG, "Resetting all choices");
        
        // Clear all saved choices
        clearAllChoices(context);
        
        // Clear notifications
        clearNotifications(context);
        
        // Broadcast reset
        broadcastChoiceUpdate(context, null, null, "RESET");
        
        // Show reset confirmation
        showNotification(context,
            context.getString(R.string.notification_title),
            "All choices have been reset",
            createMainActivityIntent(context));
    }
    
    /**
     * Saves choice selection to SharedPreferences
     */
    private void saveChoiceSelection(Context context, String choiceId, String choiceText, long timestamp) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        
        editor.putString("last_choice_id", choiceId);
        editor.putString("last_choice_text", choiceText);
        editor.putLong("last_choice_timestamp", timestamp);
        editor.putString("choice_status_" + choiceId, "SELECTED");
        
        editor.apply();
    }
    
    /**
     * Updates choice status in SharedPreferences
     */
    private void updateChoiceStatus(Context context, String choiceId, String status) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        
        editor.putString("choice_status_" + choiceId, status);
        editor.putLong("last_update_timestamp", System.currentTimeMillis());
        
        editor.apply();
    }
    
    /**
     * Clears all saved choices
     */
    private void clearAllChoices(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        
        editor.clear();
        editor.apply();
    }
    
    /**
     * Processes confirmed choice (placeholder for business logic)
     */
    private void processConfirmedChoice(Context context, String choiceId, String choiceText) {
        // Implement specific business logic here
        Log.d(TAG, "Processing confirmed choice: " + choiceId + " - " + choiceText);
        
        // Example: Send to analytics, API calls, etc.
        // Analytics.track("choice_confirmed", choiceId, choiceText);
        // ApiService.submitChoice(choiceId, choiceText);
    }
    
    /**
     * Broadcasts choice updates to other app components
     */
    private void broadcastChoiceUpdate(Context context, String choiceId, String choiceText, String status) {
        Intent broadcastIntent = new Intent("com.zai.choicescreen.CHOICE_UPDATE");
        broadcastIntent.putExtra(EXTRA_CHOICE_ID, choiceId);
        broadcastIntent.putExtra(EXTRA_CHOICE_TEXT, choiceText);
        broadcastIntent.putExtra("status", status);
        broadcastIntent.putExtra(EXTRA_TIMESTAMP, System.currentTimeMillis());
        
        context.sendBroadcast(broadcastIntent);
    }
    
    /**
     * Shows notification to user
     */
    private void showNotification(Context context, String title, String message, PendingIntent pendingIntent) {
        createNotificationChannel(context);
        
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message));
        
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(1001, builder.build());
    }
    
    /**
     * Creates notification channel for Android O+
     */
    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.notification_channel_name);
            String description = context.getString(R.string.notification_channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
    
    /**
     * Creates PendingIntent for main activity
     */
    private PendingIntent createMainActivityIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        
        return PendingIntent.getActivity(context, 0, intent, 
            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    }
    
    /**
     * Clears all notifications
     */
    private void clearNotifications(Context context) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.cancelAll();
    }
    
    /**
     * Utility method to create choice selection intent
     */
    public static Intent createChoiceSelectionIntent(Context context, String choiceId, String choiceText) {
        Intent intent = new Intent(context, ChoiceActionReceiver.class);
        intent.setAction(ACTION_CHOICE_SELECTED);
        intent.putExtra(EXTRA_CHOICE_ID, choiceId);
        intent.putExtra(EXTRA_CHOICE_TEXT, choiceText);
        intent.putExtra(EXTRA_TIMESTAMP, System.currentTimeMillis());
        return intent;
    }
    
    /**
     * Utility method to create choice confirmation intent
     */
    public static Intent createChoiceConfirmationIntent(Context context, String choiceId, String choiceText) {
        Intent intent = new Intent(context, ChoiceActionReceiver.class);
        intent.setAction(ACTION_CHOICE_CONFIRMED);
        intent.putExtra(EXTRA_CHOICE_ID, choiceId);
        intent.putExtra(EXTRA_CHOICE_TEXT, choiceText);
        return intent;
    }
}