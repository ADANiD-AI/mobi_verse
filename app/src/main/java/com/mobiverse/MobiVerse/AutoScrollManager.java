package com.mobiverse.MobiVerse;

import android.widget.ScrollView;

public class AutoScrollManager {

    private ScrollView scrollView;
    private boolean isScrolling;
    private int scrollSpeed;

    public AutoScrollManager(ScrollView scrollView) {
        this.scrollView = scrollView;
        this.isScrolling = false;
        this.scrollSpeed = 5; // Default speed
    }

    public void startAutoScroll() {
        if (!isScrolling) {
            isScrolling = true;
            new Thread(() -> {
                while (isScrolling) {
                    try {
                        Thread.sleep(100);
                        scrollView.post(() -> scrollView.smoothScrollBy(0, scrollSpeed));
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }).start();
        }
    }

    public void stopAutoScroll() {
        isScrolling = false;
    }

    public void setScrollSpeed(int speed) {
        this.scrollSpeed = speed;
    }

    public boolean isScrolling() {
        return isScrolling;
    }
}
