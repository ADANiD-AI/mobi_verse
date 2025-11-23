package com.mobiverse.launcher.search;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.ArrayList;
import java.util.List;

public class AppSearchProvider implements SearchProvider {

    private Context context;
    private PackageManager pm;

    public AppSearchProvider(Context context) {
        this.context = context;
        this.pm = context.getPackageManager();
    }

    @Override
    public List<SearchResult> query(String query) {
        List<SearchResult> results = new ArrayList<>();
        if (query == null || query.trim().isEmpty()) {
            return results;
        }

        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        final List<ResolveInfo> apps = pm.queryIntentActivities(mainIntent, 0);

        for (ResolveInfo info : apps) {
            String title = info.loadLabel(pm).toString();
            if (title.toLowerCase().contains(query.toLowerCase())) {
                Intent intent = pm.getLaunchIntentForPackage(info.activityInfo.packageName);
                results.add(new SearchResult(
                    SearchResultType.APP, 
                    title, 
                    null, 
                    info.loadIcon(pm), 
                    intent
                ));
            }
        }
        return results;
    }
}