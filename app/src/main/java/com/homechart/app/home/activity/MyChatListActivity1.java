package com.homechart.app.home.activity;

import android.os.Bundle;

import com.homechart.app.R;
import com.homechart.app.home.bean.ChatListDataUserBean;
import com.homechart.app.home.fragment.MyListFragment;

import java.util.List;

/**
 * Created by Administrator on 2017/3/25/025.
 */

public class MyChatListActivity1 extends BaseActivity {
    private MyListFragment listFragment;
    private List<ChatListDataUserBean> user_list;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_chatlist1;
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();
        user_list = (List<ChatListDataUserBean>) getIntent().getExtras().get("data");
    }
    @Override
    protected void initView() {


    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        listFragment = new MyListFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fl_chatlist, listFragment)
                .commit();

        //TODO

    }
}
