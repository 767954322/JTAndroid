package com.homechart.app.home.activity;

import com.homechart.app.commont.constants.KeyConstans;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.widget.RelativeLayout;
import android.webkit.WebViewClient;
import android.webkit.WebSettings;
import android.widget.ImageView;
import android.graphics.Bitmap;
import android.widget.TextView;
import android.webkit.WebView;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.homechart.app.R;

/**
 * @author Allen.Gu .
 * @version v1.0 .
 * @date 2017-2-24.
 * @file AdvertisementActivity.java .
 * @brief 启动动画 .
 */
public class AdvertisementActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_advertisement;
    }

    @Override
    protected void initView() {
        rl_time = (RelativeLayout) findViewById(R.id.rl_time);
        wv_webView = (WebView) findViewById(R.id.wv_webView);
        iv_flush = (ImageView) findViewById(R.id.iv_flush);
        tv_time = (TextView) findViewById(R.id.tv_time);
    }

    @Override
    protected void initListener() {
        super.initListener();
        rl_time.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_time:

                Intent intent = new Intent(AdvertisementActivity.this, ConsumerHomeActivity.class);
                startActivity(intent);
                handler.removeCallbacksAndMessages(null);
                finish();
                break;
        }
    }

    @SuppressLint("JavascriptInterface")
    @Override
    protected void initData(Bundle savedInstanceState) {
        wv_webView.setWebViewClient(new webViewClient());
        WebSettings settings = wv_webView.getSettings();
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptEnabled(true);
        wv_webView.addJavascriptInterface(new AndroidJs(AdvertisementActivity.this), "AndroidJs");
        wv_webView.loadUrl(KeyConstans.ADVERTISEMENT_WEB);
    }

    public class AndroidJs {
        Context mContext;

        AndroidJs(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void jumpDesinerCenter(String desiner_id) {
            Intent intent = new Intent(AdvertisementActivity.this, ConsumerHomeActivity.class);
            intent.putExtra("desiner_id", desiner_id);
            startActivity(intent);
            handler.removeCallbacksAndMessages(null);
            AdvertisementActivity.this.finish();
        }
    }

    class webViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            handler.postDelayed(runnable1, TIME);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            isStop = true;
            handler.sendEmptyMessage(0);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return false;
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            iv_flush.setVisibility(View.GONE);
            handler.postDelayed(runnable, TIME);
        }
    };
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                handler.postDelayed(this, TIME);
                time_has = time_has + 1;
                tv_time.setText("跳过" + (3 - time_has) + "s");
                if (time_has == 3) {//跳转主页
                    Intent intent = new Intent(AdvertisementActivity.this, ConsumerHomeActivity.class);
                    startActivity(intent);
                    handler.removeCallbacksAndMessages(null);
                    finish();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    Runnable runnable1 = new Runnable() {
        @Override
        public void run() {
            try {
                if (!isStop) {
                    handler.postDelayed(this, TIME);
                    time_has = time_has + 1;
                    if (time_has == 3) {
                        Intent intent = new Intent(AdvertisementActivity.this, ConsumerHomeActivity.class);
                        startActivity(intent);
                        handler.removeCallbacksAndMessages(null);
                        finish();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private int TIME = 1000;
    private int time_has = 0;
    public boolean isStop = false;

    private RelativeLayout rl_time;
    private WebView wv_webView;
    private ImageView iv_flush;
    private TextView tv_time;
}
