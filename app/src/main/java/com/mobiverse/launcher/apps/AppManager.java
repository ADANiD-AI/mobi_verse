package com.mobiverse.launcher.apps;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AppManager {
    private static AppManager instance;
    private Context context;
    private SharedPreferences prefs;
    private Gson gson;
    
    private Set<String> hiddenApps;
    private Set<String> lockedApps;
    private AppCategoryManager categoryManager;

    private AppManager(Context context) {
        this.context = context.getApplicationContext();
        this.prefs = context.getSharedPreferences("app_manager", Context.MODE_PRIVATE);
        this.gson = new Gson();
        this.categoryManager = new AppCategoryManager(context);
        
        loadHiddenApps();
        loadLockedApps();
    }

    public static AppManager getInstance(Context context) {
        if (instance == null) {
            instance = new AppManager(context);
        }
        return instance;
    }

    // Hide/Show Apps
    public void hideApp(String packageName) {
        hiddenApps.add(packageName);
        saveHiddenApps();
    }

    public void showApp(String packageName) {
        hiddenApps.remove(packageName);
        saveHiddenApps();
    }

    public boolean isAppHidden(String packageName) {
        return hiddenApps.contains(packageName);
    }

    public Set<String> getHiddenApps() {
        return new HashSet<>(hiddenApps);
    }

    // Lock/Unlock Apps
    public void lockApp(String packageName) {
        lockedApps.add(packageName);
        saveLockedApps();
    }

    public void unlockApp(String packageName) {
        lockedApps.remove(packageName);
        saveLockedApps();
    }

    public boolean isAppLocked(String packageName) {
        return lockedApps.contains(packageName);
    }

    public Set<String> getLockedApps() {
        return new HashSet<>(lockedApps);
    }

    // Persistence
    private void loadHiddenApps() {
        String json = prefs.getString("hidden_apps", "[]");
        hiddenApps = gson.fromJson(json, new TypeToken<HashSet<String>>(){}.getType());
        if (hiddenApps == null) hiddenApps = new HashSet<>();
    }

    private void saveHiddenApps() {
        String json = gson.toJson(hiddenApps);
        prefs.edit().putString("hidden_apps", json).apply();
    }

    private void loadLockedApps() {
        String json = prefs.getString("locked_apps", "[]");
        lockedApps = gson.fromJson(json, new TypeToken<HashSet<String>>(){}.getType());
        if (lockedApps == null) lockedApps = new HashSet<>();
    }

    private void saveLockedApps() {
        String json = gson.toJson(lockedApps);
        prefs.edit().putString("locked_apps", json).apply();
    }

    public AppCategoryManager getCategoryManager() {
        return categoryManager;
    }
}