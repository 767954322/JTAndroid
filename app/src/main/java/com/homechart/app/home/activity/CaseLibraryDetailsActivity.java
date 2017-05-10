package com.homechart.app.home.activity;

import com.homechart.app.commont.imagedetail.ImageDetailsActivity;
import com.homechart.app.commont.utils.SharedPreferencesUtils;
import com.homechart.app.home.bean.DetailPicItemCateTagBean;
import com.homechart.app.commont.constants.ClassConstant;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.homechart.app.netutils.MPServerHttpManager;
import com.homechart.app.home.bean.DetailPicItemBean;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.gms.analytics.HitBuilders;
import com.homechart.app.commont.utils.CommontUtils;
import com.homechart.app.home.bean.DetailTitalBean;
import com.homechart.app.netutils.OkStringRequest;
import com.homechart.app.commont.utils.ToastUtils;
import com.homechart.app.home.bean.DetailPicBean;
import com.homechart.app.commont.utils.ChatUtils;
import com.homechart.app.commont.utils.ImageUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.homechart.app.commont.utils.GsonUtil;
import com.google.android.gms.analytics.Tracker;
import com.hyphenate.easeui.domain.EaseUser;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.UMShareListener;
import com.homechart.app.MyHXChatActivity;
import com.hyphenate.easeui.EaseConstant;
import com.umeng.socialize.media.UMImage;
import com.homechart.app.MyApplication;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.media.UMWeb;
import com.android.volley.VolleyError;
import com.umeng.socialize.utils.Log;

import android.widget.RelativeLayout;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;

import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;

import java.util.List;

import com.homechart.app.R;

/**
 * @author Allen.Gu .
 * @version v1.0 .
 * @date 2017-2-24.
 * @file CaseLibraryDetailsActivity.java .
 * @brief 关联已有账号 .
 */
public class CaseLibraryDetailsActivity extends BaseActivity implements
        View.OnClickListener {

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_caselibrary_details;
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();
        mAlbumId = getIntent().getStringExtra(ClassConstant.CaseLibraryDetailsKey.ALBUM_ID);
        mItemId = getIntent().getStringExtra(ClassConstant.CaseLibraryDetailsKey.ITEM_ID);
    }

    @Override
    protected void initView() {

        sharedIcon = (ImageButton) findViewById(R.id.nav_secondary_imageButton);
        lvCaseLibary = (ListView) findViewById(R.id.slv_caselibrary_details);
        ibBack = (ImageButton) findViewById(R.id.nav_left_imageButton);
        rl_zhuye = (RelativeLayout) findViewById(R.id.rl_zhuye);
        rl_chat = (RelativeLayout) findViewById(R.id.rl_chat);
        iv_header = (ImageView) findViewById(R.id.iv_header);

    }

    @Override
    protected void initListener() {
        super.initListener();

        sharedIcon.setOnClickListener(this);
        iv_header.setOnClickListener(this);
        rl_zhuye.setOnClickListener(this);
        rl_chat.setOnClickListener(this);
        ibBack.setOnClickListener(this);

    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        sharedIcon.setImageResource(R.drawable.shared_icon);
        sharedIcon.setVisibility(View.VISIBLE);

        mShareListener = new CustomShareListener();
        list_show = new ArrayList<>();

        Fresco.initialize(this);
        initFirebase();
        getTitalData();
        getPicData();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_chat:
                String ifLogin = SharedPreferencesUtils.readString("login_in");
                String ticket = SharedPreferencesUtils.readString("ticket");
                if (!TextUtils.isEmpty(ifLogin)) {
                    MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                            .setCategory("case_action")  //事件类别
                            .setAction("咨询设计师按钮点击")      //事件操作
                            .setLabel(detailTitalBean.getData().getAlbum_name())
                            .build());
                    MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                            .setCategory("case_action")  //事件类别
                            .setAction("咨询设计师按钮点击only")//事件操作
                            .build());
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("designerName", detailTitalBean.getData().getAlbum_name());
                    mFirebaseAnalytics.logEvent("案例页咨询设计师", bundle1);
                    if (null != detailTitalBean && null != detailTitalBean.getData() && null != detailTitalBean.getData().getUser()) {
                        MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                                .setCategory("case_action")  //事件类别
                                .setAction("进入咨询页")      //事件操作
                                .setLabel(detailTitalBean.getData().getAlbum_name())
                                .build());
                        MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                                .setCategory("case_action")  //事件类别
                                .setAction("进入咨询页only")//事件操作
                                .build());
                        Bundle bundle2 = new Bundle();
                        bundle2.putString("designerName", detailTitalBean.getData().getAlbum_name());
                        mFirebaseAnalytics.logEvent("案例页进入咨询页", bundle2);
                        String userid = detailTitalBean.getData().getUser().getUser_id();
                        getChatList(userid, ticket);
                        EaseUser easeUser = new EaseUser(userid);
                        easeUser.setAvatar(detailTitalBean.getData().getUser().getAvatar());
                        easeUser.setNickname(detailTitalBean.getData().getUser().getNickname());
                        ChatUtils.getInstance().put(userid, easeUser);
                        Intent intent1 = new Intent(this, MyHXChatActivity.class);
                        intent1.putExtra(EaseConstant.EXTRA_USER_ID, detailTitalBean.getData().getUser().getUser_id());
                        startActivity(intent1);
                    } else {
                        ToastUtils.showCenter(CaseLibraryDetailsActivity.this, "信息获取失败，请重新返回重新获取信息");
                    }
                } else {
                    Intent intent1 = new Intent(this, UserLoginActivity.class);
                    startActivity(intent1);
                }
                break;
            case R.id.iv_header:
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("case_action")  //事件类别
                        .setAction("footerUserIcon设计师点击")      //事件操作
                        .setLabel(detailTitalBean.getData().getAlbum_name())
                        .build());
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("case_action")  //事件类别
                        .setAction("footerUserIcon设计师点击only")//事件操作
                        .build());
                Bundle bundle = new Bundle();
                bundle.putString("designerName", detailTitalBean.getData().getAlbum_name());
                mFirebaseAnalytics.logEvent("案例页下边设计师头像点击", bundle);
                if (null != detailTitalBean && null != detailTitalBean.getData() && null != detailTitalBean.getData().getUser()) {
                    Intent intent = new Intent(CaseLibraryDetailsActivity.this, DesinerCenterActivity.class);
                    intent.putExtra("user_id", detailTitalBean.getData().getUser().getUser_id());
                    intent.putExtra("ic_default_head", detailTitalBean.getData().getUser().getAvatar());
                    intent.putExtra("name", detailTitalBean.getData().getUser().getNickname());
                    startActivity(intent);
                }
                break;
            case R.id.rl_zhuye:
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("case_action")  //事件类别
                        .setAction("footerLeftBtn设计师点击")      //事件操作
                        .setLabel(detailTitalBean.getData().getAlbum_name())
                        .build());
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("case_action")  //事件类别
                        .setAction("footerLeftBtn设计师点击only")//事件操作
                        .build());
                Bundle bundle1 = new Bundle();
                bundle1.putString("designerName", detailTitalBean.getData().getAlbum_name());
                mFirebaseAnalytics.logEvent("案例页下边主页点击", bundle1);
                if (null != detailTitalBean && null != detailTitalBean.getData() && null != detailTitalBean.getData().getUser()) {
                    Intent intent = new Intent(CaseLibraryDetailsActivity.this, DesinerCenterActivity.class);
                    intent.putExtra("user_id", detailTitalBean.getData().getUser().getUser_id());
                    intent.putExtra("ic_default_head", detailTitalBean.getData().getUser().getAvatar());
                    intent.putExtra("name", detailTitalBean.getData().getUser().getNickname());
                    startActivity(intent);
                }
                break;
            case R.id.nav_left_imageButton:
                CaseLibraryDetailsActivity.this.finish();
                break;
            case R.id.iv_casedetails_avatar_pic:
                if (null != detailTitalBean && null != detailTitalBean.getData() && null != detailTitalBean.getData().getUser()) {
                    Intent intent = new Intent(CaseLibraryDetailsActivity.this, DesinerCenterActivity.class);
                    intent.putExtra("user_id", detailTitalBean.getData().getUser().getUser_id());
                    startActivity(intent);
                }
                break;
            case R.id.nav_secondary_imageButton:
                if (null != detailTitalBean && null != detailPicBean) {
                    if (null != detailPicBean.getData().getItems() && detailPicBean.getData().getItems().size() > 0) {
                        MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                                .setCategory("case_action")  //事件类别
                                .setAction("案例分享")//事件操作
                                .build());
                        Bundle bundle2 = new Bundle();
                        mFirebaseAnalytics.logEvent("案例页分享", bundle2);

                        UMImage image = new UMImage(CaseLibraryDetailsActivity.this, detailPicBean.getData().getItems().get(0).getBig_image_url());
                        image.compressStyle = UMImage.CompressStyle.SCALE;//大小压缩，默认为大小压缩，适合普通很大的图
//                        image.compressStyle = UMImage.CompressStyle.QUALITY;//质量压缩，适合长图的分享
                        UMWeb web = new UMWeb("http://idcool.com.cn/project/" + mAlbumId);
                        web.setTitle(detailTitalBean.getData().getAlbum_name());//标题
                        web.setThumb(image);  //缩略图
                        String desi = detailTitalBean.getData().getDescription();
                        if (desi.length() > 160) {
                            desi = desi.substring(0, 160) + "...";
                        }
                        web.setDescription(desi);//描述
                        new ShareAction(CaseLibraryDetailsActivity.this).setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ,
                                SHARE_MEDIA.QZONE, SHARE_MEDIA.SINA).withMedia(web).setCallback(mShareListener).open();
                    } else {
                        Toast.makeText(CaseLibraryDetailsActivity.this, "暂无内容可以分享", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Tracker t = MyApplication.getInstance().getDefaultTracker();
        t.setScreenName("案例详情页");
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }

    private void initFirebase() {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        String ifLogin = SharedPreferencesUtils.readString("login_in");
        String userid = SharedPreferencesUtils.readString("user_id");
        if (TextUtils.isEmpty(ifLogin)) {
            mFirebaseAnalytics.setUserId(CommontUtils.getPhoneImail(CaseLibraryDetailsActivity.this));
        } else {
            if (ifLogin.equals("register")) {
                mFirebaseAnalytics.setUserId(userid + "$设计师");
            } else {
                mFirebaseAnalytics.setUserId(userid);
            }
        }
    }

    public void getTitalData() {
        OkStringRequest.OKResponseCallback callback = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(CaseLibraryDetailsActivity.this, "数据加载失败");
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
        MPServerHttpManager.getInstance().getCaseDetailTitalData(mAlbumId, callback);
    }

    private void getPicData() {
        OkStringRequest.OKResponseCallback callback = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(CaseLibraryDetailsActivity.this, "数据加载失败");
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
        MPServerHttpManager.getInstance().getCaseDetailPicData(mAlbumId, picNum, offset, callback);
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
                    detailTitalBean = GsonUtil.jsonToBean(values, DetailTitalBean.class);
                    ImageUtils.displayAvatarImage(detailTitalBean.getData().getUser().getAvatar(), iv_header);
                    if (detailTitalBean.getData() != null && list_show.size() > 0) {
                        adapter = new MyScrollListAdapter(CaseLibraryDetailsActivity.this, list_show, plam, next, detailTitalBean, mItemId);
                        lvCaseLibary.setAdapter(adapter);
                        if (!TextUtils.equals(mItemId, "-1")) {
                            int position = getSelectPosition();
                            lvCaseLibary.setSelection(position + 1);
                        } else {
                            lvCaseLibary.setSelection(0);
                        }
                    } else {

                    }
                } catch (Exception e) {
                    ToastUtils.showCenter(CaseLibraryDetailsActivity.this, "数据加载失败");
                }
            } else if (tag == 2) {
                try {
                    offset = offset + 10;
                    detailPicBean = GsonUtil.jsonToBean(values, DetailPicBean.class);
                    if (null != detailPicBean.getData() && null != detailPicBean.getData().getItems()) {
                        list_show.addAll(detailPicBean.getData().getItems());
                    }
                    if (detailTitalBean.getData() != null && list_show.size() > 0) {
                        plam = detailPicBean.getData().getPlan_num();
                        adapter = new MyScrollListAdapter(CaseLibraryDetailsActivity.this, list_show, plam, next, detailTitalBean, mItemId);
                        lvCaseLibary.setAdapter(adapter);
                        if (!TextUtils.equals(mItemId, "-1")) {
                            int position = getSelectPosition();
                            lvCaseLibary.setSelection(position + 1);
                        } else {
                            lvCaseLibary.setSelection(0);
                        }
                    } else {
                    }
                } catch (Exception e) {
                    ToastUtils.showCenter(CaseLibraryDetailsActivity.this, "暂无更多案例详情");
                }
            }
        }
    };

    private int getSelectPosition() {
        int poition = 0;
        for (int i = 0; list_show.size() > i; i++) {
            if (TextUtils.equals(list_show.get(i).getItem_id(), mItemId)) {
                poition = i;
            }
        }
        return poition;
    }

    class MyScrollListAdapter extends BaseAdapter {

        private int mPlanNum;
        private int mNext;
        private Context mContext;
        private String itemId;
        private List<DetailPicItemBean> mList;
        private DetailTitalBean mDetailTitalBean;
        private boolean pageTop = true;
        private LayoutInflater inflater;
        final int TYPE_1 = 0;
        final int TYPE_2 = 1;

        public MyScrollListAdapter(Context context, List<DetailPicItemBean> list, int plan_num, int next, DetailTitalBean detailTitalBean, String mItemId) {
            this.mContext = context;
            this.mList = list;
            this.mPlanNum = plan_num;
            this.mNext = next;
            this.mDetailTitalBean = detailTitalBean;
            this.itemId = mItemId;
            inflater = LayoutInflater.from(mContext);
        }

        @Override
        public int getCount() {
            return mList.size() + 1;
        }

        @Override
        public Object getItem(int position) {
            return position == 0 ? mDetailTitalBean : mList.get(position - 1);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return TYPE_1;
            } else {
                return TYPE_2;
            }
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            int type = getItemViewType(position);
            MyHolderTop myHolderTop = null;
            MyHolder myHolder = null;
            if (convertView == null) {
                switch (type) {
                    case TYPE_1:
                        convertView = inflater.inflate
                                (R.layout.item_caselibrary_detail_pic_header, parent, false);
                        myHolderTop = new MyHolderTop();
                        myHolderTop.tv_tital_name = (TextView) convertView.findViewById(R.id.tv_tital_name);
                        myHolderTop.tv_tital_detail = (TextView) convertView.findViewById(R.id.tv_tital_detail);
                        myHolderTop.tv_casedetails_roomtype = (TextView) convertView.findViewById(R.id.tv_casedetails_roomtype);
                        myHolderTop.tv_casedetails_space = (TextView) convertView.findViewById(R.id.tv_casedetails_space);
                        myHolderTop.tv_casedetails_price = (TextView) convertView.findViewById(R.id.tv_casedetails_price);
//                        myHolderTop.tv_casedetails_style = (TextView) convertView.findViewById(R.id.tv_casedetails_style);
                        myHolderTop.iv_casedetails_avatar_pic = (ImageView) convertView.findViewById(R.id.iv_casedetails_avatar_pic);
                        myHolderTop.iv_casedetails_avatar_certification = (ImageView) convertView.findViewById(R.id.iv_casedetails_avatar_certification);
                        myHolderTop.tv_casedetails_desiner_name = (TextView) convertView.findViewById(R.id.tv_casedetails_desiner_name);
                        myHolderTop.tv_jianjie_detail = (TextView) convertView.findViewById(R.id.tv_jianjie_detail);
                        myHolderTop.tv_huxing_pic_tital = (TextView) convertView.findViewById(R.id.tv_huxing_pic_tital);
                        myHolderTop.view_left = convertView.findViewById(R.id.view_line_left_one);
                        myHolderTop.view_center = convertView.findViewById(R.id.view_line_center);
                        myHolderTop.view_no_huxing = convertView.findViewById(R.id.view_no_huxing);
                        convertView.setTag(myHolderTop);
                        break;
                    case TYPE_2:
                        convertView = inflater.inflate
                                (R.layout.item_caselibrary_detail_pic, parent, false);
                        myHolder = new MyHolder();
                        myHolder.tital = (TextView) convertView.findViewById(R.id.tv_item_caselibrary_pic_tital);
                        myHolder.tag = (TextView) convertView.findViewById(R.id.tv_item_caselibrary_pic_tag);
                        myHolder.details = (TextView) convertView.findViewById(R.id.tv_item_caselibrary_pic_detail);
                        myHolder.picItem = (SimpleDraweeView) convertView.findViewById(R.id.iv_item_caselibrary_pic);
                        myHolder.view_tag = (View) convertView.findViewById(R.id.view_tag);
                        myHolder.bellow_view = (View) convertView.findViewById(R.id.bellow_view);
                        myHolder.rl_reader_over = (RelativeLayout) convertView.findViewById(R.id.rl_reader_over);
                        convertView.setTag(myHolder);
                        break;
                }
            } else {
                switch (type) {
                    case TYPE_1:
                        myHolderTop = (MyHolderTop) convertView.getTag();
                        break;
                    case TYPE_2:
                        myHolder = (MyHolder) convertView.getTag();
                        break;
                }
            }
            //设置资源
            switch (type) {
                case TYPE_1:
                    if (null != detailPicBean) {
                        myHolderTop.tv_tital_name.setText(detailTitalBean.getData().getProperty().getLocation());
                        myHolderTop.tv_tital_detail.setText(detailTitalBean.getData().getAlbum_name());

                        int num = 0;
                        if (!TextUtils.isEmpty(detailTitalBean.getData().getProperty().getHouse_type())) {
                            ++num;
                        }

                        if (!TextUtils.isEmpty(detailTitalBean.getData().getProperty().getArea())) {
                            ++num;
                        }

                        if (!TextUtils.isEmpty(detailTitalBean.getData().getProperty().getPrice())) {
                            ++num;
                        }
                        if (num == 3) {
                            myHolderTop.view_left.setVisibility(View.VISIBLE);
                            myHolderTop.view_center.setVisibility(View.VISIBLE);
                        } else if (num == 1 || num == 0) {
                            myHolderTop.view_left.setVisibility(View.GONE);
                            myHolderTop.view_center.setVisibility(View.GONE);
                        } else if (num == 2) {
                            if (TextUtils.isEmpty(detailTitalBean.getData().getProperty().getHouse_type()) || TextUtils.isEmpty(detailTitalBean.getData().getProperty().getArea())) {
                                myHolderTop.view_left.setVisibility(View.GONE);
                            } else {
                                myHolderTop.view_center.setVisibility(View.GONE);
                            }
                        }
                        if (TextUtils.isEmpty(detailTitalBean.getData().getProperty().getHouse_type())) {
                            myHolderTop.tv_casedetails_roomtype.setVisibility(View.GONE);
                        } else {
                            myHolderTop.tv_casedetails_roomtype.setVisibility(View.VISIBLE);
                            myHolderTop.tv_casedetails_roomtype.setText(detailTitalBean.getData().getProperty().getHouse_type());
                        }
                        if (TextUtils.isEmpty(detailTitalBean.getData().getProperty().getArea())) {
                            myHolderTop.tv_casedetails_space.setVisibility(View.GONE);
                        } else {
                            myHolderTop.tv_casedetails_space.setVisibility(View.VISIBLE);
                            myHolderTop.tv_casedetails_space.setText(detailTitalBean.getData().getProperty().getArea());
                        }
                        if (TextUtils.isEmpty(detailTitalBean.getData().getProperty().getPrice())) {
                            myHolderTop.tv_casedetails_price.setVisibility(View.GONE);
                        } else {
                            myHolderTop.tv_casedetails_price.setVisibility(View.VISIBLE);
                            myHolderTop.tv_casedetails_price.setText(detailTitalBean.getData().getProperty().getPrice());
                        }


                        if (detailTitalBean.getData().getUser().getAuth_status() == 1) {
                            myHolderTop.iv_casedetails_avatar_certification.setVisibility(View.GONE);
                        } else {
                            myHolderTop.iv_casedetails_avatar_certification.setVisibility(View.VISIBLE);
                        }
                        ImageUtils.displayAvatarImage(detailTitalBean.getData().getUser().getAvatar(), myHolderTop.iv_casedetails_avatar_pic);
                        myHolderTop.iv_casedetails_avatar_pic.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (null != detailTitalBean && null != detailTitalBean.getData() && null != detailTitalBean.getData().getUser()) {
                                    MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                                            .setCategory("case_action")  //事件类别
                                            .setAction("header设计师点击")      //事件操作
                                            .setLabel(detailTitalBean.getData().getAlbum_name())
                                            .build());
                                    MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                                            .setCategory("case_action")  //事件类别
                                            .setAction("header设计师点击only")//事件操作
                                            .build());
                                    Bundle bundle1 = new Bundle();
                                    bundle1.putString("designerName", detailTitalBean.getData().getAlbum_name());
                                    mFirebaseAnalytics.logEvent("案例页上部设计师点击", bundle1);
                                    Intent intent = new Intent(CaseLibraryDetailsActivity.this, DesinerCenterActivity.class);
                                    intent.putExtra("user_id", detailTitalBean.getData().getUser().getUser_id());
                                    startActivity(intent);
                                }
                            }
                        });
                        myHolderTop.tv_casedetails_desiner_name.setText(detailTitalBean.getData().getUser().getNickname());
                        myHolderTop.tv_jianjie_detail.setText(detailTitalBean.getData().getDescription());
                        if (detailPicBean.getData().getPlan_num() > 0) {
                            myHolderTop.tv_huxing_pic_tital.setVisibility(View.VISIBLE);
                            myHolderTop.view_no_huxing.setVisibility(View.GONE);
                        } else {
                            myHolderTop.tv_huxing_pic_tital.setVisibility(View.GONE);
                            myHolderTop.view_no_huxing.setVisibility(View.VISIBLE);
                        }
                    }
                    break;
                case TYPE_2:
                    if (position == list_show.size()) {
                        if (!isTong) {
                            MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                                    .setCategory("home_action")  //事件类别
                                    .setAction("案例滑到最下边")      //事件操作
                                    .build());
                            MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                                    .setCategory("home_action")  //事件类别
                                    .setAction("案例滑到最下边only")//事件操作
                                    .build());
                            Bundle bundle1 = new Bundle();
                            mFirebaseAnalytics.logEvent("案例滑到最下边", bundle1);
                            isTong = true;
                        }
                        myHolder.rl_reader_over.setVisibility(View.VISIBLE);
                        myHolder.bellow_view.setVisibility(View.GONE);
                    } else {
                        myHolder.rl_reader_over.setVisibility(View.GONE);
                        myHolder.bellow_view.setVisibility(View.VISIBLE);
                    }
                    int itemNum = position - mPlanNum;
                    if (mPlanNum > 0 && mPlanNum >= position) {
                        myHolder.view_tag.setVisibility(View.GONE);
                        myHolder.tital.setVisibility(View.GONE);
                        myHolder.tag.setVisibility(View.GONE);
                        myHolder.details.setVisibility(View.GONE);
                        // 设置图片的宽高
                        CommontUtils.setPicHeighAndWidth1(myHolder.picItem, CaseLibraryDetailsActivity.this, mList.get(position - 1).getBig_image_width(), mList.get(position - 1).getBig_image_height(), 0);
                        ImageUtils.disBigImage_Black(mList.get(position - 1).getBig_image_url(), myHolder.picItem);
                        myHolder.picItem.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                List<String> list_url = new ArrayList<>();
                                for (int i = 0; i < list_show.size(); i++) {
                                    list_url.add(list_show.get(i).getBig_image_url());
                                }
                                Intent intent = new Intent(CaseLibraryDetailsActivity.this, ImageDetailsActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable(ClassConstant.HackViewPager.URL, (Serializable) list_url);
                                bundle.putInt(ClassConstant.HackViewPager.POSITION, position - 1);
                                intent.putExtras(bundle);
                                mContext.startActivity(intent);
                            }
                        });
                    } else {
                        if (itemNum < 10) {
                            myHolder.tital.setVisibility(View.VISIBLE);
                            myHolder.tital.setText("0" + itemNum + "   " + mList.get(position - 1).getSpace_tag_name());
                        } else {
                            myHolder.tital.setVisibility(View.VISIBLE);
                            myHolder.tital.setText(itemNum + "   " + mList.get(position - 1).getSpace_tag_name());
                        }
                        // 设置图片的宽高
                        CommontUtils.setPicHeighAndWidth1(myHolder.picItem, CaseLibraryDetailsActivity.this, mList.get(position - 1).getBig_image_width(), mList.get(position - 1).getBig_image_height(), 0);
                        ImageUtils.disBigImage(mList.get(position - 1).getBig_image_url(), myHolder.picItem);
                        myHolder.picItem.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                List<String> list_url = new ArrayList<>();
                                for (int i = 0; i < list_show.size(); i++) {
                                    list_url.add(list_show.get(i).getBig_image_url());
                                }
                                Intent intent = new Intent(CaseLibraryDetailsActivity.this, ImageDetailsActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable(ClassConstant.HackViewPager.URL, (Serializable) list_url);
                                bundle.putInt(ClassConstant.HackViewPager.POSITION, position - 1);
                                intent.putExtras(bundle);
                                mContext.startActivity(intent);
                            }
                        });
                        StringBuffer sbTag = new StringBuffer();
                        List<DetailPicItemCateTagBean> list1 = mList.get(position - 1).getCate_tag().getDecoration_tag();
                        List<DetailPicItemCateTagBean> list2 = mList.get(position - 1).getCate_tag().getLayout_tag();
                        List<DetailPicItemCateTagBean> list3 = mList.get(position - 1).getCate_tag().getOriginality_tag();
                        List<DetailPicItemCateTagBean> list4 = mList.get(position - 1).getCate_tag().getSpace_tag();
                        List<DetailPicItemCateTagBean> list5 = mList.get(position - 1).getCate_tag().getStorage_tag();
                        if (null != list1 && list1.size() > 0) {
                            for (int i = 0; i < list1.size(); i++) {
                                sbTag.append("#" + list1.get(i).getTag_name() + "    ");
                            }
                        }
                        if (null != list2 && list2.size() > 0) {
                            for (int i = 0; i < list2.size(); i++) {
                                sbTag.append("#" + list2.get(i).getTag_name() + "    ");
                            }
                        }
                        if (null != list3 && list3.size() > 0) {
                            for (int i = 0; i < list3.size(); i++) {
                                sbTag.append("#" + list3.get(i).getTag_name() + "    ");
                            }
                        }
                        if (null != list4 && list4.size() > 0) {
                            for (int i = 0; i < list4.size(); i++) {
                                sbTag.append("#" + list4.get(i).getTag_name() + "    ");
                            }
                        }
                        if (null != list5 && list5.size() > 0) {
                            for (int i = 0; i < list5.size(); i++) {
                                sbTag.append("#" + list5.get(i).getTag_name() + "    ");
                            }
                        }
                        if (TextUtils.isEmpty(sbTag.toString())) {
                            myHolder.tag.setVisibility(View.GONE);
                        } else {
                            myHolder.tag.setVisibility(View.VISIBLE);
                            myHolder.tag.setText(sbTag.toString());
                        }
                        if (TextUtils.isEmpty(mList.get(position - 1).getDescription())) {
                            myHolder.details.setVisibility(View.GONE);
                        } else {
                            myHolder.details.setVisibility(View.VISIBLE);
                        }
                        myHolder.details.setText(mList.get(position - 1).getDescription());
                    }
                    break;
            }
            return convertView;
        }

        class MyHolderTop {
            private TextView tv_tital_name;
            private TextView tv_tital_detail;
            private TextView tv_casedetails_roomtype;
            private TextView tv_casedetails_space;
            private TextView tv_casedetails_price;
            private ImageView iv_casedetails_avatar_pic;
            private ImageView iv_casedetails_avatar_certification;
            private TextView tv_casedetails_desiner_name;
            private TextView tv_jianjie_detail;
            private TextView tv_huxing_pic_tital;
            private View view_left;
            private View view_center;
            private View view_no_huxing;
        }

        class MyHolder {
            private TextView tital;
            private SimpleDraweeView picItem;
            private TextView tag;
            private TextView details;
            private View view_tag;
            private RelativeLayout rl_reader_over;
            private View bellow_view;
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
                Toast.makeText(CaseLibraryDetailsActivity.this, platform + " 收藏成功啦", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(CaseLibraryDetailsActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(CaseLibraryDetailsActivity.this, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
                if (t != null) {
                    Log.d("throw", "throw:" + t.getMessage());
                }
            }

        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {

            Toast.makeText(CaseLibraryDetailsActivity.this, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
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

    private FirebaseAnalytics mFirebaseAnalytics;
    private CustomShareListener mShareListener;
    private DetailTitalBean detailTitalBean;
    private DetailPicBean detailPicBean;
    private MyScrollListAdapter adapter;

    private List<DetailPicItemBean> list_show;
    private ListView lvCaseLibary;

    private RelativeLayout rl_zhuye;
    private RelativeLayout rl_chat;
    private ImageButton sharedIcon;
    private ImageButton ibBack;
    private ImageView iv_header;

    private boolean isTong = false;
    private String mAlbumId;
    private String mItemId;
    private int picNum = 200;
    private int offset = 0;
    private int plam = 0;
    private int next = 1;

}
