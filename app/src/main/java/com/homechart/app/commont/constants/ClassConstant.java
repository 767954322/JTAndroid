package com.homechart.app.commont.constants;

import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * @author allen .
 * @version v1.0 .
 * @date 2017-2-24.
 * @file ClassConstant.java .
 * @brief 固定常量或Key值整合 .
 */
public class ClassConstant {

    public static class HomePic {
        public static final String SIGN = "sign";//加密
        public static final String PAGER_NUM = "p";//页数
        public static final String PIC_NUM = "num";//每页个数（默认60）
        public static final String STYLE_TAG = "style_tag_id";//风格标签ID
        public static final String SPACE_TAG = "space_tag_id";//空间标签ID(传空间下的细节标签ID(不为0) 要用逗号和空间标签ID拼接
        // 2000196,2000010 两个ID一并传给后台)
    }

    public static class HomeTag {
        public static final String TYPES = "types";
    }

    //CaseLibraryDetailsActivity
    public static class CaseLibraryDetailsKey {
        public static final String ALBUM_ID = "album_id";
        public static final String ITEM_ID = "item_id";
        public static final String NUM = "num";
        public static final String OFFSET = "s";
    }

    //DesinerCenterActivity
    public static class DesinerCenterKey {
        public static final String USER_ID = "user_id";
        public static final String CASE_NUM = "num";
        public static final String CASE_OFFSET = "s";
        public static final String CASE_TYPE = "type";
    }

    public static class HackViewPager {
        public static final String URL = "scrollview_url";
        public static final String POSITION = "scrollview_position";
    }

    public static class FragmentEnum {

        public static final int ZERO = 0;
        public static final int ONE = 1;
        public static final int TWO = 2;
        public static final int THREE = 3;
        public static final int FOUR = 4;
        public static final int FIVE = 5;

    }

    public static class DesinerCitys {
        public static final String CITY_TAG = "types";
        public static final String CITY_VALUES = "citylist";
    }

    public static class DesinerInfo {
        public static final String DESINER_NUM = "num";
        public static final String PAGE_NUM = "p";
        public static final String CITY_ID = "city_id";
    }

    public static class DesinerRegister {
        public static final String MOBILE = "mobile";
        public static final String TYPE = "type";
        public static final String CHALLENGE = "challenge";
        public static final String VALIDATE = "validate";
        public static final String SECCODE = "seccode";

        public static final String OPENID = "openid";
        public static final String PLAT = "plat";
        public static final String TOKEN = "token";
        public static final String NIKENAME = "nickname";

    }

    public static class UserRegisterActivity {
        public static final String MOBILE = "mobile";
        public static final String CODE = "code";
        public static final String NIKENAME = "nickname";
        public static final String PASSWORD = "password";
        public static final String TYPE = "type";
    }

    public static class UserLoginActivity {
        public static final String USERNAME = "username";
        public static final String PASSWORD = "password";
    }

    public static class IssueBackActivity {
        public static final String FEED_CONTENT = "feed_content";
        public static final String IAMGE_ID = "image_id";
        public static final String CONTRACT_WAY = "contact_way";
    }


    public static class UMengPlatform {
        public static final SHARE_MEDIA platform_qq = SHARE_MEDIA.QQ;
        public static final SHARE_MEDIA platform_weixin = SHARE_MEDIA.WEIXIN;
        public static final SHARE_MEDIA platform_sina = SHARE_MEDIA.SINA;
        public static final SHARE_MEDIA platform_qzone = SHARE_MEDIA.QZONE;
    }

}
