package com.homechart.app.commont.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.homechart.app.home.activity.CaseLibraryDetailsActivity;
import com.homechart.app.home.bean.MyTabBean;
import com.homechart.app.home.bean.TabAllBean;
import com.homechart.app.home.bean.TabOneChild;
import com.homechart.app.home.bean.TabSpaceBean;
import com.homechart.app.home.bean.TabStylesBean;
import com.homechart.app.home.bean.TabTwoChild;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by GPD on 2017/3/1.
 */

public class CommontUtils {
    /**
     * 获取版本号
     *
     * @return
     */
    public static String getVersionName(Context context) {
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        try {
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            String version = packInfo.versionName;
            return version;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * @param context
     * @return 唯一标识
     */
    public static String getPhoneImail(Context context) {
        String phone_id = "";
        try {
            phone_id = ((TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE)).getDeviceId();
        } catch (Exception e) {
            phone_id = getPesudoUniqueID();
        }
        return phone_id;
    }

    public static String getPesudoUniqueID() {
        String m_szDevIDShort = "35" + //we make this look like a valid IMEI
                Build.BOARD.length() % 10 +
                Build.BRAND.length() % 10 +
                Build.CPU_ABI.length() % 10 +
                Build.DEVICE.length() % 10 +
                Build.DISPLAY.length() % 10 +
                Build.HOST.length() % 10 +
                Build.ID.length() % 10 +
                Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 +
                Build.PRODUCT.length() % 10 +
                Build.TAGS.length() % 10 +
                Build.TYPE.length() % 10 +
                Build.USER.length() % 10; //13 digits
        return m_szDevIDShort;
    }

    /**
     * @return 获取公共的JSONObject
     */
    public static JSONObject getPublicObject(Context context) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(COMMENT_PARAMS_V, getVersionName(context));
            jsonObject.put(COMMENT_PARAMS_VID, getPhoneImail(context));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static Map<String, String> getPublicMap(Context context) {
        Map<String, String> map = new HashMap<>();
        map.put(COMMENT_PARAMS_V, getVersionName(context));
        map.put(COMMENT_PARAMS_VID, getPhoneImail(context));
        return map;
    }

    public static final String COMMENT_PARAMS_V = "v";
    public static final String COMMENT_PARAMS_VID = "vid";


    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public static void verifyStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }

    public static List<List<MyTabBean>> getHomeTabDate(int spacePositon, TabAllBean tabAllBean) {

        List<List<MyTabBean>> list = getTopTwoList(tabAllBean);

        if (spacePositon == -1) {
            return list;
        } else {
            List<TabOneChild> list_space = tabAllBean.getData().getSpaces().get(spacePositon).getChild();
            if (list_space.size() > 0) {
                for (int i = 0; i < list_space.size(); i++) {
                    List<MyTabBean> list_space1 = new ArrayList<>();
                    List<TabTwoChild> list_two = list_space.get(i).getChild();
                    for (int j = 0; j < list_two.size(); j++) {
                        MyTabBean myTabBean = new MyTabBean(list_two.get(j).getSpace_tag_id(),
                                list_two.get(j).getSpace_tag_name(),
                                list_two.get(j).getSpace_tag_image_url(),
                                list_two.get(j).getSpace_tag_image_height(),
                                list_two.get(j).getSpace_tag_image_width(),
                                list_two.get(j).getSpace_tag_enable());
                        list_space1.add(myTabBean);
                    }
                    list.add(list_space1);
                }
            }
        }
        return list;
    }

    public static List<List<MyTabBean>> getHomeTabDate1(int spacePositon, TabAllBean tabAllBean) {

        List<List<MyTabBean>> list = getTopTwoList1(tabAllBean);


        return list;
    }


    public static List<List<MyTabBean>> getTopTwoList(TabAllBean tabAllBean) {
        List<List<MyTabBean>> list = new ArrayList<>();

        List<MyTabBean> list_style = new ArrayList<>();
        List<TabStylesBean> list1_style1 = tabAllBean.getData().getStyles();
        for (int i = 0; i < list1_style1.size(); i++) {
            MyTabBean myTabBean = new MyTabBean(list1_style1.get(i).getStyle_tag_id(),
                    list1_style1.get(i).getStyle_tag_name(),
                    list1_style1.get(i).getStyle_tag_image_url(),
                    list1_style1.get(i).getStyle_tag_image_height(),
                    list1_style1.get(i).getStyle_tag_image_width(),
                    1);
            list_style.add(myTabBean);
        }

        List<MyTabBean> list_space = new ArrayList<>();
        List<TabSpaceBean> list1_space1 = tabAllBean.getData().getSpaces();
        for (int i = 0; i < list1_space1.size(); i++) {
            MyTabBean myTabBean = new MyTabBean(list1_space1.get(i).getSpace_tag_id(),
                    list1_space1.get(i).getSpace_tag_name(),
                    list1_space1.get(i).getSpace_tag_image_url(),
                    list1_space1.get(i).getSpace_tag_image_height(),
                    list1_space1.get(i).getSpace_tag_image_width(),
                    1);
            list_space.add(myTabBean);
        }

        list.add(list_style);
        list.add(list_space);
        return list;
    }

    public static List<List<MyTabBean>> getTopTwoList1(TabAllBean tabAllBean) {
        List<List<MyTabBean>> list = new ArrayList<>();

        List<MyTabBean> list_space = new ArrayList<>();
        List<TabOneChild> list1 = tabAllBean.getData().getSpaces().get(0).getChild();
        for (int i = 0; i < list1.size(); i++) {
            List<MyTabBean> list_bean = new ArrayList<>();
            List<TabTwoChild> list2 = list1.get(i).getChild();
            for (int j = 0; j < list2.size(); j++) {
                MyTabBean myTabBean = new MyTabBean(list2.get(j).getSpace_tag_id(),
                        list2.get(j).getSpace_tag_name(),
                        list2.get(j).getSpace_tag_image_url(),
                        list2.get(j).getSpace_tag_image_height(),
                        list2.get(j).getSpace_tag_image_width(),
                        list2.get(j).getSpace_tag_enable());
                list_bean.add(myTabBean);
            }
            list.add(list_bean);
        }

        return list;
    }

    public static List<String> getTabName(TabAllBean tabAllBean, int pagePosition, int itemPosition) {
        List<String> strings = new ArrayList<>();
        String name;
        if (pagePosition == 0) {
            name = tabAllBean.getData().getStyles().get(itemPosition).getStyle_tag_name();
            strings.add(name);
            return strings;
        } else if (pagePosition == 1) {
            name = tabAllBean.getData().getSpaces().get(itemPosition).getSpace_tag_name();
            strings.add(name);
            for (TabOneChild tabOneChild : tabAllBean.getData().getSpaces().get(itemPosition).getChild()) {
                strings.add(tabOneChild.getSpace_tag_name());
            }
            return strings;
        } else {
            name = tabAllBean.getData().getSpaces().
                    get(pagePosition - 2).getChild().get(pagePosition - 2).getChild().get(itemPosition).getSpace_tag_name();
            strings.add(name);
            return strings;
        }
    }


    public static List<String> getTabName1(TabAllBean tabAllBean, List<String> list, int clickSpace, int position) {
        List<String> listNew = new ArrayList<>();

        if (clickSpace == 1) {
            listNew.add(list.get(0));
            listNew.add(tabAllBean.getData().getSpaces().get(position).getSpace_tag_name());
            if (tabAllBean.getData().getSpaces().get(position).getChild().size() > 0) {
                for (int i = 0; i < tabAllBean.getData().getSpaces().get(position).getChild().size(); i++) {
                    listNew.add(tabAllBean.getData().getSpaces().get(position).getChild().get(i).getSpace_tag_name());
                }
            }
        } else if (clickSpace == 0) {
            if (position != -1) {
                listNew.add(tabAllBean.getData().getStyles().get(position).getStyle_tag_name());
                for (int i = 1; i < list.size(); i++) {
                    listNew.add(list.get(i));
                }
            }
        } else if (clickSpace == 3) {

            return list;
        }
        return listNew;
    }


    /**
     * 清除应用缓存
     *
     * @param context
     */
    public static void clearAppCache(Context context) {
        DataCleanManager.cleanInternalCache(UIUtils.getContext());
        DataCleanManager.cleanCustomCache(context.getCacheDir().getAbsolutePath());
        MPFileUtility.clearCacheContent(context);
        ImageLoader.getInstance().clearDiskCache();
        ImageLoader.getInstance().clearMemoryCache();
    }

    /**
     * 设置缩放后的图片的宽高
     *
     * @param simpleDraweeView view
     * @param mContext
     * @param picWidth         原图的宽
     * @param picHeight        原图的高
     */
    public static void setPicHeighAndWidth(ImageView simpleDraweeView, Context mContext, int picWidth, int picHeight, int shengyu) {
        if (picWidth == 0 || picHeight == 0) {
            return;
        }
        ViewGroup.LayoutParams layoutParams = simpleDraweeView.getLayoutParams();
        int screenWidth = getScreenWidth(mContext) - shengyu;
        layoutParams.width = screenWidth;
        if (getPicHeightScale(picHeight, picWidth, screenWidth) > screenWidth) {
            layoutParams.height = screenWidth;
        } else {
            layoutParams.height = getPicHeightScale(picHeight, picWidth, screenWidth);
        }
        simpleDraweeView.setLayoutParams(layoutParams);
    }

    /**
     * 设置缩放后的图片的宽高
     *
     * @param simpleDraweeView view
     * @param mContext
     * @param picWidth         原图的宽
     * @param picHeight        原图的高
     */
    public static void setPicHeighAndWidth1(SimpleDraweeView simpleDraweeView, Context mContext, int picWidth, int picHeight, int shengyu) {
        if (picWidth == 0 || picHeight == 0) {
            return;
        }
        ViewGroup.LayoutParams layoutParams = simpleDraweeView.getLayoutParams();
        int screenWidth = getScreenWidth(mContext) - shengyu;
        layoutParams.width = screenWidth;
        layoutParams.height = getPicHeightScale(picHeight, picWidth, screenWidth);
        simpleDraweeView.setLayoutParams(layoutParams);
    }

    /**
     * 获取屏幕的宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    /**
     * 获取图片缩放后的高度
     *
     * @return
     */
    public static int getPicHeightScale(int picHeight, int picWidth, int screenWidth) {
        return (screenWidth * picHeight) / picWidth;
    }

    public static FirebaseAnalytics initFirebase(Context context) {
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        String ifLogin = SharedPreferencesUtils.readString("login_in");
        String userid = SharedPreferencesUtils.readString("user_id");
        if (TextUtils.isEmpty(ifLogin)) {
            mFirebaseAnalytics.setUserId(CommontUtils.getPhoneImail(context));
        } else {
            if (ifLogin.equals("register")) {
                mFirebaseAnalytics.setUserId(userid + "$设计师");
            } else {
                mFirebaseAnalytics.setUserId(userid);
            }
        }
        return mFirebaseAnalytics;
    }

    // 网络状态
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

}
