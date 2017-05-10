package com.homechart.app.home.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.homechart.app.MyApplication;
import com.homechart.app.MyHXChatActivity;
import com.homechart.app.R;
import com.homechart.app.commont.constants.ClassConstant;
import com.homechart.app.commont.utils.ChatUtils;
import com.homechart.app.commont.utils.CommontUtils;
import com.homechart.app.commont.utils.GsonUtil;
import com.homechart.app.commont.utils.SharedPreferencesUtils;
import com.homechart.app.commont.utils.ToastUtils;
import com.homechart.app.commont.utils.UIUtils;
import com.homechart.app.commont.view.HomeTabPopWin;
import com.homechart.app.home.activity.CaseLibraryDetailsActivity;
import com.homechart.app.home.activity.ConsumerHomeActivity;
import com.homechart.app.home.activity.DesinerCenterActivity;
import com.homechart.app.home.activity.UserLoginActivity;
import com.homechart.app.home.adapter.HomePicListAdapter;
import com.homechart.app.home.adapter.HomePicPagerAdapter;
import com.homechart.app.home.adapter.HomePicTabAdapter;
import com.homechart.app.home.bean.HomePicDateBean;
import com.homechart.app.home.bean.MyTabBean;
import com.homechart.app.home.bean.PicDateItemBean;
import com.homechart.app.home.bean.TabAllBean;
import com.homechart.app.netutils.MPServerHttpManager;
import com.homechart.app.netutils.OkStringRequest;
import com.homechart.app.widget.pulltprefresh.PullToRefreshLayout;
import com.homechart.app.widget.pulltprefresh.SwipePullListView;
import com.homechart.app.widget.widget.NavigationBar;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.umeng.socialize.utils.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomePicFragment extends BaseFragment implements
        PullToRefreshLayout.OnRefreshListener, HomePicTabAdapter.OnTabClick,
        ViewPager.OnPageChangeListener, HomePicPagerAdapter.OnGridViewItemClick,
        ListView.OnItemClickListener, NavigationBar.BarMenuClick,
        HomePicListAdapter.TagClickListener {

    private List<String> mPicTabList;
    private List<String> mPicTabListChange;
    public static TabAllBean tabAllBean_enable;

    private List<NavigationBar.MenuItem> mNavigationMenus;
    private Map<Integer, String> mSelectedMap;
    // private RecyclerView mListViewTab;
    private HomePicTabAdapter picTabAdapter;
    private PullToRefreshLayout mPullToRefreshLayout;
    private final String REFRESH_STATUS = "refresh";
    private final String LOADMORE_STATUS = "loadmore";
    private SwipePullListView mListView;
    private int picNum = 10;//每页数
    private int pageNum = 1;//第几页
    private int pageSelect = 0;
    private String mStyle = "";//默认风格
    private String mSpace = "";//默认空间
    private String spaceChild = "";//默认空间子级
    private List<PicDateItemBean> listItem;
    private List<PicDateItemBean> showList;
    private HomePicListAdapter mListAdapter;
    public TabAllBean tabAllBean;
    private HomeTabPopWin takePhotoPopWin;
    private List<List<MyTabBean>> lists;
    private List<List<MyTabBean>> lists_enable;
    private int selectSpacePositon = -1;
    private int idClickSpacePosition = 0;
    private HorizontalScrollView mScrollView;
    private NavigationBar mNavigationBar;
    private ConsumerHomeActivity consumerHomeActivity;
    private String user_id;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_home_pic;
    }

    @Override
    protected void initView() {
        consumerHomeActivity = (ConsumerHomeActivity) activity;
        // mListViewTab = (RecyclerView) rootView.findViewById(R.id.rv_pic_recyclerview);
        mScrollView = (HorizontalScrollView) rootView.findViewById(R.id.rv_pic_scrollview);
        mNavigationBar = (NavigationBar) rootView.findViewById(R.id.rv_pic_navigation);
        mPullToRefreshLayout = ((PullToRefreshLayout) rootView.findViewById(R.id.refresh_view));
        mListView = (SwipePullListView) rootView.findViewById(R.id.message_center_list_view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void initListener() {
        super.initListener();
        mPullToRefreshLayout.setOnRefreshListener(this);
        mListView.setOnItemClickListener(this);
        mNavigationBar.setBarMenuClick(this);
        takePhotoPopWin.setCallBack(mCallBack);
    }

    private HomeTabPopWin.PopupWindowCallBack mCallBack = new HomeTabPopWin.PopupWindowCallBack() {
        @Override
        public void onDismiss() {
            mNavigationMenus.get(pageSelect).setChoose(false);
            mNavigationMenus.get(pageSelect).
                    setSelected(mSelectedMap.get(pageSelect) != null);
            mNavigationBar.refreshView();
        }

        @Override
        public void onDelete(int position) {
            takePhotoPopWin.dismiss();
            takePhotoPopWin.deleteItem(position);
            mSelectedMap.remove(position);
            if (position == 0) {
                mStyle = "";
            } else if (position == 1) {
                mSpace = "";
                spaceChild = "";
            } else {
                spaceChild = "";
            }
            if (position == 1) {
                mNavigationMenus.get(1).setmMenuName(
                        UIUtils.getString(R.string.fragment_tab_space));
                int size = mNavigationMenus.size();
                for (; 2 < size; ) {
                    mNavigationMenus.remove(size - 1);
                    size--;
                }
                for (; 2 < lists.size(); ) {
                    lists.remove(lists.size() - 1);
                }
                takePhotoPopWin.setNotityChangeData(lists, null, false);
            } else if (position == 0) {
                mNavigationMenus.get(0).setmMenuName(
                        UIUtils.getString(R.string.fragment_tab_style));
                //TODO 重新获取数据
                takePhotoPopWin.setNotityChangeData(lists, null, false);
            }
            onSelectMenu(position, false);

            getPicData(REFRESH_STATUS);
        }
    };

    @Override
    protected void initData(Bundle savedInstanceState) {
        showList = new ArrayList<>();
        initListView();
        initTabView();
        initPop();
        getTabDate();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Get tracker.
        Tracker t = MyApplication.getInstance().getDefaultTracker();
        // Set screen name.
        t.setScreenName("首页");
        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }

    /**
     * 初始化ListView
     */
    private void initListView() {
        mListAdapter = new HomePicListAdapter(listItem, activity);
        mListAdapter.setClickListener(this);
        mListView.setAdapter(mListAdapter);
        onRefresh(mPullToRefreshLayout);
    }

    /**
     * 初始化Pop窗口
     */
    private void initPop() {
        if (null == takePhotoPopWin) {
            takePhotoPopWin = new HomeTabPopWin(activity, HomePicFragment.this, lists, this);
        }
    }

    /**
     * 初始化Tab
     */
    private void initTabView() {
        if (mPicTabList == null) {
            mPicTabList = new ArrayList<>();
        }
        mPicTabList.clear();
        mPicTabListChange = new ArrayList<>();
        mPicTabList.add(UIUtils.getString(R.string.fragment_tab_style));
        mPicTabList.add(UIUtils.getString(R.string.fragment_tab_space));
        mNavigationMenus = new ArrayList<>();
        mNavigationMenus.add(
                new NavigationBar.MenuItem(UIUtils.getString(R.string.fragment_tab_style)));
        mNavigationMenus.add(
                new NavigationBar.MenuItem(UIUtils.getString(R.string.fragment_tab_space)));
        mNavigationBar.setMenuItems(mNavigationMenus);
        mSelectedMap = new HashMap<>();
    }

    //获取Tab图片成功后更新Pop
    private void getTabDate() {
        if (lists == null) {
            OkStringRequest.OKResponseCallback callback = new OkStringRequest.OKResponseCallback() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    ToastUtils.showCenter(activity, "图片类型获取失败");
                }

                @Override
                public void onResponse(String s) {
                    tabAllBean = GsonUtil.jsonToBean(s, TabAllBean.class);
                    if (tabAllBean != null) {
                        lists = CommontUtils.getHomeTabDate(selectSpacePositon, tabAllBean);
                        if (null == takePhotoPopWin) {
                            takePhotoPopWin = new HomeTabPopWin(activity, HomePicFragment.this, lists, HomePicFragment.this);
                        }
                        takePhotoPopWin.setNotityChangeData(lists, null, false);
                    } else {
                        ToastUtils.showCenter(activity, "图片类型获取失败");
                    }
                }
            };
            MPServerHttpManager.getInstance().getHomeTabData(callback);
        } else {
            if (null == takePhotoPopWin) {
                takePhotoPopWin = new HomeTabPopWin(activity, HomePicFragment.this, lists, HomePicFragment.this);
            }
            takePhotoPopWin.setNotityChangeData(lists, null, false);
        }
    }

    //获取首页图片列表
    private void getPicData(final String state) {
        if (state.equals(REFRESH_STATUS)) {
            pageNum = 1;
        } else {
            ++pageNum;
            MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                    .setCategory("home_action")  //事件类别
                    .setAction("加载下一页图片")      //事件操作
                    .setLabel(pageNum + "")
                    .build());
            MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                    .setCategory("home_action")  //事件类别
                    .setAction("加载下一页图片only")//事件操作
                    .build());
            Bundle bundle = new Bundle();
            bundle.putString("page", pageNum + "");
            consumerHomeActivity.mFirebaseAnalytics.logEvent("加载下一页图片", bundle);
        }
        OkStringRequest.OKResponseCallback callback = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (state.equals(REFRESH_STATUS)) {
                    mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                } else {
                    mPullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL);
                }
            }

            @Override
            public void onResponse(String s) {
                HomePicDateBean homePicDateBean = GsonUtil.jsonToBean(s, HomePicDateBean.class);
                if (null != homePicDateBean && null != homePicDateBean.getData() && null != homePicDateBean.getData().getItems()) {
                    listItem = homePicDateBean.getData().getItems();
                    updateViewFromData(listItem, state);
                }
                if (state.equals(REFRESH_STATUS)) {
                    mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                } else {
                    mPullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                }
            }
        };
        MPServerHttpManager.getInstance().getHomePicData(pageNum, picNum, mStyle, mSpace, callback);
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        pageNum = 1;
        getPicData(REFRESH_STATUS);
        getTabDate();
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        getPicData(LOADMORE_STATUS);
    }

    private void updateViewFromData(List<PicDateItemBean> messages, String state) {
        switch (state) {
            case REFRESH_STATUS:
                showList.clear();
                showList.addAll(messages);
                mListAdapter.addMoreData(showList);
                mListView.setSelection(0);
                break;
            case LOADMORE_STATUS:
                showList.addAll(messages);
                mListAdapter.addMoreData(showList);
                break;
        }
        if (state.equals(REFRESH_STATUS)) {
            mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
        } else {
            mPullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
        }
    }

    @Override
    public void onTabClick(int position) {
        if (takePhotoPopWin == null) {
            takePhotoPopWin = new HomeTabPopWin(activity, this, lists, this);
        }
        // takePhotoPopWin.showAsDropDown(mListViewTab);
        takePhotoPopWin.setViewPagerNum(position);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    private int mLastPosition;

    @Override
    public void onPageSelected(int position) {
        if (pageSelect != position) {
            pageSelect = position;
            updateNavigation(position, true);
            if (position >= 4 || (mLastPosition >= 4 && mLastPosition > position)) {
                mScrollView.smoothScrollTo(CommontUtils.getScreenWidth(getContext()) / 4 * position, 0);
            } else {
                mScrollView.smoothScrollTo(0, 0);
            }

            if (mSelectedMap != null)
                takePhotoPopWin.setSelectedName(mSelectedMap.get(position));
        }
        mLastPosition = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != takePhotoPopWin) {
            takePhotoPopWin.dismiss();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onMenuClick(int position) {
        boolean isClick = mNavigationMenus.get(position).isChoose();
        if (isClick) {
            takePhotoPopWin.dismiss();
            mNavigationMenus.get(position).setChoose(false);
            mNavigationMenus.get(position).setSelected(mSelectedMap.get(position) != null);
            mNavigationBar.refreshView();
        } else {
            updateNavigation(position, true);
            if (takePhotoPopWin == null) {
                takePhotoPopWin = new HomeTabPopWin(activity, this, lists, this);
            }
            takePhotoPopWin.showAsDropDown(mNavigationBar);
            takePhotoPopWin.setViewPagerNum(position);
        }

        if (mSelectedMap != null)
            takePhotoPopWin.setSelectedName(mSelectedMap.get(position));
    }

    // 点击
    private void updateNavigation(int position, boolean choose) {
        for (int i = 0; i < mNavigationMenus.size(); i++) {
            if (mSelectedMap.get(i) != null)
                mNavigationMenus.get(i).setSelected(true);

            mNavigationMenus.get(i).setChoose(false);
        }
        mNavigationMenus.get(position).setChoose(choose);
        mNavigationMenus.get(position).setSelected(false);
        mNavigationBar.refreshView();
    }

    private void onSelectMenu(int position, boolean select) {
        if (position > 1) {
            for (int i = 2; i < mNavigationMenus.size(); i++) {
                if (i != position) {
                    mNavigationMenus.get(i).setChoose(false);
                    mNavigationMenus.get(i).setSelected(false);
                }
            }
            mNavigationMenus.get(position).setSelected(select);
            mNavigationMenus.get(position).setChoose(false);
        } else if (position == 0) {
            mNavigationMenus.get(position).setSelected(select);
            mNavigationMenus.get(position).setChoose(false);
        } else if (position == 1) {
            mNavigationMenus.get(position).setSelected(select);
            mNavigationMenus.get(position).setChoose(false);
        }
        mNavigationBar.refreshView();
    }

    private void updateSelected(boolean isClear, int position, String name) {
        String noeMap = mSelectedMap.get(0);
        String twoMap = mSelectedMap.get(1);
        mSelectedMap.clear();
        mSelectedMap.put(0, noeMap);
        mSelectedMap.put(1, twoMap);
        if (!isClear) {
            mSelectedMap.put(position, name);
        }
    }

    @Override
    public void onGridViewItemClick(int mPagePosition, final int position, String tag_id) {
        if (null != takePhotoPopWin) {
            takePhotoPopWin.dismiss();
        }
        if (mPagePosition == 0) {
            boolean isSelect;
            idClickSpacePosition = 0;
            String names = lists.get(mPagePosition).get(position).getSpace_tag_name();
            if (names.equals(mSelectedMap.get(mPagePosition))) {
                mNavigationMenus.get(mPagePosition).setmMenuName(
                        UIUtils.getString(R.string.fragment_tab_style));
                isSelect = false;
                mStyle = "";
                mSelectedMap.remove(mPagePosition);
                getPicData(REFRESH_STATUS);
            } else {
                mNavigationMenus.get(mPagePosition).setmMenuName(names);
                mSelectedMap.put(mPagePosition, names);
                isSelect = true;
                mStyle = tag_id;
                getPicData(REFRESH_STATUS);
            }
            //..................................................
            OkStringRequest.OKResponseCallback callback = new OkStringRequest.OKResponseCallback() {

                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    ToastUtils.showCenter(activity, "图片类型获取失败");
                }

                @Override
                public void onResponse(String s) {
                    tabAllBean_enable = GsonUtil.jsonToBean(s, TabAllBean.class);
                    lists_enable = CommontUtils.getHomeTabDate1(position, tabAllBean_enable);
                    handler1.sendEmptyMessage(0);
                }
            };
            MPServerHttpManager.getInstance().getHomeTabData(mStyle, mSpace, callback);
            //..................................................

            onSelectMenu(mPagePosition, isSelect);
        } else if (mPagePosition == 1) {
            spaceChild = tag_id;
            idClickSpacePosition = 1;
            boolean isSelect;
            List<String> names = CommontUtils.getTabName1(tabAllBean, mPicTabList, 1, position);
            if (names.get(1).equals(mSelectedMap.get(mPagePosition))) {
                mNavigationMenus.get(mPagePosition).setmMenuName(
                        UIUtils.getString(R.string.fragment_tab_space));
                isSelect = false;
                mSelectedMap.remove(mPagePosition);
                int size = mNavigationMenus.size();
                for (; 2 < size; ) {
                    mNavigationMenus.remove(size - 1);
                    size--;
                }
                mSpace = "";
                for (; 2 < lists.size(); ) {
                    lists.remove(lists.size() - 1);
                }
                takePhotoPopWin.deleteUpData();
                takePhotoPopWin.setNotityChangeData(lists, null, false);
                getPicData(REFRESH_STATUS);
            } else {
                isSelect = true;
                int size = mNavigationMenus.size();
                for (; 2 != size; ) {
                    mNavigationMenus.remove(size - 1);
                    size = mNavigationMenus.size();
                }
                updateSelected(true, -1, null);
                mNavigationMenus.get(mPagePosition).setmMenuName(names.get(1));
                mSelectedMap.put(mPagePosition, names.get(1));
                for (int i = 2; i < names.size(); i++) {
                    mNavigationMenus.add(new NavigationBar.MenuItem(names.get(i)));
                }
                lists = CommontUtils.getHomeTabDate(position, tabAllBean);
                takePhotoPopWin.setNotityChangeData(lists, null, false);
                mSpace = tag_id;
                //..................................................
                OkStringRequest.OKResponseCallback callback = new OkStringRequest.OKResponseCallback() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        ToastUtils.showCenter(activity, "图片类型获取失败");
                    }

                    @Override
                    public void onResponse(String s) {
                        tabAllBean_enable = GsonUtil.jsonToBean(s, TabAllBean.class);
                        lists_enable = CommontUtils.getHomeTabDate1(position, tabAllBean_enable);
                        handler1.sendEmptyMessage(0);
                    }
                };
                MPServerHttpManager.getInstance().getHomeTabData(mStyle, mSpace, callback);
                //..................................................


            }
            onSelectMenu(mPagePosition, isSelect);
            getPicData(REFRESH_STATUS);
        } else {
            mSpace = spaceChild + "," + tag_id;
            idClickSpacePosition = 2;
            boolean isSelect;
            String name = lists.get(mPagePosition).get(position).getSpace_tag_name();

            if (name.equals(mSelectedMap.get(mPagePosition))) {
                isSelect = false;
                mSelectedMap.remove(mPagePosition);
            } else {
                isSelect = true;
                updateSelected(false, mPagePosition, name);
            }
            onSelectMenu(mPagePosition, isSelect);
            getPicData(REFRESH_STATUS);
        }
    }

    Handler handler1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            takePhotoPopWin.setNotityChangeData(lists, lists_enable, true);
        }
    };

    public void tong(String name) {
        MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                .setCategory("home_action")  //事件类别
                .setAction("点击图片标签")      //事件操作
                .setLabel(name)
                .build());
        MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                .setCategory("home_action")  //事件类别
                .setAction("点击图片标签only")//事件操作
                .build());
        Bundle bundle = new Bundle();
        bundle.putString("label", name);
        consumerHomeActivity.mFirebaseAnalytics.logEvent("点击图片标签", bundle);
    }


    @Override
    public void onClickSpace(String space, String spaceId, String nikeName) {
        tong(space);
        String selectSpace = mSelectedMap.get(1);
        if (TextUtils.isEmpty(selectSpace)) {
            mNavigationMenus.get(1).setmMenuName(space);
            mSelectedMap.put(1, space);
            onSelectMenu(1, true);
            // 根据选择的空间名，获取后续菜单集合
            // 添加到mNavigationMenus集合中，
            // 更新头部菜单
            int position = 0;
            if (lists.size() > 1) {
                for (int i = 0; i < lists.get(1).size(); i++) {
                    if (lists.get(1).get(i).getSpace_tag_name().equals(space))
                        position = i;
                }
            }
            mSpace = spaceId;
            getPicData(REFRESH_STATUS);
            takePhotoPopWin.putSelectMap(1, space);
            List<String> names = CommontUtils.getTabName1(tabAllBean, mPicTabList, 1, position);
            for (int i = 2; i < names.size(); i++) {
                mNavigationMenus.add(new NavigationBar.MenuItem(names.get(i)));
            }
            lists = CommontUtils.getHomeTabDate(position, tabAllBean);
            mNavigationBar.refreshView();
            //TODO 重新获取数据
            takePhotoPopWin.setNotityChangeData(lists, null, false);
        }
    }

    @Override
    public void onClickStyle(String style, String styleId, String nikeName) {
        tong(style);
        String selectStyle = mSelectedMap.get(0);
        if (TextUtils.isEmpty(selectStyle)) {
            mNavigationMenus.get(0).setmMenuName(style);
            mSelectedMap.put(0, style);
            mStyle = styleId;
            getPicData(REFRESH_STATUS);
            onSelectMenu(0, true);
            takePhotoPopWin.putSelectMap(0, style);
        }
    }

    @Override
    public void onClickPic(int position) {
        if (takePhotoPopWin.isShowing()) {
            takePhotoPopWin.dismiss();
        }
        if (showList != null) {
            MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                    .setCategory("home_action")  //事件类别
                    .setAction("图片页点击图片")      //事件操作
                    .build());
            Bundle bundle = new Bundle();
            consumerHomeActivity.mFirebaseAnalytics.logEvent("图片页点击图片", bundle);


            Intent intent = new Intent(activity, CaseLibraryDetailsActivity.class);
            intent.putExtra(ClassConstant.CaseLibraryDetailsKey.ALBUM_ID, showList.get(position).getAlbum_id());
            intent.putExtra(ClassConstant.CaseLibraryDetailsKey.ITEM_ID, showList.get(position).getItem_id());
            startActivity(intent);
        }
    }


    @Override
    public void onJumpChat(int position) {
        MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                .setCategory("home_action")  //事件类别
                .setAction("咨询设计师按钮点击")      //事件操作
                .setLabel(showList.get(position).getNickname())
                .build());
        MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                .setCategory("home_action")  //事件类别
                .setAction("咨询设计师按钮点击only")//事件操作
                .build());
        Bundle bundle = new Bundle();
        bundle.putString("designerName", showList.get(position).getNickname());
        consumerHomeActivity.mFirebaseAnalytics.logEvent("咨询设计师按钮点击", bundle);
        String ifLogin = SharedPreferencesUtils.readString("login_in");
        String ticket = SharedPreferencesUtils.readString("ticket");
        if (!TextUtils.isEmpty(ifLogin)) {
            if (null != showList && showList.size() > 0) {

                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("home_action")  //事件类别
                        .setAction("成功进入咨询页面")      //事件操作
                        .setLabel(showList.get(position).getNickname())
                        .build());
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("home_action")  //事件类别
                        .setAction("成功进入咨询页面only")//事件操作
                        .build());
                Bundle bundle1 = new Bundle();
                bundle1.putString("designerName", showList.get(position).getNickname());
                consumerHomeActivity.mFirebaseAnalytics.logEvent("成功进入咨询页面", bundle1);

                String userid = showList.get(position).getUser_id();
                getChatList(userid, ticket);
                EaseUser easeUser = new EaseUser(userid);
                easeUser.setAvatar(showList.get(position).getAvatar());
                easeUser.setNickname(showList.get(position).getNickname());
                ChatUtils.getInstance().put(userid, easeUser);
                Intent intent1 = new Intent(activity, MyHXChatActivity.class);
                intent1.putExtra(EaseConstant.EXTRA_USER_ID, userid);
                startActivity(intent1);
            } else {
                ToastUtils.showCenter(activity, "信息获取失败，请重新返回重新获取信息");
            }
        } else {
            Intent intent2 = new Intent(activity, UserLoginActivity.class);
            startActivityForResult(intent2, 0);
        }
    }

    @Override
    public void onJumpDesinerCenter(int position) {
        if (null != showList && showList.size() > 0) {
            MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                    .setCategory("home_action")  //事件类别
                    .setAction("设计师点击")      //事件操作
                    .setLabel(showList.get(position).getNickname())
                    .build());
            MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                    .setCategory("home_action")  //事件类别
                    .setAction("设计师点击only")//事件操作
                    .build());
            Bundle bundle = new Bundle();
            bundle.putString("designerName", showList.get(position).getNickname());
            consumerHomeActivity.mFirebaseAnalytics.logEvent("图片页点击设计师", bundle);

            Intent intent = new Intent(activity, DesinerCenterActivity.class);
            intent.putExtra("user_id", showList.get(position).getUser_id());
            intent.putExtra("ic_default_head", showList.get(position).getAvatar());
            intent.putExtra("name", showList.get(position).getNickname());
            startActivity(intent);
        }
    }

    private void getChatList(String user_id, String ticket) {
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("test", "失败");
            }

            @Override
            public void onResponse(String s) {
                Log.d("test", "成功");
            }
        };
        MPServerHttpManager.getInstance().notifyChat(user_id, ticket, callBack);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        consumerHomeActivity.onActivityResult(requestCode, resultCode, data);
    }
}
