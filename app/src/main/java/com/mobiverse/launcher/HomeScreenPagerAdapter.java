package com.mobiverse.launcher;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class HomeScreenPagerAdapter extends FragmentStateAdapter {

    private final int totalPages;

    public HomeScreenPagerAdapter(@NonNull FragmentActivity fragmentActivity, int totalPages) {
        super(fragmentActivity);
        this.totalPages = totalPages;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return HomeScreenPageFragment.newInstance(position);
    }

    @Override
    public int getItemCount() {
        return totalPages;
    }
}