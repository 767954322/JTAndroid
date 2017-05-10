package com.homechart.app.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.homechart.app.MyApplication;
import com.homechart.app.R;
import com.homechart.app.commont.utils.CommontUtils;
import com.homechart.app.commont.utils.SharedPreferencesUtils;

/**
 * Created by Administrator on 2017/3/17/017.
 */

public class UserBundleActivity extends BaseActivity implements View.OnClickListener {
    private String name;
    private TextView tv_bundle_name;
    private Button btn_guanliang_yiyou;
    private Button btn_guanliang_kuaishu;
    private ImageButton back;
    private TextView tital;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_bundle;
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();
        name = getIntent().getStringExtra("username");
    }

    @Override
    protected void initView() {
        tv_bundle_name = (TextView) findViewById(R.id.tv_bundle_name);
        btn_guanliang_yiyou = (Button) findViewById(R.id.btn_guanliang_yiyou);
        btn_guanliang_kuaishu = (Button) findViewById(R.id.btn_guanliang_kuaishu);
        tital = (TextView) findViewById(R.id.tv_tital_comment);
        back = (ImageButton) findViewById(R.id.nav_left_imageButton);
    }

    @Override
    protected void initListener() {
        super.initListener();
        btn_guanliang_yiyou.setOnClickListener(this);
        btn_guanliang_kuaishu.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Get tracker.
        Tracker t = MyApplication.getInstance().getDefaultTracker();
        // Set screen name.
        t.setScreenName("选择绑定页");
        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        tv_bundle_name.setText(name);
        tital.setText("完善个人资料");
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        String ifLogin = SharedPreferencesUtils.readString("login_in");
        String userid = SharedPreferencesUtils.readString("user_id");
        if (TextUtils.isEmpty(ifLogin)) {
            mFirebaseAnalytics.setUserId(CommontUtils.getPhoneImail(UserBundleActivity.this));
        } else {
            if (ifLogin.equals("register")) {
                mFirebaseAnalytics.setUserId(userid + "$设计师");
            } else {
                mFirebaseAnalytics.setUserId(userid);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_guanliang_yiyou:
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("login_action")  //事件类别
                        .setAction("绑定新客户")      //事件操作
                        .build());
                Bundle bundle1 = new Bundle();
                mFirebaseAnalytics.logEvent("绑定新客户", bundle1);
                Intent intent = new Intent(UserBundleActivity.this, BeforeGLActivity.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.btn_guanliang_kuaishu:
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("login_action")  //事件类别
                        .setAction("绑定老客户")      //事件操作
                        .build());
                Bundle bundle = new Bundle();
                mFirebaseAnalytics.logEvent("绑定老客户", bundle);
                Intent intent_kuai = new Intent(UserBundleActivity.this, RunGLActivity.class);
                startActivityForResult(intent_kuai, 0);
                break;
            case R.id.nav_left_imageButton:
                UserBundleActivity.this.finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_OK:
                if (data != null) {
                    String status = data.getStringExtra("status");
                    Intent intent_result = getIntent();
                    intent_result.putExtra("status", status);
                    setResult(10, intent_result);
                    UserBundleActivity.this.finish();
                }
                break;
            default:
                break;
        }
    }
}
