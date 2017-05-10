package com.homechart.app.home.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.google.android.gms.analytics.HitBuilders;
import com.homechart.app.MyApplication;
import com.homechart.app.R;
import com.homechart.app.home.activity.ConsumerHomeActivity;
import com.homechart.app.netutils.MPServerHttpManager;
import com.homechart.app.netutils.OkStringRequest;
import com.homechart.app.commont.utils.GsonUtil;
import com.homechart.app.commont.utils.ToastUtils;
import com.homechart.app.home.activity.DesinerCenterActivity;
import com.homechart.app.home.adapter.DesinerListAdapter;
import com.homechart.app.home.bean.DesinerListBean;
import com.homechart.app.home.bean.DesinerListDataItemBean;
import com.homechart.app.widget.pulltprefresh.PullToRefreshLayout;
import com.homechart.app.widget.pulltprefresh.SwipePullListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/9/009.
 */

@SuppressLint("ValidFragment")
public class ShangCityFragment extends BaseFragment implements View.OnClickListener, PullToRefreshLayout.OnRefreshListener, ListView.OnItemClickListener {

    private static ConsumerHomeActivity consumerHomeActivity;
    private int position;
    private Context context;
    private PullToRefreshLayout mPullToRefreshLayout;
    private SwipePullListView mListView;
    private int picNum = 10;//每页数
    private int pageNum = 1;//第几页
    private DesinerListAdapter mListAdapter;
    private List<DesinerListDataItemBean> showList;
    private final String REFRESH_STATUS = "refresh";
    private final String LOADMORE_STATUS = "loadmore";

    private static ShangCityFragment threeCityFragment;

    public static ShangCityFragment getInstance(int position, Context context) {
        if (threeCityFragment == null) {
            threeCityFragment = new ShangCityFragment(position, context);
        }
        consumerHomeActivity = (ConsumerHomeActivity) context;
        return threeCityFragment;
    }

    private ShangCityFragment(int position, Context context) {
        this.position = position;
        this.context = context;
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_desiner_city;
    }

    @Override
    protected void initView() {
        mPullToRefreshLayout = ((PullToRefreshLayout) rootView.findViewById(R.id.refresh_view));
        mListView = (SwipePullListView) rootView.findViewById(R.id.message_center_list_view);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mPullToRefreshLayout.setOnRefreshListener(this);
        mListView.setOnItemClickListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        showList = new ArrayList<>();
        initListView();

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        getData(REFRESH_STATUS);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        getData(LOADMORE_STATUS);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                .setCategory("designerList_action")  //事件类别
                .setAction("设计师点击")      //事件操作
                .setLabel(showList.get(position).getNickname())
                .build());
        MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                .setCategory("designerList_action")  //事件类别
                .setAction("设计师点击only")//事件操作
                .build());
        Bundle bundle = new Bundle();
        bundle.putString("designerName", showList.get(position).getNickname());
        consumerHomeActivity.mFirebaseAnalytics.logEvent("设计师点击", bundle);
        Intent intent = new Intent(context, DesinerCenterActivity.class);
        intent.putExtra("user_id", showList.get(position).getUser_id());
        intent.putExtra("ic_default_head", showList.get(position).getAvatar());
        intent.putExtra("name", showList.get(position).getNickname());
        startActivity(intent);
    }

    private void initListView() {
        mListAdapter = new DesinerListAdapter(showList, context);
        mListView.setAdapter(mListAdapter);
        onRefresh(mPullToRefreshLayout);
    }
    @Override
    public void onResume() {
        super.onResume();
        mListView.setSelection(0);
        getData(REFRESH_STATUS);
    }

    private void getData(final String state) {
        if (state.equals(REFRESH_STATUS)) {
            pageNum = 1;
        } else {
            ++pageNum;
            MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                    .setCategory("designerList_action")  //事件类别
                    .setAction("加载下一页设计师")      //事件操作
                    .setLabel(pageNum + "")
                    .build());
            MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                    .setCategory("designerList_action")  //事件类别
                    .setAction("加载下一页设计师only")//事件操作
                    .build());
            Bundle bundle = new Bundle();
            bundle.putString("designerPage", pageNum + "");
            consumerHomeActivity.mFirebaseAnalytics.logEvent("加载下10个设计师", bundle);

        }
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(activity, "城市案例信息加载失败");
            }

            @Override
            public void onResponse(String s) {
                DesinerListBean desinerListBean = GsonUtil.jsonToBean(s, DesinerListBean.class);
                if (null != desinerListBean.getData() && null != desinerListBean.getData().getProfession()) {
                    List<DesinerListDataItemBean> listItem = desinerListBean.getData().getProfession();
                    updateViewFromData(listItem, state);
                } else {
                    if (state.equals(REFRESH_STATUS)) {
                        mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                    } else {
                        mPullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL);
                    }
                }
            }
        };
        MPServerHttpManager.getInstance().getDesinerCityCases(picNum, pageNum, position, callBack);
    }

    private void updateViewFromData(List<DesinerListDataItemBean> messages, String state) {
        switch (state) {
            case REFRESH_STATUS:
                showList.clear();
                showList.addAll(messages);
                break;
            case LOADMORE_STATUS:
                showList.addAll(messages);
                break;
        }
        mListAdapter.addMoreData(showList);
        if (state.equals(REFRESH_STATUS)) {
            mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
        } else {
            mPullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
        }
    }
}
