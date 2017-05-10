package com.homechart.app.home.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.android.gms.analytics.HitBuilders;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.homechart.app.MyApplication;
import com.homechart.app.R;
import com.homechart.app.commont.constants.ClassConstant;
import com.homechart.app.commont.utils.CommontUtils;
import com.homechart.app.commont.utils.GsonUtil;
import com.homechart.app.commont.utils.ImageUtils;
import com.homechart.app.commont.utils.SharedPreferencesUtils;
import com.homechart.app.commont.utils.ToastUtils;
import com.homechart.app.home.bean.DesinerCaseBean;
import com.homechart.app.home.bean.DesinerCaseDataItemBean;
import com.homechart.app.netutils.MPServerHttpManager;
import com.homechart.app.netutils.OkStringRequest;
import com.homechart.app.widget.pulltprefresh.PullToRefreshLayout;
import com.homechart.app.widget.pulltprefresh.SwipePullListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/21/021.
 */
public class MyCaseLibraryActivity extends BaseActivity implements
        PullToRefreshLayout.OnRefreshListener,
        ListView.OnItemClickListener,
        View.OnClickListener {
    private PullToRefreshLayout mPullToRefreshLayout;
    private SwipePullListView mListView;
    private List<DesinerCaseDataItemBean> list_show;
    private DesinerCaseBean desinerCaseBean;
    private MyAdapter adapter;
    private String user_id;
    private final String REFRESH_STATUS = "refresh";
    private final String LOADMORE_STATUS = "loadmore";
    private int offset = 0;
    private int picNum = 10;
    private ImageButton ibBack;
    private TextView tv_tital;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_mycase_libriry;
    }

    @Override
    protected void initView() {
        mPullToRefreshLayout = ((PullToRefreshLayout) findViewById(R.id.refresh_view));
        mListView = (SwipePullListView) findViewById(R.id.message_center_list_view);
        ibBack = (ImageButton) findViewById(R.id.nav_left_imageButton);
        tv_tital = (TextView) findViewById(R.id.tv_tital_comment);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mPullToRefreshLayout.setOnRefreshListener(this);
        mListView.setOnItemClickListener(this);
        ibBack.setOnClickListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        user_id = SharedPreferencesUtils.readString("user_id");
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        String ifLogin = SharedPreferencesUtils.readString("login_in");
        String userid = SharedPreferencesUtils.readString("user_id");
        if (TextUtils.isEmpty(ifLogin)) {
            mFirebaseAnalytics.setUserId(CommontUtils.getPhoneImail(MyCaseLibraryActivity.this));
        } else {
            if (ifLogin.equals("register")) {
                mFirebaseAnalytics.setUserId(userid + "$设计师");
            } else {
                mFirebaseAnalytics.setUserId(userid);
            }
        }
        tv_tital.setText("我的服务案例");
        initList();
    }

    /**
     * 初始化ListView
     */
    private void initList() {
        list_show = new ArrayList<>();
        adapter = new MyAdapter(list_show, MyCaseLibraryActivity.this);
        mListView.setAdapter(adapter);
        onRefresh(mPullToRefreshLayout);
    }


    private void getCaseLibraryInfo(final String status) {
        if (status.equals(REFRESH_STATUS)) {
            offset = 0;
        } else {
            offset = offset + 10;
        }
        OkStringRequest.OKResponseCallback callback = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(MyCaseLibraryActivity.this, "设计师案例信息加载失败");
            }

            @Override
            public void onResponse(String s) {
                desinerCaseBean = GsonUtil.jsonToBean(s, DesinerCaseBean.class);
                if (desinerCaseBean != null && desinerCaseBean.getData() != null && desinerCaseBean.getData().getAlbums() != null) {
                    List<DesinerCaseDataItemBean> list = desinerCaseBean.getData().getAlbums();
                    updateViewFromData(list, status);
                }
            }
        };
        MPServerHttpManager.getInstance().getDesinerCenterCasesInfo(user_id, offset, picNum, callback);
    }


    private void updateViewFromData(List<DesinerCaseDataItemBean> messages, String state) {
        switch (state) {
            case REFRESH_STATUS:
                list_show.clear();
                list_show.addAll(messages);
                break;
            case LOADMORE_STATUS:
                list_show.addAll(messages);
                break;
        }
        adapter.addMoreData(list_show);
        if (state.equals(REFRESH_STATUS)) {
            mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
        } else {
            mPullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (list_show != null) {
            MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                    .setCategory("myaction_action")  //事件类别
                    .setAction("查看自己的案例")      //事件操作
                    .build());
            Bundle bundle1 = new Bundle();
            mFirebaseAnalytics.logEvent("我的案例_自己查看自己的案例", bundle1);
            Intent intent = new Intent(MyCaseLibraryActivity.this, CaseLibraryDetailsActivity.class);
            intent.putExtra(ClassConstant.CaseLibraryDetailsKey.ALBUM_ID, list_show.get(position).getAlbum_id());
            intent.putExtra(ClassConstant.CaseLibraryDetailsKey.ITEM_ID, list_show.get(position).getItem_num());
            startActivity(intent);
        }
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        offset = 0;
        getCaseLibraryInfo(REFRESH_STATUS);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        getCaseLibraryInfo(LOADMORE_STATUS);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_left_imageButton:
                MyCaseLibraryActivity.this.finish();
                break;
        }
    }

    class MyAdapter extends BaseAdapter {

        private List<DesinerCaseDataItemBean> mList;
        private Context context;

        public MyAdapter(List<DesinerCaseDataItemBean> mList, Context context) {
            this.mList = mList;
            this.context = context;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            MyHolder myHolder;
            if (null == convertView) {
                myHolder = new MyHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.item_desinercenter_case, null);
                myHolder.imageView = (ImageView) convertView.findViewById(R.id.tv_item_desinercenter_imageView);
                myHolder.tv_num = (TextView) convertView.findViewById(R.id.tv_item_num);
                myHolder.tv_location = (TextView) convertView.findViewById(R.id.tv_item_desinercenter_location);
//                myHolder.tv_location_two = (TextView) convertView.findViewById(R.id.tv_item_desinercenter_location_two);
                myHolder.tv_style = (TextView) convertView.findViewById(R.id.tv_item_room);
                myHolder.tv_space = (TextView) convertView.findViewById(R.id.tv_item_roomspace);
                myHolder.tv_price = (TextView) convertView.findViewById(R.id.tv_item_roomprice);
                myHolder.view1 = (View) convertView.findViewById(R.id.view_item_two);
                myHolder.view2 = (View) convertView.findViewById(R.id.view_item_two);
                convertView.setTag(myHolder);
            } else {
                myHolder = (MyHolder) convertView.getTag();
            }
            ImageUtils.disBigImage(mList.get(position).getImage_url(), myHolder.imageView);
            myHolder.tv_num.setText(mList.get(position).getItem_num() + "P");
            if (TextUtils.isEmpty(mList.get(position).getProperty().getLocation())) {
                myHolder.tv_location.setText(mList.get(position).getAlbum_name());
            } else {
                myHolder.tv_location.setText(mList.get(position).getProperty().getLocation()
                        + "      " + mList.get(position).getAlbum_name());
            }

            if (TextUtils.isEmpty(mList.get(position).getProperty().getHouse_type())) {
                myHolder.view1.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(mList.get(position).getProperty().getHouse_type()) && TextUtils.isEmpty(mList.get(position).getProperty().getPrice()) && TextUtils.isEmpty(mList.get(position).getProperty().getArea())) {
                myHolder.view1.setVisibility(View.GONE);
            }
            if (TextUtils.isEmpty(mList.get(position).getProperty().getPrice())) {
                myHolder.view2.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(mList.get(position).getProperty().getHouse_type()) && !TextUtils.isEmpty(mList.get(position).getProperty().getPrice())) {
                myHolder.view2.setVisibility(View.GONE);
            }
            myHolder.tv_style.setText(mList.get(position).getProperty().getHouse_type());
            myHolder.tv_space.setText(mList.get(position).getProperty().getArea());
            myHolder.tv_price.setText(mList.get(position).getProperty().getPrice());
            return convertView;
        }

        public void addMoreData(List<DesinerCaseDataItemBean> list) {
            this.mList = list;
            notifyDataSetChanged();
        }

        class MyHolder {
            private ImageView imageView;
            private TextView tv_num;
            private TextView tv_location;
            private TextView tv_style;
            private TextView tv_space;
            private TextView tv_price;
            private View view1;
            private View view2;
        }
    }

}
