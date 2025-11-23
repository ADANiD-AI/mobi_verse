package com.zai.choicescreen;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final int OVERLAY_PERMISSION_REQUEST_CODE = 1001;
    private Button btnStartService, btnStopService, btnSettings;
    private boolean isServiceRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initializeViews();
        setupClickListeners();
        checkOverlayPermission();
    }

    private void initializeViews() {
        btnStartService = findViewById(R.id.btnStartService);
        btnStopService = findViewById(R.id.btnStopService);
        btnSettings = findViewById(R.id.btnSettings);
        
        updateButtonStates();
    }

    private void setupClickListeners() {
        btnStartService.setOnClickListener(v -> startChoiceScreenService());
        btnStopService.setOnClickListener(v -> stopChoiceScreenService());
        btnSettings.setOnClickListener(v -> openSettings());
    }

    private void checkOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                showPermissionDialog();
            }
        }
    }

    private void showPermissionDialog() {
        Toast.makeText(this, "Overlay permission required for choice screen", Toast.LENGTH_LONG).show();
        requestOverlayPermission();
    }

    private void requestOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, OVERLAY_PERMISSION_REQUEST_CODE);
        }
    }

    private void startChoiceScreenService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            Toast.makeText(this, "Overlay permission required", Toast.LENGTH_SHORT).show();
            requestOverlayPermission();
            return;
        }

        Intent serviceIntent = new Intent(this, ChoiceScreenService.class);
        startService(serviceIntent);
        isServiceRunning = true;
        updateButtonStates();
        Toast.makeText(this, "Choice Screen activated", Toast.LENGTH_SHORT).show();
    }

    private void stopChoiceScreenService() {
        Intent serviceIntent = new Intent(this, ChoiceScreenService.class);
        stopService(serviceIntent);
        isServiceRunning = false;
        updateButtonStates();
        Toast.makeText(this, "Choice Screen deactivated", Toast.LENGTH_SHORT).show();
    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

    private void updateButtonStates() {
        btnStartService.setEnabled(!isServiceRunning);
        btnStopService.setEnabled(isServiceRunning);
        
        btnStartService.setAlpha(isServiceRunning ? 0.5f : 1.0f);
        btnStopService.setAlpha(isServiceRunning ? 1.0f : 0.5f);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == OVERLAY_PERMISSION_REQUEST_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {
                    Toast.makeText(this, "Permission granted! You can now start the service", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission denied. Service cannot work without overlay permission", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Check if service is still running
        // This is a simplified check - in production you might want to use a more robust method
        updateButtonStates();
    }
}