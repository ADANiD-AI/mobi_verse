package com.mobiverse.launcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
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
import com.mobiverse.satellite.FloatingSatelliteService;

public class AndroidLauncherActivity extends AppCompatActivity {

    private static final int OVERLAY_PERMISSION_REQ_CODE = 1234;
    private CloudSyncManager cloudSyncManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_launcher);

        initializeViews();
        // ... (rest of the initializations)
        permissionManager = new PermissionManager(this);
        cloudSyncManager = CloudSyncManager.getInstance(this);

        // ... (rest of setup methods)
        setupHomeScreens();

        // Check for overlay permission and start the service
        checkOverlayPermission();

        syncDataWithCloud();
        registerReceiver(themeChangedReceiver, new IntentFilter("com.mobiverse.THEME_CHANGED"));
    }

    private void checkOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                       Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
        } else {
            startFloatingSatelliteService();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Settings.canDrawOverlays(this)) {
                startFloatingSatelliteService();
            } else {
                Snackbar.make(findViewById(android.R.id.content), "Overlay permission is required for the satellite messenger.", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private void startFloatingSatelliteService() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(this)) {
            startService(new Intent(this, FloatingSatelliteService.class));
        }
    }

    // ... (rest of the existing code, no changes needed below)
    private void initializeViews() {
     // ...
    }
    
    private void applyThemeStyling(){
        // ...
    }

    private void syncDataWithCloud() {
        if (cloudSyncManager.isUserLoggedIn()) {
            cloudSyncManager.syncUserSettings();
            cloudSyncManager.syncAssets();
        }
    }
}
