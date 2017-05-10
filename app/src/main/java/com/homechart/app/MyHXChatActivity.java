package com.homechart.app;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.homechart.app.commont.utils.GsonUtil;
import com.homechart.app.commont.utils.SharedPreferencesUtils;
import com.homechart.app.home.bean.ChatListBean;
import com.homechart.app.home.bean.ChatListDataUserBean;
import com.homechart.app.netutils.MPServerHttpManager;
import com.homechart.app.netutils.OkStringRequest;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.ui.EaseBaseActivity;
import com.hyphenate.easeui.ui.EaseChatFragment;

import java.io.File;
import java.util.List;

public class MyHXChatActivity extends EaseBaseActivity {
    private EaseChatFragment easeChatFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_hxchat);
        final String user_id = getIntent().getStringExtra(EaseConstant.EXTRA_USER_ID);
        String notify_msg = getIntent().getStringExtra("notify_msg");
        easeChatFragment = new EaseChatFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
        bundle.putString(EaseConstant.EXTRA_USER_ID, user_id);
        easeChatFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.fl_chat, easeChatFragment).commit();
        if (!TextUtils.isEmpty(notify_msg)) {
            final String ticket = SharedPreferencesUtils.readString("ticket");
            OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                }

                @Override
                public void onResponse(String s) {
                    ChatListBean chatListBean = GsonUtil.jsonToBean(s, ChatListBean.class);
                    List<ChatListDataUserBean> list = chatListBean.getData().getUsers();
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getUser_id().equals(user_id)) {
                            String name = list.get(i).getNickname();
                            Message msg = new Message();
                            msg.obj = name;
                            handler.sendMessage(msg);
                        }
                    }
                }
            };
            MPServerHttpManager.getInstance().getChatList(ticket, callBack);

        }


    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String name = (String) msg.obj;
            easeChatFragment.setTital(name);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        Tracker t = MyApplication.getInstance().getDefaultTracker();
        t.setScreenName("咨询页");
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        easeChatFragment.onActivityResult(requestCode, resultCode, data);
    }
}
