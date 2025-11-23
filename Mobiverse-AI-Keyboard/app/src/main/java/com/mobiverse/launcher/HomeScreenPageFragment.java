package com.mobiverse.launcher;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.mobiverse.aikeyboard.R;
import com.mobiverse.messaging.MessagingActivity;
import java.util.ArrayList;
import java.util.List;

public class HomeScreenPageFragment extends Fragment {

    private RecyclerView rvAppsGrid;
    private AppGridAdapter adapter;
    private int pageNumber;

    public static HomeScreenPageFragment newInstance(int pageNumber) {
        HomeScreenPageFragment fragment = new HomeScreenPageFragment();
        Bundle args = new Bundle();
        args.putInt("page_number", pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pageNumber = getArguments().getInt("page_number", 0);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, 
                             @Nullable ViewGroup container, 
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_screen_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        rvAppsGrid = view.findViewById(R.id.rv_apps_grid);
        
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 4);
        rvAppsGrid.setLayoutManager(layoutManager);
        
        List<AppInfo> apps = loadAppsForPage(pageNumber);
        adapter = new AppGridAdapter(apps);
        rvAppsGrid.setAdapter(adapter);

        adapter.setOnItemClickListener(appInfo -> {
            if ("Messages".equals(appInfo.getLabel())) {
                Intent intent = new Intent(getActivity(), MessagingActivity.class);
                startActivity(intent);
            } else {
                // Launch other apps
            }
        });
    }

    private List<AppInfo> loadAppsForPage(int page) {
        List<AppInfo> apps = new ArrayList<>();
        if (page == 0) { // Add to the first page
            AppInfo messagingApp = new AppInfo();
            messagingApp.setLabel("Messages");
            try {
                messagingApp.setIcon(getContext().getPackageManager().getApplicationIcon("com.google.android.apps.messaging"));
            } catch (Exception e) {
                // Set a default icon if messaging app icon is not found
                messagingApp.setIcon(getContext().getDrawable(R.mipmap.ic_launcher));
            }
            apps.add(messagingApp);
        }
        return apps;
    }
}