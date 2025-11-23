package com.mobiverse.launcher;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.mobiverse.aikeyboard.R;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AppDrawerBottomSheet extends BottomSheetDialogFragment {

    private RecyclerView rvAllApps;
    private AppGridAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, 
                             @Nullable ViewGroup container, 
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_app_drawer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvAllApps = view.findViewById(R.id.rv_all_apps);
        
        // 4 columns grid
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 4);
        rvAllApps.setLayoutManager(layoutManager);
        
        // Load all installed apps
        List<AppInfo> allApps = loadAllApps();
        adapter = new AppGridAdapter(allApps);
        rvAllApps.setAdapter(adapter);
    }

    private List<AppInfo> loadAllApps() {
        PackageManager pm = requireContext().getPackageManager();
        List<AppInfo> apps = new ArrayList<>();
        Intent i = new Intent(Intent.ACTION_MAIN, null);
        i.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> allAppsInfo = pm.queryIntentActivities(i, 0);
        for (ResolveInfo ri : allAppsInfo) {
            apps.add(new AppInfo(ri.loadLabel(pm), ri.activityInfo.packageName, ri.loadIcon(pm)));
        }

        // Sort the apps alphabetically
        Collections.sort(apps, (app1, app2) -> app1.getLabel().toString().compareTo(app2.getLabel().toString()));

        return apps;
    }
}