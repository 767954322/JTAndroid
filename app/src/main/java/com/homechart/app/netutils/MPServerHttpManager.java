package com.homechart.app.netutils;

import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HttpHeaderParser;
import com.homechart.app.MyApplication;
import com.homechart.app.commont.constants.ClassConstant;
import com.homechart.app.commont.constants.KeyConstans;
import com.homechart.app.commont.constants.UrlConstants;
import com.homechart.app.commont.utils.CommontUtils;
import com.homechart.app.commont.utils.Md5Util;
import com.homechart.app.commont.utils.SharedPreferencesUtils;
import com.homechart.app.commont.utils.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.util.HashMap;
import java.util.Map;

/**
 * @author allen .
 * @version v1.0 .
 * @date 2017-2-24.
 * @file MPServerHttpManager.java .
 * @brief 请求工具类 .
 */
public class MPServerHttpManager {
    public CookieManager cookieManager;
    private RequestQueue queue = MyApplication.getInstance().queue;
    private static MPServerHttpManager mpServerHttpManager = new MPServerHttpManager();

    private MPServerHttpManager() {
    }

    public static MPServerHttpManager getInstance() {
        return mpServerHttpManager;
    }

    /**
     * 获取首页图片上方Tab数据
     *
     * @param callback
     */
    public void getHomeTabData(OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.HOME_TAB_URL, callback) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = CommontUtils.getPublicMap(MyApplication.getInstance());
                map.put(ClassConstant.HomeTag.TYPES, "tag");
                String tabMd5String = Md5Util.getMD5twoTimes("jiami" + KeyConstans.ENCRYPTION_KEY);
                map.put(ClassConstant.HomePic.SIGN, "jiami" + "," + tabMd5String);
                return map;
            }
        };
        queue.add(okStringRequest);
    }

    /**
     * 获取首页图片上方Tab数据
     *
     * @param callback
     */
    public void getHomeTabData(final String style_tag_id, final String space_tag_id, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.HOME_TAB_URL, callback) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = CommontUtils.getPublicMap(MyApplication.getInstance());
                map.put("style_tag_id", style_tag_id);
                map.put("space_tag_id", space_tag_id);
                map.put(ClassConstant.HomeTag.TYPES, "tag");
                String tabMd5String = Md5Util.getMD5twoTimes("jiami" + KeyConstans.ENCRYPTION_KEY);
                map.put(ClassConstant.HomePic.SIGN, "jiami" + "," + tabMd5String);
                return map;
            }
        };
        queue.add(okStringRequest);
    }

    /**
     * 获取首页图片列表数据
     *
     * @param pageNum
     * @param picNum
     * @param stype
     * @param space
     * @param callback
     */
    public void getHomePicData(final int pageNum,
                               final int picNum,
                               final String stype,
                               final String space,
                               OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.HOME_PIC_URL, callback) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = CommontUtils.getPublicMap(MyApplication.getInstance());
                map.put(ClassConstant.HomePic.PAGER_NUM, String.valueOf(pageNum));
                map.put(ClassConstant.HomePic.PIC_NUM, String.valueOf(picNum));
                if (!TextUtils.isEmpty(stype))
                    map.put(ClassConstant.HomePic.STYLE_TAG, stype);
                if (!TextUtils.isEmpty(space))
                    map.put(ClassConstant.HomePic.SPACE_TAG, space);
                String tabMd5String = Md5Util.getMD5twoTimes("jiami" + KeyConstans.ENCRYPTION_KEY);
                map.put(ClassConstant.HomePic.SIGN, "jiami" + "," + tabMd5String);
                return map;
            }
        };
        queue.add(okStringRequest);
    }


    /**
     * 获取案例详情头部数据
     *
     * @param mAlbumId
     * @param callback
     */
    public void getCaseDetailTitalData(final String mAlbumId,
                                       OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.DETAILS_CASE_TITAL, callback) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = CommontUtils.getPublicMap(MyApplication.getInstance());
                map.put(ClassConstant.CaseLibraryDetailsKey.ALBUM_ID, mAlbumId);
                String tabMd5String = Md5Util.getMD5twoTimes("jiami" + KeyConstans.ENCRYPTION_KEY);
                map.put(ClassConstant.HomePic.SIGN, "jiami" + "," + tabMd5String);
                return map;
            }
        };
        queue.add(okStringRequest);
    }

    /**
     * 获取案例详情图片数据
     *
     * @param mAlbumId
     * @param callback
     */
    public void getCaseDetailPicData(final String mAlbumId,
                                     final int picNum,
                                     final int offset,
                                     OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.DETAILS_CASE_PIC, callback) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = CommontUtils.getPublicMap(MyApplication.getInstance());
                String tabMd5String = Md5Util.getMD5twoTimes("jiami" + KeyConstans.ENCRYPTION_KEY);
                map.put(ClassConstant.HomePic.SIGN, "jiami" + "," + tabMd5String);
                map.put(ClassConstant.CaseLibraryDetailsKey.ALBUM_ID, mAlbumId);
                map.put(ClassConstant.CaseLibraryDetailsKey.NUM, String.valueOf(picNum));
                map.put(ClassConstant.CaseLibraryDetailsKey.OFFSET, String.valueOf(offset));
                return map;
            }
        };
        queue.add(okStringRequest);
    }

    /**
     * 获取设计师中心个人信息（头部信息）
     *
     * @param user_id
     * @param callback
     */
    public void getDesinerCenterDesinerInfo(final String user_id,
                                            OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.DETAILS_CENTER_TITAL, callback) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = CommontUtils.getPublicMap(MyApplication.getInstance());
                String tabMd5String = Md5Util.getMD5twoTimes("jiami" + KeyConstans.ENCRYPTION_KEY);
                map.put(ClassConstant.HomePic.SIGN, "jiami" + "," + tabMd5String);
                map.put(ClassConstant.DesinerCenterKey.USER_ID, user_id);
                return map;
            }
        };
        queue.add(okStringRequest);
    }

    /**
     * 获取设计师中心案例信息（下部图片）
     *
     * @param user_id
     * @param offset
     * @param picNum
     */
    public void getDesinerCenterCasesInfo(final String user_id,
                                          final int offset,
                                          final int picNum,
                                          OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.DETAILS_CENTER_PIC, callback) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = CommontUtils.getPublicMap(MyApplication.getInstance());
                String tabMd5String = Md5Util.getMD5twoTimes("jiami" + KeyConstans.ENCRYPTION_KEY);
                map.put(ClassConstant.HomePic.SIGN, "jiami" + "," + tabMd5String);
                map.put(ClassConstant.DesinerCenterKey.USER_ID, user_id);
                map.put(ClassConstant.DesinerCenterKey.CASE_NUM, String.valueOf(picNum));
                map.put(ClassConstant.DesinerCenterKey.CASE_OFFSET, String.valueOf(offset));
                map.put(ClassConstant.DesinerCenterKey.CASE_TYPE, "2");
                return map;
            }
        };
        queue.add(okStringRequest);
    }

    /**
     * 获取设计师城市列表
     *
     * @param callback
     */
    public void getDesinerCityList(OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.DETAILS_CITYS, callback) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = CommontUtils.getPublicMap(MyApplication.getInstance());
                String tabMd5String = Md5Util.getMD5twoTimes("jiami" + KeyConstans.ENCRYPTION_KEY);
                map.put(ClassConstant.HomePic.SIGN, "jiami" + "," + tabMd5String);
                map.put(ClassConstant.DesinerCitys.CITY_TAG, ClassConstant.DesinerCitys.CITY_VALUES);
                return map;
            }
        };
        queue.add(okStringRequest);
    }

    /**
     * 获取设计师城市案例列表
     *
     * @param picNum
     * @param pageNum
     * @param position
     * @param callback
     */
    public void getDesinerCityCases(final int picNum,
                                    final int pageNum,
                                    final int position,
                                    OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.DETAILS_LISTS, callback) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = CommontUtils.getPublicMap(MyApplication.getInstance());
                String tabMd5String = Md5Util.getMD5twoTimes("jiami" + KeyConstans.ENCRYPTION_KEY);
                map.put(ClassConstant.HomePic.SIGN, "jiami" + "," + tabMd5String);
                map.put(ClassConstant.DesinerInfo.DESINER_NUM, String.valueOf(picNum));
                map.put(ClassConstant.DesinerInfo.PAGE_NUM, String.valueOf(pageNum));
                map.put(ClassConstant.DesinerInfo.CITY_ID, String.valueOf(position));
                return map;
            }
        };
        queue.add(okStringRequest);
    }

    /**
     * 从服务器获取极验证需要的三个参数
     *
     * @param callback
     */
    public void getParamsFromMyServiceJY(OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.JIYAN_GETPARAM, callback) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = CommontUtils.getPublicMap(MyApplication.getInstance());
                String tabMd5String = Md5Util.getMD5twoTimes("jiami" + KeyConstans.ENCRYPTION_KEY);
                map.put(ClassConstant.HomePic.SIGN, "jiami" + "," + tabMd5String);
                return map;
            }
            @Override
            protected Response<String> parseNetworkResponse(
                    NetworkResponse response) {
                try {
                    Map<String, String> responseHeaders = response.headers;
                    String rawCookies = responseHeaders.get("Set-Cookie");
                    cookieManager = new CookieManager();
                    cookieManager.getCookieStore().add(null, HttpCookie.parse(rawCookies).get(0));
                    String dataString = new String(response.data, "UTF-8");
                    return Response.success(dataString, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                }
            }
        };
        queue.add(okStringRequest);
    }

    /**
     * 级验滑动后，先检验是否可以发送短信
     *
     * @param phoneNum
     * @param cookieType
     * @param callback
     */
    public void checkPhoneNumStatus(final String phoneNum, final String cookieType, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.JIYAN_PHONE_SENDNUM, callback) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = CommontUtils.getPublicMap(MyApplication.getInstance());
                String tabMd5String = Md5Util.getMD5twoTimes("jiami" + KeyConstans.ENCRYPTION_KEY);
                map.put(ClassConstant.HomePic.SIGN, "jiami" + "," + tabMd5String);
                map.put(ClassConstant.DesinerRegister.MOBILE, phoneNum);
                map.put(ClassConstant.DesinerRegister.TYPE, "signup");
                return map;
            }

            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap localHashMap = new HashMap();
                if (null != cookieManager && cookieManager.getCookieStore().getCookies().size() > 0) {
                    localHashMap.put("Cookie", TextUtils.join(";", cookieManager.getCookieStore().getCookies()));
                }
                return localHashMap;
            }

            @Override
            protected Response<String> parseNetworkResponse(
                    NetworkResponse response) {
                try {
                    String dataString = new String(response.data, "UTF-8");
                    return Response.success(dataString, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                }
            }
        };
        queue.add(okStringRequest);
    }

    /**
     * 发送验证码
     *
     * @param phone
     * @param challenge
     * @param validate
     * @param seccode
     * @param cookieType
     * @param callback
     */
    public void sendMessageByJY(final String phone, final String challenge, final String validate, final String seccode, final String cookieType, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.JIYAN_SEND_MESSAGE, callback) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = CommontUtils.getPublicMap(MyApplication.getInstance());
                String tabMd5String = Md5Util.getMD5twoTimes("jiami" + KeyConstans.ENCRYPTION_KEY);
                map.put(ClassConstant.HomePic.SIGN, "jiami" + "," + tabMd5String);
                map.put(ClassConstant.DesinerRegister.MOBILE, phone);
                map.put(ClassConstant.DesinerRegister.TYPE, "signup");
                map.put(ClassConstant.DesinerRegister.CHALLENGE, challenge);
                map.put(ClassConstant.DesinerRegister.VALIDATE, validate);
                map.put(ClassConstant.DesinerRegister.SECCODE, seccode);
                return map;
            }

            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap localHashMap = new HashMap();
                if (null != cookieManager && cookieManager.getCookieStore().getCookies().size() > 0) {
                    localHashMap.put("Cookie", TextUtils.join(";", cookieManager.getCookieStore().getCookies()));
                }
                return localHashMap;
            }

            //设置编码格式
            @Override
            protected Response<String> parseNetworkResponse(
                    NetworkResponse response) {
                try {
                    String dataString = new String(response.data, "UTF-8");
                    return Response.success(dataString, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                }
            }
        };
        queue.add(okStringRequest);
    }

    /**
     * 注册用户
     *
     * @param phone
     * @param yanzheng
     * @param nikeName
     * @param passWord
     * @param cookieType
     * @param callback
     */
    public void registerPersion(final String phone, final String yanzheng, final String nikeName, final String passWord, final String cookieType, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.REGISTER_PERSION, callback) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = CommontUtils.getPublicMap(MyApplication.getInstance());
                String tabMd5String = Md5Util.getMD5twoTimes("jiami" + KeyConstans.ENCRYPTION_KEY);
                map.put(ClassConstant.HomePic.SIGN, "jiami" + "," + tabMd5String);
                map.put(ClassConstant.UserRegisterActivity.MOBILE, phone);
                map.put(ClassConstant.UserRegisterActivity.CODE, yanzheng);
                map.put(ClassConstant.UserRegisterActivity.NIKENAME, nikeName);
                map.put(ClassConstant.UserRegisterActivity.PASSWORD, passWord);
                return map;
            }

            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap localHashMap = new HashMap();
                if (null != cookieManager && cookieManager.getCookieStore().getCookies().size() > 0) {
                    localHashMap.put("Cookie", TextUtils.join(";", cookieManager.getCookieStore().getCookies()));
                }
                return localHashMap;
            }

            //设置编码格式
            @Override
            protected Response<String> parseNetworkResponse(
                    NetworkResponse response) {
                // TODO Auto-generated method stub
                try {
                    String dataString = new String(response.data, "UTF-8");
                    return Response.success(dataString, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                }
            }
        };
        queue.add(okStringRequest);
    }

    /**
     * 注册用户
     *
     * @param phone
     * @param yanzheng
     * @param nikeName
     * @param passWord
     * @param cookieType
     * @param callback
     */
    public void registerPersionShared(final String phone, final String yanzheng, final String nikeName, final String passWord, final String cookieType, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.REGISTER_PERSION, callback) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = CommontUtils.getPublicMap(MyApplication.getInstance());
                String tabMd5String = Md5Util.getMD5twoTimes("jiami" + KeyConstans.ENCRYPTION_KEY);
                map.put(ClassConstant.HomePic.SIGN, "jiami" + "," + tabMd5String);
                map.put(ClassConstant.UserRegisterActivity.MOBILE, phone);
                map.put(ClassConstant.UserRegisterActivity.CODE, yanzheng);
                map.put(ClassConstant.UserRegisterActivity.NIKENAME, nikeName);
                map.put(ClassConstant.UserRegisterActivity.PASSWORD, passWord);
                return map;
            }

            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap localHashMap = new HashMap();
                String shared_cookie = SharedPreferencesUtils.readString("shared_cookie");
                localHashMap.put("Cookie", shared_cookie);
                return localHashMap;
            }

            //设置编码格式
            @Override
            protected Response<String> parseNetworkResponse(
                    NetworkResponse response) {
                // TODO Auto-generated method stub
                try {
                    String dataString = new String(response.data, "UTF-8");
                    return Response.success(dataString, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                }
            }
        };
        queue.add(okStringRequest);
    }
    /**
     * 用户登录
     *
     * @param name
     * @param pass
     * @param callback
     */
    public void bundleGLLogin(final String name, final String pass, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.BUNDLE_BEFORE, callback) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = CommontUtils.getPublicMap(MyApplication.getInstance());
                String tabMd5String = Md5Util.getMD5twoTimes("jiami" + KeyConstans.ENCRYPTION_KEY);
                map.put(ClassConstant.HomePic.SIGN, "jiami" + "," + tabMd5String);
                map.put(ClassConstant.UserLoginActivity.USERNAME, name);
                map.put(ClassConstant.UserLoginActivity.PASSWORD, pass);
                return map;
            }

            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap localHashMap = new HashMap();
                String shared_cookie = SharedPreferencesUtils.readString("shared_cookie");
                localHashMap.put("Cookie", shared_cookie);
                return localHashMap;
            }

            //设置编码格式
            @Override
            protected Response<String> parseNetworkResponse(
                    NetworkResponse response) {
                // TODO Auto-generated method stub
                try {
                    String dataString = new String(response.data, "UTF-8");
                    return Response.success(dataString, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                }
            }
        };
        queue.add(okStringRequest);
    }
    /**
     * 检验验证码是否正确
     *
     * @param yanzhengma
     * @param cookieType
     * @param callback
     */
    public void checkYanZhengMa(final String yanzhengma, final String cookieType, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.REGISTER_CHECKCODE, callback) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = CommontUtils.getPublicMap(MyApplication.getInstance());
                String tabMd5String = Md5Util.getMD5twoTimes("jiami" + KeyConstans.ENCRYPTION_KEY);
                map.put(ClassConstant.HomePic.SIGN, "jiami" + "," + tabMd5String);
                map.put(ClassConstant.UserRegisterActivity.CODE, yanzhengma);
                map.put(ClassConstant.UserRegisterActivity.TYPE, "signup");
                return map;
            }

            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap localHashMap = new HashMap();
                if (null != cookieManager && cookieManager.getCookieStore().getCookies().size() > 0) {
                    localHashMap.put("Cookie", TextUtils.join(";", cookieManager.getCookieStore().getCookies()));
                }
                return localHashMap;
            }

            //设置编码格式
            @Override
            protected Response<String> parseNetworkResponse(
                    NetworkResponse response) {
                try {
                    String dataString = new String(response.data, "UTF-8");
                    return Response.success(dataString, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                }
            }
        };
        queue.add(okStringRequest);
    }


    /**
     * 用户登录
     *
     * @param name
     * @param pass
     * @param callback
     */
    public void userLogin(final String name, final String pass, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.LOGIN_PERSION, callback) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = CommontUtils.getPublicMap(MyApplication.getInstance());
                String tabMd5String = Md5Util.getMD5twoTimes("jiami" + KeyConstans.ENCRYPTION_KEY);
                map.put(ClassConstant.HomePic.SIGN, "jiami" + "," + tabMd5String);
                map.put(ClassConstant.UserLoginActivity.USERNAME, name);
                map.put(ClassConstant.UserLoginActivity.PASSWORD, pass);
                return map;
            }
        };
        queue.add(okStringRequest);
    }

    /**
     * 第三方登录
     *
     * @param openid
     * @param token
     * @param plat
     * @param callback
     */
    public void platLogin(final String openid, final String token, final String plat, final String nickname, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.LOGIN_PLAT, callback) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = CommontUtils.getPublicMap(MyApplication.getInstance());
                String tabMd5String = Md5Util.getMD5twoTimes("jiami" + KeyConstans.ENCRYPTION_KEY);
                map.put(ClassConstant.HomePic.SIGN, "jiami" + "," + tabMd5String);
                map.put(ClassConstant.DesinerRegister.OPENID, openid);
                map.put(ClassConstant.DesinerRegister.PLAT, plat);
                map.put(ClassConstant.DesinerRegister.TOKEN, token);
                map.put(ClassConstant.DesinerRegister.NIKENAME, nickname);
                return map;
            }

            @Override
            protected Response<String> parseNetworkResponse(
                    NetworkResponse response) {
                try {
                    Map<String, String> responseHeaders = response.headers;
                    String rawCookies = responseHeaders.get("Set-Cookie");
                    if (cookieManager == null) {
                        cookieManager = new CookieManager();
                    }
                    SharedPreferencesUtils.writeString("shared_cookie", HttpCookie.parse(rawCookies).get(0).toString());
                    cookieManager.getCookieStore().add(null, HttpCookie.parse(rawCookies).get(0));
                    String dataString = new String(response.data, "UTF-8");
                    return Response.success(dataString, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                }
            }
        };
        queue.add(okStringRequest);
    }

    /**
     * 发送反馈
     *
     * @param feed_content
     * @param image_id
     * @param contact_way
     * @param callback
     */
    public void putFileToService(final String ticket,
                                 final String feed_content,
                                 final String image_id,
                                 final String contact_way,
                                 OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.PUT_ISSUE, callback) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = CommontUtils.getPublicMap(MyApplication.getInstance());
                map.put(ClassConstant.IssueBackActivity.FEED_CONTENT, feed_content);
                map.put(ClassConstant.IssueBackActivity.IAMGE_ID, image_id);
                map.put(ClassConstant.IssueBackActivity.CONTRACT_WAY, contact_way);
                map.put("ticket", ticket);
                String tabMd5String = Md5Util.getMD5twoTimes("jiami" + KeyConstans.ENCRYPTION_KEY);
                map.put(ClassConstant.HomePic.SIGN, "jiami" + "," + tabMd5String);
                return map;
            }
        };
        queue.add(okStringRequest);
    }


    /**
     * 获取聊天列表
     *
     * @param ticket
     * @param callback
     */
    public void getChatList(final String ticket, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.CHAT_LIST, callback) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = CommontUtils.getPublicMap(MyApplication.getInstance());
                String tabMd5String = Md5Util.getMD5twoTimes("jiami" + KeyConstans.ENCRYPTION_KEY);
                map.put(ClassConstant.HomePic.SIGN, "jiami" + "," + tabMd5String);
                map.put("ticket", ticket);
                return map;
            }
        };
        queue.add(okStringRequest);
    }

    /**
     * 通知聊天
     *
     * @param userid
     */
    public void notifyChat(final String userid, final String ticket, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.CHAT_LIST, callback) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = CommontUtils.getPublicMap(MyApplication.getInstance());
                map.put("ticket", ticket);
                map.put("toUserId", userid);
                String tabMd5String = Md5Util.getMD5twoTimes("jiami" + KeyConstans.ENCRYPTION_KEY);
                map.put(ClassConstant.HomePic.SIGN, "jiami" + "," + tabMd5String);
                return map;
            }
        };
        queue.add(okStringRequest);
    }

}
