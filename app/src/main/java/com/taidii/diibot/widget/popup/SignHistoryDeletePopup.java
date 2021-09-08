package com.taidii.diibot.widget.popup;

import android.content.Context;

public class SignHistoryDeletePopup extends CustomPopup{

    private HistoryDeleteListener historyDeleteListener;

    public SignHistoryDeletePopup(Context context,HistoryDeleteListener historyDeleteListener) {
        super(context);
        this.historyDeleteListener = historyDeleteListener;
    }

    public interface HistoryDeleteListener{

        void delete();
    }

    @Override
    public void clickConfirm() {
        historyDeleteListener.delete();
    }

}
