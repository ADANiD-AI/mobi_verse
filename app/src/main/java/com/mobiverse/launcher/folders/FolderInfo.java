package com.mobiverse.launcher.folders;

import java.util.ArrayList;
import java.util.List;

public class FolderInfo {
    private long id;
    private String name;
    private List<String> appPackages;
    private boolean isSmartFolder;

    public FolderInfo(String name, boolean isSmart) {
        this.id = System.currentTimeMillis();
        this.name = name;
        this.appPackages = new ArrayList<>();
        this.isSmartFolder = isSmart;
    }

    public long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public List<String> getAppPackages() { return appPackages; }
    public boolean isSmartFolder() { return isSmartFolder; }
    public void setAppPackages(List<String> packages) { this.appPackages = packages; }
    public void addApp(String packageName) { if (!appPackages.contains(packageName)) appPackages.add(packageName); }
    public void removeApp(String packageName) { appPackages.remove(packageName); }
}