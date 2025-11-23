package com.mobiverse.launcher.folders;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppCategorizer {

    public enum AppCategory {
        COMMUNICATION,
        SOCIAL,
        PRODUCTIVITY,
        ENTERTAINMENT,
        GAMES,
        UTILITIES,
        NEWS,
        SHOPPING,
        FINANCE,
        HEALTH,
        EDUCATION,
        OTHER
    }

    private Context context;

    public AppCategorizer(Context context) {
        this.context = context;
    }

    public Map<AppCategory, List<String>> categorizeApps(List<ApplicationInfo> apps) {
        Map<AppCategory, List<String>> categorizedApps = new HashMap<>();
        for (AppCategory cat : AppCategory.values()) {
            categorizedApps.put(cat, new ArrayList<>());
        }

        for (ApplicationInfo app : apps) {
            AppCategory category = getCategoryForApp(app);
            categorizedApps.get(category).add(app.packageName);
        }
        return categorizedApps;
    }

    private AppCategory getCategoryForApp(ApplicationInfo app) {
        // Simplified categorization logic. 
        // A more robust solution would use Google Play categories or a predefined mapping.
        if ((app.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
            return AppCategory.UTILITIES;
        }

        String pkg = app.packageName.toLowerCase();
        if (pkg.contains("social") || pkg.contains("facebook") || pkg.contains("twitter") || pkg.contains("instagram")) {
            return AppCategory.SOCIAL;
        }
        if (pkg.contains("game")) {
            return AppCategory.GAMES;
        }
        if (pkg.contains("news")) {
            return AppCategory.NEWS;
        }

        return AppCategory.OTHER; 
    }
}