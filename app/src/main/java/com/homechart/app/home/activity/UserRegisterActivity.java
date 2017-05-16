package com.homechart.app.home.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.baidu.mapapi.SDKInitializer;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.homechart.app.MyApplication;
import com.homechart.app.R;
import com.homechart.app.commont.CountDownTimer;
import com.homechart.app.commont.constants.ClassConstant;
import com.homechart.app.commont.constants.KeyConstans;
import com.homechart.app.commont.constants.UrlConstants;
import com.homechart.app.commont.utils.ChatUtils;
import com.homechart.app.commont.utils.CustomProgress;
import com.homechart.app.geetest.GeetestTest;
import com.homechart.app.netutils.MPServerHttpManager;
import com.homechart.app.netutils.OkStringRequest;
import com.homechart.app.commont.utils.CommontUtils;
import com.homechart.app.commont.utils.GsonUtil;
import com.homechart.app.commont.utils.Md5Util;
import com.homechart.app.commont.utils.RegexUtil;
import com.homechart.app.commont.utils.SharedPreferencesUtils;
import com.homechart.app.commont.utils.ToastUtils;
import com.homechart.app.commont.utils.UIUtils;
import com.homechart.app.geetest.GtDialog;
import com.homechart.app.home.bean.JiYanBean;
import com.homechart.app.home.bean.LoginBean;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.controller.EaseUI;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/14/014.
 */

public class UserRegisterActivity extends BaseActivity implements View.OnClickListener, GeetestTest.CallBack {
    private ImageButton back;
    private TextView tital;
    private TextView loginQQ;
    private TextView loginWX;
    private TextView loginSina;
    private ImageView iv_show_pass;
    private boolean isChecked = true;
    private TextView tv_get_yanzhengma;
    private Context context = UserRegisterActivity.this;
    private Button registerButton;
    private EditText etPhone;
    private EditText etYanZheng;
    private EditText etNikeName;
    private EditText etPassWord;
    private RelativeLayout rl_jumpto_mast;

    CountDownTimer timer = new CountDownTimer(60000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            tv_get_yanzhengma.setText(millisUntilFinished / 1000 + "秒");
        }

        @Override
        public void onFinish() {
            tv_get_yanzhengma.setEnabled(true);
            tv_get_yanzhengma.setText("获取验证码");
        }
    };
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_register_ui;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Get tracker.
        Tracker t = MyApplication.getInstance().getDefaultTracker();
        // Set screen name.
        t.setScreenName("注册页");
        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    protected void initView() {
        rl_jumpto_mast = (RelativeLayout) findViewById(R.id.rl_jumpto_mast);
        back = (ImageButton) findViewById(R.id.nav_left_imageButton);
        tital = (TextView) findViewById(R.id.tv_tital_comment);
        loginQQ = (TextView) findViewById(R.id.tv_login_qq);
        loginWX = (TextView) findViewById(R.id.tv_login_weixin);
        loginSina = (TextView) findViewById(R.id.tv_login_sina);
        tv_get_yanzhengma = (TextView) findViewById(R.id.tv_get_yanzhengma);
        iv_show_pass = (ImageView) findViewById(R.id.iv_show_pass);
        registerButton = (Button) findViewById(R.id.btn_regiter_demand);
        etPhone = (EditText) findViewById(R.id.et_regiter_phone);
        etYanZheng = (EditText) findViewById(R.id.et_regiter_yanzhengma);
        etNikeName = (EditText) findViewById(R.id.et_regiter_name);
        etPassWord = (EditText) findViewById(R.id.et_register_password);
    }

    @Override
    protected void initListener() {
        super.initListener();
        back.setOnClickListener(this);
        loginQQ.setOnClickListener(this);
        loginWX.setOnClickListener(this);
        loginSina.setOnClickListener(this);
        iv_show_pass.setOnClickListener(this);
        tv_get_yanzhengma.setOnClickListener(this);
        registerButton.setOnClickListener(this);
        rl_jumpto_mast.setOnClickListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        tital.setText("注册");
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        String ifLogin = SharedPreferencesUtils.readString("login_in");
        String userid = SharedPreferencesUtils.readString("user_id");
        if (TextUtils.isEmpty(ifLogin)) {
            mFirebaseAnalytics.setUserId(CommontUtils.getPhoneImail(UserRegisterActivity.this));
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
            case R.id.iv_show_pass:
                if (isChecked) {
                    etPassWord.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    iv_show_pass.setImageResource(R.drawable.zhengyan);
                    isChecked = false;
                } else {
                    etPassWord.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    iv_show_pass.setImageResource(R.drawable.biyan);
                    isChecked = true;
                }
                break;
            case R.id.nav_left_imageButton:
                UserRegisterActivity.this.finish();
                break;
            case R.id.tv_login_qq:
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("register_action")  //事件类别
                        .setAction("QQ")      //事件操作
                        .build());
                Bundle bundle = new Bundle();
                mFirebaseAnalytics.logEvent("注册页面_QQ点击", bundle);
                CustomProgress.show(UserRegisterActivity.this, "授权中...", false, null);
                UMShareAPI.get(UserRegisterActivity.this).getPlatformInfo(UserRegisterActivity.this, SHARE_MEDIA.QQ, umAuthListener);
                break;
            case R.id.tv_login_weixin:
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("register_action")  //事件类别
                        .setAction("WeChat")      //事件操作
                        .build());
                Bundle bundle2 = new Bundle();
                mFirebaseAnalytics.logEvent("注册页面_点击WeChat", bundle2);
                CustomProgress.show(UserRegisterActivity.this, "授权中...", false, null);
                UMShareAPI.get(UserRegisterActivity.this).getPlatformInfo(UserRegisterActivity.this, SHARE_MEDIA.WEIXIN, umAuthListener);
                break;
            case R.id.tv_login_sina:
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("register_action")  //事件类别
                        .setAction("Sina")      //事件操作
                        .build());
                Bundle bundle1 = new Bundle();
                mFirebaseAnalytics.logEvent("注册页面_点击sina", bundle1);
                CustomProgress.show(UserRegisterActivity.this, "授权中...", false, null);
                UMShareAPI.get(UserRegisterActivity.this).getPlatformInfo(UserRegisterActivity.this, SHARE_MEDIA.SINA, umAuthListener);
                break;
            case R.id.tv_get_yanzhengma:
                CustomProgress.show(UserRegisterActivity.this, "加载中...", false, null);
                getJYNeedParams();
                break;
            case R.id.btn_regiter_demand:
                checkCode();
                break;
            case R.id.rl_jumpto_mast:
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("register_action")  //事件类别
                        .setAction("查看协议")      //事件操作
                        .build());
                Bundle bundle3 = new Bundle();
                mFirebaseAnalytics.logEvent("查看协议", bundle3);
                Intent intent = new Intent(this, UserMastActivity.class);
                startActivity(intent);
                break;
        }
    }

    //检验验证码
    private void checkCode() {
        String phone = etPhone.getText().toString();
        if (TextUtils.isEmpty(phone) || !phone.matches(RegexUtil.PHONE_REGEX)) {
            ToastUtils.showCenter(UserRegisterActivity.this, UIUtils.getString(R.string.phonenum_error));
            return;
        }
        final String yanzhengma = etYanZheng.getText().toString();
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }

            @Override
            public void onResponse(String s) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(s);
                    int status = jsonObject.getInt("status");
                    if (status == 0) {
                        ToastUtils.showCenter(UserRegisterActivity.this, UIUtils.getString(R.string.yanzhengma_error));
                    } else {
                        registerPerson();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        MPServerHttpManager.getInstance().checkYanZhengMa(yanzhengma, "Cookie_rungl", callBack);
    }

    //注册
    private void registerPerson() {
        final String phone = etPhone.getText().toString();
        final String yanzheng = etYanZheng.getText().toString();
        final String nikeName = etNikeName.getText().toString();
        boolean nikename_right = nikeName.matches(RegexUtil.ADDRESS_REGEX);
        if (TextUtils.isEmpty(nikeName) || !nikename_right) {
            ToastUtils.showCenter(UserRegisterActivity.this, UIUtils.getString(R.string.nikename_error));
            return;
        }
        final String passWord = etPassWord.getText().toString();
        if (TextUtils.isEmpty(passWord)) {
            ToastUtils.showCenter(UserRegisterActivity.this, UIUtils.getString(R.string.password_error));
            return;
        }
        MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                .setCategory("register_action")  //事件类别
                .setAction("注册页面_点击注册按钮")      //事件操作
                .build());
        Bundle bundle = new Bundle();
        mFirebaseAnalytics.logEvent("注册页面_点击注册按钮", bundle);
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(UserRegisterActivity.this, "注册失败，请重新注册！");
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int status = jsonObject.getInt("status");
                    String info = jsonObject.getString("info");
                    if (status == 1) {
                        loginPersion(s);
                    } else {
                        ToastUtils.showCenter(UserRegisterActivity.this, info);
                    }
                } catch (JSONException e) {
                }
            }
        };
        MPServerHttpManager.getInstance().registerPersion(phone, yanzheng, nikeName, passWord, "Cookie_register", callBack);
    }

    //已绑定，登录
    private void loginPersion(String strJson) {
        CustomProgress.cancelDialog();
        try {
            JSONObject jsonObject = new JSONObject(strJson);
            String info = jsonObject.getString("info");
            if (TextUtils.isEmpty(info)) {
                LoginBean loginBean = GsonUtil.jsonToBean(strJson, LoginBean.class);
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
                ChatUtils.LoginEaseUI();
                if (TextUtils.isEmpty(loginBean.getData().getUser_info().getProfession())) {
                    Intent intent_result = getIntent();
                    intent_result.putExtra("status", "consumer");
                    SharedPreferencesUtils.writeString("login_in", "consumer");
                    setResult(10, intent_result);
                    UserRegisterActivity.this.finish();
                } else {
                    Intent intent_result = getIntent();
                    intent_result.putExtra("status", "register");
                    SharedPreferencesUtils.writeString("login_in", "register");
                    setResult(RESULT_OK, intent_result);
                    UserRegisterActivity.this.finish();
                }
            } else {
                ToastUtils.showCenter(UserRegisterActivity.this, info);
            }
        } catch (JSONException e) {
            ToastUtils.showCenter(UserRegisterActivity.this, "登录失败，请重新登录");
        }
    }

    private void getJYNeedParams() {
        String phone = etPhone.getText().toString();
        if (TextUtils.isEmpty(phone) || !phone.matches(RegexUtil.PHONE_REGEX)) {
            CustomProgress.cancelDialog();
            ToastUtils.showCenter(UserRegisterActivity.this, UIUtils.getString(R.string.phonenum_error));
            return;
        }
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CustomProgress.cancelDialog();
                ToastUtils.showCenter(UserRegisterActivity.this, "信息加载失败，请重新加载");
            }

            @Override
            public void onResponse(String s) {
                JiYanBean bean = GsonUtil.jsonToBean(s, JiYanBean.class);
                try {
                    JSONObject parmas = new JSONObject();
                    parmas.put("gt", bean.getData().getGt());
                    parmas.put("success", bean.getData().getSuccess());
                    parmas.put("challenge", bean.getData().getChallenge());
//                    openGtTest(context, parmas);
                    GeetestTest.openGtTest(context, parmas, UserRegisterActivity.this);
                } catch (JSONException e) {
                    e.printStackTrace();
                    CustomProgress.cancelDialog();
                }
            }
        };
        MPServerHttpManager.getInstance().getParamsFromMyServiceJY(callBack);
    }

    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //授权开始的回调
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            String openid = data.get("uid");
            String token = data.get("access_token");
            String name = data.get("name");
            String iconurl = data.get("iconurl");
            String plat = "";
            switch (platform.toString()) {
                case "SINA":
                    plat = "sina";
                    MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                            .setCategory("register_action")  //事件类别
                            .setAction("注册页面_微博登录成功")      //事件操作
                            .build());
                    Bundle bundle = new Bundle();
                    mFirebaseAnalytics.logEvent("注册页面_微博登录成功", bundle);
                    break;
                case "QQ":
                    plat = "qq";
                    MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                            .setCategory("register_action")  //事件类别
                            .setAction("注册页面_QQ")      //事件操作
                            .build());
                    Bundle bundle1 = new Bundle();
                    mFirebaseAnalytics.logEvent("注册页面_QQ登陆成功", bundle1);
                    break;
                case "WEIXIN":
                    plat = "weixin";
                    MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                            .setCategory("register_action")  //事件类别
                            .setAction("注册页面_WeChat")      //事件操作
                            .build());
                    Bundle bundle2 = new Bundle();
                    mFirebaseAnalytics.logEvent("注册页面_WeChat登陆成功", bundle2);
                    break;
            }
            platLogin(openid, token, plat, name, iconurl);
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            ToastUtils.showCenter(UserRegisterActivity.this, "授权失败，请重新尝试");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            ToastUtils.showCenter(UserRegisterActivity.this, "授权取消");
        }
    };

    private void platLogin(final String openid, final String token, final String plat, final String name, final String iconurl) {
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CustomProgress.cancelDialog();
                ToastUtils.showCenter(UserRegisterActivity.this, "请求服务器失败，请重新尝试");
            }

            @Override
            public void onResponse(String s) {
                CustomProgress.cancelDialog();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String info = jsonObject.getString("info");
                    if (TextUtils.isEmpty(info)) {
                        loginPersion(s);
                    } else {
                        bundlePersion(name, iconurl);
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(UserRegisterActivity.this, "请求服务器失败，请重新尝试");
                }
            }
        };
        MPServerHttpManager.getInstance().platLogin(openid, token, plat,name , callBack);
    }

    //级验滑动后，先检验是否可以发送短信
    private void checkPhoneNumStatus(final String challenge, final String validate, final String seccode) {
        String phonenum = etPhone.getText().toString().trim();
        //判断手机号码是否还可以验证
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(UserRegisterActivity.this, "手机号码验证失败，请重新验证");
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int status = jsonObject.getInt("status");
                    String info = jsonObject.getString("info");
                    if (status == 1) {
                        sendMessage(challenge, validate, seccode);
                    } else {
                        ToastUtils.showCenter(UserRegisterActivity.this, info);
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(UserRegisterActivity.this, "手机号码验证失败，请重新验证");
                }
            }
        };
        MPServerHttpManager.getInstance().checkPhoneNumStatus(phonenum, "Cookie_register", callBack);
    }

    //发送短信
    private void sendMessage(final String challenge, final String validate, final String seccode) {
        final String phone = etPhone.getText().toString().trim();
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(UserRegisterActivity.this, "发送失败");
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int status = jsonObject.getInt("status");
                    String info = jsonObject.getString("info");
                    if (status == 1) {
                        tv_get_yanzhengma.setEnabled(false);
                        timer.start();
                        ToastUtils.showCenter(UserRegisterActivity.this, "发送成功");
                    } else {
                        ToastUtils.showCenter(UserRegisterActivity.this, info);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    ToastUtils.showCenter(UserRegisterActivity.this, "发送失败");
                }
            }
        };
        MPServerHttpManager.getInstance().sendMessageByJY(phone, challenge, validate, seccode, "Cookie_register", callBack);
    }

    //未绑定跳转绑定界面
    private void bundlePersion(String name, String iconurl) {
        Intent intent = new Intent(UserRegisterActivity.this, NewUserNameActivity.class);
        intent.putExtra("username", name);
        intent.putExtra("iconurl", iconurl);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 10:
                if (data != null) {
                    CustomProgress.cancelDialog();
                    String status = data.getStringExtra("status");
                    Intent intent_result = getIntent();
                    intent_result.putExtra("status", status);
                    setResult(10, intent_result);
                    UserRegisterActivity.this.finish();
                }
                break;
            default:
                break;
        }
    }

    //打开极验获取到返回的数据
    @Override
    public void geetestCallBack(String challenge, String validate, String seccode) {
        checkPhoneNumStatus(challenge, validate, seccode);
    }
}
