package com.homechart.app.home.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.homechart.app.MyApplication;
import com.homechart.app.R;
import com.homechart.app.commont.utils.ChatUtils;
import com.homechart.app.commont.utils.CommontUtils;
import com.homechart.app.commont.utils.GsonUtil;
import com.homechart.app.commont.utils.ImageUtils;
import com.homechart.app.commont.utils.SharedPreferencesUtils;
import com.homechart.app.commont.utils.ToastUtils;
import com.homechart.app.commont.utils.UIUtils;
import com.homechart.app.home.bean.ChatListBean;
import com.homechart.app.home.bean.ChatListDataUserBean;
import com.homechart.app.home.fragment.HomeCenterDesinerFragment;
import com.homechart.app.netutils.MPServerHttpManager;
import com.homechart.app.netutils.OkStringRequest;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.homechart.app.MyHXChatActivity;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.easeui.utils.EaseSmileUtils;
import com.hyphenate.easeui.widget.EaseConversationList;
import com.hyphenate.util.DateUtils;
import com.umeng.socialize.utils.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.bingoogolapple.badgeview.BGABadgeTextView;

/**
 * Created by Administrator on 2017/3/23/023.
 */

public class MyChatListActivity extends BaseActivity implements View.OnClickListener, ListView.OnItemClickListener {
    private ListView lv_chatlist;
    private List<ChatListDataUserBean> user_list;
    private ImageButton nav_left_imageButton;
    private String ticket;
    private List<EMConversation> list_last;
    private TextView tv_tital_comment;
    private FirebaseAnalytics mFirebaseAnalytics;
    private NetChangReceiver netChangReceiver1;
    private MyChatListAdapter adapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_chatlist;
    }


    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();
        user_list = (List<ChatListDataUserBean>) getIntent().getExtras().get("data");
        ticket = SharedPreferencesUtils.readString("ticket");
    }

    @Override
    protected void initView() {
        lv_chatlist = (ListView) findViewById(R.id.lv_chatlist);
        tv_tital_comment = (TextView) findViewById(R.id.tv_tital_comment);
        nav_left_imageButton = (ImageButton) findViewById(R.id.nav_left_imageButton);
    }


    @Override
    protected void initListener() {
        super.initListener();
        nav_left_imageButton.setOnClickListener(this);
        lv_chatlist.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_left_imageButton:
                MyChatListActivity.this.finish();
                break;
        }
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        tv_tital_comment.setText("咨询");
        initBroad();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        String ifLogin = SharedPreferencesUtils.readString("login_in");
        String userid = SharedPreferencesUtils.readString("user_id");
        if (TextUtils.isEmpty(ifLogin)) {
            mFirebaseAnalytics.setUserId(CommontUtils.getPhoneImail(MyChatListActivity.this));
        } else {
            if (ifLogin.equals("register")) {
                mFirebaseAnalytics.setUserId(userid + "$设计师");
            } else {
                mFirebaseAnalytics.setUserId(userid);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Get tracker.
        Tracker t = MyApplication.getInstance().getDefaultTracker();
        // Set screen name.
        t.setScreenName("咨询记录页");
        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());

//        if (user_list != null) {
//            list_last = loadConversationList();
//            adapter = new MyChatListAdapter(user_list, MyChatListActivity.this);
//            lv_chatlist.setAdapter(adapter);
//        } else {
//            getChatList();
//        }
        list_last = loadConversationList();
        getChatList();
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

    /**
     * load conversation list
     *
     * @return +
     */
    protected List<EMConversation> loadConversationList() {
        // get all conversations
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
        /**
         * lastMsgTime will change if there is new message during sorting
         * so use synchronized to make sure timestamp of last message won't change.
         */
        synchronized (conversations) {
            for (EMConversation conversation : conversations.values()) {
                if (conversation.getAllMessages().size() != 0) {
                    sortList.add(new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(), conversation));
                }
            }
        }
        try {
            // Internal is TimSort algorithm, has bug
            sortConversationByLastChatTime(sortList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<EMConversation> list = new ArrayList<EMConversation>();
        for (Pair<Long, EMConversation> sortItem : sortList) {
            list.add(sortItem.second);
        }
        return list;
    }

    /**
     * sort conversations according time stamp of last message
     *
     * @param conversationList
     */
    private void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {
        Collections.sort(conversationList, new Comparator<Pair<Long, EMConversation>>() {
            @Override
            public int compare(final Pair<Long, EMConversation> con1, final Pair<Long, EMConversation> con2) {

                if (con1.first.equals(con2.first)) {
                    return 0;
                } else if (con2.first.longValue() > con1.first.longValue()) {
                    return 1;
                } else {
                    return -1;
                }
            }

        });
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
                    MyChatListAdapter adapter = new MyChatListAdapter(user_list, MyChatListActivity.this);
                    lv_chatlist.setAdapter(adapter);
                } else {
                    ToastUtils.showCenter(MyChatListActivity.this, "咨询记录获取失败");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                .setCategory("my_action")  //事件类别
                .setAction("咨询设计师")      //事件操作
                .setLabel(user_list.get(position).getNickname())
                .build());
        MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                .setCategory("my_action")  //事件类别
                .setAction("咨询设计师only")      //事件操作
                .build());
        Bundle bundle = new Bundle();
        bundle.putString("designerName", user_list.get(position).getNickname());
        mFirebaseAnalytics.logEvent("咨询记录页_咨询设计师", bundle);
        String userid = user_list.get(position).getUser_id();
        EaseUser easeUser = new EaseUser(userid);
        easeUser.setAvatar(user_list.get(position).getAvatar());
        easeUser.setNickname(user_list.get(position).getNickname());
        ChatUtils.getInstance().put(userid, easeUser);
        getChatList(userid, ticket);
        Intent intent1 = new Intent(this, MyHXChatActivity.class);
        intent1.putExtra(EaseConstant.EXTRA_USER_ID, userid);
        startActivity(intent1);
    }

    private void getChatList(String user_id, String ticket) {
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("test", "失败");
            }

            @Override
            public void onResponse(String s) {
                Log.d("test", "成功");
            }
        };
        MPServerHttpManager.getInstance().notifyChat(user_id, ticket, callBack);
    }

    class MyChatListAdapter extends BaseAdapter {
        private List<ChatListDataUserBean> user_list;
        private Context context;
        private EaseConversationList.EaseConversationListHelper helper;

        public MyChatListAdapter(List<ChatListDataUserBean> user_list, Context context) {
            this.user_list = user_list;
            this.context = context;
            helper = new EaseConversationList.EaseConversationListHelper() {
                @Override
                public String onSetItemSecondaryText(EMMessage lastMessage) {
                    return null;
                }
            };
        }

        @Override
        public int getCount() {
            return user_list.size();
        }

        @Override
        public Object getItem(int position) {
            return user_list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MyHolder myHolder;
            if (convertView == null) {
                myHolder = new MyHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.item_chatlist, null);
                myHolder.iv_person_header = (ImageView) convertView.findViewById(R.id.iv_person_header);
                myHolder.tv_person_name = (TextView) convertView.findViewById(R.id.tv_person_name);
                myHolder.tv_person_content = (TextView) convertView.findViewById(R.id.tv_person_content);
                myHolder.tv_person_time = (TextView) convertView.findViewById(R.id.tv_person_time);
                myHolder.tv_unreader_icon = (BGABadgeTextView) convertView.findViewById(R.id.tv_unreader_icon);
                convertView.setTag(myHolder);
            } else {
                myHolder = (MyHolder) convertView.getTag();
            }
            ImageUtils.displayRoundImage(user_list.get(position).getAvatar(), myHolder.iv_person_header);
            String nikeNmae = user_list.get(position).getNickname();
            if (nikeNmae.trim().length() > 6) {
                nikeNmae = user_list.get(position).getNickname().substring(0, 6) + "...";
            }
            if (list_last != null && list_last.size() > 0) {
                for (int i = 0; i < list_last.size(); i++) {
                    if (list_last.get(i).conversationId().equals(user_list.get(position).getUser_id())) {
                        EMMessage lastMessage = list_last.get(i).getLastMessage();
                        String content = null;
                        if (helper != null) {
                            content = helper.onSetItemSecondaryText(lastMessage);
                        }
                        myHolder.tv_person_content.setText(EaseSmileUtils.getSmiledText(MyChatListActivity.this, EaseCommonUtils.getMessageDigest(lastMessage, (MyChatListActivity.this))),
                                TextView.BufferType.SPANNABLE);
                        if (content != null) {
                            myHolder.tv_person_content.setText(content);
                        }
                        myHolder.tv_person_time.setText(DateUtils.getTimestampString(new Date(lastMessage.getMsgTime())));
                    }
                }
            }
            myHolder.tv_person_name.setText(nikeNmae);
            //TODO 查询单个聊天室未读
            EMConversation conversation = EMClient.getInstance().chatManager().getConversation(user_list.get(position).getUser_id());
            if (conversation != null) {
                int count = conversation.getUnreadMsgCount();
                if (count > 0) {
                    myHolder.tv_unreader_icon.setVisibility(View.VISIBLE);
                    myHolder.tv_unreader_icon.getBadgeViewHelper().setBadgeBgColorInt(UIUtils.getColor(R.color.bg_e79056));
                    myHolder.tv_unreader_icon.showTextBadge(count + "");
                } else {
                    myHolder.tv_unreader_icon.setVisibility(View.GONE);
                }
            }

            return convertView;
        }

        class MyHolder {
            private ImageView iv_person_header;
            private BGABadgeTextView tv_unreader_icon;
            private TextView tv_person_name;
            private TextView tv_person_content;
            private TextView tv_person_time;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(netChangReceiver1);
    }

    private void initBroad() {
        //实例化IntentFilter对象
        IntentFilter filter = new IntentFilter();
        filter.addAction("notify_unreader");
        filter.addAction("notify_reader");
        netChangReceiver1 = new NetChangReceiver();
        //注册广播接收
        registerReceiver(netChangReceiver1, filter);
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
                getChatList();
//                adapter.notifyDataSetChanged();
            }
        }
    };

}
