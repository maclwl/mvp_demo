package com.taidii.diibot.widget.popup;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.EditText;

import com.taidii.diibot.R;

import razerdp.basepopup.BasePopupWindow;

public class SearchPopup extends BasePopupWindow implements View.OnClickListener{

    private EditText edit_search;
    private SearchPopupListener searchPopupListener;

    public SearchPopup(Context context,SearchPopupListener searchPopupListener) {
        super(context);
        this.searchPopupListener = searchPopupListener;
        setPopupGravity(Gravity.CENTER);
        initEvent();
    }

    public interface SearchPopupListener{

        void search(String keyword);
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getDefaultScaleAnimation(true);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getDefaultScaleAnimation(false);
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_search);
    }

    private void initEvent(){
        edit_search = findViewById(R.id.edit_search);
        findViewById(R.id.ll_close).setOnClickListener(this);
        findViewById(R.id.im_confirm).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_close:
                dismiss();
                break;
            case R.id.im_confirm:
                searchPopupListener.search(edit_search.getText().toString());
                dismiss();
                break;
        }
    }
}
