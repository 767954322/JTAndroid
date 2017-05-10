package com.homechart.app.home.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.volley.VolleyError;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.homechart.app.MyApplication;
import com.homechart.app.R;
import com.homechart.app.commont.utils.ChatUtils;
import com.homechart.app.commont.utils.CommontUtils;
import com.homechart.app.commont.utils.GsonUtil;
import com.homechart.app.commont.utils.SharedPreferencesUtils;
import com.homechart.app.home.bean.ChatListBean;
import com.homechart.app.home.bean.ChatListDataUserBean;
import com.homechart.app.home.fragment.HomeCenterDesinerFragment;
import com.homechart.app.home.fragment.HomeCenterFragment;
import com.homechart.app.home.fragment.HomeDesignerFragment;
import com.homechart.app.home.fragment.HomePicFragment;
import com.homechart.app.netutils.MPServerHttpManager;
import com.homechart.app.netutils.OkStringRequest;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.model.EaseNotifier;
import com.homechart.app.MyHXChatActivity;
import com.jauker.widget.BadgeView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Allen.Gu  .
 * @version v1.0 .
 * @date 2017-2-24.
 * @file ConsumerHomeActivity.java .
 * @brief 进入主页 .
 */
public class ConsumerHomeActivity
        extends BaseActivity
        implements RadioGroup.OnCheckedChangeListener,
        EMMessageListener {
    private FragmentTransaction transaction;
    private HomePicFragment mHomePicFragment;
    private HomeDesignerFragment mHomeDesignerFragment;
    private HomeCenterFragment mHomeCenterFragment;
    private Fragment mTagFragment;
    private RadioGroup mRadioGroup;
    private int jumpPosition = 0;
    private HomeCenterDesinerFragment mHomeCenterDesinerFragment;
    private List<Fragment> mFragmentArrayList = new ArrayList<>();
    public FirebaseAnalytics mFirebaseAnalytics;
    private String user_id;
    private BadgeView badgeView;
    private RadioButton radio_btn_center;
    private ImageView iv_red_icon;
    private NetChangReceiver netChangReceiver;
    private Tracker mTracker;
    private String desiner_id;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_home_main;
    }

    @Override
    protected void initView() {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        String ifLogin = SharedPreferencesUtils.readString("login_in");
        String userid = SharedPreferencesUtils.readString("user_id");
        if (TextUtils.isEmpty(ifLogin)) {
            mFirebaseAnalytics.setUserId(CommontUtils.getPhoneImail(ConsumerHomeActivity.this));
        } else {
            if (ifLogin.equals("register")) {
                mFirebaseAnalytics.setUserId(userid + "$设计师");
            } else {
                mFirebaseAnalytics.setUserId(userid);
            }
        }
        mRadioGroup = (RadioGroup) findViewById(R.id.rg_home_radio_group);
        radio_btn_center = (RadioButton) findViewById(R.id.radio_btn_center);
        iv_red_icon = (ImageView) findViewById(R.id.iv_red_icon);
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();
        desiner_id = getIntent().getStringExtra("desiner_id");
    }

    @Override
    protected void initListener() {
        super.initListener();
        mRadioGroup.setOnCheckedChangeListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        //注册EventBus
        initBroad();
//        gaTJ();
//        fareBaseTJ();
        //设置权限
        CommontUtils.verifyStoragePermissions(this);
        //通知
        initNotify();
        EMClient.getInstance().chatManager().addMessageListener(this);
        //注册广播
        registerBoradcastReceiver();
        if (findViewById(R.id.main_content) != null) {
            if (null == mHomePicFragment) {
                mHomePicFragment = new HomePicFragment();
            }
            mTagFragment = mHomePicFragment;
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.main_content, mHomePicFragment).commit();
        }
        mRadioGroup.check(R.id.radio_btn_pic);
        ChatUtils.LoginEaseUI();
        if (!TextUtils.isEmpty(desiner_id)) {
            Intent intent = new Intent(ConsumerHomeActivity.this, DesinerCenterActivity.class);
            intent.putExtra("desiner_id", desiner_id);
            startActivity(intent);
            desiner_id = "";
        }
    }

    private void initBroad() {
        //实例化IntentFilter对象
        IntentFilter filter = new IntentFilter();
        filter.addAction("unreader");
        filter.addAction("reader");
        netChangReceiver = new NetChangReceiver();
        //注册广播接收
        registerReceiver(netChangReceiver, filter);
    }

    private void initNotify() {
        EaseUI.getInstance().getNotifier().setNotificationInfoProvider(new EaseNotifier.EaseNotificationInfoProvider() {

            String name = "";

            @Override
            public String getTitle(EMMessage message) {
                //修改标题,这里使用默认
                return "新消息";
            }

            @Override
            public int getSmallIcon(EMMessage message) {

                //设置小图标，这里为默认
                return R.drawable.icon_app;
            }

            @Override
            public String getDisplayedText(EMMessage message) {

                return "你收到新消息，请及时查看";
            }

            @Override
            public String getLatestText(EMMessage message, int fromUsersNum, int messageNum) {
                return "你收到新消息了，请及时查看";
            }

            @Override
            public Intent getLaunchIntent(EMMessage message) {

                //设置点击通知栏跳转事件
                EaseUser easeUser = new EaseUser(message.getFrom());
                easeUser.setNickname(name);
                ChatUtils.getInstance().put(message.getFrom(), easeUser);

                Intent intent = new Intent(ConsumerHomeActivity.this, MyHXChatActivity.class);
                intent.putExtra(EaseConstant.EXTRA_USER_ID, message.getFrom());
                intent.putExtra("notify_msg", "notify_msg");
                return intent;
            }
        });
    }

//    private void fareBaseTJ() {
//        Bundle bundle = new Bundle();
//        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "android进入首页");
//        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "android进入首页");
//        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "android进入首页");
//        mFirebaseAnalytics.logEvent("android进入首页", bundle);
//    }
//
//    private void gaTJ() {
//        MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
//                .setCategory("android进入首页")  //事件类别
//                .setAction("进入首页")      //事件操作
//                .setLabel("android")
//                .build());
//    }

    private void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("login_out");
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.radio_btn_pic:
                if (null == mHomePicFragment) {
                    mHomePicFragment = new HomePicFragment();
                }
                if (mTagFragment != mHomePicFragment) {
                    mTagFragment = mHomePicFragment;
                    replaceFragment(mHomePicFragment);
                }
                jumpPosition = 0;
                break;
            case R.id.radio_btn_designer:
                if (null == mHomeDesignerFragment) {
                    mHomeDesignerFragment = new HomeDesignerFragment(getSupportFragmentManager());
                }
                if (mTagFragment != mHomeDesignerFragment) {
                    mTagFragment = mHomeDesignerFragment;
                    replaceFragment(mHomeDesignerFragment);
                }
                jumpPosition = 1;
                break;
            case R.id.radio_btn_center:
                String ifLogin = SharedPreferencesUtils.readString("login_in");
                if (TextUtils.isEmpty(ifLogin)) {
                    if (jumpPosition == 0) {
                        mRadioGroup.check(R.id.radio_btn_pic);
                    } else if (jumpPosition == 1) {
                        mRadioGroup.check(R.id.radio_btn_designer);
                    }
                    Intent intent = new Intent(this, UserLoginActivity.class);
                    ConsumerHomeActivity.this.startActivityForResult(intent, 0);
                } else if (ifLogin.equals("login_out")) {
                    jumpPosition = 0;
                    mRadioGroup.check(R.id.radio_btn_pic);
                    SharedPreferencesUtils.writeString("login_in", "");
                } else {
                    if (ifLogin.equals("register")) {
                        if (null == mHomeCenterDesinerFragment) {
                            mHomeCenterDesinerFragment = new HomeCenterDesinerFragment();
                        }
                        if (mTagFragment != mHomeCenterDesinerFragment) {
                            mTagFragment = mHomeCenterDesinerFragment;
                            replaceFragment(mHomeCenterDesinerFragment);
                        }
                    } else if (ifLogin.equals("consumer")) {
                        if (null == mHomeCenterFragment) {
                            mHomeCenterFragment = new HomeCenterFragment();
                        }
                        if (mTagFragment != mHomeCenterFragment) {
                            mTagFragment = mHomeCenterFragment;
                            replaceFragment(mHomeCenterFragment);
                        }
                    }
                }
                break;
        }
    }

    public void replaceFragment(Fragment fragment) {
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_content, fragment);
        transaction.commit();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_OK:
                if (data != null) {
                    String status = data.getStringExtra("status");
                    mRadioGroup.check(R.id.radio_btn_center);
                    if (null != status && status.equals("register")) {
                        if (null == mHomeCenterDesinerFragment) {
                            mHomeCenterDesinerFragment = new HomeCenterDesinerFragment();
                        }
                        if (mTagFragment != mHomeCenterDesinerFragment) {
                            mTagFragment = mHomeCenterDesinerFragment;
                            replaceFragment(mHomeCenterDesinerFragment);
                        }
                        jumpPosition = 2;
                    } else if (null != status && status.equals("consumer")) {
                        if (null == mHomeCenterFragment) {
                            mHomeCenterFragment = new HomeCenterFragment();
                        }
                        if (mTagFragment != mHomeCenterFragment) {
                            mTagFragment = mHomeCenterFragment;
                            replaceFragment(mHomeCenterFragment);
                        }
                        jumpPosition = 2;
                    }
                }
                break;
            case 2:
                if (data != null) {
                    String login = data.getStringExtra("Login");
                    if (login.equals("OK")) {
                        SharedPreferencesUtils.writeString("login_in", "login_out");
                        mRadioGroup.check(R.id.radio_btn_pic);
                        if (null == mHomePicFragment) {
                            mHomePicFragment = new HomePicFragment();
                        }
                        if (mTagFragment != mHomePicFragment) {
                            mTagFragment = mHomePicFragment;
                            replaceFragment(mHomePicFragment);
                        }
                        jumpPosition = 0;
                        mHomeCenterDesinerFragment = null;
                        mHomeCenterFragment = null;
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解除注册
        unregisterReceiver(netChangReceiver);
        EMClient.getInstance().chatManager().removeMessageListener(this);
    }

    @Override
    public void onMessageReceived(List<EMMessage> messages) {
        for (EMMessage message : messages) {
            EaseUI.getInstance().getNotifier().vibrateAndPlayTone(message);
            EaseUI.getInstance().getNotifier().onNewMsg(message);
        }
        int size = 0;
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        for (EMConversation conversation : conversations.values()) {
            if (conversation.getUnreadMsgCount() != 0) {
                size += conversation.getUnreadMsgCount();
            }
        }
        if (size > 0) {
            Intent intent = new Intent();
            intent.setAction("notify_unreader");
            sendBroadcast(intent);

            Intent intent1 = new Intent();
            intent1.setAction("unreader");
            sendBroadcast(intent1);
        } else {
            Intent intent = new Intent();
            intent.setAction("notify_reader");
            sendBroadcast(intent);

            Intent intent1 = new Intent();
            intent1.setAction("reader");
            sendBroadcast(intent1);
        }
    }

    @Override
    public void onCmdMessageReceived(List<EMMessage> list) {
    }

    @Override
    public void onMessageRead(List<EMMessage> list) {
    }

    @Override
    public void onMessageDelivered(List<EMMessage> list) {
    }

    @Override
    public void onMessageChanged(EMMessage emMessage, Object o) {
    }

    //创建一个继承BroadcastReceiver的广播监听器；
    class NetChangReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("unreader")) {
                iv_red_icon.setVisibility(View.VISIBLE);
            } else if (action.equals("reader")) {
                iv_red_icon.setVisibility(View.GONE);
            }
        }
    }
}
