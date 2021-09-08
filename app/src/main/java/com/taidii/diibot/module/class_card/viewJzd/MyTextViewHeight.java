package com.taidii.diibot.module.class_card.viewJzd;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class MyTextViewHeight extends AppCompatTextView {

    private Paint paint1;
    private Paint paint2;

    public MyTextViewHeight(Context context) {
        super(context, null);
    }

    public MyTextViewHeight(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
        // TODO Auto-generated constructor stub
    }

    private void initView(Context context, AttributeSet attrs) {
        paint1 = new Paint();
        paint1.setColor(getResources().getColor(android.R.color.holo_blue_dark));
        paint1.setStyle(Paint.Style.FILL);
        paint2 = new Paint();
        paint2.setStyle(Paint.Style.FILL);
        paint2.setColor(Color.GREEN);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if(heightMode == MeasureSpec.AT_MOST&&widthMode == MeasureSpec.AT_MOST){
            //高度宽度为wrap_content时
            setMeasuredDimension(getMeasuredWidth()+20, getMeasuredHeight()+20);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredWidth(), paint1);
        canvas.drawRect(10, 10, getMeasuredWidth() - 10,
                getMeasuredHeight() - 10, paint2);
        canvas.translate(10, 10);
        super.onDraw(canvas);
    }
}