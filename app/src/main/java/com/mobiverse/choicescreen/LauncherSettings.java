package com.zai.choicescreen;

import java.util.Map;

public class LauncherSettings {
    public String currentThemeId;
    public String iconPackId;
    public String wallpaperUrl;
    public Map<String, String> gestureMapping;
    public Map<String, Object> homeScreenLayout;
    public String gridSize;

    public LauncherSettings() {
        // Default constructor required for calls to DataSnapshot.getValue(LauncherSettings.class)
    }
}
