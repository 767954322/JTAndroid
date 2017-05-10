package com.homechart.app.commont.imagedetail;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.gms.analytics.HitBuilders;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.homechart.app.MyApplication;
import com.homechart.app.R;
import com.homechart.app.commont.constants.ClassConstant;
import com.homechart.app.commont.utils.CommontUtils;
import com.homechart.app.commont.utils.ImageUtils;
import com.homechart.app.commont.utils.SharedPreferencesUtils;
import com.homechart.app.commont.utils.UIUtils;
import com.homechart.app.home.activity.BaseActivity;
import com.homechart.app.home.activity.DesinerInfoActivity;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.utils.Log;

import java.util.ArrayList;
import java.util.List;


public class ImageDetailsActivity extends BaseActivity implements View.OnClickListener {
    private HackyViewPager mViewPager;
    private List<String> imageLists;
    private int intExtra;
    private ArrayList<String> mImageUrl = new ArrayList<>();
    private ImageButton nav_left_imageButton;
    private ImageButton nav_secondary_imageButton;
    private TextView tv_tital_comment;
    private CustomShareListener mShareListener;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_case_detail;
    }

    @Override
    protected void initView() {
        mViewPager = (HackyViewPager) findViewById(R.id.case_librafy_detail_activity_vp);
        nav_left_imageButton = (ImageButton) findViewById(R.id.nav_left_imageButton);
        nav_secondary_imageButton = (ImageButton) findViewById(R.id.nav_secondary_imageButton);
        tv_tital_comment = (TextView) findViewById(R.id.tv_tital_comment);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_left_imageButton:
                finish();
                break;
            case R.id.nav_secondary_imageButton:
                if (imageLists != null && imageLists.size() > intExtra) {
                    MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                            .setCategory("case_action")  //事件类别
                            .setAction("大图分享")      //事件操作
                            .build());
                    Bundle bundle = new Bundle();
                    mFirebaseAnalytics.logEvent("大图分享", bundle);
                    UMImage image = new UMImage(ImageDetailsActivity.this, imageLists.get(intExtra));
                    new ShareAction(ImageDetailsActivity.this).setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ,
                            SHARE_MEDIA.QZONE, SHARE_MEDIA.SINA).withText("分享图片").withMedia(image).setCallback(mShareListener).open();
                }
                break;
        }
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();
        imageLists = (List<String>) getIntent().getSerializableExtra(ClassConstant.HackViewPager.URL);
        intExtra = getIntent().getIntExtra(ClassConstant.HackViewPager.POSITION, 0);//获得点击的位置
    }

    int load_position = 0;

    @Override
    protected void initListener() {
        super.initListener();
        nav_left_imageButton.setOnClickListener(this);
        nav_secondary_imageButton.setOnClickListener(this);
        load_position = intExtra;
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


            }

            @Override
            public void onPageSelected(int position) {
                if (position > load_position) {
                    MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                            .setCategory("phone_action")  //事件类别
                            .setAction("右翻图片")      //事件操作
                            .build());
                    Bundle bundle = new Bundle();
                    mFirebaseAnalytics.logEvent("右翻图片", bundle);
                } else {
                    MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                            .setCategory("phone_action")  //事件类别
                            .setAction("左翻图片")      //事件操作
                            .build());
                    Bundle bundle = new Bundle();
                    mFirebaseAnalytics.logEvent("左翻图片", bundle);
                }
                intExtra = position;
                load_position = position;
                tv_tital_comment.setText((position + 1) + "/" + imageLists.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    protected void initData(Bundle savedInstanceState) {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        String ifLogin = SharedPreferencesUtils.readString("login_in");
        String userid = SharedPreferencesUtils.readString("user_id");
        if (TextUtils.isEmpty(ifLogin)) {
            mFirebaseAnalytics.setUserId(CommontUtils.getPhoneImail(ImageDetailsActivity.this));
        } else {
            if (ifLogin.equals("register")) {
                mFirebaseAnalytics.setUserId(userid + "$设计师");
            } else {
                mFirebaseAnalytics.setUserId(userid);
            }
        }
        Fresco.initialize(this);
        updateViewFromData();
        mShareListener = new CustomShareListener();
        if (imageLists != null) {
            mViewPager.setAdapter(new SamplePagerAdapter(mImageUrl));
            mViewPager.setCurrentItem(intExtra);
            tv_tital_comment.setText((intExtra + 1) + "/" + imageLists.size());
        }
    }

    class CustomShareListener implements UMShareListener {


        private CustomShareListener() {
        }

        @Override
        public void onStart(SHARE_MEDIA platform) {
            Log.d("test", "开始了");
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {

            if (platform.name().equals("WEIXIN_FAVORITE")) {
                Toast.makeText(ImageDetailsActivity.this, platform + " 收藏成功啦", Toast.LENGTH_SHORT).show();
            } else {
                if (platform != SHARE_MEDIA.MORE && platform != SHARE_MEDIA.SMS
                        && platform != SHARE_MEDIA.EMAIL
                        && platform != SHARE_MEDIA.FLICKR
                        && platform != SHARE_MEDIA.FOURSQUARE
                        && platform != SHARE_MEDIA.TUMBLR
                        && platform != SHARE_MEDIA.POCKET
                        && platform != SHARE_MEDIA.PINTEREST

                        && platform != SHARE_MEDIA.INSTAGRAM
                        && platform != SHARE_MEDIA.GOOGLEPLUS
                        && platform != SHARE_MEDIA.YNOTE
                        && platform != SHARE_MEDIA.EVERNOTE) {
                    Toast.makeText(ImageDetailsActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
                }

            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            if (platform != SHARE_MEDIA.MORE && platform != SHARE_MEDIA.SMS
                    && platform != SHARE_MEDIA.EMAIL
                    && platform != SHARE_MEDIA.FLICKR
                    && platform != SHARE_MEDIA.FOURSQUARE
                    && platform != SHARE_MEDIA.TUMBLR
                    && platform != SHARE_MEDIA.POCKET
                    && platform != SHARE_MEDIA.PINTEREST

                    && platform != SHARE_MEDIA.INSTAGRAM
                    && platform != SHARE_MEDIA.GOOGLEPLUS
                    && platform != SHARE_MEDIA.YNOTE
                    && platform != SHARE_MEDIA.EVERNOTE) {
                Toast.makeText(ImageDetailsActivity.this, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
                if (t != null) {
                    Log.d("throw", "throw:" + t.getMessage());
                }
            }

        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {

            Toast.makeText(ImageDetailsActivity.this, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 获取所有图片的url地址
     * <p>
     * TODO  BUG
     */

    private void updateViewFromData() {
        if (imageLists != null) {
            mImageUrl.addAll(imageLists);
        }
    }


    class SamplePagerAdapter extends PagerAdapter {

        private ArrayList<String> mImageUrl1;

        public SamplePagerAdapter(ArrayList<String> mImageUrl1) {
            this.mImageUrl1 = mImageUrl1;
        }

        @Override
        public int getCount() {
            return mImageUrl1.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, final int position) {
            PhotoView photoView = new PhotoView(container.getContext());
//            photoView.setBackgroundColor(UIUtils.getColor(R.color.white));
            ImageUtils.disBigImage_Black(mImageUrl1.get(position), photoView);
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            photoView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
                @Override
                public void onViewTap(View view, float x, float y) {
                    ImageDetailsActivity.this.finish();
                }
            });
            photoView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    return false;
                }
            });
            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }
}
