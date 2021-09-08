package com.taidii.diibot.module.class_card.viewJzd;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import com.taidii.diibot.R;
import com.taidii.diibot.utils.Utils;

import static android.view.View.MeasureSpec.AT_MOST;
import static android.view.View.MeasureSpec.EXACTLY;
import static android.view.View.MeasureSpec.UNSPECIFIED;
import static com.taidii.diibot.utils.ConvertUtils.px2sp;

public class MyTextView extends AppCompatTextView {

    private Context context;

    public MyTextView(Context context) {
        this(context, null);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        int wSize = MeasureSpec.getSize(widthMeasureSpec);

        int hMode = MeasureSpec.getMode(heightMeasureSpec);
        int hSize = MeasureSpec.getSize(heightMeasureSpec);

        hSize =  this.getMeasuredHeight();
        TextPaint newPaint = new TextPaint();
        String textStr = getText().toString();
        float textSize = getResources().getDisplayMetrics().scaledDensity  * px2sp(context, getTextSize());
        newPaint.setTextSize(textSize);
        float textWidth = getLineLength(textStr, getTextSize());
        int targetWidith = 0;
        int newWidthMeasureSpec = 0;
        int newHeightMeasureSpec = 0;
        switch (wMode){
            case UNSPECIFIED:
                wSize = (int) (Utils.dip2px(context.getResources().getDimension(R.dimen.dp_2)) + textWidth);
                newWidthMeasureSpec = MeasureSpec.makeMeasureSpec(wSize, wMode);
                break;

            case EXACTLY:
                newWidthMeasureSpec = MeasureSpec.makeMeasureSpec(wSize, wMode);
                break;

            case AT_MOST:
                targetWidith = (int) (Utils.dip2px(context.getResources().getDimension(R.dimen.dp_2)) + textWidth);
                if(wSize > targetWidith){
                    wSize = targetWidith;
                }
                newWidthMeasureSpec = MeasureSpec.makeMeasureSpec(wSize, wMode);
                break;
        }
        newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(hSize, hMode);
        setMeasuredDimension(newWidthMeasureSpec, newHeightMeasureSpec);
    }

    public int getLineLength(String str, float textSize) {
        Paint pFont = new Paint();
        Typeface fromAsset = Typeface.createFromAsset(context.getAssets(), "fonts/DFPHannotateW5-GB.ttf");
        pFont.setTypeface(fromAsset);
        pFont.setTextSize(textSize);
        return (int) pFont.measureText(str);
    }
}