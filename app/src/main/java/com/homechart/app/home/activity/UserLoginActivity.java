package com.homechart.app.home.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.homechart.app.commont.constants.ClassConstant;
import com.homechart.app.commont.constants.KeyConstans;
import com.homechart.app.commont.constants.UrlConstants;
import com.homechart.app.commont.utils.ChatUtils;
import com.homechart.app.commont.utils.CommontUtils;
import com.homechart.app.commont.utils.CustomProgress;
import com.homechart.app.commont.utils.GsonUtil;
import com.homechart.app.commont.utils.Md5Util;
import com.homechart.app.commont.utils.SharedPreferencesUtils;
import com.homechart.app.commont.utils.ToastUtils;
import com.homechart.app.commont.utils.UIUtils;
import com.homechart.app.home.bean.LoginBean;
import com.homechart.app.netutils.MPServerHttpManager;
import com.homechart.app.netutils.OkStringRequest;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.controller.EaseUI;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.utils.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;


/**
 * Created by Administrator on 2017/3/13/013.
 */

public class UserLoginActivity extends BaseActivity implements View.OnClickListener {
    private EditText loginPase;
    private EditText loginName;
    private ImageView iv_show_pass;
    private boolean isChecked = true;
    private TextView loginQQ;
    private TextView loginWX;
    private TextView loginSina;
    private TextView registerPersion;
    private ImageButton back;
    private TextView tv_gorget_pass;
    private Button btn_send_demand;
    private TextView tv_tital_comment;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_login_in;
    }

    @Override
    protected void initView() {

        tv_tital_comment = (TextView) findViewById(R.id.tv_tital_comment);
        loginQQ = (TextView) findViewById(R.id.tv_login_qq);
        loginWX = (TextView) findViewById(R.id.tv_login_weixin);
        loginSina = (TextView) findViewById(R.id.tv_login_sina);
        registerPersion = (TextView) findViewById(R.id.tv_goto_register);
        loginName = (EditText) findViewById(R.id.et_login_name);
        loginPase = (EditText) findViewById(R.id.et_login_password);
        iv_show_pass = (ImageView) findViewById(R.id.iv_show_pass);
        back = (ImageButton) findViewById(R.id.nav_left_imageButton);
        tv_gorget_pass = (TextView) findViewById(R.id.tv_gorget_pass);
        btn_send_demand = (Button) findViewById(R.id.btn_send_demand);

    }

    @Override
    protected void initListener() {
        super.initListener();
        iv_show_pass.setOnClickListener(this);
        loginQQ.setOnClickListener(this);
        loginWX.setOnClickListener(this);
        loginSina.setOnClickListener(this);
        registerPersion.setOnClickListener(this);
        back.setOnClickListener(this);
        tv_gorget_pass.setOnClickListener(this);
        btn_send_demand.setOnClickListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        changeEditTextHint("邮箱/手机/昵称", loginName, 14);
        changeEditTextHint("请输入密码", loginPase, 14);
        tv_tital_comment.setText("登录");
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        String ifLogin = SharedPreferencesUtils.readString("login_in");
        String userid = SharedPreferencesUtils.readString("user_id");
        if (TextUtils.isEmpty(ifLogin)) {
            mFirebaseAnalytics.setUserId(CommontUtils.getPhoneImail(UserLoginActivity.this));
        } else {
            if (ifLogin.equals("register")) {
                mFirebaseAnalytics.setUserId(userid + "$设计师");
            } else {
                mFirebaseAnalytics.setUserId(userid);
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        // Get tracker.
        Tracker t = MyApplication.getInstance().getDefaultTracker();
        // Set screen name.
        t.setScreenName("登录页");
        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }

    //改变hint大小
    private void changeEditTextHint(String hint, EditText editText, int textSize) {
        // 新建一个可以添加属性的文本对象
        SpannableString ss = new SpannableString(hint);
        // 新建一个属性对象,设置文字的大小
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(textSize, true);
        // 附加属性到文本
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 设置hint
        editText.setHint(new SpannedString(ss)); // 一定要进行转换,否则属性会消失
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
            case R.id.tv_login_qq:
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("login_action")  //事件类别
                        .setAction("QQ")      //事件操作
                        .build());
                Bundle bundle = new Bundle();
                mFirebaseAnalytics.logEvent("登录页面_QQ点击", bundle);
                CustomProgress.show(UserLoginActivity.this, "授权中...", false, null);
                UMShareAPI.get(UserLoginActivity.this).getPlatformInfo(UserLoginActivity.this, SHARE_MEDIA.QQ, umAuthListener);
                break;
            case R.id.tv_login_weixin:
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("login_action")  //事件类别
                        .setAction("WeChat")      //事件操作
                        .build());
                Bundle bundle1 = new Bundle();
                mFirebaseAnalytics.logEvent("登录页面_微信点击", bundle1);
                CustomProgress.show(UserLoginActivity.this, "授权中...", false, null);
                UMShareAPI.get(UserLoginActivity.this).getPlatformInfo(UserLoginActivity.this, SHARE_MEDIA.WEIXIN, umAuthListener);
                break;
            case R.id.tv_login_sina:
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("login_action")  //事件类别
                        .setAction("Sina")      //事件操作
                        .build());
                Bundle bundle2 = new Bundle();
                mFirebaseAnalytics.logEvent("登录页面_Sina点击", bundle2);
                CustomProgress.show(UserLoginActivity.this, "授权中...", false, null);
                UMShareAPI.get(UserLoginActivity.this).getPlatformInfo(UserLoginActivity.this, SHARE_MEDIA.SINA, umAuthListener);
                break;
            case R.id.tv_goto_register:
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("login_action")  //事件类别
                        .setAction("点击注册按钮")      //事件操作
                        .build());

                Bundle bundle3 = new Bundle();
                mFirebaseAnalytics.logEvent("登录页面_注册按钮点击", bundle3);
                Intent intent = new Intent(UserLoginActivity.this, UserRegisterActivity.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.nav_left_imageButton:
                UserLoginActivity.this.finish();
                break;
            case R.id.tv_gorget_pass:
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("login_action")  //事件类别
                        .setAction("找回密码")      //事件操作
                        .build());

                Bundle bundle4 = new Bundle();
                mFirebaseAnalytics.logEvent("登录页面_找回密码", bundle4);
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
            ToastUtils.showCenter(UserLoginActivity.this, "请输入邮箱/手机/昵称");
            return;
        }
        final String pass = loginPase.getText().toString();
        if (TextUtils.isEmpty(pass)) {
            ToastUtils.showCenter(UserLoginActivity.this, "请输入密码");
            return;
        }

        MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                .setCategory("login_action")  //事件类别
                .setAction("点击登录按钮")      //事件操作
                .build());

        Bundle bundle3 = new Bundle();
        mFirebaseAnalytics.logEvent("登录页面_登录按钮点击", bundle3);
        String newPass = Md5Util.getMD5(pass);
        newPass = newPass.subSequence(0, 30).toString();
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }

            @Override
            public void onResponse(String s) {
                loginPersion(s);
            }
        };
        MPServerHttpManager.getInstance().userLogin(name, newPass, callBack);
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
            String plat = "";
            switch (platform.toString()) {
                case "SINA":
                    plat = "weibo";
                    MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                            .setCategory("login_action")  //事件类别
                            .setAction("登录界面_微博登陆成功")      //事件操作
                            .build());
                    Bundle bundle1 = new Bundle();
                    mFirebaseAnalytics.logEvent("登录界面_微博登陆成功", bundle1);
                    break;
                case "QQ":
                    MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                            .setCategory("login_action")  //事件类别
                            .setAction("登录界面_QQ登陆成功")      //事件操作
                            .build());
                    Bundle bundle = new Bundle();
                    mFirebaseAnalytics.logEvent("登录界面_QQ登陆成功", bundle);
                    plat = "qq";
                    break;
                case "WEIXIN":
                    MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                            .setCategory("login_action")  //事件类别
                            .setAction("登录界面_微信登陆成功")      //事件操作
                            .build());
                    Bundle bundle2 = new Bundle();
                    mFirebaseAnalytics.logEvent("登录界面_微信登陆成功", bundle2);
                    plat = "weixin";
                    break;
            }
            platLogin(openid, token, plat, name);
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            CustomProgress.cancelDialog();
            ToastUtils.showCenter(UserLoginActivity.this, "授权失败，请重新尝试");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            CustomProgress.cancelDialog();
            ToastUtils.showCenter(UserLoginActivity.this, "授权取消");
        }
    };

    private void platLogin(final String openid, final String token, final String plat, final String name) {
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CustomProgress.cancelDialog();
                ToastUtils.showCenter(UserLoginActivity.this, "请求服务器失败，请重新尝试");
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
                        bundlePersion(name);
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(UserLoginActivity.this, "请求服务器失败，请重新尝试");
                }
            }
        };
        MPServerHttpManager.getInstance().platLogin(openid, token, plat, callBack);
    }

    //未绑定跳转绑定界面
    private void bundlePersion(String name) {
        Intent intent = new Intent(UserLoginActivity.this, UserBundleActivity.class);
        intent.putExtra("username", name);
        startActivityForResult(intent, 0);
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
                    Intent intent_result = UserLoginActivity.this.getIntent();
                    intent_result.putExtra("status", "consumer");
                    SharedPreferencesUtils.writeString("login_in", "consumer");
                    setResult(RESULT_OK, intent_result);
                    UserLoginActivity.this.finish();
                } else {
                    Intent intent_result = getIntent();
                    intent_result.putExtra("status", "register");
                    SharedPreferencesUtils.writeString("login_in", "register");
                    setResult(RESULT_OK, intent_result);
                    UserLoginActivity.this.finish();
                }
            } else {
                ToastUtils.showCenter(UserLoginActivity.this, info);
            }
        } catch (JSONException e) {
            ToastUtils.showCenter(UserLoginActivity.this, "登录失败，请重新登录");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 10:
                if (data != null) {
                    String status = data.getStringExtra("status");
                    Intent intent_result = getIntent();
                    intent_result.putExtra("status", status);
                    setResult(RESULT_OK, intent_result);
                    this.finish();
                }
                break;
            default:
                break;
        }
    }
}
