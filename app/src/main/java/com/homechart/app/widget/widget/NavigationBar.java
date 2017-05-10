package com.homechart.app.widget.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.homechart.app.R;
import com.homechart.app.commont.utils.CommontUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/3/23/023.
 */

public class NavigationBar extends View {

    private static final int NORMAL_COLOR = Color.parseColor("#707070");
    private static final int CLICK_COLOR = Color.parseColor("#262626");

    private Paint mPaint;
    private List<MenuItem> mMenuItems;
    private int mRadius = 8;
    private Drawable mSelectedDrawable;
    private int mDrawableHalfWidth;
    private int mDrawableHalfHeight;
    private Matrix mSelectedMatrix;
    private Paint.FontMetrics mFontMetrics;
    private RectF mTextBgRectF;
    private BarMenuClick mBarMenuClick;
    private int mMenuSize;
    private int mSingleWidth;

    public void setMenuItems(List<MenuItem> mMenuItems) {
        this.mMenuItems = mMenuItems;
        mMenuSize = mMenuItems.size();
        invalidate();
    }

    public void setBarMenuClick(BarMenuClick mBarMenuClick) {
        this.mBarMenuClick = mBarMenuClick;
    }

    public NavigationBar(Context context) {
        this(context, null);
    }

    public NavigationBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NavigationBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(NORMAL_COLOR);
        mPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mPaint.setTextSize(getContext().getResources().getDimension(R.dimen.size_10));
        mSelectedDrawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_selected);
        mSelectedMatrix = new Matrix();
        mDrawableHalfWidth = mSelectedDrawable.getIntrinsicWidth() / 2;
        mDrawableHalfHeight = mSelectedDrawable.getIntrinsicHeight() / 2;
        mFontMetrics = mPaint.getFontMetrics();
        mTextBgRectF = new RectF();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mMenuSize >= 5) {
            int width = (CommontUtils.getScreenWidth(getContext()) / 4) * mMenuSize;
            setMeasuredDimension(width, heightMeasureSpec);
        } else {
            int width = CommontUtils.getScreenWidth(getContext());
            setMeasuredDimension(width, heightMeasureSpec);
        }
    }

    public void refreshView() {
        mMenuSize = mMenuItems.size();
        requestLayout();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        if (mMenuItems != null && mMenuSize > 0) {
            MenuItem menuItem;
            int textWidth;
            int textHeight;
            int singleHeight;
            int x;
            int lastX = 0;
            mSingleWidth = width / mMenuSize;
            for (int index = 0; index < mMenuSize; index++) {
                mPaint.setColor(NORMAL_COLOR);
                menuItem = mMenuItems.get(index);
                x = mSingleWidth * index + mSingleWidth / 2;
                singleHeight = height / 3;
                if (index > 0)
                    canvas.drawLine(lastX + mRadius * 2, singleHeight * 2, x - mRadius * 2, singleHeight * 2, mPaint);
                if (!menuItem.isChoose
                        && !menuItem.isSelected)
                    mPaint.setColor(NORMAL_COLOR);
                if (menuItem.isChoose)
                    mPaint.setColor(CLICK_COLOR);
                if (menuItem.isSelected) {
                    mSelectedMatrix.setTranslate(x - mDrawableHalfWidth, singleHeight * 2 - mDrawableHalfHeight);
                    canvas.drawBitmap(drawableToBitmap(mSelectedDrawable), mSelectedMatrix, mPaint);
                } else {
                    canvas.drawCircle(x, singleHeight * 2, mRadius, mPaint);
                }
                mPaint.setColor(NORMAL_COLOR);
                textWidth = (int) (x - mPaint.measureText(menuItem.mMenuName) / 2);
                textHeight = (int) Math.ceil(mFontMetrics.descent - mFontMetrics.ascent) + 10;
                if (menuItem.isChoose) {
                    mPaint.setColor(Color.BLACK);
                    mTextBgRectF.set(textWidth - mRadius * 2, 10,
                            x + mPaint.measureText(menuItem.mMenuName) / 2 + mRadius * 2, textHeight + mRadius);
                    canvas.drawRoundRect(mTextBgRectF, 18, 18, mPaint);
                    mPaint.setColor(Color.WHITE);
                    canvas.drawText(menuItem.mMenuName, textWidth, textHeight, mPaint);
                } else {
                    canvas.drawText(menuItem.mMenuName, textWidth, textHeight, mPaint);
                }

                lastX = x;
            }
        }
    }

    public static class MenuItem {
        private String mMenuName;
        private boolean isSelected;
        private boolean isChoose;

        public MenuItem(String mMenuName) {
            this.mMenuName = mMenuName;
        }

        public String getMenuName() {
            return mMenuName;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public boolean isChoose() {
            return isChoose;
        }

        public void setmMenuName(String mMenuName) {
            this.mMenuName = mMenuName;
        }

        public void setChoose(boolean choose) {
            isChoose = choose;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap;
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap.Config config =
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                        : Bitmap.Config.RGB_565;
        bitmap = Bitmap.createBitmap(w, h, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int pos = -1;
        float downX = 0;
        float upX;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                Log.d("pos, dx: ", downX + "");
                Log.d("pos: ", downX / (getWidth() / mMenuItems.size()) + "");
                break;
            case MotionEvent.ACTION_UP:
                upX = event.getX();
                Log.d("pos, upX: ", upX + "");
                Log.d("pos: ", (upX + downX) / 2 / mMenuItems.size() + "");
                // Log.d("pos: ", upY /  getWidth() * mMenuItems.size() + "");
                pos = (int) (upX / (getWidth() / mMenuItems.size()));
                mBarMenuClick.onMenuClick(pos);
                break;
        }
        return true;
    }

    public interface BarMenuClick {
        void onMenuClick(int position);
    }
}
