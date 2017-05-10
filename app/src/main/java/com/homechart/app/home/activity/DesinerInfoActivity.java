package com.homechart.app.home.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.homechart.app.MyApplication;
import com.homechart.app.R;
import com.homechart.app.commont.utils.ChatUtils;
import com.homechart.app.commont.utils.CommontUtils;
import com.homechart.app.commont.utils.ImageUtils;
import com.homechart.app.commont.utils.SharedPreferencesUtils;
import com.homechart.app.commont.utils.ToastUtils;
import com.homechart.app.commont.utils.UIUtils;
import com.homechart.app.home.bean.DesinerCenterBean;
import com.homechart.app.netutils.MPServerHttpManager;
import com.homechart.app.netutils.OkStringRequest;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.homechart.app.MyHXChatActivity;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.utils.Log;

/**
 * Created by GPD on 2017/3/6.
 */

public class DesinerInfoActivity extends BaseActivity implements View.OnClickListener {
    private ImageButton ibBack;
    private DesinerCenterBean desinerCenterBean;
    private ImageView ivAvatar;
    private ImageView iv_zhuye;
    private ImageView iv_header;
    private ImageView ivLocation;
    private TextView tv_zhuye;
    private TextView ivDesinerLocation;
    private TextView ivDesinerName;
    private TextView ivDesinerPrice;
    private TextView ivDesinerBiaoZun;
    private TextView ivDesinerXiangMu;
    private TextView ivDesinerQuYu;
    private TextView ivDesinerLiuCheng;
    private TextView ivDesinerGuanYuWo;
    private TextView ivLianXiOne;
    private TextView ivLianXiSix;
    private TextView ivLianXiTwo;
    private TextView ivLianXiThree;
    private TextView ivLianXiFour;
    private TextView ivLianXiFive;
    private TextView ivDesinerPricEnd;
    private TextView ivTitalBiaoZun;
    private TextView ivTitalLiuCheng;
    private RelativeLayout rl_one;
    private RelativeLayout rl_two;
    private RelativeLayout rl_phone;
    private RelativeLayout rl_chat;
    private RelativeLayout rl_three;
    private RelativeLayout rl_four;
    private RelativeLayout rl_five;
    private RelativeLayout rl_six;
    private TextView ivTitalLianXi;
    private ImageButton sharedIcon;
    private String user_id;
    private CustomShareListener mShareListener;
    private String header;
    private String name;
    private FirebaseAnalytics mFirebaseAnalytics;
    private TextView tv_lianxi_one;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_desiner_info;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Get tracker.
        Tracker t = MyApplication.getInstance().getDefaultTracker();
        // Set screen name.
        t.setScreenName("设计师详情页2");
        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());

    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();
        desinerCenterBean = (DesinerCenterBean) getIntent().getSerializableExtra("info");
        user_id = getIntent().getStringExtra("user_id");
        header = getIntent().getStringExtra("ic_default_head");
        name = getIntent().getStringExtra("name");
    }

    @Override
    protected void initView() {
        sharedIcon = (ImageButton) findViewById(R.id.nav_secondary_imageButton);
        ibBack = (ImageButton) findViewById(R.id.nav_left_imageButton);
        ivAvatar = (ImageView) findViewById(R.id.iv_header_desiner_center);
        iv_header = (ImageView) findViewById(R.id.iv_header);
        iv_zhuye = (ImageView) findViewById(R.id.iv_zhuye);
        ivLocation = (ImageView) findViewById(R.id.iv_desiner_location);
        tv_zhuye = (TextView) findViewById(R.id.tv_zhuye);
        tv_lianxi_one = (TextView) findViewById(R.id.tv_lianxi_one);
        ivDesinerLocation = (TextView) findViewById(R.id.tv_desiner_location);
        ivDesinerName = (TextView) findViewById(R.id.tv_name_desiner_center);
        ivDesinerPrice = (TextView) findViewById(R.id.tv_info_price);
        ivDesinerPricEnd = (TextView) findViewById(R.id.tv_info_end);
        ivDesinerBiaoZun = (TextView) findViewById(R.id.tv_info_biaozun);
        ivDesinerXiangMu = (TextView) findViewById(R.id.tv_info_xiangmu);
        ivDesinerQuYu = (TextView) findViewById(R.id.tv_info_quyu);
        ivDesinerLiuCheng = (TextView) findViewById(R.id.tv_info_liucheng);
        ivDesinerGuanYuWo = (TextView) findViewById(R.id.tv_info_guanyuwo);
        ivLianXiOne = (TextView) findViewById(R.id.tv_lianxi_one);
        ivLianXiTwo = (TextView) findViewById(R.id.tv_lianxi_two);
        ivLianXiThree = (TextView) findViewById(R.id.tv_lianxi_three);
        ivLianXiFour = (TextView) findViewById(R.id.tv_lianxi_four);
        ivLianXiFive = (TextView) findViewById(R.id.tv_lianxi_five);
        ivLianXiSix = (TextView) findViewById(R.id.tv_lianxi_six);
        ivTitalBiaoZun = (TextView) findViewById(R.id.tv_tital_biaozun);
        ivTitalLiuCheng = (TextView) findViewById(R.id.tv_tital_liucheng);
        ivTitalLianXi = (TextView) findViewById(R.id.tv_tital_lianxifangshi);
        rl_one = (RelativeLayout) findViewById(R.id.rl_one);
        rl_two = (RelativeLayout) findViewById(R.id.rl_two);
        rl_phone = (RelativeLayout) findViewById(R.id.rl_phone);
        rl_chat = (RelativeLayout) findViewById(R.id.rl_chat);
        rl_three = (RelativeLayout) findViewById(R.id.rl_three);
        rl_four = (RelativeLayout) findViewById(R.id.rl_four);
        rl_five = (RelativeLayout) findViewById(R.id.rl_five);
        rl_six = (RelativeLayout) findViewById(R.id.rl_six);

    }

    @Override
    protected void initListener() {
        super.initListener();
        ibBack.setOnClickListener(this);
        sharedIcon.setOnClickListener(this);
        rl_phone.setOnClickListener(this);
        rl_chat.setOnClickListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        sharedIcon.setImageResource(R.drawable.shared_icon);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        String ifLogin = SharedPreferencesUtils.readString("login_in");
        String userid = SharedPreferencesUtils.readString("user_id");
        if (TextUtils.isEmpty(ifLogin)) {
            mFirebaseAnalytics.setUserId(CommontUtils.getPhoneImail(DesinerInfoActivity.this));
        } else {
            if (ifLogin.equals("register")) {
                mFirebaseAnalytics.setUserId(userid + "$设计师");
            } else {
                mFirebaseAnalytics.setUserId(userid);
            }
        }
        mShareListener = new CustomShareListener();
        if (desinerCenterBean != null) {
            if (TextUtils.isEmpty(desinerCenterBean.getData().getMobile())) {
                tv_zhuye.setTextColor(UIUtils.getColor(R.color.nophone));
                iv_zhuye.setImageResource(R.drawable.nophone);
            }
            //desinerCenterBean.getData().getMobile()
            //TODO
//            if(){
//                tv_lianxi_one
//            }
            ImageUtils.displayRoundImage(desinerCenterBean.getData().getAvatar(), ivAvatar);
            ImageUtils.displayRoundImage(desinerCenterBean.getData().getAvatar(), iv_header);
            ivDesinerName.setText(desinerCenterBean.getData().getNickname());
            if (!TextUtils.isEmpty(desinerCenterBean.getData().getLocation().trim())) {
                ivDesinerLocation.setVisibility(View.VISIBLE);
                ivLocation.setVisibility(View.VISIBLE);
                ivDesinerLocation.setText(desinerCenterBean.getData().getLocation());
            } else {
                ivDesinerLocation.setVisibility(View.GONE);
                ivLocation.setVisibility(View.GONE);
            }
            if (TextUtils.isEmpty(desinerCenterBean.getData().getService_price())) {
                ivDesinerPricEnd.setVisibility(View.GONE);
                ivDesinerPrice.setText("价格面议");
            } else {
                ivDesinerPrice.setText(desinerCenterBean.getData().getService_price());
            }
            if (TextUtils.isEmpty(desinerCenterBean.getData().getFee_scale())) {
                ivTitalBiaoZun.setVisibility(View.GONE);
                ivDesinerBiaoZun.setVisibility(View.GONE);
            } else {
                ivDesinerBiaoZun.setText(Html.fromHtml(desinerCenterBean.getData().getFee_scale()));
            }

            if (TextUtils.isEmpty(desinerCenterBean.getData().getService_items())) {
                ivTitalLiuCheng.setText("");
            } else {
                ivDesinerXiangMu.setText(Html.fromHtml(desinerCenterBean.getData().getService_items()));
            }

            ivDesinerQuYu.setText(desinerCenterBean.getData().getService_area());
            ivDesinerLiuCheng.setText(Html.fromHtml(desinerCenterBean.getData().getService_flow()));
            ivDesinerGuanYuWo.setText(Html.fromHtml(desinerCenterBean.getData().getDescription()));

            if (TextUtils.isEmpty(desinerCenterBean.getData().getMobile())) {
                rl_one.setVisibility(View.GONE);
            } else {
                rl_one.setVisibility(View.VISIBLE);
                ivLianXiOne.setText(desinerCenterBean.getData().getMobile());
            }//1
            if (TextUtils.isEmpty(desinerCenterBean.getData().getEmail())) {
                rl_two.setVisibility(View.GONE);
            } else {
                ivLianXiTwo.setText(desinerCenterBean.getData().getEmail());
            }//2
            if (TextUtils.isEmpty(desinerCenterBean.getData().getHomepage())) {
                rl_three.setVisibility(View.GONE);
            } else {
                ivLianXiThree.setText(desinerCenterBean.getData().getHomepage());
            }//3
            if (TextUtils.isEmpty(desinerCenterBean.getData().getWechat())) {
                rl_four.setVisibility(View.GONE);
            } else {
                ivLianXiFour.setText(desinerCenterBean.getData().getWechat());
            }//4
            if (TextUtils.isEmpty(desinerCenterBean.getData().getQq())) {
                rl_five.setVisibility(View.GONE);
            } else {
                ivLianXiFive.setText(desinerCenterBean.getData().getQq());
            }//5
            if (TextUtils.isEmpty(desinerCenterBean.getData().getAddress())) {
                rl_six.setVisibility(View.GONE);
            } else {
                ivLianXiSix.setText(desinerCenterBean.getData().getAddress());
            }//6

            if (TextUtils.isEmpty(desinerCenterBean.getData().getMobile()) &&
                    TextUtils.isEmpty(desinerCenterBean.getData().getEmail()) &&
                    TextUtils.isEmpty(desinerCenterBean.getData().getHomepage()) &&
                    TextUtils.isEmpty(desinerCenterBean.getData().getWechat()) &&
                    TextUtils.isEmpty(desinerCenterBean.getData().getQq()) &&
                    TextUtils.isEmpty(desinerCenterBean.getData().getAddress())) {
                ivTitalLianXi.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_left_imageButton:
                DesinerInfoActivity.this.finish();
                break;
            case R.id.nav_secondary_imageButton:
                if (null != desinerCenterBean && null != desinerCenterBean.getData() && null != desinerCenterBean.getData()) {
                    MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                            .setCategory("designerInfo2_action")  //事件类别
                            .setAction("分享设计师")      //事件操作
                            .build());
                    Bundle bundle = new Bundle();
                    mFirebaseAnalytics.logEvent("设计师详情页2分享设计师", bundle);
                    UMImage image = new UMImage(DesinerInfoActivity.this, desinerCenterBean.getData().getAvatar());
                    image.compressStyle = UMImage.CompressStyle.SCALE;//大小压缩，默认为大小压缩，适合普通很大的图
                    image.compressStyle = UMImage.CompressStyle.QUALITY;//质量压缩，适合长图的分享
//                        UMWeb web = new UMWeb("http://idcool.com.cn/project/" + mAlbumId);
                    UMWeb web = new UMWeb("http://www.idcool.com.cn/u/" + user_id);
                    web.setTitle(desinerCenterBean.getData().getNickname());//标题
                    web.setThumb(image);  //缩略图
                    web.setDescription(desinerCenterBean.getData().getDescription());//描述
                    new ShareAction(DesinerInfoActivity.this).setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ,
                            SHARE_MEDIA.QZONE, SHARE_MEDIA.SINA).withMedia(web).setCallback(mShareListener).open();
                } else {
                    Toast.makeText(DesinerInfoActivity.this, "暂无内容可以分享", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.rl_phone:
                if (null != desinerCenterBean
                        && null != desinerCenterBean.getData()
                        && null != desinerCenterBean.getData().getMobile()) {
                    if (!TextUtils.isEmpty(desinerCenterBean.getData().getMobile())) {
                        MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                                .setCategory("designerInfo2_action")  //事件类别
                                .setAction("打电话")      //事件操作
                                .setLabel(desinerCenterBean.getData().getNickname())
                                .build());
                        MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                                .setCategory("designerInfo2_action")  //事件类别
                                .setAction("打电话only")//事件操作
                                .build());
                        Bundle bundle = new Bundle();
                        bundle.putString("designerName", desinerCenterBean.getData().getNickname());
                        mFirebaseAnalytics.logEvent("设计师详情页2打电话", bundle);
                        openCall(desinerCenterBean.getData().getMobile());
                    }
                } else {
                    ToastUtils.showCenter(DesinerInfoActivity.this, "请重新获取号码");
                }
                break;
            case R.id.rl_chat:
                String ifLogin = SharedPreferencesUtils.readString("login_in");
                String ticket = SharedPreferencesUtils.readString("ticket");
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("dedsignerInfo2_action")  //事件类别
                        .setAction("咨询按钮点击")      //事件操作
                        .setLabel(name)
                        .build());
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("dedsignerInfo2_action")  //事件类别
                        .setAction("咨询按钮点击only")//事件操作
                        .build());
                Bundle bundle1 = new Bundle();
                bundle1.putString("designerName", name);
                mFirebaseAnalytics.logEvent("设计师详情页2咨询按钮点击", bundle1);
                if (!TextUtils.isEmpty(ifLogin)) {
                    MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                            .setCategory("dedsignerInfo2_action")  //事件类别
                            .setAction("进入咨询设计师")      //事件操作
                            .setLabel(name)
                            .build());
                    MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                            .setCategory("dedsignerInfo2_action")  //事件类别
                            .setAction("进入咨询设计师only")//事件操作
                            .build());
                    Bundle bundle2 = new Bundle();
                    bundle2.putString("designerName", name);
                    mFirebaseAnalytics.logEvent("设计师详情页2进入咨询设计师", bundle2);
                    getChatList(user_id, ticket);
                    EaseUser easeUser = new EaseUser(user_id);
                    easeUser.setAvatar(header);
                    easeUser.setNickname(name);
                    ChatUtils.getInstance().put(user_id, easeUser);
                    Intent intent1 = new Intent(this, MyHXChatActivity.class);
                    intent1.putExtra(EaseConstant.EXTRA_USER_ID, user_id);
                    startActivity(intent1);
                } else {
                    Intent intent2 = new Intent(DesinerInfoActivity.this, UserLoginActivity.class);
                    startActivity(intent2);
                }
                break;
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
                Toast.makeText(DesinerInfoActivity.this, platform + " 收藏成功啦", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(DesinerInfoActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(DesinerInfoActivity.this, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
                if (t != null) {
                    Log.d("throw", "throw:" + t.getMessage());
                }
            }

        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {

            Toast.makeText(DesinerInfoActivity.this, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    }
}
