package com.homechart.app.widget.pulltprefresh;

import android.content.Context;
import android.util.AttributeSet;


/**
 * @author luchongbin .
 * @version 1.0 .
 * @date 16-6-13 上午10:14
 * @file PullableListView.java  .
 * @brief 自定义 ListView.
 */
public class ScrollSwipePullListView extends SwipeListView implements Pullable {
    ///构造方法.
    public ScrollSwipePullListView(Context context) {
        super(context);
    }

    ///构造方法.
    public ScrollSwipePullListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    ///构造方法.
    public ScrollSwipePullListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean canPullDown() {
        if (getCount() == 0) {
            // 没有item的时候也可以下拉刷新
            return true;
        } else // 滑到ListView的顶部了
            return getFirstVisiblePosition() == 0
                    && getChildAt(0).getTop() >= 0;
    }

    @Override
    public boolean canPullUp() {
        if (getCount() == 0) {
            // 没有item的时候也可以上拉加载
            return true;
        } else if (getLastVisiblePosition() == (getCount() - 1)) {
            // 滑到底部了
            if (getChildAt(getLastVisiblePosition() - getFirstVisiblePosition()) != null
                    && getChildAt(
                    getLastVisiblePosition()
                            - getFirstVisiblePosition()).getBottom() <= getMeasuredHeight())
                return true;
        }
        return false;
    }

    /**
     * Integer.MAX_VALUE >> 2,如果不设置，系统默认设置是显示两条
     */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);

    }
}
