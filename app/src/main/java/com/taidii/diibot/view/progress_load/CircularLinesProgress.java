package com.taidii.diibot.view.progress_load;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

public class CircularLinesProgress extends View {
    private int mLinesNumber = 12;
    private int mLineWidth = 6;
    private int mWidth;
    private int mHeight;
    private Paint mPaint;
    private float mCurrentProgress = 0f;
    private Paint mChangePaint;
    private int mRunContent = 0;
    private ValueAnimator valueAnimator;

    public void setCurrentProgress(float mCurrentProgress) {
        this.mCurrentProgress = mCurrentProgress;
        postInvalidate();
    }

    public CircularLinesProgress(Context context) {
        this(context, null);
    }

    public CircularLinesProgress(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircularLinesProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.GRAY);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mLineWidth);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);


        mChangePaint = new Paint();
        mChangePaint.setDither(true);
        mChangePaint.setAntiAlias(true);
        mChangePaint.setColor(Color.WHITE);
        mChangePaint.setStyle(Paint.Style.STROKE);
        mChangePaint.setStrokeWidth(mLineWidth);
        mChangePaint.setStrokeCap(Paint.Cap.ROUND);
        mChangePaint.setStrokeJoin(Paint.Join.ROUND);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(mWidth >= mHeight ? mHeight : mWidth, mWidth >= mHeight ? mHeight : mWidth);

        mLineWidth = mHeight / mLinesNumber;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setStrokeWidth(mLineWidth);
        mChangePaint.setStrokeWidth(mLineWidth);
        int x = mWidth / 2;
        int y = mHeight / 2;
        int r = (int) (mWidth * 0.45);
        canvas.save();
        if (mRunContent % 2 == 0) {
            for (int i = 0; i < mLinesNumber; i++) {
                //绘制下层菊花
                canvas.drawLine(x, y - r, x, (float) (y - r + x * 0.4), mPaint);
                canvas.rotate(360 / mLinesNumber, x, y);
            }

            //应该画多少
            int currentContent = (int) (mLinesNumber * mCurrentProgress);
            for (int i = currentContent; i > 0; i--) {
                //绘制下层菊花
                canvas.drawLine(x, y - r, x, (float) (y - r + x * 0.4), mChangePaint);
                canvas.rotate(360 / mLinesNumber, x, y);
            }
        } else {
            for (int i = 0; i < mLinesNumber; i++) {
                //绘制下层菊花
                canvas.drawLine(x, y - r, x, (float) (y - r + x * 0.4), mChangePaint);
                canvas.rotate(360 / mLinesNumber, x, y);
            }
            //应该画多少
            int currentContent = (int) (mLinesNumber * mCurrentProgress);
            for (int i = currentContent; i > 0; i--) {
                //绘制下层菊花
                canvas.drawLine(x, y - r, x, (float) (y - r + x * 0.4), mPaint);
                canvas.rotate(360 / mLinesNumber, x, y);
            }
        }
        canvas.restore();
    }

    public void start() {
        if(valueAnimator==null){
            valueAnimator = ValueAnimator.ofFloat(0, 1.0f);
            valueAnimator.setDuration(1000);
            valueAnimator.setRepeatCount(-1);
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationRepeat(Animator animation) {
                    mRunContent += 1;
                }
            });
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    float value = (float) valueAnimator.getAnimatedValue();
                    setCurrentProgress(value);
                }
            });
            valueAnimator.start();
        }

    }

    public void cancel(){
        if(valueAnimator!=null){
            valueAnimator.cancel();
            valueAnimator.end();
            valueAnimator.addUpdateListener(null);
            valueAnimator.addListener(null);
        }
    }
}
