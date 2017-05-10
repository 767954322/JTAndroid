package com.homechart.app.home.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

//import com.google.android.gms.analytics.HitBuilders;
//import com.google.android.gms.analytics.Tracker;
import com.homechart.app.MyApplication;
import com.homechart.app.R;
import com.homechart.app.commont.alertview.AlertAnimateUtil;
import com.homechart.app.commont.alertview.AlertView;
import com.homechart.app.commont.alertview.AlertView2;
import com.homechart.app.commont.alertview.OnItemClickListener;
import com.homechart.app.commont.log.LogManager;
import com.homechart.app.commont.utils.CommontUtils;
import com.homechart.app.commont.utils.SharedPreferencesUtils;
import com.homechart.app.commont.utils.ToastUtils;
import com.homechart.app.commont.utils.UIUtils;
import com.homechart.app.home.adapter.WelcomePagerAdapter;
import com.homechart.app.widget.widget.SwipeViewPager;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * @author allen .
 * @version v1.0 .
 * @date 2017-2-24.
 * @file WelcomeActivity.java .
 * @brief 首次进入滑动页 .
 */
public class WelcomeActivity extends BaseActivity {

    private SwipeViewPager mWelcomeViewPager;

    protected int getLayoutResId() {
        return R.layout.activity_welcome;
    }

    protected void initView() {
        // 全屏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = this.getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        mWelcomeViewPager = (SwipeViewPager) findViewById(R.id.welcome_view_pager);
    }

    protected void initData(Bundle savedInstanceState) {
        if (getNextActivityToLaunch()) {
            //跳转到广告页
            Intent intent = new Intent(this, AdvertisementActivity1.class);
            startActivity(intent);
            finish();
        } else {
            WelcomePagerAdapter adapter = new WelcomePagerAdapter(WelcomeActivity.this, getAdData());
//            //初始化轮播图下面小点
//            mWelcomeViewPager.updateIndicatorView(getAdData().size());
            mWelcomeViewPager.setAdapter(adapter);
            //设置权限
            CommontUtils.verifyStoragePermissions(WelcomeActivity.this);
        }
    }

    public List<Integer> getAdData() {
        List<Integer> adList = new ArrayList<>();
        adList.add(R.drawable.page_one);
        adList.add(R.drawable.page_two);
        adList.add(R.drawable.page_three);
        return adList;
    }

    private boolean getNextActivityToLaunch() {
        Boolean isfirst = SharedPreferencesUtils.readBoolean(WelcomePagerAdapter.ISFIRST);
        return isfirst;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //判断请求码
        switch (requestCode) {
            case 1:
                if (ContextCompat.checkSelfPermission(WelcomeActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(WelcomeActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 2);
                }
                break;
            case 2:
                if (ContextCompat.checkSelfPermission(WelcomeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(WelcomeActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    new AlertView(UIUtils.getString(R.string.addpromiss),
                            null, UIUtils.getString(R.string.setpromiss), new String[]{UIUtils.getString(R.string.okpromiss)},
                            null, this, AlertView.Style.ActionSheet, new OnItemClickListener() {
                        @Override
                        public void onItemClick(Object object, int position) {
                            if (position == -1) {
                                Uri packageURI = Uri.parse("package:" + WelcomeActivity.this.getPackageName());
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                                startActivity(intent);
                            }
                        }
                    }).show();
                }
        }
    }
}
