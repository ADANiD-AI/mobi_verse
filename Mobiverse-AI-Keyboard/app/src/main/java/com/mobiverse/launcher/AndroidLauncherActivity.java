package com.mobiverse.launcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mobiverse.aikeyboard.R;
import com.mobiverse.launcher.search.SearchActivity;
import com.mobiverse.launcher.suggestions.AppUsageTracker;
import com.mobiverse.launcher.themes.LauncherTheme;
import com.mobiverse.launcher.themes.ThemeManager;
import java.util.List;

public class AndroidLauncherActivity extends AppCompatActivity {

    private ViewPager2 vpHomeScreens;
    private LinearLayout pageIndicator;
    private LinearLayout dockContainer;
    private FloatingActionButton fabAppDrawer;
    private EditText searchInputLauncher;
    
    private HomeScreenPagerAdapter pagerAdapter;
    private int totalPages = 3; // Default 3 home screens
    private AppUsageTracker appUsageTracker;
    private ThemeManager themeManager;
    private LauncherTheme currentTheme;

    private final BroadcastReceiver themeChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            recreate(); // Re-apply theme by recreating the activity
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_launcher);

        initializeViews();
        appUsageTracker = new AppUsageTracker(this);
        themeManager = ThemeManager.getInstance(this);
        currentTheme = themeManager.getCurrentTheme();

        applyCurrentTheme();
        setupHomeScreens();
        setupDock();
        setupAppDrawer();
        setupSearch();

        registerReceiver(themeChangedReceiver, new IntentFilter("com.mobiverse.THEME_CHANGED"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(themeChangedReceiver);
    }

    private void initializeViews() {
        vpHomeScreens = findViewById(R.id.vp_home_screens);
        pageIndicator = findViewById(R.id.page_indicator);
        dockContainer = findViewById(R.id.dock_container);
        fabAppDrawer = findViewById(R.id.fab_app_drawer);
        searchInputLauncher = findViewById(R.id.et_search_input_launcher);
    }

    private void applyCurrentTheme() {
        if (currentTheme != null) {
            findViewById(android.R.id.content).setBackgroundColor(currentTheme.getPrimaryColor());
            fabAppDrawer.setBackgroundTintList(ColorStateList.valueOf(currentTheme.getAccentColor()));
        }
    }

    private void setupHomeScreens() {
        pagerAdapter = new HomeScreenPagerAdapter(this, totalPages);
        vpHomeScreens.setAdapter(pagerAdapter);
        
        // Set middle page as default
        vpHomeScreens.setCurrentItem(1, false);
        
        // Page change listener for indicator
        vpHomeScreens.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                updatePageIndicator(position);
            }
        });
        
        createPageIndicator();
    }

    private void createPageIndicator() {
        pageIndicator.removeAllViews();
        for (int i = 0; i < totalPages; i++) {
            android.view.View dot = new android.view.View(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                24, 24
            );
            params.setMargins(8, 0, 8, 0);
            dot.setLayoutParams(params);
            dot.setBackground(getDrawable(R.drawable.page_indicator_dot));
            dot.setAlpha(i == 1 ? 1.0f : 0.3f);
            pageIndicator.addView(dot);
        }
        updatePageIndicator(1);
    }

    private void updatePageIndicator(int position) {
        for (int i = 0; i < pageIndicator.getChildCount(); i++) {
            pageIndicator.getChildAt(i).setAlpha(i == position ? 1.0f : 0.3f);
        }
    }

    private void setupDock() {
        // Add 4-5 most used apps to dock
        // Note: Requires PACKAGE_USAGE_STATS permission
        List<String> dockApps = appUsageTracker.getMostUsedApps(5);
        
        // Create app icons for dock (simplified)
        for (String packageName : dockApps) {
            ImageView appIcon = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                (int) (64 * currentTheme.getIconSize()), (int) (64 * currentTheme.getIconSize())
            );
            params.setMargins(12, 0, 12, 0);
            appIcon.setLayoutParams(params);
            appIcon.setImageResource(R.drawable.ic_app_placeholder);
            appIcon.setOnClickListener(v -> launchApp(packageName));
            dockContainer.addView(appIcon);
        }
    }

    private void setupAppDrawer() {
        fabAppDrawer.setOnClickListener(v -> openAppDrawer());
    }

    private void setupSearch() {
        searchInputLauncher.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    Intent intent = new Intent(AndroidLauncherActivity.this, SearchActivity.class);
                    intent.putExtra("query", s.toString());
                    startActivity(intent);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void openAppDrawer() {
        // Open bottom sheet or full screen app drawer
        AppDrawerBottomSheet drawer = new AppDrawerBottomSheet();
        drawer.show(getSupportFragmentManager(), "app_drawer");
    }

    private void launchApp(String packageName) {
        Intent intent = getPackageManager()
            .getLaunchIntentForPackage(packageName);
        if (intent != null) {
            startActivity(intent);
        }
    }
}