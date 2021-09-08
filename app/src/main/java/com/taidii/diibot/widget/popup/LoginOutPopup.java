package com.taidii.diibot.widget.popup;

import android.content.Context;

public class LoginOutPopup extends CustomPopup{

    private LoginOutListener loginOutListener;

    public LoginOutPopup(Context context,LoginOutListener loginOutListener) {
        super(context);
        this.loginOutListener = loginOutListener;
    }

    public interface LoginOutListener{

        void loginOut();
    }

    @Override
    public void clickConfirm() {
        loginOutListener.loginOut();
    }
}
