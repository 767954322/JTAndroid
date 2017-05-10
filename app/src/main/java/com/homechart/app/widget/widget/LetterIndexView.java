package com.homechart.app.widget.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.homechart.app.R;

/**
 * 城市
 */
public class LetterIndexView extends View {

    /**
     * 默认侧边栏显示字母
     */
    private String[] mWords = {"热门", "A", "B", "C", "D", "E", "F", "G",
            "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z", "#"};
    private int mWordsLength;

    /**
     * 是否选中
     */
    private int mChoose = -1;
    /**
     * 提示显示文本框
     */
    private TextView mTextViewDialog;
    /**
     * 相应的画笔
     */
    private Paint mPaint;

    /**
     * 是否按下
     */
    private boolean mIsDown = false;

    /**
     * 点击回调
     */
    private ItemClickCallBack mClickCallBack;
    private int mSingleHeight = 30;

    public LetterIndexView(Context context) {
        this(context, null);
    }

    public LetterIndexView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LetterIndexView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.parseColor("#262626"));
        mPaint.setAntiAlias(true);
        mPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mWordsLength = mWords.length;
        mSingleHeight = (int) getResources().getDimension(R.dimen.size_15);
        mPaint.setTextSize(getResources().getDimension(R.dimen.size_10));
    }

    public void setTextViewDialog(TextView textViewDialog) {
        mTextViewDialog = textViewDialog;
    }

    public void setClickCallBack(ItemClickCallBack clickCallBack) {
        mClickCallBack = clickCallBack;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (heightMode == MeasureSpec.AT_MOST)
            heightSize = mSingleHeight * mWordsLength + 16;

        setMeasuredDimension(widthMeasureSpec, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();

        for (int i = 0; i < mWordsLength; i++) {
            String index = mWords[i];
            float xPos = width / 2 - mPaint.measureText(index) / 2;
            float yPos = mSingleHeight * i + mSingleHeight + 16;
            canvas.drawText(index, xPos, yPos, mPaint);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float y = event.getY();
        int oldChoose = mChoose;
        int choose = (int) (y / getHeight() * mWordsLength);

        switch (action) {
            case MotionEvent.ACTION_UP:
                mIsDown = false;
                mChoose = -1;
                invalidate();

                if (mTextViewDialog != null) {
                    mTextViewDialog.setVisibility(View.INVISIBLE);
                }
                break;
            default:
                mIsDown = true;
                if (choose >= 0) {
                    if (oldChoose != choose) {
                        if (choose < mWordsLength) {
                            String indexWord = mWords[choose];
                            if (mClickCallBack != null) {
                                mClickCallBack.onClick(indexWord);
                            }

                            if (mTextViewDialog != null) {
                                mTextViewDialog.setText(indexWord);
                                mTextViewDialog.setVisibility(View.VISIBLE);
                            }

                            mChoose = choose;
                            invalidate();
                        }
                    }
                }
                break;
        }
        return true;
    }

    public interface ItemClickCallBack {
        void onClick(Object object);
    }
}
