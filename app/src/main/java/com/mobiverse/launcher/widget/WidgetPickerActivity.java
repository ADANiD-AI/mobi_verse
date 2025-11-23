package com.mobiverse.launcher.widget;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.mobiverse.launcher.R;

import java.util.ArrayList;
import java.util.List;

public class WidgetPickerActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private WidgetPickerAdapter adapter;
    private WidgetManager widgetManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_picker);

        widgetManager = WidgetManager.getInstance(this);
        recyclerView = findViewById(R.id.rv_widget_picker);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<WidgetInfo> availableWidgets = getAvailableWidgets();
        adapter = new WidgetPickerAdapter(this, availableWidgets, widgetInfo -> {
            widgetManager.addWidget(widgetInfo);
            finish(); // Close picker after adding
        });

        recyclerView.setAdapter(adapter);
    }

    private List<WidgetInfo> getAvailableWidgets() {
        List<WidgetInfo> widgets = new ArrayList<>();
        widgets.add(new WidgetInfo("clock_" + System.currentTimeMillis(), WidgetInfo.WidgetType.CLOCK, 2, 4));
        widgets.add(new WidgetInfo("weather_" + System.currentTimeMillis(), WidgetInfo.WidgetType.WEATHER, 2, 2));
        widgets.add(new WidgetInfo("search_" + System.currentTimeMillis(), WidgetInfo.WidgetType.SEARCH_BAR, 1, 4));
        widgets.add(new WidgetInfo("prayer_" + System.currentTimeMillis(), WidgetInfo.WidgetType.ISLAMIC_PRAYER_TIMES, 2, 4));
        widgets.add(new WidgetInfo("quran_" + System.currentTimeMillis(), WidgetInfo.WidgetType.QURAN_VERSE_OF_THE_DAY, 1, 4));
        widgets.add(new WidgetInfo("quick_settings_" + System.currentTimeMillis(), WidgetInfo.WidgetType.QUICK_SETTINGS, 1, 2));
        return widgets;
    }
}