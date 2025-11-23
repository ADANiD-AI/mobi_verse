package com.mobiverse.launcher.widget;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class WidgetManager {
    private static WidgetManager instance;
    private Context context;
    private SharedPreferences prefs;
    private Gson gson;
    
    private List<WidgetInfo> activeWidgets;

    private WidgetManager(Context context) {
        this.context = context.getApplicationContext();
        this.prefs = context.getSharedPreferences("launcher_widgets", Context.MODE_PRIVATE);
        this.gson = new Gson();
        
        loadActiveWidgets();
    }

    public static WidgetManager getInstance(Context context) {
        if (instance == null) {
            instance = new WidgetManager(context);
        }
        return instance;
    }

    public void addWidget(WidgetInfo widget) {
        activeWidgets.add(widget);
        saveActiveWidgets();
        notifyWidgetChange();
    }

    public void removeWidget(String widgetId) {
        activeWidgets.removeIf(w -> w.getId().equals(widgetId));
        saveActiveWidgets();
        notifyWidgetChange();
    }

    public List<WidgetInfo> getActiveWidgets() {
        return new ArrayList<>(activeWidgets);
    }

    public void updateWidgetSize(String widgetId, int rowSpan, int colSpan) {
        for (WidgetInfo widget : activeWidgets) {
            if (widget.getId().equals(widgetId)) {
                widget.setRowSpan(rowSpan);
                widget.setColSpan(colSpan);
                break;
            }
        }
        saveActiveWidgets();
        notifyWidgetChange();
    }

    private void loadActiveWidgets() {
        String json = prefs.getString("active_widgets", "[]");
        activeWidgets = gson.fromJson(json, new TypeToken<ArrayList<WidgetInfo>>(){}.getType());
        if (activeWidgets == null) activeWidgets = new ArrayList<>();
    }

    private void saveActiveWidgets() {
        String json = gson.toJson(activeWidgets);
        prefs.edit().putString("active_widgets", json).apply();
    }

    private void notifyWidgetChange() {
        // Broadcast to update the UI
        context.sendBroadcast(new android.content.Intent("com.mobiverse.WIDGET_CHANGED"));
    }
}