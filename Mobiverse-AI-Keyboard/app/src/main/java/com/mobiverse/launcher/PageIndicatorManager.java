package com.mobiverse.launcher;

import android.content.Context;
import android.widget.LinearLayout;

public class PageIndicatorManager {

    private Context context;
    private LinearLayout pageIndicatorLayout;

    public PageIndicatorManager(Context context, LinearLayout pageIndicatorLayout) {
        this.context = context;
        this.pageIndicatorLayout = pageIndicatorLayout;
    }

    public void createPageIndicator(int totalPages, int initialPage) {
        pageIndicatorLayout.removeAllViews();
        for (int i = 0; i < totalPages; i++) {
            android.view.View dot = new android.view.View(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(24, 24);
            params.setMargins(8, 0, 8, 0);
            dot.setLayoutParams(params);
            dot.setBackground(context.getDrawable(R.drawable.page_indicator_dot));
            pageIndicatorLayout.addView(dot);
        }
        updatePageIndicator(initialPage);
    }

    public void updatePageIndicator(int position) {
        for (int i = 0; i < pageIndicatorLayout.getChildCount(); i++) {
            pageIndicatorLayout.getChildAt(i).setAlpha(i == position ? 1.0f : 0.3f);
        }
    }
}
