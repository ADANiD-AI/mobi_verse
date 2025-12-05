
package com.mobiverse.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.FragmentActivity;

import com.mobiverse.MobiVerse.TasbihActivity;
import com.mobiverse.MobiVerse.MobiverseGame;
import com.mobiverse.MobiVerse.MobiverseVPN;
import com.mobiverse.MobiVerse.MobiverseBrowser;
import com.mobiverse.aikeyboard.AIKeyboardService;
import com.mobiverse.launcher.search.SearchActivity;

public class MainActivity extends AppCompatActivity {

    private ADANiDAuthManager authManager;
    private GenesisTxLogger txLogger;
    private String adanId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        authManager = new ADANiDAuthManager(this);
        txLogger = new GenesisTxLogger();

        // Start biometric authentication
        authManager.authenticateWith5Layers(this, new ADANiDAuthManager.AuthCallback() {
            @Override
            public void onAuthenticationSuccess(String authenticatedAdanId) {
                adanId = authenticatedAdanId;
                Toast.makeText(MainActivity.this, "ADANiD Authentication Successful: " + adanId, Toast.LENGTH_LONG).show();
                setupDashboard();
            }

            @Override
            public void onAuthenticationError(String errorMessage) {
                Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                // Handle authentication failure (e.g., close the app or show a retry option)
                finish();
            }
        });
    }

    private void setupDashboard() {
        GridLayout dashboardGrid = findViewById(R.id.dashboard_grid);
        dashboardGrid.setVisibility(View.VISIBLE);

        // Define modules
        String[] modules = {
            "Voice", "Game", "Tasbih", "Finance", "Quran", "Wallet",
            "VPN", "Browser", "AI Choice", "Threat Detector"
        };

        for (String module : modules) {
            AppCompatButton button = new AppCompatButton(this);
            button.setText(module);
            button.setOnClickListener(v -> {
                txLogger.logTx(adanId, "module_opened", module.toLowerCase());
                handleModuleClick(module);
            });
            dashboardGrid.addView(button);
        }
    }

    private void handleModuleClick(String module) {
        // Launch corresponding activities
        switch (module) {
            case "Tasbih":
                startActivity(new Intent(this, TasbihActivity.class));
                break;
            case "Game":
                startActivity(new Intent(this, MobiverseGame.class));
                break;
            case "VPN":
                startActivity(new Intent(this, MobiverseVPN.class));
                break;
            case "Browser":
                startActivity(new Intent(this, MobiverseBrowser.class));
                break;
            case "AI Choice":
                startActivity(new Intent(this, AIKeyboardService.class));
                break;
            default:
                Toast.makeText(this, module + " module coming soon!", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
