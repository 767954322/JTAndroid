package com.homechart.app.commont.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.homechart.app.R;
import com.homechart.app.home.adapter.HomePicPagerAdapter;
import com.homechart.app.home.bean.MyTabBean;

import java.util.List;

public class HomeTabPopWin extends PopupWindow {

    private final MyViewPager vp_home_tag;
    private final HomePicPagerAdapter homePicPagerAdapter;
    private View view;
    private Context mContext;
    private List<List<MyTabBean>> mLists;
    private List<List<MyTabBean>> mLists_enable;
    private HomePicPagerAdapter.OnGridViewItemClick onGridViewItemClick;
    private PopupWindowCallBack mCallBack;
    private TextView mSelectText;
    private RelativeLayout mTextLayout;
    private Drawable mDeleteDrawable;
    private boolean mIfChoose;

    public HomeTabPopWin(Context context, ViewPager.OnPageChangeListener onPageChangeListener, List<List<MyTabBean>> lists, HomePicPagerAdapter.OnGridViewItemClick onGridViewItemClick) {

        this.mContext = context;
        this.mLists = lists;
        this.onGridViewItemClick = onGridViewItemClick;

        this.view = LayoutInflater.from(context).inflate(R.layout.wk_flow_popwindow, null);

        //找对象
        vp_home_tag = (MyViewPager) this.view.findViewById(R.id.vp_home_tag);
        vp_home_tag.setOffscreenPageLimit(2);
        mTextLayout = (RelativeLayout) this.view.findViewById(R.id.rl_tag_chose);
        mSelectText = (TextView) this.view.findViewById(R.id.tv_select_name);
        // 设置按钮监听
        vp_home_tag.addOnPageChangeListener(onPageChangeListener);
        mSelectText.setOnClickListener(mDeleteListener);

        // 设置外部可点击
        this.setOutsideTouchable(true);
        homePicPagerAdapter = new HomePicPagerAdapter(lists, context, onGridViewItemClick);
        vp_home_tag.setAdapter(homePicPagerAdapter);

    /* 设置弹出窗口特征 */
        // 设置视图
        this.setContentView(this.view);
        // 设置弹出窗体的宽和高
        this.setHeight(RelativeLayout.LayoutParams.MATCH_PARENT);
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        this.setOutsideTouchable(false);
        // 设置弹出窗体可点击
        this.setFocusable(false);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        this.view.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                int height = vp_home_tag.getHeight() + mTextLayout.getHeight();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y > height) {
                        dismiss();
                        mCallBack.onDismiss();
                    }
                }
                return true;
            }
        });
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置弹出窗体的背景
        this.setBackgroundDrawable(dw);

        // 设置弹出窗体显示时的动画
//        this.setAnimationStyle(R.style.Transparent_Dialog);
        mDeleteDrawable = ContextCompat.getDrawable(mContext, R.drawable.guanbi);
        mDeleteDrawable.setBounds(0, 0, mDeleteDrawable.getMinimumWidth(), mDeleteDrawable.getMinimumHeight());
        mSelectText.setCompoundDrawables(null, null, mDeleteDrawable, null);
        mSelectText.setCompoundDrawablePadding((int) mContext.getResources().getDimension(R.dimen.size_5));
    }

    public void setCallBack(PopupWindowCallBack callBack) {
        mCallBack = callBack;
    }

    public void setSelectedName(String name) {
        if (TextUtils.isEmpty(name)) {
            mSelectText.setVisibility(View.GONE);
        } else {
            mSelectText.setText(name);
            mSelectText.setVisibility(View.VISIBLE);
        }
    }

    public void setSelectedItem(int position, String name) {
        if (TextUtils.isEmpty(name)) {
            mSelectText.setVisibility(View.GONE);
            if (position > 1)
                deleteItem(position);
        } else {
            mSelectText.setText(name);
            mSelectText.setVisibility(View.VISIBLE);
        }
    }

    public void setNotityChangeData(List<List<MyTabBean>> lists, List<List<MyTabBean>> lists_enable, boolean ifChoose) {
        mLists = lists;
        mLists_enable = lists_enable;
        mIfChoose = ifChoose;
        homePicPagerAdapter.setDataChanged(lists,mLists_enable);
    }

    public void putSelectMap(int position, String name) {
        homePicPagerAdapter.putSelectMap(position, name);
    }

    public void setViewPagerNum(int num) {
        if (null != vp_home_tag) {
            if (vp_home_tag.getCurrentItem() != num) {
                vp_home_tag.setCurrentItem(num);
            }
        }
    }

    private View.OnClickListener mDeleteListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCallBack.onDelete(vp_home_tag.getCurrentItem());
        }
    };

    public interface PopupWindowCallBack {
        void onDismiss();

        void onDelete(int position);
    }

    public void deleteItem(int position) {

//        if(position == 1){
//            homePicPagerAdapter.deleteItem(position);
//        }
        if (position == 1) {
            homePicPagerAdapter.updateSelected();
        }
        homePicPagerAdapter.deleteItem(position);
        homePicPagerAdapter.notifyDataSetChanged();
    }

    public void deleteUpData() {
        homePicPagerAdapter.updateSelected();
    }
}
