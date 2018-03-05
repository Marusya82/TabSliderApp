package com.marina.tanasyuk.cityguide;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom Pager Adapter that allows adding {@link TabViewImpl}.
 */

public class TabViewPagerAdapter extends PagerAdapter {

    private final List<TabViewImpl> mCustomTabViewList = new ArrayList<>();
    private final String[] tabTitles = new String[]{"Bar", "Bistro", "Cafe"};
    private Context context;

    TabViewPagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return mCustomTabViewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    void addView(TabViewImpl customTabView) {
        mCustomTabViewList.add(customTabView);
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.item_tab, collection, false);
        collection.addView(layout);
        mCustomTabViewList.get(position).onAttachView(layout);
        return layout;
    }

    TabViewImpl getItem(int position) {
        return mCustomTabViewList.get(position);
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
        mCustomTabViewList.get(position).onDestroyView();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
