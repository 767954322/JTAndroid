package com.homechart.app.home.fragment;

import android.content.Intent;

import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.homechart.app.MyHXChatActivity;

/**
 * Created by Administrator on 2017/3/9/009.
 */
public class MyListFragment extends EaseConversationListFragment {

    @Override
    protected void initView() {
        super.initView();
        hideTitleBar();

        setConversationListItemClickListener(new EaseConversationListItemClickListener() {
            @Override
            public void onListItemClicked(EMConversation conversation) {

               String id =  conversation.conversationId();
//                EMMessage message = EMMessage.createTxtSendMessage("11", id);
//                conversation.appendMessage(message);
                Intent intent1 = new Intent(getActivity(), MyHXChatActivity.class);
                intent1.putExtra(EaseConstant.EXTRA_USER_ID, id);
                startActivity(intent1);
            }
        });
    }
}
