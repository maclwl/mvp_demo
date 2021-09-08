package com.taidii.diibot.view.titlebar;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.taidii.diibot.R;

/**
 * 通用标题栏
 */
public class CommonTitleBar extends RelativeLayout {
    // 防重复点击时间
    private static final int BTN_LIMIT_TIME = 500;

    private TextView leftButton;
    private ImageView leftButtonImg;
    private TextView middleButton;
    private TextView rightButton;
    private ImageView rightButtonImg;
    private RelativeLayout root;

    private int leftBtnIconId;
    private String leftBtnStr;
    private String titleTxtStr;
    private String rightBtnStr;
    private int rightBtnIconId;

    private String bgStyle;

    public CommonTitleBar(Context context) {
        super(context);
    }

    public CommonTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.CommonTitleBar);
        leftBtnStr = arr.getString(R.styleable.CommonTitleBar_leftBtnTxt);
        leftBtnIconId = arr.getResourceId(R.styleable.CommonTitleBar_leftBtnIcon, 0);
        titleTxtStr = arr.getString(R.styleable.CommonTitleBar_titleTxt);
        rightBtnStr = arr.getString(R.styleable.CommonTitleBar_rightBtnTxt);
        rightBtnIconId = arr.getResourceId(R.styleable.CommonTitleBar_rightBtnIcon, 0);
        bgStyle = arr.getString(R.styleable.CommonTitleBar_baseStyle);
        if (isInEditMode()) {
            LayoutInflater.from(context).inflate(R.layout.view_title_bar, this);
            return;
        }
        LayoutInflater.from(context).inflate(R.layout.view_title_bar, this);
        root = (RelativeLayout) findViewById(R.id.title_out_frame);
        arr.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (isInEditMode()) {
            return;
        }
        leftButtonImg = (ImageView) findViewById(R.id.title_left_btn);
        leftButton = (TextView) findViewById(R.id.title_left);
        middleButton = (TextView) findViewById(R.id.title_middle);
        rightButtonImg = (ImageView) findViewById(R.id.title_right_btn);
        rightButton = (TextView) findViewById(R.id.title_right);

        if (leftBtnIconId != 0) {
            leftButtonImg.setImageResource(leftBtnIconId);
            leftButtonImg.setVisibility(View.VISIBLE);
        } else {
            leftButtonImg.setVisibility(View.GONE);
        }
        if (rightBtnIconId != 0) {
            rightButtonImg.setImageResource(rightBtnIconId);
            rightButtonImg.setVisibility(View.VISIBLE);
        } else {
            rightButtonImg.setVisibility(View.GONE);
        }
        setLeftTxtBtn(leftBtnStr);
        setTitleTxt(titleTxtStr);
        setRightTxtBtn(rightBtnStr);

        if ("0".equals(bgStyle != null ? bgStyle : "0")) {
            //root.setBackgroundResource(R.color.title_bg_color);
        } else {
            root.setBackgroundResource(R.color.transparent);
        }
        // 默认左侧按钮点击事件
        leftButtonImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getContext();
                if (context instanceof Activity) {
                    ((Activity) context).finish();
                }
            }
        });
    }

    public void setRightTxtBtn(String btnTxt) {
        if (!TextUtils.isEmpty(btnTxt)) {
            rightButton.setText(btnTxt);
            rightButton.setTextColor(Color.WHITE);
            rightButton.setVisibility(View.VISIBLE);
        } else {
            rightButton.setVisibility(View.GONE);
        }
    }

    public void setRightButtonClickable(boolean clickable) {
        rightButton.setClickable(clickable);
    }

    public void setLeftTxtBtn(String leftBtnStr) {
        if (!TextUtils.isEmpty(leftBtnStr)) {
            leftButton.setText(leftBtnStr);
            leftButton.setTextColor(Color.WHITE);
            leftButton.setVisibility(View.VISIBLE);
        } else {
            leftButton.setVisibility(View.GONE);
        }
    }

    public void setTitleTxt(String title) {
        if (!TextUtils.isEmpty(title)) {
            middleButton.setText(title);
            middleButton.setVisibility(View.VISIBLE);
        } else {
            middleButton.setVisibility(View.GONE);
        }
    }

    public void setTitleTxtColor(int color) {
        middleButton.setTextColor(color);
    }

    public void hideLeftBtn() {
        leftButton.setVisibility(View.GONE);
        leftButtonImg.setVisibility(View.GONE);
        findViewById(R.id.title_left_area).setOnClickListener(null);
    }

    public void hideRightBtn() {
        rightButton.setVisibility(View.GONE);
        rightButtonImg.setVisibility(View.GONE);
        findViewById(R.id.title_right_area).setOnClickListener(null);
    }

    public void setLeftBtnOnclickListener(OnClickListener listener) {
        OnClickListener myListener = new GlobalLimitClickOnClickListener(listener, BTN_LIMIT_TIME);
        findViewById(R.id.title_left_area).setOnClickListener(myListener);
    }

    public void setRightBtnOnclickListener(OnClickListener listener) {
        OnClickListener myListener = new GlobalLimitClickOnClickListener(listener, BTN_LIMIT_TIME);
        findViewById(R.id.title_right_area).setOnClickListener(myListener);
    }

}
