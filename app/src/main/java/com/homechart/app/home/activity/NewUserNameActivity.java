package com.homechart.app.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.homechart.app.R;
import com.homechart.app.commont.utils.ChatUtils;
import com.homechart.app.commont.utils.CustomProgress;
import com.homechart.app.commont.utils.GsonUtil;
import com.homechart.app.commont.utils.ImageUtils;
import com.homechart.app.commont.utils.SharedPreferencesUtils;
import com.homechart.app.commont.utils.ToastUtils;
import com.homechart.app.home.bean.LoginBean;
import com.homechart.app.home.ui.ProEditText;
import com.homechart.app.netutils.MPServerHttpManager;
import com.homechart.app.netutils.OkStringRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by gumenghao on 17/5/16.
 */

public class NewUserNameActivity extends BaseActivity
        implements View.OnClickListener,
        ProEditText.RightPicOnclickListener {

    private RelativeLayout mToMast;
    private ImageView mHeader;
    private ImageButton mBack;
    private ProEditText mName;
    private TextView mTital;
    private Button mLogin;

    private String iconurl;
    private String openid;
    private String token;
    private String name;
    private String plat;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_newuser_name;
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();

        openid = getIntent().getStringExtra("openid");
        token = getIntent().getStringExtra("token");
        plat = getIntent().getStringExtra("plat");
        name = getIntent().getStringExtra("username");
        iconurl = getIntent().getStringExtra("iconurl");
    }

    @Override
    protected void initView() {
        mTital = (TextView) findViewById(R.id.tv_tital_comment);
        mHeader = (ImageView) findViewById(R.id.iv_user_header);
        mBack = (ImageButton) findViewById(R.id.nav_left_imageButton);
        mName = (ProEditText) findViewById(R.id.et_login_name);
        mLogin = (Button) findViewById(R.id.btn_login_demand);
        mToMast = (RelativeLayout) findViewById(R.id.rl_jumpto_mast);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mBack.setOnClickListener(this);
        mLogin.setOnClickListener(this);
        mToMast.setOnClickListener(this);
        mName.addTextChangedListener(watcher);
        mName.setRightPicOnclickListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mTital.setText("新账号");
        mName.setText(name);
        ImageUtils.displayRoundImage(iconurl, mHeader);
        ToastUtils.showCenter(this, "昵称已存在");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_left_imageButton:
                NewUserNameActivity.this.finish();
                break;
            case R.id.btn_login_demand:
                platLogin(openid, token, plat, name);
                break;
            case R.id.rl_jumpto_mast:
                Intent intent = new Intent(this, UserMastActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void rightPicClick() {
        name = "";
        mName.setText("");
    }

    private void platLogin
            (final String openid,
             final String token,
             final String plat,
             final String name) {

        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CustomProgress.cancelDialog();
                ToastUtils.showCenter(NewUserNameActivity.this, "请求服务器失败，请重新尝试");
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
                        ToastUtils.showCenter(NewUserNameActivity.this, info);
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(NewUserNameActivity.this, "请求服务器失败，请重新尝试");
                }
            }
        };
        MPServerHttpManager.getInstance().platLogin(openid, token, plat, name, callBack);
    }

    private TextWatcher watcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            name = s.toString();
        }
    };


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

                Intent intent_result = NewUserNameActivity.this.getIntent();
                if (TextUtils.isEmpty(loginBean.getData().getUser_info().getProfession())) {
                    intent_result.putExtra("status", "consumer");
                    SharedPreferencesUtils.writeString("login_in", "consumer");
                } else {
                    intent_result.putExtra("status", "register");
                    SharedPreferencesUtils.writeString("login_in", "register");
                }
                setResult(10, intent_result);
                NewUserNameActivity.this.finish();

            } else {
                ToastUtils.showCenter(NewUserNameActivity.this, info);
            }
        } catch (JSONException e) {
            ToastUtils.showCenter(NewUserNameActivity.this, "登录失败，请重新登录");
        }
    }

}
