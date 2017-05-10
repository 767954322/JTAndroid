package com.homechart.app.home.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.homechart.app.home.bean.DesinerCityMyBean;
import com.homechart.app.home.fragment.FiveCityFragment;
import com.homechart.app.home.fragment.GuangCityFragment;
import com.homechart.app.home.fragment.AllCityFragment;
import com.homechart.app.home.fragment.ShenCityFragment;
import com.homechart.app.home.fragment.ShangCityFragment;
import com.homechart.app.home.fragment.BeiCityFragment;

import java.util.List;

/**
 * Created by luchongbin on 16-8-16.
 */
public class SixProductsAdapter extends FragmentStatePagerAdapter {
    private List<DesinerCityMyBean> tabItems;
    private Context mContext;

    public SixProductsAdapter(FragmentManager fm) {
        super(fm);
    }

    public SixProductsAdapter(FragmentManager fm, List<DesinerCityMyBean> tabItems, Context context) {
        super(fm);
        this.tabItems = tabItems;
        this.mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment =  AllCityFragment.getInstance(tabItems.get(position).getCity_id(), mContext);
                break;
            case 1:
                fragment = BeiCityFragment.getInstance(tabItems.get(position).getCity_id(), mContext);
                break;
            case 2:
                fragment = ShangCityFragment.getInstance(tabItems.get(position).getCity_id(), mContext);
                break;
            case 3:
                fragment = GuangCityFragment.getInstance(tabItems.get(position).getCity_id(), mContext);
                break;
            case 4:
                fragment = FiveCityFragment.getInstance(tabItems.get(position).getCity_id(), mContext);
                break;
            case 5:
                fragment = ShenCityFragment.getInstance(tabItems.get(position).getCity_id(), mContext);
                break;
        }
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabItems.get(position).getName();
    }

    @Override
    public int getCount() {
        return tabItems.size();
    }
}
