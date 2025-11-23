package com.mobiverse.launcher.suggestions;

import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import java.util.Collections;
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

    private boolean hasUsageStatsPermission() {
        AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), context.getPackageName());
        return mode == AppOpsManager.MODE_ALLOWED;
    }

    public List<UsageStats> getRecentUsageStats() {
        if (!hasUsageStatsPermission()) {
            return Collections.emptyList();
        }
        long endTime = System.currentTimeMillis();
        long beginTime = endTime - (1000 * 60 * 60 * 24); // Last 24 hours

        return usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, beginTime, endTime)
            .stream()
            .sorted((a, b) -> Long.compare(b.getLastTimeUsed(), a.getLastTimeUsed()))
            .collect(Collectors.toList());
    }

    public List<String> getMostUsedApps(int count) {
        if (!hasUsageStatsPermission()) {
            return Collections.emptyList();
        }
        long endTime = System.currentTimeMillis();
        long beginTime = endTime - (1000 * 60 * 60 * 24 * 7); // Last 7 days

        return usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, beginTime, endTime)
                .stream()
                .sorted(Comparator.comparingLong(UsageStats::getTotalTimeInForeground).reversed())
                .limit(count)
                .map(UsageStats::getPackageName)
                .collect(Collectors.toList());
    }
}