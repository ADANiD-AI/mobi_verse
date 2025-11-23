package com.mobiverse.launcher.themes;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class LauncherTheme implements Serializable {
    private String id;
    private String name;
    private String author;
    private ThemeType type;
    
    // Visual Properties
    private String wallpaperUrl;
    private int primaryColor;
    private int accentColor;
    private int backgroundColor;
    private int textColor;
    
    // Layout Properties
    private LayoutStyle layoutStyle;
    private int gridColumns;
    private int gridRows;
    private int numHomeScreens; // New property
    private boolean showAppNames;
    private float iconSize;
    
    // Icon Pack
    private String iconPackId;
    
    // Advanced
    private Map<String, Object> customProperties;

    public enum ThemeType {
        PRESET, USER_CREATED, DOWNLOADED, AI_GENERATED
    }

    public enum LayoutStyle {
        IOS_STYLE, ANDROID_STOCK, WINDOWS_METRO, MIUI_STYLE, ONE_UI, CUSTOM_GRID, LIST_VIEW, CATEGORY_BASED
    }

    public LauncherTheme(String id, String name) {
        this.id = id;
        this.name = name;
        this.customProperties = new HashMap<>();
        this.numHomeScreens = 3; // Default value
    }

    // Getters and Setters
    public String getId() { return id; }
    public String getName() { return name; }
    public ThemeType getType() { return type; }
    public void setType(ThemeType type) { this.type = type; }
    
    public LayoutStyle getLayoutStyle() { return layoutStyle; }
    public void setLayoutStyle(LayoutStyle style) { this.layoutStyle = style; }
    
    public int getGridColumns() { return gridColumns; }
    public void setGridColumns(int columns) { this.gridColumns = columns; }
    
    public int getGridRows() { return gridRows; }
    public void setGridRows(int rows) { this.gridRows = rows; }

    public int getNumHomeScreens() { return numHomeScreens; }
    public void setNumHomeScreens(int num) { this.numHomeScreens = num; }
    
    public String getWallpaperUrl() { return wallpaperUrl; }
    public void setWallpaperUrl(String url) { this.wallpaperUrl = url; }
    
    public int getPrimaryColor() { return primaryColor; }
    public void setPrimaryColor(int color) { this.primaryColor = color; }
    
    public int getAccentColor() { return accentColor; }
    public void setAccentColor(int color) { this.accentColor = color; }
    
    public float getIconSize() { return iconSize; }
    public void setIconSize(float size) { this.iconSize = size; }
    
    public boolean isShowAppNames() { return showAppNames; }
    public void setShowAppNames(boolean show) { this.showAppNames = show; }
    
    public void setCustomProperty(String key, Object value) {
        customProperties.put(key, value);
    }
    
    public Object getCustomProperty(String key) {
        return customProperties.get(key);
    }
}
