package com.homechart.app.home.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.homechart.app.MyApplication;
import com.homechart.app.R;
import com.homechart.app.commont.constants.ClassConstant;
import com.homechart.app.commont.utils.ChatUtils;
import com.homechart.app.commont.utils.CommontUtils;
import com.homechart.app.commont.utils.ImageUtils;
import com.homechart.app.commont.utils.SharedPreferencesUtils;
import com.homechart.app.commont.utils.UIUtils;
import com.homechart.app.home.bean.DetailTitalBean;
import com.homechart.app.home.ui.MyScrollView;
import com.homechart.app.netutils.MPServerHttpManager;
import com.homechart.app.netutils.OkStringRequest;
import com.homechart.app.commont.utils.GsonUtil;
import com.homechart.app.commont.utils.ToastUtils;
import com.homechart.app.home.bean.DesinerCaseBean;
import com.homechart.app.home.bean.DesinerCaseDataItemBean;
import com.homechart.app.home.bean.DesinerCenterBean;
import com.homechart.app.scrolllistview.MyListView;
import com.homechart.app.scrolllistview.PullToRefreshView;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.homechart.app.MyHXChatActivity;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.utils.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by GPD on 2017/3/6.
 */

public class DesinerCenterActivity extends BaseActivity implements View.OnClickListener
        , ListView.OnItemClickListener
        , PullToRefreshView.OnFooterRefreshListener {

    private DesinerCenterBean desinerCenterBean;
    private DesinerCaseBean desinerCaseBean;
    private MyScrollView scrollview;
    private ImageButton ibBack;
    private String user_id;
    private ImageView ivAvatar;
    private TextView ivDesinerName;
    private TextView ivDesinerLocation;
    private TextView ivDesinerPrice;
    private TextView ivDesinerNum;
    private ImageView ivLocation;
    private MyListView lvListView;
    private RelativeLayout rlIntoDesinerInfo;
    private List<String> list;
    private ImageButton sharedIcon;
    private CustomShareListener mShareListener;
    private ImageView iv_header;
    private PullToRefreshView mPullToRefreshView;
    private List<DesinerCaseDataItemBean> list_show;
    private int picNum = 10;
    private int offset = 0;
    private MyAdapter adapter;
    private Intent intent;
    private String header;
    private String name;
    private RelativeLayout rl_phone;
    private RelativeLayout rl_chat;
    private RelativeLayout common_navbar;
    private TextView tv_zhuye;
    private ImageView iv_zhuye;
    private TextView ivDesinerPriceNo;
    private View view_bettom;
    private ImageView iv_renzheng;
    private FirebaseAnalytics mFirebaseAnalytics;
    private String desiner_id;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_desiner_center;
    }

    @Override
    protected void initView() {
        common_navbar = (RelativeLayout) findViewById(R.id.common_navbar);
        scrollview = (MyScrollView) findViewById(R.id.sv_scrollview);
        ibBack = (ImageButton) findViewById(R.id.nav_left_imageButton);
        iv_renzheng = (ImageView) findViewById(R.id.iv_renzheng);
        ivAvatar = (ImageView) findViewById(R.id.iv_header_desiner_center);
        ivLocation = (ImageView) findViewById(R.id.iv_desiner_location);
        ivDesinerName = (TextView) findViewById(R.id.tv_name_desiner_center);
        ivDesinerLocation = (TextView) findViewById(R.id.tv_desiner_location);
        ivDesinerPrice = (TextView) findViewById(R.id.tv_desiner_price);
        ivDesinerPriceNo = (TextView) findViewById(R.id.tv_desiner_price_no);
        ivDesinerNum = (TextView) findViewById(R.id.tv_desinercenter_num);
        lvListView = (MyListView) findViewById(R.id.slv_desinercenter_listview);
        mPullToRefreshView = (PullToRefreshView) findViewById(R.id.main_pull_refresh_view);
        rlIntoDesinerInfo = (RelativeLayout) findViewById(R.id.rl_desinercenter_into_detail);
        sharedIcon = (ImageButton) findViewById(R.id.nav_secondary_imageButton);
        iv_header = (ImageView) findViewById(R.id.iv_header);
        rl_phone = (RelativeLayout) findViewById(R.id.rl_phone);
        rl_chat = (RelativeLayout) findViewById(R.id.rl_chat);
        tv_zhuye = (TextView) findViewById(R.id.tv_zhuye);
        iv_zhuye = (ImageView) findViewById(R.id.iv_zhuye);
        view_bettom = (View) findViewById(R.id.view_bettom);
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();
        intent = getIntent();
        desiner_id = intent.getStringExtra("desiner_id");
        user_id = intent.getStringExtra("user_id");
        header = intent.getStringExtra("ic_default_head");
        name = intent.getStringExtra("name");
        if (!TextUtils.isEmpty(desiner_id)) {
            user_id = desiner_id;
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
        ibBack.setOnClickListener(this);
        iv_header.setOnClickListener(this);
        rlIntoDesinerInfo.setOnClickListener(this);
        lvListView.setOnItemClickListener(this);
        sharedIcon.setOnClickListener(this);
        rl_phone.setOnClickListener(this);
        rl_chat.setOnClickListener(this);
        mPullToRefreshView.setOnFooterRefreshListener(this);
        mPullToRefreshView.setLastUpdated(new Date().toLocaleString());
        scrollview.setMyOnScrollChangedListener(new MyScrollView.MyOnScrollChangedListener() {
            @Override
            public void myOnScrollChanged(int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY < 300) {
                    ibBack.setImageResource(R.drawable.tital_back_wight);
                    sharedIcon.setImageResource(R.drawable.shared_icon_white);
                    float alpha = (float) (300 - scrollY) / 300;
                    common_navbar.setBackgroundResource(R.color.white);
                    common_navbar.getBackground().setAlpha((int) ((1 - alpha) * 255));
                } else {
                    common_navbar.setBackgroundResource(R.color.white);
                    sharedIcon.setImageResource(R.drawable.shared_icon);
                    ibBack.setImageResource(R.drawable.tital_back);
                    common_navbar.getBackground().setAlpha(255);
                }
            }
        });
    }

    public DetailTitalBean detailTitalBean;

    @Override
    protected void initData(Bundle savedInstanceState) {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        String ifLogin = SharedPreferencesUtils.readString("login_in");
        String userid = SharedPreferencesUtils.readString("user_id");
        if (TextUtils.isEmpty(ifLogin)) {
            mFirebaseAnalytics.setUserId(CommontUtils.getPhoneImail(DesinerCenterActivity.this));
        } else {
            if (ifLogin.equals("register")) {
                mFirebaseAnalytics.setUserId(userid + "$设计师");
            } else {
                mFirebaseAnalytics.setUserId(userid);
            }
        }
        mShareListener = new CustomShareListener();
        Fresco.initialize(this);
        sharedIcon.setImageResource(R.drawable.shared_icon_white);
        list_show = new ArrayList<>();
        adapter = new MyAdapter(list_show, DesinerCenterActivity.this);
        lvListView.setAdapter(adapter);
        getDesinerInfo();
        getCaseLibraryInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Get tracker.
        Tracker t = MyApplication.getInstance().getDefaultTracker();
        // Set screen name.
        t.setScreenName("设计师详情页1");
        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
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

    public void openCall(final String numPhone) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(numPhone);
        builder.setPositiveButton("呼叫", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + numPhone));
                startActivity(intent);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                ToastUtils.showCenter(DesinerCenterActivity.this, "已取消");
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_header:
                break;
            case R.id.rl_chat:
                String ifLogin = SharedPreferencesUtils.readString("login_in");
                String ticket = SharedPreferencesUtils.readString("ticket");
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("dedsignerInfo1_action")  //事件类别
                        .setAction("咨询按钮点击")      //事件操作
                        .setLabel(name)
                        .build());
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("dedsignerInfo1_action")  //事件类别
                        .setAction("咨询按钮点击only")//事件操作
                        .build());
                Bundle bundle1 = new Bundle();
                bundle1.putString("designerName", name);
                mFirebaseAnalytics.logEvent("设计师详情页1咨询按钮点击", bundle1);
                if (!TextUtils.isEmpty(ifLogin)) {
                    MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                            .setCategory("dedsignerInfo1_action")  //事件类别
                            .setAction("进入咨询设计师")      //事件操作
                            .setLabel(name)
                            .build());
                    MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                            .setCategory("dedsignerInfo1_action")  //事件类别
                            .setAction("进入咨询设计师only")//事件操作
                            .build());
                    Bundle bundle2 = new Bundle();
                    bundle2.putString("designerName", name);
                    mFirebaseAnalytics.logEvent("设计师详情页1进入咨询设计师", bundle2);
                    getChatList(user_id, ticket);
                    EaseUser easeUser = new EaseUser(user_id);
                    easeUser.setAvatar(header);
                    easeUser.setNickname(name);
                    ChatUtils.getInstance().put(user_id, easeUser);
                    Intent intent1 = new Intent(this, MyHXChatActivity.class);
                    intent1.putExtra(EaseConstant.EXTRA_USER_ID, user_id);
                    startActivity(intent1);
                } else {
                    Intent intent2 = new Intent(DesinerCenterActivity.this, UserLoginActivity.class);
                    startActivity(intent2);
                }
                break;
            case R.id.rl_phone:
                if (null != desinerCenterBean
                        && null != desinerCenterBean.getData()
                        && null != desinerCenterBean.getData().getTelephone()) {
                    if (!TextUtils.isEmpty(desinerCenterBean.getData().getTelephone())) {
                        MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                                .setCategory("designerInfo1_action")  //事件类别
                                .setAction("打电话")      //事件操作
                                .setLabel(desinerCenterBean.getData().getNickname())
                                .build());
                        MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                                .setCategory("designerInfo1_action")  //事件类别
                                .setAction("打电话only")//事件操作
                                .build());
                        Bundle bundle = new Bundle();
                        bundle.putString("designerName", desinerCenterBean.getData().getNickname());
                        mFirebaseAnalytics.logEvent("设计师详情页1打电话", bundle);
                        openCall(desinerCenterBean.getData().getMobile());
                    }
                } else {
                    ToastUtils.showCenter(DesinerCenterActivity.this, "请重新获取号码");
                }
                break;
            case R.id.nav_left_imageButton:
                DesinerCenterActivity.this.finish();
                break;
            case R.id.nav_secondary_imageButton:
                //TODO
                if (null != desinerCenterBean && null != desinerCenterBean.getData() && null != desinerCenterBean.getData()) {

                    MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                            .setCategory("designerInfo1_action")  //事件类别
                            .setAction("分享设计师")      //事件操作
                            .build());
                    Bundle bundle = new Bundle();
                    mFirebaseAnalytics.logEvent("设计师详情页1分享设计师", bundle);


                    UMImage image = new UMImage(DesinerCenterActivity.this, desinerCenterBean.getData().getAvatar());
                    image.compressStyle = UMImage.CompressStyle.SCALE;//大小压缩，默认为大小压缩，适合普通很大的图
                    image.compressStyle = UMImage.CompressStyle.QUALITY;//质量压缩，适合长图的分享
//                        UMWeb web = new UMWeb("http://idcool.com.cn/project/" + mAlbumId);
                    UMWeb web = new UMWeb("http://www.idcool.com.cn/u/" + user_id);
                    web.setTitle(desinerCenterBean.getData().getNickname().replace("<p>", "").replace("</p>", "").replace("<span>", "").replace("</span>", ""));//标题
                    web.setThumb(image);  //缩略图
                    web.setDescription(desinerCenterBean.getData().getDescription().replace("<p>", "").replace("</p>", "").replace("<span>", "").replace("</span>", ""));//描述
                    new ShareAction(DesinerCenterActivity.this).setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ,
                            SHARE_MEDIA.QZONE, SHARE_MEDIA.SINA).withMedia(web).setCallback(mShareListener).open();
                } else {
                    Toast.makeText(DesinerCenterActivity.this, "暂无内容可以分享", Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.rl_desinercenter_into_detail:
                if (null != desinerCenterBean) {
                    MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                            .setCategory("designerInfo1_action")  //事件类别
                            .setAction("完整信息点击")      //事件操作
                            .build());
                    Bundle bundle = new Bundle();
                    mFirebaseAnalytics.logEvent("设计师详情页1完整信息点击", bundle);

                    Intent intent = new Intent(DesinerCenterActivity.this, DesinerInfoActivity.class);
                    intent.putExtra("info", desinerCenterBean);
                    intent.putExtra("user_id", user_id);
                    intent.putExtra("ic_default_head", header);
                    intent.putExtra("name", name);
                    startActivity(intent);
                }

                break;
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (list != null) {
            Intent intent = new Intent(DesinerCenterActivity.this, CaseLibraryDetailsActivity.class);
            intent.putExtra(ClassConstant.CaseLibraryDetailsKey.ALBUM_ID, list_show.get(position).getAlbum_id());
            intent.putExtra(ClassConstant.CaseLibraryDetailsKey.ITEM_ID, "-1");
            startActivity(intent);
        }
    }

    //获取设计师信息
    private void getDesinerInfo() {
        OkStringRequest.OKResponseCallback callback = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(DesinerCenterActivity.this, "设计师信息加载失败");
            }

            @Override
            public void onResponse(String s) {
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putInt("tag", 1);
                bundle.putString("values", s);
                message.obj = bundle;
                handler.sendMessage(message);
            }
        };
        MPServerHttpManager.getInstance().getDesinerCenterDesinerInfo(user_id, callback);
    }

    private void getCaseLibraryInfo() {
        OkStringRequest.OKResponseCallback callback = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(DesinerCenterActivity.this, "设计师案例信息加载失败");
            }

            @Override
            public void onResponse(String s) {
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putInt("tag", 2);
                bundle.putString("values", s);
                message.obj = bundle;
                handler.sendMessage(message);
            }
        };
        MPServerHttpManager.getInstance().getDesinerCenterCasesInfo(user_id, offset, picNum, callback);

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = (Bundle) msg.obj;
            int tag = bundle.getInt("tag");
            String values = bundle.getString("values");
            if (tag == 1) {
                try {
                    desinerCenterBean = GsonUtil.jsonToBean(values, DesinerCenterBean.class);
                    upTitalUI();
                } catch (Exception e) {
                    ToastUtils.showCenter(DesinerCenterActivity.this, "数据加载失败");
                }
            } else if (tag == 2) {
                try {
                    desinerCaseBean = GsonUtil.jsonToBean(values, DesinerCaseBean.class);
                    offset = offset + 10;
                    upCaseUI();
                } catch (Exception e) {
                    ToastUtils.showCenter(DesinerCenterActivity.this, "  喜欢ta, 那就联系ta  ");
                    mPullToRefreshView.onFooterRefreshComplete();
                }
            }
        }
    };

    private boolean over = false;

    private void upCaseUI() {
        if (null != desinerCaseBean && null != desinerCaseBean.getData() && null != desinerCaseBean.getData().getAlbums()) {
            list_show.addAll(desinerCaseBean.getData().getAlbums());
            adapter.notifyDataSetChanged();
            if (TextUtils.equals(list_show.size() + "", desinerCenterBean.getData().getProject_num())) {
                view_bettom.setVisibility(View.VISIBLE);
            } else {
                view_bettom.setVisibility(View.GONE);
            }
            mPullToRefreshView.onFooterRefreshComplete();
            list = new ArrayList<>();
            for (int i = 0; i < list_show.size(); i++) {
                list.add(list_show.get(i).getImage_url());
            }
            if (desinerCaseBean.getData().getNext() == 0) {
                if (over) {
                    ToastUtils.showCenter(DesinerCenterActivity.this, "  喜欢ta, 那就联系ta  ");
                }
                over = true;
            }
        }

    }

    private void upTitalUI() {
        if (null != desinerCenterBean) {
            if (TextUtils.isEmpty(desinerCenterBean.getData().getMobile())) {
                tv_zhuye.setTextColor(UIUtils.getColor(R.color.nophone));
                iv_zhuye.setImageResource(R.drawable.nophone);
            }
            ImageUtils.displayRoundImage(desinerCenterBean.getData().getAvatar(), ivAvatar);
            ImageUtils.displayRoundImage(desinerCenterBean.getData().getAvatar(), iv_header);
            if (desinerCenterBean.getData().getAuth_status().equals("2")) {
                iv_renzheng.setVisibility(View.VISIBLE);
            } else {
                iv_renzheng.setVisibility(View.GONE);
            }
            ivDesinerName.setText(desinerCenterBean.getData().getNickname());
            if (!TextUtils.isEmpty(desinerCenterBean.getData().getLocation().trim())) {
                ivDesinerLocation.setVisibility(View.VISIBLE);
                ivLocation.setVisibility(View.VISIBLE);
                ivDesinerLocation.setText(desinerCenterBean.getData().getLocation());
            } else {
                ivDesinerLocation.setVisibility(View.GONE);
                ivLocation.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(desinerCenterBean.getData().getService_price().trim())) {
                ivDesinerPrice.setVisibility(View.VISIBLE);
                ivDesinerPrice.setText(desinerCenterBean.getData().getService_price());
            } else {
                ivDesinerPrice.setVisibility(View.GONE);
                ivDesinerPriceNo.setVisibility(View.VISIBLE);
            }
            ivDesinerNum.setText("我的服务案例" + "(" + desinerCenterBean.getData().getProject_num() + ")");
        }
    }

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        getCaseLibraryInfo();

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
                myHolder.tv_style = (TextView) convertView.findViewById(R.id.tv_item_room);
                myHolder.tv_space = (TextView) convertView.findViewById(R.id.tv_item_roomspace);
                myHolder.tv_price = (TextView) convertView.findViewById(R.id.tv_item_roomprice);
                myHolder.view1 = (View) convertView.findViewById(R.id.view_item_two);
                myHolder.view2 = (View) convertView.findViewById(R.id.view_item_three);
                myHolder.last_view = (View) convertView.findViewById(R.id.last_view);
                myHolder.last_view = (View) convertView.findViewById(R.id.last_view);
                myHolder.rl_case_all = (RelativeLayout) convertView.findViewById(R.id.rl_case_all);
                convertView.setTag(myHolder);
            } else {
                myHolder = (MyHolder) convertView.getTag();
            }
            if (!mList.get(position).getItem_num().equals("0")) {
                myHolder.rl_case_all.setVisibility(View.VISIBLE);
                if (!mList.get(position).getImage_url().equals((myHolder.imageView.getTag()))) {
                    myHolder.imageView.setTag(mList.get(position).getImage_url());
                    ImageUtils.displayHalfRoundImage(mList.get(position).getImage_url(), myHolder.imageView);
                    myHolder.tv_num.setText(mList.get(position).getItem_num() + "P");
                    if (TextUtils.isEmpty(mList.get(position).getProperty().getLocation())) {
                        myHolder.tv_location.setText(mList.get(position).getAlbum_name());
                    } else {
                        myHolder.tv_location.setText(mList.get(position).getProperty().getLocation()
                                + "      " + mList.get(position).getAlbum_name());
                    }
                    int num = 0;
                    if (!TextUtils.isEmpty(mList.get(position).getProperty().getHouse_type())) {
                        ++num;
                    }

                    if (!TextUtils.isEmpty(mList.get(position).getProperty().getArea())) {
                        ++num;
                    }

                    if (!TextUtils.isEmpty(mList.get(position).getProperty().getPrice())) {
                        ++num;
                    }
                    if (num == 3) {
                        myHolder.view1.setVisibility(View.VISIBLE);
                        myHolder.view1.setVisibility(View.VISIBLE);
                    } else if (num == 1 || num == 0) {
                        myHolder.view1.setVisibility(View.GONE);
                        myHolder.view2.setVisibility(View.GONE);
                    } else if (num == 2) {
                        if (TextUtils.isEmpty(mList.get(position).getProperty().getHouse_type()) || TextUtils.isEmpty(mList.get(position).getProperty().getArea())) {
                            myHolder.view1.setVisibility(View.GONE);
                        } else {
                            myHolder.view2.setVisibility(View.GONE);
                        }
                    }
                    if (TextUtils.isEmpty(mList.get(position).getProperty().getHouse_type())) {
                        myHolder.tv_style.setVisibility(View.GONE);
                    } else {
                        myHolder.tv_style.setVisibility(View.VISIBLE);
                        myHolder.tv_style.setText(mList.get(position).getProperty().getHouse_type());
                    }
                    if (TextUtils.isEmpty(mList.get(position).getProperty().getArea())) {
                        myHolder.tv_space.setVisibility(View.GONE);
                    } else {
                        myHolder.tv_space.setVisibility(View.VISIBLE);
                        myHolder.tv_space.setText(mList.get(position).getProperty().getArea());
                    }
                    if (TextUtils.isEmpty(mList.get(position).getProperty().getPrice())) {
                        myHolder.tv_price.setVisibility(View.GONE);
                    } else {
                        myHolder.tv_price.setVisibility(View.VISIBLE);
                        myHolder.tv_price.setText(mList.get(position).getProperty().getPrice());
                    }
                }
            } else {
                myHolder.rl_case_all.setVisibility(View.GONE);
            }
            return convertView;
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
            private View last_view;
            private RelativeLayout rl_case_all;
        }
    }

    class CustomShareListener implements UMShareListener {


        private CustomShareListener() {
        }

        @Override
        public void onStart(SHARE_MEDIA platform) {
            Log.d("test", "开始了");
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {

            if (platform.name().equals("WEIXIN_FAVORITE")) {
                Toast.makeText(DesinerCenterActivity.this, platform + " 收藏成功啦", Toast.LENGTH_SHORT).show();
            } else {
                if (platform != SHARE_MEDIA.MORE && platform != SHARE_MEDIA.SMS
                        && platform != SHARE_MEDIA.EMAIL
                        && platform != SHARE_MEDIA.FLICKR
                        && platform != SHARE_MEDIA.FOURSQUARE
                        && platform != SHARE_MEDIA.TUMBLR
                        && platform != SHARE_MEDIA.POCKET
                        && platform != SHARE_MEDIA.PINTEREST

                        && platform != SHARE_MEDIA.INSTAGRAM
                        && platform != SHARE_MEDIA.GOOGLEPLUS
                        && platform != SHARE_MEDIA.YNOTE
                        && platform != SHARE_MEDIA.EVERNOTE) {
                    Toast.makeText(DesinerCenterActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
                }

            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            if (platform != SHARE_MEDIA.MORE && platform != SHARE_MEDIA.SMS
                    && platform != SHARE_MEDIA.EMAIL
                    && platform != SHARE_MEDIA.FLICKR
                    && platform != SHARE_MEDIA.FOURSQUARE
                    && platform != SHARE_MEDIA.TUMBLR
                    && platform != SHARE_MEDIA.POCKET
                    && platform != SHARE_MEDIA.PINTEREST

                    && platform != SHARE_MEDIA.INSTAGRAM
                    && platform != SHARE_MEDIA.GOOGLEPLUS
                    && platform != SHARE_MEDIA.YNOTE
                    && platform != SHARE_MEDIA.EVERNOTE) {
                Toast.makeText(DesinerCenterActivity.this, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
                if (t != null) {
                    Log.d("throw", "throw:" + t.getMessage());
                }
            }

        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {

            Toast.makeText(DesinerCenterActivity.this, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    }
}
