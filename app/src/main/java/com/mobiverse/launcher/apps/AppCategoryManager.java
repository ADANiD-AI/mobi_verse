package com.mobiverse.launcher.apps;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.*;

public class AppCategoryManager {
    private Context context;
    private SharedPreferences prefs;
    private Gson gson;
    
    private Map<String, List<String>> categoryApps; // Category -> List of package names
    private Map<String, String> appCategories; // Package name -> Category

    public enum DefaultCategory {
        SOCIAL("Social", "📱"),
        PRODUCTIVITY("Productivity", "💼"),
        ENTERTAINMENT("Entertainment", "🎮"),
        FINANCE("Finance", "💰"),
        ISLAMIC("Islamic", "🕌"),
        TOOLS("Tools", "🔧"),
        GAMES("Games", "🎯"),
        SHOPPING("Shopping", "🛒"),
        TRAVEL("Travel", "✈️"),
        UNCATEGORIZED("Uncategorized", "📦");

        public final String name;
        public final String icon;

        DefaultCategory(String name, String icon) {
            this.name = name;
            this.icon = icon;
        }
    }

    public AppCategoryManager(Context context) {
        this.context = context;
        this.prefs = context.getSharedPreferences("app_categories", Context.MODE_PRIVATE);
        this.gson = new Gson();
        
        loadCategories();
        initializeDefaultCategories();
    }

    public void setAppCategory(String packageName, String category) {
        // Remove from old category
        String oldCategory = appCategories.get(packageName);
        if (oldCategory != null && categoryApps.containsKey(oldCategory)) {
            categoryApps.get(oldCategory).remove(packageName);
        }

        // Add to new category
        appCategories.put(packageName, category);
        if (!categoryApps.containsKey(category)) {
            categoryApps.put(category, new ArrayList<>());
        }
        categoryApps.get(category).add(packageName);

        saveCategories();
    }

    public String getAppCategory(String packageName) {
        return appCategories.getOrDefault(packageName, DefaultCategory.UNCATEGORIZED.name);
    }

    public List<String> getAppsInCategory(String category) {
        return new ArrayList<>(categoryApps.getOrDefault(category, new ArrayList<>()));
    }

    public Set<String> getAllCategories() {
        return new HashSet<>(categoryApps.keySet());
    }

    public void createCustomCategory(String categoryName, String icon) {
        if (!categoryApps.containsKey(categoryName)) {
            categoryApps.put(categoryName, new ArrayList<>());
            saveCategories();
        }
    }

    private void initializeDefaultCategories() {
        for (DefaultCategory cat : DefaultCategory.values()) {
            if (!categoryApps.containsKey(cat.name)) {
                categoryApps.put(cat.name, new ArrayList<>());
            }
        }
    }

    private void loadCategories() {
        String categoryAppsJson = prefs.getString("category_apps", "{}");
        String appCategoriesJson = prefs.getString("app_categories", "{}");

        categoryApps = gson.fromJson(categoryAppsJson, 
            new TypeToken<HashMap<String, List<String>>>(){}.getType());
        appCategories = gson.fromJson(appCategoriesJson, 
            new TypeToken<HashMap<String, String>>(){}.getType());

        if (categoryApps == null) categoryApps = new HashMap<>();
        if (appCategories == null) appCategories = new HashMap<>();
    }

    private void saveCategories() {
        prefs.edit()
            .putString("category_apps", gson.toJson(categoryApps))
            .putString("app_categories", gson.toJson(appCategories))
            .apply();
    }
}