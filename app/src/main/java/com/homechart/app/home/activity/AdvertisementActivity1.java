package com.homechart.app.home.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.homechart.app.R;
import com.homechart.app.commont.constants.KeyConstans;
import com.homechart.app.commont.utils.CommontUtils;

/**
 * @author Allen.Gu .
 * @version v1.0 .
 * @date 2017-2-24.
 * @file AdvertisementActivity.java .
 * @brief 启动动画 .
 */
public class AdvertisementActivity1 extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rl_time;
    private ImageView iv_flush;
    private WebView wv_webView;
    private int show_time = 3;
    private TextView tv_time;
    private boolean loadweb_success = true;
    private Handler handler = new Handler() {
    };

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
                Intent intent = new Intent(AdvertisementActivity1.this, ConsumerHomeActivity.class);
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
        wv_webView.addJavascriptInterface(new AndroidJs(AdvertisementActivity1.this), "AndroidJs");
        wv_webView.loadUrl(KeyConstans.ADVERTISEMENT_WEB);
        handler.postDelayed(runnable, 1500);
    }

    class webViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return false;
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            loadweb_success = false;
        }
    }

    public class AndroidJs {
        Context mContext;

        AndroidJs(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void jumpDesinerCenter(String desiner_id) {
            Intent intent = new Intent(AdvertisementActivity1.this, ConsumerHomeActivity.class);
            intent.putExtra("desiner_id", desiner_id);
            startActivity(intent);
            handler.removeCallbacksAndMessages(null);
            AdvertisementActivity1.this.finish();
        }
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (CommontUtils.isNetworkConnected(AdvertisementActivity1.this) && loadweb_success) {
                wv_webView.setVisibility(View.VISIBLE);
                iv_flush.setVisibility(View.GONE);
                handler.postDelayed(runnable_timeui, 1000);
            } else {
                Intent intent = new Intent(AdvertisementActivity1.this, ConsumerHomeActivity.class);
                startActivity(intent);
                handler.removeCallbacksAndMessages(null);
                finish();
            }
        }
    };

    Runnable runnable_timeui = new Runnable() {
        @Override
        public void run() {
            --show_time;
            if (show_time == -1) {
                Intent intent = new Intent(AdvertisementActivity1.this, ConsumerHomeActivity.class);
                startActivity(intent);
                handler.removeCallbacksAndMessages(null);
                finish();
            } else {
                handler.postDelayed(runnable_timeui, 1000);
                tv_time.setText("跳过" + show_time + "s");
            }
        }
    };
}
