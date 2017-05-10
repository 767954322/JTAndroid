package com.homechart.app.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.homechart.app.MyApplication;
import com.homechart.app.R;
import com.homechart.app.commont.alertview.AlertView;
import com.homechart.app.commont.alertview.OnItemClickListener;
import com.homechart.app.commont.constants.ClassConstant;
import com.homechart.app.commont.utils.ChatUtils;
import com.homechart.app.commont.utils.CommontUtils;
import com.homechart.app.commont.utils.CustomProgress;
import com.homechart.app.commont.utils.DataCleanManager;
import com.homechart.app.commont.utils.MPFileUtility;
import com.homechart.app.commont.utils.SharedPreferencesUtils;
import com.homechart.app.commont.utils.ToastUtils;
import com.homechart.app.commont.utils.UIUtils;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.io.File;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/17/017.
 */

public class SetActivity extends BaseActivity implements View.OnClickListener, OnItemClickListener {
    private Button btn_outlogin;
    private ImageButton back;
    private AlertView mAlertView;
    private TextView tv_clear_num;
    private File cacheDir;
    private File filesDir;
    private File externalCacheDir;
    private String cacheSize;
    private RelativeLayout rl_set_clear_cash;
    private String filesSize;
    private String externalSize;
    private Intent intentLogout;
    private TextView tv_banben_code;
    private RelativeLayout rl_set_fankui;
    private TextView tv_tital_comment;
    private RelativeLayout rl_set_banben;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_home_set;
    }

    @Override
    protected void initView() {
        back = (ImageButton) findViewById(R.id.nav_left_imageButton);
        btn_outlogin = (Button) findViewById(R.id.btn_outlogin);
        btn_outlogin = (Button) findViewById(R.id.btn_outlogin);
        tv_tital_comment = (TextView) findViewById(R.id.tv_tital_comment);
        tv_clear_num = (TextView) findViewById(R.id.tv_clear_num);
        tv_banben_code = (TextView) findViewById(R.id.tv_banben_code);
        rl_set_clear_cash = (RelativeLayout) findViewById(R.id.rl_set_clear_cash);
        rl_set_fankui = (RelativeLayout) findViewById(R.id.rl_set_fankui);
        rl_set_banben = (RelativeLayout) findViewById(R.id.rl_set_banben);
    }

    @Override
    protected void initListener() {
        super.initListener();
        btn_outlogin.setOnClickListener(this);
        back.setOnClickListener(this);
        rl_set_fankui.setOnClickListener(this);
        rl_set_clear_cash.setOnClickListener(this);
        rl_set_banben.setOnClickListener(this);
    }


    @Override
    protected void initData(Bundle savedInstanceState) {
        intentLogout = getIntent();
        tv_tital_comment.setText("设置");
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        String ifLogin = SharedPreferencesUtils.readString("login_in");
        String userid = SharedPreferencesUtils.readString("user_id");
        if (TextUtils.isEmpty(ifLogin)) {
            mFirebaseAnalytics.setUserId(CommontUtils.getPhoneImail(SetActivity.this));
        } else {
            if (ifLogin.equals("register")) {
                mFirebaseAnalytics.setUserId(userid + "$设计师");
            } else {
                mFirebaseAnalytics.setUserId(userid);
            }
        }
        cacheDir = getCacheDir();
        filesDir = getFilesDir();
        externalCacheDir = getExternalCacheDir();
        String banben_code = CommontUtils.getVersionName(this);
        tv_banben_code.setText("v " + banben_code);
        showDialog();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Get tracker.
        Tracker t = MyApplication.getInstance().getDefaultTracker();
        // Set screen name.
        t.setScreenName("设置页");
        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
        try {
            double mpCachesize = DataCleanManager.getFolderSize(MPFileUtility.getCacheRootDirectoryHandle(this));
            double othercachesize = DataCleanManager.getFolderSize(cacheDir);
            cacheSize = DataCleanManager.getFormatSize(mpCachesize + othercachesize);
            filesSize = DataCleanManager.getCacheSize(filesDir);
            externalSize = DataCleanManager.getCacheSize(externalCacheDir);
        } catch (Exception e) {
            e.printStackTrace();
        }
        tv_clear_num.setText(cacheSize);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_set_banben:
                if (false) {
                    MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                            .setCategory("my_action")  //事件类别
                            .setAction("更新")      //事件操作
                            .build());
                    Bundle bundle = new Bundle();
                    mFirebaseAnalytics.logEvent("更新", bundle);
                }
                break;
            case R.id.btn_outlogin:
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("my_action")  //事件类别
                        .setAction("退出登录")      //事件操作
                        .build());
                //TODO
                Intent intent = new Intent();
                intent.setAction("notify_reader");
                sendBroadcast(intent);

                Intent intent1 = new Intent();
                intent1.setAction("reader");
                sendBroadcast(intent1);

                Bundle bundle = new Bundle();
                mFirebaseAnalytics.logEvent("我的页面_退出登录", bundle);
                ChatUtils.LoginOutEaseUI();
                CommontUtils.clearAppCache(this);
                //清除友盟授权
                clearUMengOauth();
                clearStared();
                Intent intent_result = getIntent();
                intent_result.putExtra("Login", "OK");
                setResult(2, intent_result);
                SetActivity.this.finish();
                break;
            case R.id.nav_left_imageButton:
                SetActivity.this.finish();
            case R.id.rl_set_clear_cash:
                mAlertView.show();
                break;
            case R.id.rl_set_fankui:
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("my_action")  //事件类别
                        .setAction("反馈点击")      //事件操作
                        .build());
                Bundle bundle6 = new Bundle();
                mFirebaseAnalytics.logEvent("我的页面_反馈点击", bundle6);
                Intent intent2 = new Intent(this, IssueBackActivity.class);
                startActivity(intent2);
                break;
        }
    }

    private void clearUMengOauth() {
        UMShareAPI.get(this).deleteOauth(SetActivity.this, ClassConstant.UMengPlatform.platform_qq, umAuthListener);
        UMShareAPI.get(this).deleteOauth(SetActivity.this, ClassConstant.UMengPlatform.platform_weixin, umAuthListener);
        UMShareAPI.get(this).deleteOauth(SetActivity.this, ClassConstant.UMengPlatform.platform_sina, umAuthListener);
    }

    private void clearStared() {
        SharedPreferencesUtils.clear(this, "login_in");
        SharedPreferencesUtils.clear(this, "user_id");
        SharedPreferencesUtils.clear(this, "nickname");
        SharedPreferencesUtils.clear(this, "avatar");
        SharedPreferencesUtils.clear(this, "ticket");
        SharedPreferencesUtils.clear(this, "mobile");
        SharedPreferencesUtils.clear(this, "email");
        SharedPreferencesUtils.clear(this, "profession");
        SharedPreferencesUtils.clear(this, "project_num");
        SharedPreferencesUtils.clear(this, "ease_username");
        SharedPreferencesUtils.clear(this, "ease_password");
    }

    /***
     * 提示框
     */
    private void showDialog() {
        mAlertView =
                new AlertView(UIUtils.getString(R.string.clear_cath_toast),
                        null, UIUtils.getString(R.string.cancel), new String[]{UIUtils.getString(R.string.clear_cath_sure)},
                        null, this, AlertView.Style.ActionSheet, this);
    }

    @Override
    public void onItemClick(Object object, int position) {
        if (object == mAlertView && position != AlertView.CANCELPOSITION) {
            if (getCacheSize().equals("0.0B")) {
                ToastUtils.showCenter(this, UIUtils.getString(R.string.no_cath));
            } else {
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("my_action")  //事件类别
                        .setAction("清除缓存")      //事件操作
                        .build());
                Bundle bundle = new Bundle();
                mFirebaseAnalytics.logEvent("我的页面_清除缓存", bundle);
                CustomProgress.show(SetActivity.this, "清除缓存中...", false, null);
                CommontUtils.clearAppCache(this);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tv_clear_num.setText("0.0MB");
                        CustomProgress.cancelDialog();
                        ToastUtils.showCenter(SetActivity.this, "缓存已清完");
                    }
                }, 1000);
            }
        } else {
            return;
        }
        return;
    }

    /**
     * file-普通的文件存储
     * database-数据库文件（.db文件）
     * sharedPreference-配置数据(.xml文件）
     * cache-图片缓存文件
     */
    private String getCacheSize() {
        try {
            //cacheSize = DataCleanManager.getCacheSize(cacheDir);
            double mpCachesize = DataCleanManager.getFolderSize(MPFileUtility.getCacheRootDirectoryHandle(this));
            double othercachesize = DataCleanManager.getFolderSize(cacheDir);

            cacheSize = DataCleanManager.getFormatSize(mpCachesize + othercachesize);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return cacheSize;
    }

    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //授权开始的回调
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
        }
    };
}
