package com.mobiverse.launcher.suggestions;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SuggestionEngine {

    private Context context;
    private AppUsageTracker appUsageTracker;

    public SuggestionEngine(Context context) {
        this.context = context;
        this.appUsageTracker = new AppUsageTracker(context);
    }

    public List<ApplicationInfo> getSuggestedApps() {
        // This is a simple suggestion logic. A more advanced engine would 
        // use machine learning models and consider context like location, time of day, etc.

        return appUsageTracker.getRecentUsageStats()
            .stream()
            .limit(5) // Suggest top 5 most recently used apps
            .map(stat -> {
                try {
                    return context.getPackageManager().getApplicationInfo(stat.getPackageName(), 0);
                } catch (PackageManager.NameNotFoundException e) {
                    return null;
                }
            })
            .filter(info -> info != null)
            .collect(Collectors.toList());
    }
}