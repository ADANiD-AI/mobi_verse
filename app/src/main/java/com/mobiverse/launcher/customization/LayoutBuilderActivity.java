package com.mobiverse.launcher.customization;

import android.os.Bundle;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.mobiverse.launcher.themes.LauncherTheme;
import com.mobiverse.launcher.themes.ThemeManager;

public class LayoutBuilderActivity extends AppCompatActivity {

    private GridLayout previewGrid;
    private SeekBar sbColumns, sbRows, sbIconSize;
    private TextView tvColumns, tvRows, tvIconSize;
    private Spinner spLayoutStyle;
    private Button btnSave, btnReset;
    
    private LauncherTheme workingTheme;
    private ThemeManager themeManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_builder);

        themeManager = ThemeManager.getInstance(this);
        workingTheme = new LauncherTheme("custom_" + System.currentTimeMillis(), "My Layout");
        workingTheme.setLayoutStyle(LauncherTheme.LayoutStyle.CUSTOM_GRID);

        initializeViews();
        setupListeners();
        updatePreview();
    }

    private void initializeViews() {
        previewGrid = findViewById(R.id.preview_grid);
        sbColumns = findViewById(R.id.sb_columns);
        sbRows = findViewById(R.id.sb_rows);
        sbIconSize = findViewById(R.id.sb_icon_size);
        tvColumns = findViewById(R.id.tv_columns);
        tvRows = findViewById(R.id.tv_rows);
        tvIconSize = findViewById(R.id.tv_icon_size);
        spLayoutStyle = findViewById(R.id.sp_layout_style);
        btnSave = findViewById(R.id.btn_save);
        btnReset = findViewById(R.id.btn_reset);

        // Set default values
        sbColumns.setMax(10);
        sbColumns.setProgress(4);
        sbRows.setMax(10);
        sbRows.setProgress(5);
        sbIconSize.setMax(200);
        sbIconSize.setProgress(100);
    }

    private void setupListeners() {
        sbColumns.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress < 2) progress = 2;
                workingTheme.setGridColumns(progress);
                tvColumns.setText("Columns: " + progress);
                updatePreview();
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        sbRows.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress < 2) progress = 2;
                workingTheme.setGridRows(progress);
                tvRows.setText("Rows: " + progress);
                updatePreview();
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        sbIconSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float size = progress / 100.0f;
                if (size < 0.5f) size = 0.5f;
                workingTheme.setIconSize(size);
                tvIconSize.setText("Icon Size: " + (int)(size * 100) + "%");
                updatePreview();
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        btnSave.setOnClickListener(v -> saveLayout());
        btnReset.setOnClickListener(v -> resetToDefaults());
    }

    private void updatePreview() {
        previewGrid.removeAllViews();
        previewGrid.setColumnCount(workingTheme.getGridColumns());
        previewGrid.setRowCount(workingTheme.getGridRows());

        int totalSlots = workingTheme.getGridColumns() * workingTheme.getGridRows();
        int iconSizePx = (int) (48 * workingTheme.getIconSize());

        for (int i = 0; i < totalSlots && i < 20; i++) { // Limit to 20 for preview
            android.widget.ImageView iconView = new android.widget.ImageView(this);
            iconView.setImageResource(R.drawable.ic_app_placeholder);
            
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = iconSizePx;
            params.height = iconSizePx;
            params.setMargins(8, 8, 8, 8);
            iconView.setLayoutParams(params);
            
            previewGrid.addView(iconView);
        }
    }

    private void saveLayout() {
        themeManager.createCustomTheme(workingTheme);
        themeManager.applyTheme(workingTheme);
        finish();
    }

    private void resetToDefaults() {
        sbColumns.setProgress(4);
        sbRows.setProgress(5);
        sbIconSize.setProgress(100);
    }
}