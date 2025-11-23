package com.mobiverse.launcher.themes;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class ThemeManager {
    private static ThemeManager instance;
    private Context context;
    private SharedPreferences prefs;
    private FirebaseFirestore firestore;
    private Gson gson;
    
    private LauncherTheme currentTheme;
    private List<LauncherTheme> availableThemes;

    private ThemeManager(Context context) {
        this.context = context.getApplicationContext();
        this.prefs = context.getSharedPreferences("launcher_themes", Context.MODE_PRIVATE);
        this.firestore = FirebaseFirestore.getInstance();
        this.gson = new Gson();
        this.availableThemes = new ArrayList<>();
        
        loadCurrentTheme();
        loadPresetThemes();
    }

    public static ThemeManager getInstance(Context context) {
        if (instance == null) {
            instance = new ThemeManager(context);
        }
        return instance;
    }

    private void loadPresetThemes() {
        // iOS Style
        LauncherTheme iosTheme = new LauncherTheme("ios_style", "iOS Style");
        iosTheme.setType(LauncherTheme.ThemeType.PRESET);
        iosTheme.setLayoutStyle(LauncherTheme.LayoutStyle.IOS_STYLE);
        iosTheme.setGridColumns(4);
        iosTheme.setGridRows(6);
        iosTheme.setNumHomeScreens(5);
        iosTheme.setShowAppNames(true);
        iosTheme.setIconSize(1.0f);
        iosTheme.setPrimaryColor(0xFF007AFF);
        iosTheme.setAccentColor(0xFF34C759);
        availableThemes.add(iosTheme);

        // Windows Metro
        LauncherTheme windowsTheme = new LauncherTheme("windows_metro", "Windows Metro");
        windowsTheme.setType(LauncherTheme.ThemeType.PRESET);
        windowsTheme.setLayoutStyle(LauncherTheme.LayoutStyle.WINDOWS_METRO);
        windowsTheme.setGridColumns(3);
        windowsTheme.setGridRows(4);
        windowsTheme.setNumHomeScreens(2);
        windowsTheme.setShowAppNames(true);
        windowsTheme.setIconSize(1.2f);
        windowsTheme.setPrimaryColor(0xFF0078D4);
        windowsTheme.setAccentColor(0xFF00BCF2);
        availableThemes.add(windowsTheme);

        // Stock Android
        LauncherTheme stockTheme = new LauncherTheme("stock_android", "Stock Android");
        stockTheme.setType(LauncherTheme.ThemeType.PRESET);
        stockTheme.setLayoutStyle(LauncherTheme.LayoutStyle.ANDROID_STOCK);
        stockTheme.setGridColumns(5);
        stockTheme.setGridRows(5);
        stockTheme.setNumHomeScreens(3);
        stockTheme.setShowAppNames(true);
        stockTheme.setIconSize(1.0f);
        stockTheme.setPrimaryColor(0xFF1A73E8);
        stockTheme.setAccentColor(0xFF34A853);
        availableThemes.add(stockTheme);

        // MIUI Style
        LauncherTheme miuiTheme = new LauncherTheme("miui_style", "MIUI Style");
        miuiTheme.setType(LauncherTheme.ThemeType.PRESET);
        miuiTheme.setLayoutStyle(LauncherTheme.LayoutStyle.MIUI_STYLE);
        miuiTheme.setGridColumns(4);
        miuiTheme.setGridRows(5);
        miuiTheme.setNumHomeScreens(4);
        miuiTheme.setShowAppNames(true);
        miuiTheme.setIconSize(1.1f);
        miuiTheme.setPrimaryColor(0xFFFF6900);
        miuiTheme.setAccentColor(0xFFFFBF00);
        availableThemes.add(miuiTheme);

        // One UI (Samsung)
        LauncherTheme oneUITheme = new LauncherTheme("one_ui", "One UI");
        oneUITheme.setType(LauncherTheme.ThemeType.PRESET);
        oneUITheme.setLayoutStyle(LauncherTheme.LayoutStyle.ONE_UI);
        oneUITheme.setGridColumns(4);
        oneUITheme.setGridRows(5);
        oneUITheme.setNumHomeScreens(3);
        oneUITheme.setShowAppNames(true);
        oneUITheme.setIconSize(1.0f);
        oneUITheme.setPrimaryColor(0xFF1B5CCC);
        oneUITheme.setAccentColor(0xFF5599FF);
        availableThemes.add(oneUITheme);

        // Islamic Theme
        LauncherTheme islamicTheme = new LauncherTheme("islamic_green", "Islamic Green");
        islamicTheme.setType(LauncherTheme.ThemeType.PRESET);
        islamicTheme.setLayoutStyle(LauncherTheme.LayoutStyle.CUSTOM_GRID);
        islamicTheme.setGridColumns(4);
        islamicTheme.setGridRows(5);
        islamicTheme.setNumHomeScreens(3);
        islamicTheme.setShowAppNames(true);
        islamicTheme.setIconSize(1.0f);
        islamicTheme.setPrimaryColor(0xFF00875A);
        islamicTheme.setAccentColor(0xFFFFD700);
        islamicTheme.setWallpaperUrl("islamic_pattern_green.jpg");
        availableThemes.add(islamicTheme);
    }

    public void applyTheme(LauncherTheme theme) {
        this.currentTheme = theme;
        saveCurrentTheme();
        notifyThemeChanged();
    }

    public LauncherTheme getCurrentTheme() {
        if (currentTheme == null) {
            currentTheme = availableThemes.get(0); // Default to first theme
        }
        return currentTheme;
    }

    public List<LauncherTheme> getAvailableThemes() {
        return new ArrayList<>(availableThemes);
    }

    public void createCustomTheme(LauncherTheme theme) {
        theme.setType(LauncherTheme.ThemeType.USER_CREATED);
        availableThemes.add(theme);
        saveThemeToFirestore(theme);
    }

    private void saveCurrentTheme() {
        String json = gson.toJson(currentTheme);
        prefs.edit().putString("current_theme", json).apply();
    }

    private void loadCurrentTheme() {
        String json = prefs.getString("current_theme", null);
        if (json != null) {
            currentTheme = gson.fromJson(json, LauncherTheme.class);
        }
    }

    private void saveThemeToFirestore(LauncherTheme theme) {
        firestore.collection("user_themes")
            .document(theme.getId())
            .set(theme);
    }

    private void notifyThemeChanged() {
        context.sendBroadcast(new android.content.Intent("com.mobiverse.THEME_CHANGED"));
    }
}
