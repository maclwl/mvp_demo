package com.taidii.diibot.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.taidii.diibot.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SignDateTimePickerUtil {

    private TimePickerView pvTime;
    private SignDateTimeListener signDateTimeListener;

    public SignDateTimePickerUtil(SignDateTimeListener signDateTimeListener) {
        this.signDateTimeListener = signDateTimeListener;
    }

    public interface SignDateTimeListener{

        void signDateTime(String time,String dateTime);
    }

    public void signTimePick(Context context){
        pvTime = new TimePickerBuilder(context, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                signDateTimeListener.signDateTime(getTime(date),getDateTime(date));
            }
        })
                .setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                    @Override
                    public void onTimeSelectChanged(Date date) { }
                })
                .setType(new boolean[]{false, false, false, true, true, false})
                .isDialog(true) //默认设置false ，内部实现将DecorView 作为它的父控件。
                .setLayoutRes(R.layout.picker_date, new CustomListener() {

                    @Override
                    public void customLayout(View v) {
                        Button btnCancel = v.findViewById(R.id.btn_cancel);
                        Button btnConfirm = v.findViewById(R.id.btn_confirm);
                        btnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvTime.dismiss();
                            }
                        });
                        btnConfirm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvTime.returnData();
                                pvTime.dismiss();
                            }
                        });
                    }
                })
                .setItemVisibleCount(3) //若设置偶数，实际值会加1（比如设置6，则最大可见条目为7）
                .setLineSpacingMultiplier(2.0f)
                .isAlphaGradient(true)
                .build();

        Dialog mDialog = pvTime.getDialog();
        if (mDialog != null) {

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.CENTER);
            params.leftMargin = 0;
            params.rightMargin = 0;
            pvTime.getDialogContainerLayout().setLayoutParams(params);
            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_scale_anim);//修改动画样式
                dialogWindow.setGravity(Gravity.CENTER);//改成Bottom,底部显示
                dialogWindow.setDimAmount(0.3f);
            }
        }
        pvTime.show();
    }

    private String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        return format.format(date);
    }

    private String getDateTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

}
