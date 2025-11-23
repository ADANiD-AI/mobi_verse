package com.mobiverse.MobiVerse;

import android.webkit.WebView;
import java.util.ArrayList;
import java.util.List;

public class MobiverseBrowser {

    private WebView webView;
    private List<String> tabs;
    private List<String> bookmarks;
    private List<String> history;
    private int currentTab;

    public MobiverseBrowser(WebView webView) {
        this.webView = webView;
        this.webView.getSettings().setJavaScriptEnabled(true);
        this.tabs = new ArrayList<>();
        this.bookmarks = new ArrayList<>();
        this.history = new ArrayList<>();
        this.currentTab = -1;
    }

    public void loadUrl(String url) {
        this.webView.loadUrl(url);
        if (currentTab != -1) {
            tabs.set(currentTab, url);
        }
        history.add(url);
    }

    public void addTab(String url) {
        tabs.add(url);
        currentTab = tabs.size() - 1;
        loadUrl(url);
    }

    public void removeTab(int tabId) {
        if (tabId >= 0 && tabId < tabs.size()) {
            tabs.remove(tabId);
            if (currentTab >= tabId) {
                currentTab--;
            }
            if (tabs.isEmpty()) {
                currentTab = -1;
            } else if (currentTab == -1) {
                currentTab = 0;
            }
            loadUrl(tabs.get(currentTab));
        }
    }

    public void switchTab(int tabId) {
        if (tabId >= 0 && tabId < tabs.size()) {
            currentTab = tabId;
            loadUrl(tabs.get(currentTab));
        }
    }

    public void addBookmark(String url) {
        bookmarks.add(url);
    }

    public void removeBookmark(String url) {
        bookmarks.remove(url);
    }

    public List<String> getBookmarks() {
        return bookmarks;
    }

    public List<String> getHistory() {
        return history;
    }

    public void clearHistory() {
        history.clear();
    }
}
