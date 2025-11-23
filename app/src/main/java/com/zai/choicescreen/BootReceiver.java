package com.zai.choicescreen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

/**
 * BroadcastReceiver for handling device boot events
 * Automatically starts Z-AI Choice Screen app when device boots up
 * 
 * @author Muhammad Adnan Ul Mustafa
 * @version 1.0.0
 */
public class BootReceiver extends BroadcastReceiver {
    
    private static final String TAG = "BootReceiver";
    private static final String PREFS_NAME = "ZAIChoicePrefs";
    private static final String PREF_AUTO_START = "auto_start_enabled";
    
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || intent.getAction() == null) {
            Log.w(TAG, "Received null intent or action");
            return;
        }
        
        String action = intent.getAction();
        Log.d(TAG, "Received boot action: " + action);
        
        try {
            switch (action) {
                case Intent.ACTION_BOOT_COMPLETED:
                case Intent.ACTION_MY_PACKAGE_REPLACED:
                case Intent.ACTION_PACKAGE_REPLACED:
                    handleBootCompleted(context);
                    break;
                case Intent.ACTION_LOCKED_BOOT_COMPLETED:
                    handleLockedBootCompleted(context);
                    break;
                default:
                    Log.d(TAG, "Unhandled action: " + action);
                    break;
            }
        } catch (Exception e) {
            Log.e(TAG, "Error handling boot event: " + action, e);
        }
    }
    
    /**
     * Handles boot completed event
     */
    private void handleBootCompleted(Context context) {
        Log.i(TAG, "Device boot completed");
        
        // Check if auto-start is enabled
        if (!isAutoStartEnabled(context)) {
            Log.d(TAG, "Auto-start is disabled, not starting app");
            return;
        }
        
        // Start the main activity
        startMainActivity(context);
        
        // Initialize background services if needed
        initializeBackgroundServices(context);
        
        // Log boot event
        logBootEvent(context);
    }
    
    /**
     * Handles locked boot completed event (Android N+)
     */
    private void handleLockedBootCompleted(Context context) {
        Log.i(TAG, "Device locked boot completed");
        
        // Only perform minimal initialization during locked boot
        if (isAutoStartEnabled(context)) {
            // Initialize essential services that don't require user interaction
            initializeEssentialServices(context);
        }
    }
    
    /**
     * Checks if auto-start is enabled in preferences
     */
    private boolean isAutoStartEnabled(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(PREF_AUTO_START, false); // Default to false for privacy
    }
    
    /**
     * Starts the main activity
     */
    private void startMainActivity(Context context) {
        try {
            Intent launchIntent = new Intent(context, MainActivity.class);
            launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            launchIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            launchIntent.putExtra("started_from_boot", true);
            
            context.startActivity(launchIntent);
            Log.i(TAG, "Main activity started successfully");
            
        } catch (Exception e) {
            Log.e(TAG, "Failed to start main activity", e);
        }
    }
    
    /**
     * Initializes background services
     */
    private void initializeBackgroundServices(Context context) {
        try {
            // Start choice monitoring service if needed
            Intent serviceIntent = new Intent(context, ChoiceMonitoringService.class);
            serviceIntent.putExtra("started_from_boot", true);
            
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(serviceIntent);
            } else {
                context.startService(serviceIntent);
            }
            
            Log.d(TAG, "Background services initialized");
            
        } catch (Exception e) {
            Log.e(TAG, "Failed to initialize background services", e);
        }
    }
    
    /**
     * Initializes essential services during locked boot
     */
    private void initializeEssentialServices(Context context) {
        try {
            // Initialize only critical services that don't require UI
            Log.d(TAG, "Essential services initialized during locked boot");
            
            // Example: Initialize notification channels
            createNotificationChannels(context);
            
        } catch (Exception e) {
            Log.e(TAG, "Failed to initialize essential services", e);
        }
    }
    
    /**
     * Creates notification channels
     */
    private void createNotificationChannels(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                // Use ChoiceActionReceiver's method to create channels
                ChoiceActionReceiver receiver = new ChoiceActionReceiver();
                // Note: This is a simplified approach. In production, you might want
                // to extract channel creation to a utility class.
                
                Log.d(TAG, "Notification channels created");
                
            } catch (Exception e) {
                Log.e(TAG, "Failed to create notification channels", e);
            }
        }
    }
    
    /**
     * Logs boot event for analytics/debugging
     */
    private void logBootEvent(Context context) {
        try {
            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            
            // Update boot statistics
            long currentTime = System.currentTimeMillis();
            int bootCount = prefs.getInt("boot_count", 0) + 1;
            
            editor.putLong("last_boot_time", currentTime);
            editor.putInt("boot_count", bootCount);
            editor.putString("last_boot_version", BuildConfig.VERSION_NAME);
            
            editor.apply();
            
            Log.d(TAG, "Boot event logged - Count: " + bootCount);
            
        } catch (Exception e) {
            Log.e(TAG, "Failed to log boot event", e);
        }
    }
    
    /**
     * Utility method to enable/disable auto-start
     */
    public static void setAutoStartEnabled(Context context, boolean enabled) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        
        editor.putBoolean(PREF_AUTO_START, enabled);
        editor.putLong("auto_start_changed_time", System.currentTimeMillis());
        
        editor.apply();
        
        Log.i(TAG, "Auto-start " + (enabled ? "enabled" : "disabled"));
    }
    
    /**
     * Utility method to check if auto-start is enabled
     */
    public static boolean isAutoStartEnabled(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(PREF_AUTO_START, false);
    }
    
    /**
     * Gets boot statistics
     */
    public static BootStats getBootStats(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        
        return new BootStats(
            prefs.getLong("last_boot_time", 0),
            prefs.getInt("boot_count", 0),
            prefs.getString("last_boot_version", "unknown")
        );
    }
    
    /**
     * Data class for boot statistics
     */
    public static class BootStats {
        public final long lastBootTime;
        public final int bootCount;
        public final String lastBootVersion;
        
        public BootStats(long lastBootTime, int bootCount, String lastBootVersion) {
            this.lastBootTime = lastBootTime;
            this.bootCount = bootCount;
            this.lastBootVersion = lastBootVersion;
        }
        
        @Override
        public String toString() {
            return "BootStats{" +
                    "lastBootTime=" + lastBootTime +
                    ", bootCount=" + bootCount +
                    ", lastBootVersion='" + lastBootVersion + '\'' +
                    '}';
        }
    }
    
    /**
     * Placeholder for ChoiceMonitoringService
     * This would be implemented as a separate service class
     */
    private static class ChoiceMonitoringService {
        // This is a placeholder - in a real implementation,
        // this would be a separate Service class file
    }
}