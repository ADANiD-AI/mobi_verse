package com.mobiverse.launcher.suggestions;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Comparator;

public class AppUsageTracker {

    private Context context;
    private UsageStatsManager usageStatsManager;

    public AppUsageTracker(Context context) {
        this.context = context;
        this.usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
    }

    public List<UsageStats> getRecentUsageStats() {
        long endTime = System.currentTimeMillis();
        long beginTime = endTime - (1000 * 60 * 60 * 24); // Last 24 hours

        return usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, beginTime, endTime)
            .stream()
            .sorted((a, b) -> Long.compare(b.getLastTimeUsed(), a.getLastTimeUsed()))
            .collect(Collectors.toList());
    }

    public List<String> getMostUsedApps(int count) {
        long endTime = System.currentTimeMillis();
        long beginTime = endTime - (1000 * 60 * 60 * 24 * 7); // Last 7 days

        return usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, beginTime, endTime)
                .stream()
                .sorted(Comparator.comparingLong(UsageStats::getTotalTimeInForeground).reversed())
                .limit(count)
                .map(UsageStats::getPackageName)
                .collect(Collectors.toList());
    }

    // You might need to request permission for USAGE_STATS
    // <uses-permission android:name="android.permission.PACKAGE_USAGE_STATS" />
}