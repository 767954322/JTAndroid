package com.homechart.app.commont.view;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.flexible.flexibleadapter.common.SmoothScrollGridLayoutManager;
import com.flexible.flexibleadapter.items.AbstractFlexibleItem;
import com.flexible.flexibleadapter.items.IHeader;
import com.homechart.app.R;
import com.homechart.app.commont.utils.UIUtils;
import com.homechart.app.picheader.ExampleAdapter;
import com.homechart.app.picheader.HeaderItem;
import com.homechart.app.picheader.SimpleItem;
import com.homechart.app.widget.widget.LetterIndexView;

import java.util.ArrayList;
import java.util.List;

public class DesinerCityPopWin extends PopupWindow {

    private final RecyclerView mRecyclerView;
    private final RelativeLayout pop_city;
    private View view;
    private Activity mContext;
    private static SimpleItem.CityOnClick onClick;
    private LetterIndexView mLetterIndexView;
    private List<AbstractFlexibleItem> mItems;

    public DesinerCityPopWin(Activity context, List<AbstractFlexibleItem> headerItems, SimpleItem.CityOnClick onClick) {
        this.mContext = context;
        this.onClick = onClick;
        this.mItems = headerItems;
        this.view = LayoutInflater.from(context).inflate(R.layout.wk_city_popwindow2, null);

        final ExampleAdapter mAdapter = new ExampleAdapter(this.mItems, mContext);
        GridLayoutManager gridLayoutManager = new SmoothScrollGridLayoutManager(mContext, 3);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                //noinspection ConstantConditions
                return mAdapter.getItem(position).getSpanSize(3, position);
            }
        });
        //找对象
        mLetterIndexView = (LetterIndexView) this.view.findViewById(R.id.letterView);
        mLetterIndexView.setClickCallBack(mLetterCallBack);
        mRecyclerView = (RecyclerView) this.view.findViewById(R.id.recycler);
        pop_city = (RelativeLayout) this.view.findViewById(R.id.pop_city);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);

        mAdapter.setLongPressDragEnabled(false)
                .setHandleDragEnabled(true)
                .setSwipeEnabled(false)
                .setUnlinkAllItemsOnRemoveHeaders(true)
                // Show Headers at startUp, 1st call, correctly executed, no warning log message!
                .setDisplayHeadersAtStartUp(true)
                .setStickyHeaders(true)
                // Simulate developer 2nd call mistake, now it's safe, not executed, no warning log message!
                // Simulate developer 3rd call mistake, still safe, not executed, warning log message displayed!
                .showAllHeaders();

        // 设置外部可点击
        this.setOutsideTouchable(true);

    /* 设置弹出窗口特征 */
        // 设置视图
        this.setContentView(this.view);
        // 设置弹出窗体的宽和高
        this.setHeight(RelativeLayout.LayoutParams.MATCH_PARENT);
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        this.setOutsideTouchable(false);
        // 设置弹出窗体可点击
        this.setFocusable(false);
        // 设置外部可点击
        this.setOutsideTouchable(false);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        this.view.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = pop_city.getTop();

                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y > height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
        // 实例化一个ColorDrawable颜色为半透明
//        ColorDrawable dw = new ColorDrawable(0xb0000000);
//         设置弹出窗体的背景
        this.setBackgroundDrawable(new ColorDrawable(UIUtils.getColor(R.color.white)));

        // 设置弹出窗体显示时的动画
//        this.setAnimationStyle(R.style.Transparent_Dialog);

        // 设置弹出窗体可点击
        this.setFocusable(false);
    }


    public List<AbstractFlexibleItem> getItems(List<String> strings) {
        List<AbstractFlexibleItem> headerItems = new ArrayList<>();
        HeaderItem header = null;
        for (int i = 0; i < strings.size(); i++) {
            if (i == 0) {
                header = newHeader(strings.get(i));
            } else if (i > 0) {
                if (!strings.get(i).equals(strings.get(i - 1))) {
                    header = newHeader(strings.get(i));
                }
            }
            headerItems.add(newSimpleItem(i + 1, header));
        }
        return headerItems;
    }

    /*public int getIndex(String letter) {
        if (mItems != null && mItems.size() > 0){
            int size = mItems.size();
            AbstractFlexibleItem item;
            for (int i = 0; i < size; i++) {
                item = mItems.get(i);
            }
        }
    }*/

    private LetterIndexView.ItemClickCallBack mLetterCallBack = new LetterIndexView.ItemClickCallBack() {
        @Override
        public void onClick(Object object) {
            if (mItems != null && mItems.size() > 0) {
                int size = mItems.size();
                Object o;
                for (int i = 0; i < size; i++) {
                    o = mItems.get(i);
                    if (o instanceof HeaderItem) {
                        if (((HeaderItem) o).getTitle().equals(object)) {
                            mRecyclerView.scrollToPosition(i);
                            return;
                        }
                    }
                }
            }
        }
    };

    public List<String> getData() {
        List<String> strings = new ArrayList<>();
        strings.add("所说的发送到1");
        strings.add("所说的发送到2");
        strings.add("所说的发送到3");
        strings.add("发送到1");
        strings.add("发送到2");
        strings.add("发送到3");
        strings.add("发送到4");
        strings.add("A1");
        strings.add("A2");
        strings.add("A3");
        strings.add("A4");
        strings.add("A");
        strings.add("A");
        strings.add("A");
        strings.add("A");
        strings.add("A");
        strings.add("A");
        strings.add("B1");
        strings.add("B2");
        strings.add("B3");
        strings.add("B4");
        strings.add("B5");
        strings.add("B");
        strings.add("B");
        strings.add("B");
        strings.add("B");
        strings.add("B");
        strings.add("B");
        strings.add("B");
        strings.add("C1");
        strings.add("C2");
        strings.add("C3");
        strings.add("C4");
        strings.add("C5");
        strings.add("C6");
        strings.add("C");
        strings.add("C");
        strings.add("C");
        strings.add("C");
        strings.add("C");
        strings.add("C");
        strings.add("C");
        strings.add("C");
        strings.add("C");
        strings.add("C");
        strings.add("D1");
        strings.add("D2");
        strings.add("D3");
        strings.add("D4");
        strings.add("D5");
        strings.add("D");
        strings.add("D");
        strings.add("D");
        strings.add("E1");
        strings.add("E2");
        strings.add("E3");
        strings.add("E4");
        strings.add("E5");
        strings.add("E");
        strings.add("E");
        strings.add("E");
        strings.add("E");
        strings.add("F1");
        strings.add("F2");
        strings.add("F3");
        strings.add("F4");
        strings.add("F5");
        strings.add("F");
        strings.add("F");
        strings.add("F");
        strings.add("F");
        strings.add("F");
        strings.add("F");
        strings.add("F");
        strings.add("F");
        strings.add("F");
        strings.add("F");
        strings.add("F");
        strings.add("G1");
        strings.add("G2");
        strings.add("G3");
        strings.add("G4");
        strings.add("G5");
        strings.add("G");
        strings.add("G");
        strings.add("G");
        strings.add("G");
        strings.add("G");
        strings.add("G");
        strings.add("G");
        strings.add("G");
        return strings;
    }


    public static HeaderItem newHeader(String title) {
        HeaderItem header = new HeaderItem(title);
        header.setTitle(title);
        return header;
    }

    public static SimpleItem newSimpleItem(int i, IHeader header) {
        HeaderItem headerItem = (HeaderItem) header;
        SimpleItem item = new SimpleItem("" + i, headerItem, onClick);
        //TODO通过这个获取city_ID
//        item.setTitle("");
        return item;
    }

}
