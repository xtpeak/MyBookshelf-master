package com.monke.monkeybook.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.monke.monkeybook.utils.ColorUtil;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.core.graphics.ColorUtils;

public class BatteryView extends View {

    private static final int BACK_ALPHA = 85;
    private static final int TEXT_ALPHA = 165;

    private static final int OUT_BORDER_WIDTH = 3;
    private static final int OUT_RECT_RADIUS = 4;
    private static final int INNER_SPACING = 2;
    private static final int POLE_RECT_RADIUS = 3;

    private float[] radiusArray = {POLE_RECT_RADIUS, POLE_RECT_RADIUS, 0f, 0f, 0f, 0f, POLE_RECT_RADIUS, POLE_RECT_RADIUS};

    private Paint mPaint;
    private TextPaint mTextPaint;
    private int mColor = Color.BLACK;
    private int mTextColor = Color.WHITE;

    private int mProgress = 100;

    private RectF mRect;
    private Path mPorPath;

    private boolean showBatteryNumber;

    public BatteryView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setDither(true);

        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setDither(true);
        mTextPaint.setFakeBoldText(true);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        mRect = new RectF();
        mPorPath = new Path();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        int poleWidth = getHeight() / 6;
        int halfBorderWidth = OUT_BORDER_WIDTH / 2;

        mRect.set(poleWidth + halfBorderWidth, halfBorderWidth, getWidth() - halfBorderWidth, getHeight() - halfBorderWidth);

        //外框
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(OUT_BORDER_WIDTH);
        mPaint.setColor(mColor);
        canvas.drawRoundRect(mRect, OUT_RECT_RADIUS, OUT_RECT_RADIUS, mPaint);

        //电量背景
        float left = mRect.left + INNER_SPACING + halfBorderWidth;
        float right = mRect.right - INNER_SPACING - halfBorderWidth;
        mRect.set(left, mRect.top + INNER_SPACING + halfBorderWidth , right, mRect.bottom - INNER_SPACING - halfBorderWidth);
        mPaint.setStyle(Paint.Style.FILL);
        if (showBatteryNumber) {
            mPaint.setColor(ColorUtils.setAlphaComponent(mColor, BACK_ALPHA));
            canvas.drawRoundRect(mRect, 2, 2, mPaint);
        }

        //电量进度
        float offset = (1 - 1.0f * mProgress / 100) * (right - left);
        mRect.left += offset;
        mPaint.setColor(mColor);
        canvas.drawRoundRect(mRect, 2, 2, mPaint);

        //电量文字
        if (showBatteryNumber) {
            mTextPaint.setTextSize(mRect.height());
            mTextPaint.setColor(ColorUtils.setAlphaComponent(mTextColor, TEXT_ALPHA));
            String progress = String.valueOf(mProgress);
            float centerX = (right - left) / 2 + left;
            float centerY = 1.0f * getHeight() / 2;
            Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
            float top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
            float bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
            float baseLineY = centerY - top / 2 - bottom / 2;//基线中间点的y轴计算公
            canvas.drawText(progress, centerX, baseLineY, mTextPaint);
        }

        //电极
        int halfHeight = getHeight() / 2;
        int halfPorHeight = getHeight() / 5;
        mRect.set(0, halfHeight - halfPorHeight, poleWidth, halfHeight + halfPorHeight);
        mPorPath.reset();
        mPorPath.addRoundRect(mRect, radiusArray, Path.Direction.CCW);
        canvas.drawPath(mPorPath, mPaint);
    }

    public void setColor(@ColorInt int color) {
        mColor = color;
        boolean isDark = ColorUtil.isDark(mColor);
        mTextColor = isDark ? Color.WHITE : Color.BLACK;
        invalidate();
    }

    public void setProgress(int progress) {
        mProgress = progress;
        invalidate();
    }

    public void setShowBatteryNumber(boolean showBatteryNumber) {
        this.showBatteryNumber = showBatteryNumber;
        invalidate();
    }
}
