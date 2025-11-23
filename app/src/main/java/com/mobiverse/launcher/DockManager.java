package com.mobiverse.launcher;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.mobiverse.launcher.suggestions.AppUsageTracker;
import com.mobiverse.launcher.themes.LauncherTheme;
import java.util.Arrays;
import java.util.List;

public class DockManager {

    private Context context;
    private LinearLayout dockContainer;
    private AppUsageTracker appUsageTracker;
    private LauncherTheme currentTheme;

    public DockManager(Context context, LinearLayout dockContainer, AppUsageTracker appUsageTracker, LauncherTheme currentTheme) {
        this.context = context;
        this.dockContainer = dockContainer;
        this.appUsageTracker = appUsageTracker;
        this.currentTheme = currentTheme;
    }

    public void setupDock() {
        dockContainer.removeAllViews(); // Clear previous icons
        List<String> dockApps = appUsageTracker.getMostUsedApps(5);

        if (dockApps.isEmpty()) {
            dockApps = getDefaultDockApps();
        }

        for (String packageName : dockApps) {
            try {
                Drawable icon = context.getPackageManager().getApplicationIcon(packageName);
                ImageView appIcon = new ImageView(context);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    (int) (64 * currentTheme.getIconSize()),
                    (int) (64 * currentTheme.getIconSize())
                );
                params.setMargins(12, 0, 12, 0);
                appIcon.setLayoutParams(params);
                appIcon.setImageDrawable(icon);
                appIcon.setOnClickListener(v -> launchApp(packageName));
                dockContainer.addView(appIcon);
            } catch (PackageManager.NameNotFoundException e) {
                // App not found, skip it
                e.printStackTrace();
            }
        }
    }

    private List<String> getDefaultDockApps() {
        // A default set of apps to show if usage stats are not available
        return Arrays.asList(
            "com.android.chrome",
            "com.google.android.youtube",
            "com.whatsapp",
            "com.instagram.android",
            "com.facebook.katana"
        );
    }

    private void launchApp(String packageName) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent != null) {
            context.startActivity(intent);
        }
    }
}
