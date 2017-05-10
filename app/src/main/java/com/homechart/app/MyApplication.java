package com.homechart.app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.baidu.mapapi.SDKInitializer;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.homechart.app.commont.utils.ChatUtils;
import com.homechart.app.commont.utils.CommontUtils;
import com.homechart.app.commont.utils.ImageUtils;
import com.homechart.app.commont.utils.SharedPreferencesUtils;
import com.homechart.app.commont.utils.UILImageLoader;
import com.hyphenate.chat.EMChatManager;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.controller.EaseUI;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.common.SocializeConstants;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ImageLoader;
import cn.finalteam.galleryfinal.ThemeConfig;

public class MyApplication extends MultiDexApplication {

    private static MyApplication myApplication;
    public static RequestQueue queue;

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        initPick();
        initYouMeng();
        initData();
        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
//        options.setHuaweiPushAppId(appId);
//        options.setMipushConfig(APP_ID, APP_KEY);
        //初始化
        EaseUI.getInstance().init(MyApplication.getInstance(), options);
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(false);
        //百度初始化
        SDKInitializer.initialize(this);
        ChatUtils.getInstance().init();
    }

    private void initPick() {
        ThemeConfig themeConfig = ThemeConfig.DEFAULT;
        FunctionConfig functionConfig = new FunctionConfig.Builder()
                .setMutiSelectMaxSize(1)
                .setEnableEdit(true)
                .setCropSquare(true).build();
        ImageLoader imageLoader = new UILImageLoader();
        CoreConfig coreConfig = new CoreConfig.Builder(this, imageLoader, themeConfig)
                .setFunctionConfig(functionConfig).build();
        GalleryFinal.init(coreConfig);
    }

    private void initYouMeng() {
        //初始化友盟sdk
        UMShareAPI.get(this.getApplicationContext());
        //配置三方平台的appkey
        PlatformConfig.setWeixin("wx9f5d6adeded62a61", "a32cd3c376f0881d7f6a5679a473a5bc");
        PlatformConfig.setQQZone("101164104", "28e0959303e8960b06bfa217f959f1d7");
        PlatformConfig.setSinaWeibo("3994674331", "7450446dd1d555532d14982a13f70408", "http://sns.whalecloud.com/sina2/callback");
        Config.DEBUG = true;
    }

    private void initData() {
        queue = Volley.newRequestQueue(this);
        ImageUtils.initImageLoader(this);
    }

    public static synchronized MyApplication getInstance() {
        return myApplication;
    }


    public static RequestQueue getRequestQuery(Context context) {

        if (queue == null) {
            queue = Volley.newRequestQueue(context);
        }
        return queue;
    }

    private Tracker mTracker;

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     *
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
    }

}
