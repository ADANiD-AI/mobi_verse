package com.mobiverse.launcher.search;

import java.util.List;

public interface SearchProvider {
    enum SearchResultType {
        APP,
        CONTACT,
        SETTING,
        WEB_SUGGESTION,
        CUSTOM_ACTION
    }

    class SearchResult {
        public final SearchResultType type;
        public final CharSequence title;
        public final CharSequence description;
        public final android.graphics.drawable.Drawable icon;
        public final android.content.Intent intent;

        public SearchResult(SearchResultType type, CharSequence title, CharSequence description, android.graphics.drawable.Drawable icon, android.content.Intent intent) {
            this.type = type;
            this.title = title;
            this.description = description;
            this.icon = icon;
            this.intent = intent;
        }
    }

    List<SearchResult> query(String query);
}