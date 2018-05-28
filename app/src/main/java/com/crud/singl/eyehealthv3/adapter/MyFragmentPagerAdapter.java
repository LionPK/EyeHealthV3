package com.crud.singl.eyehealthv3.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import com.crud.singl.eyehealthv3.R;
import com.crud.singl.eyehealthv3.fragment.InstalledFragment;
import com.crud.singl.eyehealthv3.fragment.StatsFragment;
import com.crud.singl.eyehealthv3.fragment.TopFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Copyright by Mr.Praneed Klanboon
 * Email: Praneed.kla@northbkk.ac.th
 * */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter
    implements ViewPager.OnPageChangeListener {

    private static final int FRAGMENT_POSITION_STATS = 0;
    private static final int FRAGMENT_POSITION_TOP = 1;
    private static final int FRAGMENT_POSITION_INSTALLED = 2;
    private static final int PAGE_COUNT = 3;

    private Context context;
    private Map<Integer, Fragment> tabs = new HashMap<>();
    private List<String> tabTitles = new ArrayList<>(3);

    public MyFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;

        tabTitles.add(context.getResources().getString(R.string.tab_stats));
        tabTitles.add(context.getResources().getString(R.string.tab_top));
        tabTitles.add(context.getResources().getString(R.string.tab_installed));

        tabs.put(FRAGMENT_POSITION_STATS, new StatsFragment());
        tabs.put(FRAGMENT_POSITION_TOP, new TopFragment());
        tabs.put(FRAGMENT_POSITION_INSTALLED, new InstalledFragment());
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        return tabs.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles.get(position);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}