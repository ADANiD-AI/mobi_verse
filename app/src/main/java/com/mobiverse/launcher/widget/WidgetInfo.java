package com.mobiverse.launcher.widget;

import java.io.Serializable;

public class WidgetInfo implements Serializable {
    private String id;
    private WidgetType type;
    private int rowSpan;
    private int colSpan;

    public enum WidgetType {
        CLOCK,
        WEATHER,
        QUICK_SETTINGS,
        SEARCH_BAR,
        ISLAMIC_PRAYER_TIMES,
        QURAN_VERSE_OF_THE_DAY
    }

    public WidgetInfo(String id, WidgetType type, int rowSpan, int colSpan) {
        this.id = id;
        this.type = type;
        this.rowSpan = rowSpan;
        this.colSpan = colSpan;
    }

    // Getters and Setters
    public String getId() { return id; }
    public WidgetType getType() { return type; }
    public int getRowSpan() { return rowSpan; }
    public int getColSpan() { return colSpan; }
    public void setRowSpan(int span) { this.rowSpan = span; }
    public void setColSpan(int span) { this.colSpan = span; }
}