package com.RobX.MyHealth;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

// Created for the three tabs in the Article Fragment
public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull ArticleFragment ArticleFragment)
    {
        super(ArticleFragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position) {
            case 0:
                return new PressStatementFragment();
            case 1:
                return new VirusKnowledgeFragment();
            default:
                return new RumourClarificationFragment();
        }
    }
    @Override
    public int getItemCount() {return 3; }
}

