package com.mobiverse.aikeyboard;

import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class KeyboardSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyboard_settings);

        Button btnEnable = findViewById(R.id.btn_enable_keyboard);
        btnEnable.setOnClickListener(v -> {
            startActivity(new android.content.Intent(
                android.provider.Settings.ACTION_INPUT_METHOD_SETTINGS));
        });

        Button btnTest = findViewById(R.id.btn_test_keyboard);
        btnTest.setOnClickListener(v -> {
            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                .showInputMethodPicker();
        });
    }
}