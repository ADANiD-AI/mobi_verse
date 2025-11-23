package com.mobiverse.launcher.theming;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobiverse.launcher.R;

import java.util.List;

public class ThemeSettingsActivity extends AppCompatActivity implements ThemeSettingsAdapter.OnThemeSelectedListener {

    private ThemeManager themeManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme_settings);

        themeManager = ThemeManager.getInstance(this);

        RecyclerView recyclerView = findViewById(R.id.themes_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<LauncherTheme> availableThemes = themeManager.getAvailableThemes();
        ThemeSettingsAdapter adapter = new ThemeSettingsAdapter(this, availableThemes, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onThemeSelected(LauncherTheme theme) {
        themeManager.applyTheme(theme);
    }
}
