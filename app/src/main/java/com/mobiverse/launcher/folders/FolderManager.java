package com.mobiverse.launcher.folders;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class FolderManager {
    private static FolderManager instance;
    private Context context;
    private SharedPreferences prefs;
    private Gson gson;
    private List<FolderInfo> folders;

    private FolderManager(Context context) {
        this.context = context.getApplicationContext();
        this.prefs = context.getSharedPreferences("launcher_folders", Context.MODE_PRIVATE);
        this.gson = new Gson();
        loadFolders();
    }

    public static FolderManager getInstance(Context context) {
        if (instance == null) {
            instance = new FolderManager(context);
        }
        return instance;
    }

    public List<FolderInfo> getFolders() {
        return folders;
    }

    public void addFolder(FolderInfo folder) {
        folders.add(folder);
        saveFolders();
    }

    public void removeFolder(long folderId) {
        folders.removeIf(f -> f.getId() == folderId);
        saveFolders();
    }

    public void updateFolder(FolderInfo updatedFolder) {
        for (int i = 0; i < folders.size(); i++) {
            if (folders.get(i).getId() == updatedFolder.getId()) {
                folders.set(i, updatedFolder);
                break;
            }
        }
        saveFolders();
    }

    private void loadFolders() {
        String json = prefs.getString("folders_list", "[]");
        folders = gson.fromJson(json, new TypeToken<ArrayList<FolderInfo>>(){}.getType());
        if (folders == null) {
            folders = new ArrayList<>();
        }
    }

    private void saveFolders() {
        String json = gson.toJson(folders);
        prefs.edit().putString("folders_list", json).apply();
    }

    // public void categorizeApps(List<AppInfo> allApps) {
    //     // Logic to auto-categorize apps into smart folders
    // }
}