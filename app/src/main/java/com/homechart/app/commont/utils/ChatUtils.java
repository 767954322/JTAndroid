package com.homechart.app.commont.utils;

import android.text.TextUtils;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;
import com.umeng.socialize.utils.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/25/025.
 */

public class ChatUtils {
    Map<String, EaseUser> map = new HashMap<>();
    private static ChatUtils chatUtils = new ChatUtils();

    private ChatUtils() {
    }

    public static ChatUtils getInstance() {

        if (chatUtils == null) {
            chatUtils = new ChatUtils();
        }
        return chatUtils;
    }

    public static void LoginEaseUI() {
        String user_name = SharedPreferencesUtils.readString("ease_username");
        String user_pass = SharedPreferencesUtils.readString("ease_password");
        if (!TextUtils.isEmpty(user_name) && !TextUtils.isEmpty(user_pass)) {
            EMClient.getInstance().login(user_name, user_pass, new EMCallBack() {//回调
                @Override
                public void onSuccess() {
                    EMClient.getInstance().groupManager().loadAllGroups();
                    EMClient.getInstance().chatManager().loadAllConversations();
                    Log.d("easeui", "登录聊天服务器成功");
                }

                @Override
                public void onProgress(int progress, String status) {

                }

                @Override
                public void onError(int code, String message) {
                    Log.d("easeui", "登录聊天服务器失败");
                }
            });
        }
    }


    public static void LoginOutEaseUI() {
        EMClient.getInstance().logout(true, new EMCallBack() {
            @Override
            public void onSuccess() {
                Log.d("easeui", "聊天退出成功");
                ChatUtils.getInstance().clear();
            }

            @Override
            public void onError(int i, String s) {
                Log.d("easeui", "聊天退出失败");
            }

            @Override
            public void onProgress(int i, String s) {
            }
        });
    }

    public void init() {
        //get easeui instance
        EaseUI easeUI = EaseUI.getInstance();
        //需要EaseUI库显示用户头像和昵称设置此provider
        easeUI.setUserProfileProvider(new EaseUI.EaseUserProfileProvider() {
            @Override
            public EaseUser getUser(String username) {
                EaseUser easeUser = map.get(username);
                if(easeUser==null){
                    easeUser = new EaseUser(username);
                    return easeUser;
                }else {
                    return easeUser;
                }
            }
        });
    }

    public void put(String username,EaseUser easeUser){
        map.put(username,easeUser);
    }

    public void clear(){
        map.clear();
    }

}


