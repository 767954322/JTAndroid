package com.homechart.app.home.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.homechart.app.MyApplication;
import com.homechart.app.R;
import com.homechart.app.commont.utils.ChatUtils;
import com.homechart.app.commont.utils.GsonUtil;
import com.homechart.app.commont.utils.ImageUtils;
import com.homechart.app.commont.utils.SharedPreferencesUtils;
import com.homechart.app.commont.utils.ToastUtils;
import com.homechart.app.home.activity.ConsumerHomeActivity;
import com.homechart.app.home.activity.MyCaseLibraryActivity;
import com.homechart.app.home.activity.MyChatListActivity;
import com.homechart.app.home.activity.SetActivity;
import com.homechart.app.home.bean.ChatListBean;
import com.homechart.app.home.bean.ChatListDataUserBean;
import com.homechart.app.netutils.MPServerHttpManager;
import com.homechart.app.netutils.OkStringRequest;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.domain.EaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by GPD on 2017/2/27.
 */

public class HomeCenterFragment extends BaseFragment implements View.OnClickListener {

    private boolean mReceiverTag = false;
    private ImageButton back;
    private ImageView toXiang;
    private TextView name;
    private RelativeLayout rl_center_chart;
    private RelativeLayout rl_center_shezhi;
    private String user_id;
    private String nickname;
    private String avatar;
    private String ticket;
    private String mobile;
    private String email;
    private String profession;
    private String project_num;
    private String ease_username;
    private String ease_password;
    private TextView tv_tital_comment;
    private List<ChatListDataUserBean> user_list;
    private TextView tv_center_chart_next;
    private TextView tv_chat_new_msg;
    private ConsumerHomeActivity consumerHomeActivity;
    private NetChangReceiver netChangReceiver2;


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_home_center;
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();
        user_id = SharedPreferencesUtils.readString("user_id");
        nickname = SharedPreferencesUtils.readString("nickname");
        avatar = SharedPreferencesUtils.readString("avatar");
        ticket = SharedPreferencesUtils.readString("ticket");
        mobile = SharedPreferencesUtils.readString("mobile");
        email = SharedPreferencesUtils.readString("email");
        profession = SharedPreferencesUtils.readString("profession");
        project_num = SharedPreferencesUtils.readString("project_num");
        ease_username = SharedPreferencesUtils.readString("ease_username");
        ease_password = SharedPreferencesUtils.readString("ease_password");

    }

    @Override
    protected void initView() {
        consumerHomeActivity = (ConsumerHomeActivity) activity;
        back = (ImageButton) rootView.findViewById(R.id.nav_left_imageButton);
        toXiang = (ImageView) rootView.findViewById(R.id.iv_persion_touxiang);
        name = (TextView) rootView.findViewById(R.id.tv_center_name);
        tv_chat_new_msg = (TextView) rootView.findViewById(R.id.tv_chat_new_msg);
        tv_tital_comment = (TextView) rootView.findViewById(R.id.tv_tital_comment);
        tv_center_chart_next = (TextView) rootView.findViewById(R.id.tv_center_chart_next);
        rl_center_chart = (RelativeLayout) rootView.findViewById(R.id.rl_center_chart);
        rl_center_shezhi = (RelativeLayout) rootView.findViewById(R.id.rl_center_shezhi);
    }

    @Override
    protected void initListener() {
        super.initListener();
        rl_center_chart.setOnClickListener(this);
        rl_center_shezhi.setOnClickListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        tv_tital_comment.setText("我的");
        back.setVisibility(View.GONE);
        getChatList();
        ImageUtils.displayRoundImage(avatar, toXiang);
        name.setText(nickname);
        initBroad();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Get tracker.
        Tracker t = MyApplication.getInstance().getDefaultTracker();
        // Set screen name.
        t.setScreenName("我的页面");
        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
        changeChatUI();
        getChatList();
        EaseUser easeUser = new EaseUser(user_id);
        easeUser.setAvatar(avatar);
        easeUser.setNickname(nickname);
        ChatUtils.getInstance().put(user_id, easeUser);
    }

    public void changeChatUI() {
        int size = 0;
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        for (EMConversation conversation : conversations.values()) {
            if (conversation.getUnreadMsgCount() != 0) {
                size += conversation.getUnreadMsgCount();
            }
        }
        if (size > 0) {
            tv_chat_new_msg.setVisibility(View.VISIBLE);
            Intent intent = new Intent();
            intent.setAction("unreader");
            activity.sendBroadcast(intent);
        } else {
            tv_chat_new_msg.setVisibility(View.GONE);
            Intent intent = new Intent();
            intent.setAction("reader");
            activity.sendBroadcast(intent);
        }
    }

    private void getChatList() {

        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }

            @Override
            public void onResponse(String s) {
                Message message = new Message();
                message.obj = s;
                handler.sendMessage(message);
            }
        };
        MPServerHttpManager.getInstance().getChatList(ticket, callBack);

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = (String) msg.obj;
            try {
                JSONObject jsonObject = new JSONObject(result);
                int status = jsonObject.getInt("status");
                if (status == 1) {
                    ChatListBean chatListBean = GsonUtil.jsonToBean(result, ChatListBean.class);
                    user_list = null;
                    user_list = chatListBean.getData().getUsers();
                    tv_center_chart_next.setText(user_list.size() + "");
                } else {
                    ToastUtils.showCenter(activity, "咨询记录获取失败");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.rl_center_shezhi:
                Bundle bundle6 = new Bundle();
                consumerHomeActivity.mFirebaseAnalytics.logEvent("我的页面_设置点击", bundle6);
                Intent intent = new Intent(activity, SetActivity.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.rl_center_service:
                Intent intent1 = new Intent(activity, MyCaseLibraryActivity.class);
                startActivityForResult(intent1, 0);
                break;
            case R.id.rl_center_chart:
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("my_action")  //事件类别
                        .setAction("咨询记录点击")      //事件操作
                        .build());
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("my_action")  //事件类别
                        .setAction("咨询记录点击")//事件操作
                        .build());
                Bundle bundle4 = new Bundle();
                consumerHomeActivity.mFirebaseAnalytics.logEvent("我的页面_咨询记录点击", bundle4);
                Intent intent2 = new Intent(activity, MyChatListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", (Serializable) user_list);
                intent2.putExtras(bundle);
                startActivity(intent2);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mReceiverTag) {   //判断广播是否注册
            try {
                activity.unregisterReceiver(netChangReceiver2);
            } catch (IllegalArgumentException e) {
                if (e.getMessage().contains("Receiver not registered")) {
                } else {
                    throw e;
                }
            }
        }
    }

    private void initBroad() {
        if (!mReceiverTag) {
            //实例化IntentFilter对象
            IntentFilter filter = new IntentFilter();
            filter.addAction("notify_unreader");
            filter.addAction("notify_reader");
            netChangReceiver2 = new NetChangReceiver();
            //注册广播接收
            getActivity().registerReceiver(netChangReceiver2, filter);
            mReceiverTag = true;    //标识值 赋值为 true 表示广播已被注册
        }
    }

    //创建一个继承BroadcastReceiver的广播监听器；
    class NetChangReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("notify_unreader")) {
                Message message = new Message();
                message.obj = "notify_unreader";
                handler1.sendMessage(message);
            } else if (action.equals("notify_reader")) {
                Message message = new Message();
                message.obj = "notify_reader";
                handler1.sendMessage(message);
            }
        }
    }

    Handler handler1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String context = (String) msg.obj;
            if (context.equals("notify_unreader")) {
                tv_chat_new_msg.setVisibility(View.VISIBLE);
            } else if (context.equals("notify_reader")) {
                tv_chat_new_msg.setVisibility(View.GONE);
            }
        }
    };


}
