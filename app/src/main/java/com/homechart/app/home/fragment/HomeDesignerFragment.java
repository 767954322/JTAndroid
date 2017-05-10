package com.homechart.app.home.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.flexible.flexibleadapter.items.AbstractFlexibleItem;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.homechart.app.MyApplication;
import com.homechart.app.R;
import com.homechart.app.commont.matertab.MaterialTabs;
import com.homechart.app.home.activity.ConsumerHomeActivity;
import com.homechart.app.netutils.MPServerHttpManager;
import com.homechart.app.netutils.OkStringRequest;
import com.homechart.app.commont.slippingviewpager.NoSlippingViewPager;
import com.homechart.app.commont.utils.DensityUtil;
import com.homechart.app.commont.utils.GsonUtil;
import com.homechart.app.commont.utils.ToastUtils;
import com.homechart.app.commont.utils.UIUtils;
import com.homechart.app.commont.view.DesinerCityPopWin;
import com.homechart.app.picheader.HeaderItem;
import com.homechart.app.picheader.SimpleItem;
import com.homechart.app.home.adapter.SixProductsAdapter;
import com.homechart.app.home.bean.DesinerCityBean;
import com.homechart.app.home.bean.DesinerCityDataItemBean;
import com.homechart.app.home.bean.DesinerCityDataItemCityBean;
import com.homechart.app.home.bean.DesinerCityMyBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GPD on 2017/2/27.
 */

@SuppressLint("ValidFragment")
public class HomeDesignerFragment extends BaseFragment
        implements View.OnClickListener,
        SimpleItem.CityOnClick {

    private List<DesinerCityDataItemBean> desinerCityDataItemBeen;
    private List<DesinerCityDataItemCityBean> list_city;
    private List<AbstractFlexibleItem> headerItems;
    private List<DesinerCityMyBean> list;
    private List<String> list_citys;

    private ConsumerHomeActivity consumerHomeActivity;
    private NoSlippingViewPager noSlippingViewPager;
    private SixProductsAdapter sixProductsAdapter;
    private MaterialTabs pagerSlidingTabStrip;

    private DesinerCityPopWin desinerCityPop;
    private FragmentManager fragmentManager;
    private TextView moreCity;
    private int mHopTag = 0;
    private View lineView;

    public HomeDesignerFragment(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_home_designer;
    }

    @Override
    protected void initView() {
        consumerHomeActivity = (ConsumerHomeActivity) activity;
        pagerSlidingTabStrip = (MaterialTabs) rootView.findViewById(R.id.consumer_six_products_tabs);
        noSlippingViewPager = (NoSlippingViewPager) rootView.findViewById(R.id.consumer_six_products_viewPager);
        moreCity = (TextView) rootView.findViewById(R.id.tv_desiner_city_more);
        lineView = (View) rootView.findViewById(R.id.line_tital);
    }

    @Override
    protected void initListener() {
        super.initListener();
        moreCity.setOnClickListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        noSlippingViewPager.setPagingEnabled(false);
        list = new ArrayList<>();
        list_citys = new ArrayList<>();
        OkStringRequest.OKResponseCallback callback = new OkStringRequest.OKResponseCallback() {


            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(activity, "城市信息加载失败");
            }

            @Override
            public void onResponse(String s) {
                DesinerCityBean desinerCityBean = GsonUtil.jsonToBean(s, DesinerCityBean.class);
                desinerCityDataItemBeen = desinerCityBean.getData().getCitylist();
                for (int i = 0; i < desinerCityDataItemBeen.size(); i++) {
                    list_city = desinerCityDataItemBeen.get(i).getCitys();
                    String tag_name = desinerCityDataItemBeen.get(i).getCate_name();
                    DesinerCityMyBean cityMyBean;
                    for (int j = 0; j < list_city.size(); j++) {
                        cityMyBean = new DesinerCityMyBean(tag_name, list_city.get(j).getCity_name(), list_city.get(j).getCity_id());
                        if (mHopTag < 5) {
                            list.add(cityMyBean);
                            ++mHopTag;
                        }
                        list_citys.add(cityMyBean.getLetter());
                    }
                }
                getData(desinerCityDataItemBeen);
                flushUI();
            }
        };
        MPServerHttpManager.getInstance().getDesinerCityList(callback);
    }

    private void getData(List<DesinerCityDataItemBean> desinerCityDataItemBeen) {
        SimpleItem simpleItem;

        headerItems = new ArrayList<>();
        for (int i = 0; i < desinerCityDataItemBeen.size(); i++) {

            HeaderItem header = new HeaderItem(i + "");
            header.setTitle(desinerCityDataItemBeen.get(i).getCate_name());

            list_city = desinerCityDataItemBeen.get(i).getCitys();
            for (int j = 0; j < list_city.size(); j++) {
                int cityId = list_city.get(j).getCity_id();
                String cityName = list_city.get(j).getCity_name();
                simpleItem = new SimpleItem(String.valueOf(cityId), header, this);
                simpleItem.setTitle(cityName);
                headerItems.add(simpleItem);
            }
        }
    }

    private void flushUI() {
        sixProductsAdapter = new SixProductsAdapter(fragmentManager, list, activity);
        noSlippingViewPager.setAdapter(sixProductsAdapter);
        noSlippingViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Fragment fragment = sixProductsAdapter.getItem(position);
                fragment.onResume();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());
        noSlippingViewPager.setPageMargin(pageMargin);
        initTab();
    }

    private void initTab() {
//        pagerSlidingTabStrip.setScrollBarStyle();
        pagerSlidingTabStrip.setBackgroundColor(Color.WHITE);//Tab的背景色
        pagerSlidingTabStrip.setIndicatorColor(UIUtils.getColor(R.color.case_text_tital_name));//下滑指示器的颜色
        pagerSlidingTabStrip.setIndicatorHeight(DensityUtil.dip2px(activity, 2));//下滑指示器的高度
        pagerSlidingTabStrip.setTextColorSelected(UIUtils.getColor(R.color.case_text_tital_name));
        pagerSlidingTabStrip.setTextColorUnselected(UIUtils.getColor(R.color.bg_8f8f8f));//设置未选中的tab字体颜色
        pagerSlidingTabStrip.setTabPaddingLeftRight(45);//设置tab距离左右的padding值
        pagerSlidingTabStrip.setTabTypefaceSelectedStyle(Typeface.BOLD);//选中时候字体
        pagerSlidingTabStrip.setTabTypefaceUnselectedStyle(Typeface.NORMAL);//未选中时候字体
        pagerSlidingTabStrip.setTextSize(DensityUtil.dip2px(activity, 17));
//        pagerSlidingTabStrip.setPaddingMiddle(true);//设置tab控件居中
        pagerSlidingTabStrip.setOnClickItemListener(new MaterialTabs.OnClickItemListener() {
            @Override
            public void onClickItemListener(int position) {
                String name = "";
                switch (position) {
                    case 0:
                        name = "全国";
                        break;
                    case 1:
                        name = "北京";
                        break;
                    case 2:
                        name = "上海";
                        break;
                    case 3:
                        name = "广州";
                        break;
                    case 4:
                        name = "深圳";
                        break;
                }
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("designerList_action")  //事件类别
                        .setAction("筛选的城市")      //事件操作
                        .setLabel(name)
                        .build());
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("designerList_action")  //事件类别
                        .setAction("筛选的城市only")//事件操作
                        .build());
                Bundle bundle = new Bundle();
                bundle.putString("chooseCity", name);
                consumerHomeActivity.mFirebaseAnalytics.logEvent("筛选的城市", bundle);
            }
        });

        pagerSlidingTabStrip.setViewPager(noSlippingViewPager);
        noSlippingViewPager.setCurrentItem(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_desiner_city_more:
                if (headerItems != null) {
                    if (desinerCityPop == null) {
                        desinerCityPop = new DesinerCityPopWin(activity, headerItems, this);
                    }
                    if (desinerCityPop.isShowing()) {
                        MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                                .setCategory("designerList_action")  //事件类别
                                .setAction("收起城市列表")      //事件操作
                                .build());

                        Bundle bundle = new Bundle();
                        consumerHomeActivity.mFirebaseAnalytics.logEvent("收起城市列表", bundle);
                        moreCity.setText("更多");
                        desinerCityPop.dismiss();
                    } else {
                        MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                                .setCategory("designerList_action")  //事件类别
                                .setAction("展开城市列表")      //事件操作
                                .build());
                        Bundle bundle = new Bundle();
                        consumerHomeActivity.mFirebaseAnalytics.logEvent("展开城市列表", bundle);
                        moreCity.setText("收起");
                        desinerCityPop.showAsDropDown(lineView);
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void cityOnClick(String position, String name) {

        MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                .setCategory("designerList_action")  //事件类别
                .setAction("筛选的城市")      //事件操作
                .setLabel(name)
                .build());
        MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                .setCategory("designerList_action")  //事件类别
                .setAction("筛选的城市only")//事件操作
                .build());
        Bundle bundle = new Bundle();
        bundle.putString("chooseCity", name);
        consumerHomeActivity.mFirebaseAnalytics.logEvent("筛选的城市", bundle);

        DesinerCityMyBean brandItem = new DesinerCityMyBean("", name, Integer.parseInt(position.trim()));
        if (TextUtils.equals(name, "北京")) {
            noSlippingViewPager.setCurrentItem(1);
            if (desinerCityPop.isShowing()) {
                moreCity.setText("更多");
                desinerCityPop.dismiss();
            }
        } else if (TextUtils.equals(name, "上海")) {
            noSlippingViewPager.setCurrentItem(2);
            if (desinerCityPop.isShowing()) {
                moreCity.setText("更多");
                desinerCityPop.dismiss();
            }
        } else if (TextUtils.equals(name, "广州")) {
            noSlippingViewPager.setCurrentItem(3);
            if (desinerCityPop.isShowing()) {
                moreCity.setText("更多");
                desinerCityPop.dismiss();
            }
        } else if (TextUtils.equals(name, "深圳")) {
            noSlippingViewPager.setCurrentItem(4);
            if (desinerCityPop.isShowing()) {
                moreCity.setText("更多");
                desinerCityPop.dismiss();
            }
        } else {
            if (list.size() == 5) {
                list.add(brandItem);
            } else if (list.size() == 6) {
                list.remove(5);
                list.add(brandItem);
            }
            sixProductsAdapter.notifyDataSetChanged();
            noSlippingViewPager.setCurrentItem(list.size() - 1);
            if (desinerCityPop.isShowing()) {
                moreCity.setText("更多");
                desinerCityPop.dismiss();
            }
            updataSixFragment(brandItem.getCity_id());
        }
    }

    private void updataSixFragment(int city_id) {
        ShenCityFragment sixCityFragment = (ShenCityFragment) sixProductsAdapter.getItem(5);
        sixCityFragment.changeCityData(city_id);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Get tracker.
        Tracker t = MyApplication.getInstance().getDefaultTracker();
        // Set screen name.
        t.setScreenName("设计师列表页");
        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }
}
