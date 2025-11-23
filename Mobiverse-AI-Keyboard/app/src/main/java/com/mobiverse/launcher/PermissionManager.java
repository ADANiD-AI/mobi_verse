package com.mobiverse.launcher;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

public class PermissionManager {

    private Context context;

    public PermissionManager(Context context) {
        this.context = context;
    }

    public boolean hasUsageStatsPermission() {
        AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), context.getPackageName());
        return mode == AppOpsManager.MODE_ALLOWED;
    }

    public void requestUsageStatsPermission() {
        context.startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
    }
}
