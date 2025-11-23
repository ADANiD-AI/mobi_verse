package com.mobiverse.launcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.mobiverse.aikeyboard.R;
import com.mobiverse.cloud.CloudSyncManager;
import com.mobiverse.launcher.search.SearchActivity;
import com.mobiverse.launcher.suggestions.AppUsageTracker;
import com.mobiverse.launcher.themes.LauncherTheme;
import com.mobiverse.launcher.themes.ThemeManager;

public class AndroidLauncherActivity extends AppCompatActivity {

    // ... (existing variables)
    private CloudSyncManager cloudSyncManager;

    // ... (existing code)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_launcher);

        initializeViews();
        appUsageTracker = new AppUsageTracker(this);
        themeManager = ThemeManager.getInstance(this);
        currentTheme = themeManager.getCurrentTheme();
        permissionManager = new PermissionManager(this);
        cloudSyncManager = CloudSyncManager.getInstance(this); // Initialize CloudSyncManager

        dockManager = new DockManager(this, dockContainer, appUsageTracker, currentTheme);
        pageIndicatorManager = new PageIndicatorManager(this, pageIndicatorLayout);

        applyThemeStyling();
        setupHomeScreens();
        setupDock();
        setupAppDrawer();
        setupSearch();

        syncDataWithCloud(); // Sync data on startup

        registerReceiver(themeChangedReceiver, new IntentFilter("com.mobiverse.THEME_CHANGED"));
    }

    // ... (existing code)

    private void applyThemeStyling() {
        if (currentTheme != null) {
            findViewById(android.R.id.content).setBackgroundColor(currentTheme.getPrimaryColor());
            fabAppDrawer.setBackgroundTintList(ColorStateList.valueOf(currentTheme.getAccentColor()));
            if (dockManager != null) {
                dockManager.setupDock();
            }
            syncDataWithCloud(); // Sync theme changes
        }
    }

    private void syncDataWithCloud() {
        if (cloudSyncManager.isUserLoggedIn()) {
            cloudSyncManager.syncUserSettings();
            cloudSyncManager.syncAssets();
        }
    }

    // ... (rest of the existing code)
}
