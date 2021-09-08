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
import com.taidii.diibot.entity.enums.VisitDateEnum;
import com.taidii.diibot.entity.enums.VisitTimeEnum;

import java.text.SimpleDateFormat;
import java.util.Date;

public class VisitDateTimePickerUtil {

    private TimePickerView pvTime;
    private VisitDateTimeListener visitDateTimeListener;

    public VisitDateTimePickerUtil(VisitDateTimeListener visitDateTimeListener) {
        this.visitDateTimeListener = visitDateTimeListener;
    }

    public interface VisitDateTimeListener{

        void visitInDate(String date, String dateTime);

        void visitOutDate(String date,String dateTime);

        void visitInTime(String time,String dateTime);

        void visitOutTime(String time,String dateTime);
    }

    public void visitDatePick(Context context, VisitDateEnum type) {

        pvTime = new TimePickerBuilder(context, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                switch (type){
                    case VISIT_DATE_IN:
                        visitDateTimeListener.visitInDate(getDate(date),getDateTime(date));
                        break;
                    case VISIT_DATE_OUT:
                        visitDateTimeListener.visitOutDate(getDate(date),getDateTime(date));
                        break;
                }
            }
        })
                .setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                    @Override
                    public void onTimeSelectChanged(Date date) { }
                })
                .setType(new boolean[]{true, true, true, false, false, false})
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

    public void visitTimePick(Context context, VisitTimeEnum type){
        pvTime = new TimePickerBuilder(context, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                switch (type){
                    case VISIT_TIME_IN:
                        visitDateTimeListener.visitInTime(getTime(date),getDateTime(date));
                        break;
                    case VISIT_TIME_OUT:
                        visitDateTimeListener.visitOutTime(getTime(date),getDateTime(date));
                        break;
                }
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

    private String getDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
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
