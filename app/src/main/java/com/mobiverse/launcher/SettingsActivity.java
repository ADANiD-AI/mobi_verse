package com.mobiverse.launcher;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.mobiverse.launcher.backup.BackupRestoreActivity;
import com.mobiverse.launcher.gestures.GestureSettingsActivity;
import com.mobiverse.launcher.theming.ThemeSettingsActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Find the CardView buttons
        CardView themeSettingsButton = findViewById(R.id.cv_theme_settings);
        CardView gestureSettingsButton = findViewById(R.id.cv_gesture_settings);
        CardView backupRestoreButton = findViewById(R.id.cv_backup_restore);
        CardView aboutButton = findViewById(R.id.cv_about);

        // Set click listeners
        themeSettingsButton.setOnClickListener(v -> {
            startActivity(new Intent(this, ThemeSettingsActivity.class));
        });

        gestureSettingsButton.setOnClickListener(v -> {
            startActivity(new Intent(this, GestureSettingsActivity.class));
        });

        backupRestoreButton.setOnClickListener(v -> {
            startActivity(new Intent(this, BackupRestoreActivity.class));
        });

        aboutButton.setOnClickListener(v -> {
            // Implement About screen later
        });
    }
}
