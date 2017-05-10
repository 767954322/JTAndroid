package com.homechart.app.home.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.homechart.app.R;

import java.util.ArrayList;
import java.util.List;
/**
 * 字母导航列表
 */
public class AlphabetListView extends FrameLayout {
    private Context mContext;
    private ListView mListView;
    private LinearLayout alphabetLayout = null;
    private TextView mTextView;    //用于存放 点击右侧字母导航提示内容
    private List<String> indexArray;//索引值存放数组
    private int length = 0;//索引值长度
    private AlphabetPositionListener positionListener;
    private float screenDensity;
    private Handler mHandler;
    private HideIndicator mHideIndicator = new HideIndicator();
    private int indicatorDuration = 1000;
    private int textSize = 10;


    public void setIndicatorDuration(int duration) {
        this.indicatorDuration = duration;
    }

    private final class HideIndicator implements Runnable {
        @Override
        public void run() {
            mTextView.setVisibility(View.INVISIBLE);
        }
    }

    public AlphabetListView(Context context) {
        super(context);
        init(context);
    }

    public AlphabetListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * 相关初始化工作
     * @param context
     */
    private void init(Context context) {
        mContext = context;
        screenDensity = context.getResources().getDisplayMetrics().density;
        mHandler = new Handler();
        /***************设置TextView相关属性***************************/
        mTextView = new TextView(mContext);
        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 70);
        mTextView.setTextColor(Color.parseColor("#ffffff"));
        mTextView.setBackgroundResource(R.drawable.alpha_center_corner);
        mTextView.setGravity(Gravity.CENTER);
        mTextView.setVisibility(View.INVISIBLE);
        int width_height = convertDIP2PX(100);
        FrameLayout.LayoutParams textLayoutParams = new FrameLayout.LayoutParams(width_height, width_height);
        textLayoutParams.gravity = Gravity.CENTER;
        mTextView.setLayoutParams(textLayoutParams);
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }


    public void setAdapter(ListView expandListView, BaseAdapter adapter, AlphabetPositionListener positionListener, List<String> list) {
        if (positionListener == null) {
            throw new IllegalArgumentException("AlphabetPositionListener is required");
        }
        this.removeAllViews();
        mListView = expandListView;
        expandListView.setAdapter(adapter);
        this.positionListener = positionListener;
        this.indexArray = list;

        if (indexArray != null && indexArray.size() > 0) {
            length = list.size();
            initAlphabetLayout(mContext);
        }
        this.addView(mListView);
        if (alphabetLayout != null) {
            this.addView(alphabetLayout);
            this.addView(mTextView);
        } else {
        }
    }

    /**
     * 添加这个方法是为了当字母列表的容器内容变化时直接传递进变化后的容器
     *
     * @param dataList
     * @return
     */
    public AlphabetListView setDataList(ArrayList<String> dataList) {
        this.indexArray = dataList;
        return this;
    }

    public void setAdapter(ExpandableListView expandListView, BaseExpandableListAdapter adapter, AlphabetPositionListener positionListener, ArrayList<String> index) {
        if (positionListener == null) {
            throw new IllegalArgumentException("AlphabetPositionListener is required");
        }
        this.removeAllViews();
        mListView = expandListView;
        expandListView.setAdapter(adapter);
        this.positionListener = positionListener;
        this.indexArray = index;

        if (indexArray != null && indexArray.size() > 0) {
            length = index.size();
            initAlphabetLayout(mContext);
        }
        this.addView(mListView);
        if (alphabetLayout != null) {
            this.addView(alphabetLayout);
            this.addView(mTextView);
        } else {
        }
    }


    //初始化字母索引布局
    private void initAlphabetLayout(Context context) {
        alphabetLayout = new LinearLayout(context);
        alphabetLayout.setOrientation(LinearLayout.VERTICAL);
        FrameLayout.LayoutParams alphabetLayoutParams = new FrameLayout.LayoutParams(convertDIP2PX(25), ViewGroup.LayoutParams.MATCH_PARENT);
        alphabetLayoutParams.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
        alphabetLayoutParams.rightMargin = convertDIP2PX(3);
        alphabetLayoutParams.topMargin = convertDIP2PX(topMargin);
        alphabetLayoutParams.bottomMargin = convertDIP2PX(20);
        mTextView.setBackgroundResource(R.drawable.alpha_center_corner);
        alphabetLayout.setPadding(convertDIP2PX(6), 0, convertDIP2PX(6), 0);
        alphabetLayout.setLayoutParams(alphabetLayoutParams);
        alphabetLayout.setBackgroundColor(Color.parseColor("#00E0E0E0"));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        params.weight = 1;
        params.gravity = Gravity.CENTER_HORIZONTAL;
        for (int i = 0, count = indexArray.size(); i < count; i++) {
            /*******************将字母导航的内容添加到右侧*******************************/
            TextView textView = new TextView(context);
            if (indexArray.get(i).equals("荐")) {
                textView.setTextColor(Color.parseColor("#CC0000"));
            } else {
                textView.setTextColor(Color.argb(140, 105, 115, 125));
            }

            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
            textView.getPaint().setFakeBoldText(true);
            textView.setText(indexArray.get(i));
            textView.setGravity(Gravity.CENTER);
            textView.setLayoutParams(params);
            textView.setTag(i + 1);
            TextPaint tp = textView.getPaint();
            tp.setFakeBoldText(true);
            alphabetLayout.addView(textView);
        }
        //字母索引添加touch事件
        alphabetLayout.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //设置字母索引背景
                        //alphabetLayout.setBackgroundColor(Color.parseColor("#FF0000"));
                        alphabetLayout.setBackgroundResource(R.drawable.alaph_list_corner);
                        float len = (float) (length == 0 ? 0.1 : (float) length);
                        int l = (int) (event.getY() / (alphabetLayout.getHeight() / len));
                        if (l >= length){
                            l = length - 1;
                        }
                        else if (l < 0) {
                            l = 0;
                        }
                        //得到对应的字母在分组标签对应的列表的位置
                        int pos = positionListener.getPosition(indexArray.get(l));
                        if (pos != -1) {
                            mTextView.setText(indexArray.get(l));
                            mTextView.setVisibility(View.VISIBLE);
                            mHandler.removeCallbacks(mHideIndicator);
                            mHandler.postDelayed(mHideIndicator, indicatorDuration);
                            if (mListView instanceof ExpandableListView) {
                                ((ExpandableListView) mListView).setSelectedGroup(pos);
                            } else {
                                mListView.setSelection(pos);
                            }
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        l = (int) ((event.getY() + alphabetLayout.getHeight() / length / 2) / (alphabetLayout.getHeight() / length)) - 1;
                        if (l >= length) l = length - 1;
                        else if (l < 0) l = 0;
                        pos = positionListener.getPosition(indexArray.get(l));
                        if (pos != -1) {
                            mTextView.setText(indexArray.get(l));
                            mTextView.setVisibility(View.VISIBLE);
                            mHandler.removeCallbacks(mHideIndicator);
                            mHandler.postDelayed(mHideIndicator, indicatorDuration);
                            if (mListView instanceof ExpandableListView) {
                                ((ExpandableListView) mListView).setSelectedGroup(pos);
                            } else {
                                mListView.setSelection(pos);
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        alphabetLayout.setBackgroundColor(Color.parseColor("#00E0E0E0"));
                        break;
                }
                return true;
            }
        });
    }

    private int topMargin = 10;

    public void setTopMargin(int dp) {
        topMargin = dp;
    }

    /**
     * dp转换成px
     * @param dip
     * @return
     */
    public int convertDIP2PX(float dip) {
        return (int) (dip * screenDensity + 0.5f * (dip >= 0 ? 1 : -1));
    }

    /**
     * 定义一个抽象类供被点中的字母回调
     */
    public interface AlphabetPositionListener {

        //返回对应的内容(字母)在分组标签对应的列表的位置
        int getPosition(String letter);
    }

}
