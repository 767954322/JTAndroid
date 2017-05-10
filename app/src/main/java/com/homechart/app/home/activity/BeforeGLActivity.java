package com.homechart.app.home.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.homechart.app.MyApplication;
import com.homechart.app.R;
import com.homechart.app.commont.constants.ClassConstant;
import com.homechart.app.commont.constants.KeyConstans;
import com.homechart.app.commont.constants.UrlConstants;
import com.homechart.app.commont.utils.CommontUtils;
import com.homechart.app.commont.utils.GsonUtil;
import com.homechart.app.commont.utils.Md5Util;
import com.homechart.app.commont.utils.SharedPreferencesUtils;
import com.homechart.app.commont.utils.ToastUtils;
import com.homechart.app.home.bean.LoginBean;
import com.homechart.app.netutils.MPServerHttpManager;
import com.homechart.app.netutils.OkStringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


/**
 * @author Allen.Gu .
 * @version v1.0 .
 * @date 2017-2-24.
 * @file BeforeGLActivity.java .
 * @brief 关联已有账号 .
 */
public class BeforeGLActivity extends BaseActivity implements View.OnClickListener {


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_associated_account;
    }

    @Override
    protected void initView() {
        loginName = (EditText) findViewById(R.id.et_login_name);
        loginPase = (EditText) findViewById(R.id.et_login_password);
        iv_show_pass = (ImageView) findViewById(R.id.iv_show_pass);
        back = (ImageButton) findViewById(R.id.nav_left_imageButton);
        tv_gorget_pass = (TextView) findViewById(R.id.tv_gorget_pass);
        btn_send_demand = (Button) findViewById(R.id.btn_send_demand);
        tital = (TextView) findViewById(R.id.tv_tital_comment);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Get tracker.
        Tracker t = MyApplication.getInstance().getDefaultTracker();
        // Set screen name.
        t.setScreenName("绑定页");
        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    protected void initListener() {
        super.initListener();

        iv_show_pass.setOnClickListener(this);
        back.setOnClickListener(this);
        tv_gorget_pass.setOnClickListener(this);
        btn_send_demand.setOnClickListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        tital.setText("完善个人资料");
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        String ifLogin = SharedPreferencesUtils.readString("login_in");
        String userid = SharedPreferencesUtils.readString("user_id");
        if (TextUtils.isEmpty(ifLogin)) {
            mFirebaseAnalytics.setUserId(CommontUtils.getPhoneImail(BeforeGLActivity.this));
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
            case R.id.nav_left_imageButton:
                BeforeGLActivity.this.finish();
                break;
            case R.id.iv_show_pass:
                if (isChecked) {
                    //选择状态 显示明文--设置为可见的密码
                    loginPase.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    iv_show_pass.setImageResource(R.drawable.zhengyan);
                    isChecked = false;
                } else {
                    //默认状态显示密码--设置文本 要一起写才能起作用 InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD
                    loginPase.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    iv_show_pass.setImageResource(R.drawable.biyan);
                    isChecked = true;
                }
                break;
            case R.id.tv_gorget_pass:
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("login_action")  //事件类别
                        .setAction("找回密码bind")      //事件操作
                        .build());
                Bundle bundle = new Bundle();
                mFirebaseAnalytics.logEvent("找回密码bind", bundle);
                Intent intent1 = new Intent();
                intent1.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("http://m.idcool.com.cn/account/findpassword?from=client&version=Android");
                intent1.setData(content_url);
                startActivity(intent1);
                break;
            case R.id.btn_send_demand:
                loginButton();
                break;
        }
    }

    private void loginButton() {
        final String name = loginName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            ToastUtils.showCenter(BeforeGLActivity.this, "请输入邮箱/手机/昵称");
            return;
        }
        final String pass = loginPase.getText().toString();
        if (TextUtils.isEmpty(pass)) {
            ToastUtils.showCenter(BeforeGLActivity.this, "请输入密码");
            return;
        }
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String str = jsonObject.getString("info");
                    if (TextUtils.isEmpty(str)) {
                        MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                                .setCategory("login_action")  //事件类别
                                .setAction("绑定新客户成功")      //事件操作
                                .build());
                        Bundle bundle = new Bundle();
                        mFirebaseAnalytics.logEvent("绑定新客户成功", bundle);
                        LoginBean loginBean = GsonUtil.jsonToBean(s, LoginBean.class);
                        SharedPreferencesUtils.writeString("user_id", loginBean.getData().getUser_info().getUser_id());
                        SharedPreferencesUtils.writeString("nickname", loginBean.getData().getUser_info().getNickname());
                        SharedPreferencesUtils.writeString("avatar", loginBean.getData().getUser_info().getAvatar());
                        SharedPreferencesUtils.writeString("ticket", loginBean.getData().getUser_info().getTicket());
                        SharedPreferencesUtils.writeString("mobile", loginBean.getData().getUser_info().getMobile());
                        SharedPreferencesUtils.writeString("email", loginBean.getData().getUser_info().getEmail());
                        SharedPreferencesUtils.writeString("profession", loginBean.getData().getUser_info().getProfession());
                        SharedPreferencesUtils.writeString("project_num", loginBean.getData().getUser_info().getProject_num());
                        SharedPreferencesUtils.writeString("ease_username", loginBean.getData().getUser_info().getEase_username());
                        SharedPreferencesUtils.writeString("ease_password", loginBean.getData().getUser_info().getEase_password());
                        if (TextUtils.isEmpty(loginBean.getData().getUser_info().getProfession())) {
                            Intent intent_result = getIntent();
                            intent_result.putExtra("status", "consumer");
                            SharedPreferencesUtils.writeString("login_in", "consumer");
                            setResult(RESULT_OK, intent_result);
                            BeforeGLActivity.this.finish();
                        } else {
                            Intent intent_result = getIntent();
                            intent_result.putExtra("status", "register");
                            SharedPreferencesUtils.writeString("login_in", "register");
                            setResult(RESULT_OK, intent_result);
                            BeforeGLActivity.this.finish();
                        }
                    } else {
                        ToastUtils.showCenter(BeforeGLActivity.this, str);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    ToastUtils.showCenter(BeforeGLActivity.this, "关联失败，请重新关联");
                }
            }
        };
        MPServerHttpManager.getInstance().bundleGLLogin(name, pass, callBack);
    }

    private EditText loginName;
    private EditText loginPase;
    private ImageView iv_show_pass;
    private ImageButton back;
    private TextView tv_gorget_pass;
    private Button btn_send_demand;
    private boolean isChecked = true;
    private TextView tital;
    private FirebaseAnalytics mFirebaseAnalytics;
}
