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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/17/017.
 */

public class RunGLActivity extends BaseActivity implements View.OnClickListener, GeetestTest.CallBack {
    private ImageButton back;
    private TextView tital;
    private EditText etPhone;
    private EditText etYanZheng;
    private EditText etNikeName;
    private EditText etPassWord;
    private ImageView iv_show_pass;
    private Button registerButton;
    private TextView tv_get_yanzhengma;
    private boolean isChecked = true;
    private Context context = RunGLActivity.this;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_register_kuaishu;
    }

    @Override
    protected void initView() {
        back = (ImageButton) findViewById(R.id.nav_left_imageButton);
        tital = (TextView) findViewById(R.id.tv_tital_comment);
        iv_show_pass = (ImageView) findViewById(R.id.iv_show_pass);
        registerButton = (Button) findViewById(R.id.btn_regiter_demand);
        etPhone = (EditText) findViewById(R.id.et_regiter_phone);
        etYanZheng = (EditText) findViewById(R.id.et_regiter_yanzhengma);
        etNikeName = (EditText) findViewById(R.id.et_regiter_name);
        etPassWord = (EditText) findViewById(R.id.et_register_password);
        tv_get_yanzhengma = (TextView) findViewById(R.id.tv_get_yanzhengma);
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
        back.setOnClickListener(this);
        iv_show_pass.setOnClickListener(this);
        tv_get_yanzhengma.setOnClickListener(this);
        registerButton.setOnClickListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        tital.setText("完善个人资料");
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        String ifLogin = SharedPreferencesUtils.readString("login_in");
        String userid = SharedPreferencesUtils.readString("user_id");
        if (TextUtils.isEmpty(ifLogin)) {
            mFirebaseAnalytics.setUserId(CommontUtils.getPhoneImail(RunGLActivity.this));
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
                RunGLActivity.this.finish();
                break;
            case R.id.tv_get_yanzhengma:
                CustomProgress.show(RunGLActivity.this, "加载中...", false, null);
                getJYNeedParams();
                break;
            case R.id.btn_regiter_demand:
                checkCode();
                break;
        }
    }

    //检验验证码
    private void checkCode() {
        String phone = etPhone.getText().toString();
        if (TextUtils.isEmpty(phone) || !phone.matches(RegexUtil.PHONE_REGEX)) {
            ToastUtils.showCenter(RunGLActivity.this, UIUtils.getString(R.string.phonenum_error));
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
                        ToastUtils.showCenter(RunGLActivity.this, UIUtils.getString(R.string.yanzhengma_error));
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
            ToastUtils.showCenter(RunGLActivity.this, UIUtils.getString(R.string.nikename_error));
            return;
        }
        final String passWord = etPassWord.getText().toString();
        if (TextUtils.isEmpty(passWord)) {
            ToastUtils.showCenter(RunGLActivity.this, UIUtils.getString(R.string.password_error));
            return;
        }
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(RunGLActivity.this, "注册失败，请重新注册！");
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
                        ToastUtils.showCenter(RunGLActivity.this, info);
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(RunGLActivity.this, "注册失败");
                }
            }
        };
        MPServerHttpManager.getInstance().registerPersionShared(phone, yanzheng, nikeName, passWord, "Cookie_rungl", callBack);
    }

    //从本地服务器获取级验需要的参数
    private void getJYNeedParams() {
        String phone = etPhone.getText().toString();
        if (TextUtils.isEmpty(phone) || !phone.matches(RegexUtil.PHONE_REGEX)) {
            CustomProgress.cancelDialog();
            ToastUtils.showCenter(RunGLActivity.this, UIUtils.getString(R.string.phonenum_error));
            return;
        }
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CustomProgress.cancelDialog();
                ToastUtils.showCenter(RunGLActivity.this, "信息加载失败，请重新加载");
            }

            @Override
            public void onResponse(String s) {
                JiYanBean bean = GsonUtil.jsonToBean(s, JiYanBean.class);
                try {
                    JSONObject parmas = new JSONObject();
                    parmas.put("gt", bean.getData().getGt());
                    parmas.put("success", bean.getData().getSuccess());
                    parmas.put("challenge", bean.getData().getChallenge());
                    CustomProgress.cancelDialog();
                    GeetestTest.openGtTest(context, parmas, RunGLActivity.this);
                } catch (JSONException e) {
                    e.printStackTrace();
                    CustomProgress.cancelDialog();
                }
            }
        };
        MPServerHttpManager.getInstance().getParamsFromMyServiceJY(callBack);
    }

    //级验滑动后，先检验是否可以发送短信
    private void checkPhoneNumStatus(final String challenge, final String validate, final String seccode) {
        String phonenum = etPhone.getText().toString().trim();
        //判断手机号码是否还可以验证
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CustomProgress.cancelDialog();
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
                        ToastUtils.showCenter(RunGLActivity.this, info);
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(RunGLActivity.this, "手机号码验证失败，请重新验证");
                }
            }
        };
        MPServerHttpManager.getInstance().checkPhoneNumStatus(phonenum, "Cookie_rungl", callBack);
    }

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

    //发送短信
    private void sendMessage(final String challenge, final String validate, final String seccode) {
        final String phone = etPhone.getText().toString().trim();
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(RunGLActivity.this, "发送失败");
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
                        ToastUtils.showCenter(RunGLActivity.this, "发送成功");
                    } else {
                        ToastUtils.showCenter(RunGLActivity.this, info);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    ToastUtils.showCenter(RunGLActivity.this, "发送失败");
                }
            }
        };
        MPServerHttpManager.getInstance().sendMessageByJY(phone, challenge, validate, seccode, "Cookie_rungl", callBack);
    }

    //已绑定，登录
    private void loginPersion(String strJson) {
        try {
            JSONObject jsonObject = new JSONObject(strJson);
            String info = jsonObject.getString("info");
            if (TextUtils.isEmpty(info)) {
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("login_action")  //事件类别
                        .setAction("绑定老客户")      //事件操作
                        .build());
                Bundle bundle = new Bundle();
                mFirebaseAnalytics.logEvent("绑定老客户", bundle);
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
                if (TextUtils.isEmpty(loginBean.getData().getUser_info().getProfession())) {
                    Intent intent_result = getIntent();
                    intent_result.putExtra("status", "consumer");
                    SharedPreferencesUtils.writeString("login_in", "consumer");
                    setResult(RESULT_OK, intent_result);
                    RunGLActivity.this.finish();
                } else {
                    Intent intent_result = getIntent();
                    intent_result.putExtra("status", "register");
                    SharedPreferencesUtils.writeString("login_in", "register");
                    setResult(RESULT_OK, intent_result);
                    RunGLActivity.this.finish();
                }
            } else {
                ToastUtils.showCenter(RunGLActivity.this, info);
            }
        } catch (JSONException e) {
            ToastUtils.showCenter(RunGLActivity.this, "登录失败，请重新登录");
        }
    }

    @Override
    public void geetestCallBack(String challenge, String validate, String seccode) {
        checkPhoneNumStatus(challenge, validate, seccode);
    }
}
