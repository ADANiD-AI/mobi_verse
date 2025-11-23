package com.mobiverse.launcher.search;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SearchManager {

    private Context context;
    private List<SearchProvider> searchProviders;
    private ExecutorService executorService;

    public interface SearchCallback {
        void onSearchResults(List<SearchProvider.SearchResult> results);
    }

    public SearchManager(Context context) {
        this.context = context;
        this.searchProviders = new ArrayList<>();
        this.executorService = Executors.newCachedThreadPool();
        
        // Add default search providers
        searchProviders.add(new AppSearchProvider(context));
        searchProviders.add(new ContactSearchProvider(context));
        // You can add more providers here (e.g., for settings, files, web)
    }

    public void performSearch(String query, String correctedQuery, SearchCallback callback) {
        executorService.submit(() -> {
            List<SearchProvider.SearchResult> allResults = new ArrayList<>();
            for (SearchProvider provider : searchProviders) {
                allResults.addAll(provider.query(correctedQuery != null ? correctedQuery : query));
            }
            
            // Post results back to the main thread
            android.os.Handler mainHandler = new android.os.Handler(context.getMainLooper());
            mainHandler.post(() -> callback.onSearchResults(allResults));
        });
    }

    public void addSearchProvider(SearchProvider provider) {
        searchProviders.add(provider);
    }
}