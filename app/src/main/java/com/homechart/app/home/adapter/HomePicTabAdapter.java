package com.homechart.app.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.homechart.app.R;
import com.homechart.app.commont.utils.CommontUtils;

import java.util.List;

/**
 * Created by GPD on 2017/2/28.
 */

public class HomePicTabAdapter extends RecyclerView.Adapter<HomePicTabAdapter.MyRecyclerHolder> {

    private List<String> tabData;
    private Context context;
    private int width;
    private OnTabClick onTabClick;
    private MyRecyclerHolder holder;

    public HomePicTabAdapter(List<String> tabData, Context context, int width, OnTabClick onTabClick) {
        this.tabData = tabData;
        this.context = context;
        this.width = width;
        this.onTabClick = onTabClick;
    }

    @Override
    public MyRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_fragment_pictab_list, parent, false);
        return new MyRecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(MyRecyclerHolder holder, int position) {
        this.holder = holder;
        initItemData(holder, position);
    }

    @Override
    public int getItemCount() {
        return tabData.size();
    }

    class MyRecyclerHolder extends RecyclerView.ViewHolder {
        private RelativeLayout rl_tabitem_equal_two;
        private View view_left;
        private View view_right;
        private TextView tv_tabname;
        private ImageView iv_tabicon;
        ;

        public MyRecyclerHolder(View itemView) {
            super(itemView);
            tv_tabname = (TextView) itemView.findViewById(R.id.tv_tab_name);
            view_left = (View) itemView.findViewById(R.id.view_line_left);
            view_right = (View) itemView.findViewById(R.id.view_line_right);
            iv_tabicon = (ImageView) itemView.findViewById(R.id.tv_tab_icon);
            rl_tabitem_equal_two = (RelativeLayout) itemView.findViewById(R.id.rl_item_equal_two);
        }
    }

    private void initItemData(MyRecyclerHolder holder, final int position) {
        //..............................1...................................
//        if (tabData.size() > 7) {
//            if (position == 0) {
//                holder.view_right.setVisibility(View.GONE);
//                holder.view_left.setVisibility(View.VISIBLE);
//            } else if (position == (tabData.size() - 1)) {
//                holder.view_right.setVisibility(View.VISIBLE);
//                holder.view_left.setVisibility(View.GONE);
//            }
//        } else {
//            holder.rl_tabitem_equal_two.getLayoutParams().width = width / tabData.size();
//            holder.rl_tabitem_equal_two.setLayoutParams(holder.rl_tabitem_equal_two.getLayoutParams());
//            if (position == 0) {
//                holder.view_right.setVisibility(View.GONE);
//                holder.view_left.setVisibility(View.VISIBLE);
//            } else if (position == (tabData.size() - 1)) {
//                holder.view_right.setVisibility(View.VISIBLE);
//                holder.view_left.setVisibility(View.GONE);
//            }
//        }
        //..............................1...................................

        //..............................2...................................
        if (tabData.size() > 5) {
            holder.rl_tabitem_equal_two.getLayoutParams().width =
                    CommontUtils.getScreenWidth(context) / 5;
            holder.rl_tabitem_equal_two.setLayoutParams(holder.rl_tabitem_equal_two.getLayoutParams());
        } else {
            holder.rl_tabitem_equal_two.getLayoutParams().width = width / tabData.size();
            holder.rl_tabitem_equal_two.setLayoutParams(holder.rl_tabitem_equal_two.getLayoutParams());
        }
        //..............................2...................................
        holder.tv_tabname.setText(tabData.get(position));

        holder.rl_tabitem_equal_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 回掉
                onTabClick.onTabClick(position);
            }
        });
    }

    public interface OnTabClick {
        public void onTabClick(int position);
    }

    public void setDataChanged(List<String> mPicTabList) {
        tabData = mPicTabList;
        notifyDataSetChanged();
    }

    public void changeStateItem(int position, boolean isSelect) {


    }
}
