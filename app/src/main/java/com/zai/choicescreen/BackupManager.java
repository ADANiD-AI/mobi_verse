package com.zai.choicescreen;

import android.content.Context;
import android.util.Log;

public class BackupManager {

    private DatabaseManager databaseManager;
    private ThemeManager themeManager;

    public BackupManager(Context context) {
        databaseManager = new DatabaseManager();
        themeManager = new ThemeManager(context);
    }

    public void backupSettings() {
        LauncherSettings settings = themeManager.getCurrentLauncherSettings();
        databaseManager.saveLauncherSettings(settings);
        Log.d("BackupManager", "Launcher settings backed up.");
    }

    public void restoreSettings() {
        // In a real app, you would fetch the settings from the database
        // and apply them using the ThemeManager.
        Log.d("BackupManager", "Launcher settings restored.");
    }
}
