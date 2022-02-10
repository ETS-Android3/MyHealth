package com.RobX.MyHealth;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ArticleFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_article, container, false);

        // Set the colour of status bar
        getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getContext(),R.color.health_green));

        // region Declare and Link Variables
        TabLayout tabLayout = view.findViewById(R.id.articleTabLayout);
        ViewPager2 viewPager = view.findViewById(R.id.articleViewPager);
        // endregion

        // Use ViewPager to set the tab layout
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);
        new TabLayoutMediator(tabLayout, viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        switch (position)
                        {
                            // Rename the tabs
                            case 0: tab.setText(getResources().getString(R.string.press_statement)); break;
                            case 1: tab.setText(getResources().getString(R.string.virus_knowledge)); break;
                            case 2: tab.setText(getResources().getString(R.string.rumour_clarifications)); break;
                        }
                    }
                }).attach();

        // Inflate the layout for this fragment
        return view;
    }
}
