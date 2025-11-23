package com.mobiverse.launcher;

import android.graphics.drawable.Drawable;

public class AppInfo {
    private CharSequence label;
    private CharSequence packageName;
    private Drawable icon;

    public AppInfo(CharSequence label, CharSequence packageName, Drawable icon) {
        this.label = label;
        this.packageName = packageName;
        this.icon = icon;
    }

    public CharSequence getLabel() {
        return label;
    }

    public CharSequence getPackageName() {
        return packageName;
    }

    public Drawable getIcon() {
        return icon;
    }
}