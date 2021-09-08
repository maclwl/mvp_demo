package com.taidii.diibot.module.login.presenter;

import android.text.TextUtils;
import com.taidii.diibot.R;
import com.taidii.diibot.app.Constant;
import com.taidii.diibot.app.GlobalParams;
import com.taidii.diibot.base.mvp.BasePresenter;
import com.taidii.diibot.entity.school.CenterTypeBean;
import com.taidii.diibot.module.login.conract.LoginContract;
import com.taidii.diibot.module.login.model.LoginModel;
import com.taidii.diibot.net.ResultCallback;
import com.taidii.diibot.utils.SharePrefUtils;

public class LoginPresenter extends BasePresenter<LoginContract.Model,LoginContract.View> implements LoginContract.Presenter{

    @Override
    protected LoginContract.Model createModel() {
        return new LoginModel();
    }

    @Override
    public void getCenterType(String username,String password) {
        if (TextUtils.isEmpty(username)){
            getView().showMessage(R.string.hint_name);
            return;
        }
        if (TextUtils.isEmpty(password)){
            getView().showMessage(R.string.hint_psw);
            return;
        }
        getModel().getCenterType(username,password,new ResultCallback<CenterTypeBean>() {
            @Override
            public void onStart() {
                getView().showLoading();
            }

            @Override
            public void onSuccess(CenterTypeBean centerTypeBean) {
                /*status*/
                switch (centerTypeBean.getStatus()){
                    case 0:
                        //用户名或密码错误
                        getView().showMessage(R.string.error_name_psw);
                        break;
                    default:
                        /*登录成功，保存用户名密码*/
                        SharePrefUtils.saveString(Constant.USERNAME, username);
                        SharePrefUtils.saveString(Constant.PASSWORD, password);
                        GlobalParams.username = username;
                        GlobalParams.password = password;
                        //判断center_type（2是机构，0是学校）
                        switch (centerTypeBean.getCenter_type()){
                            case 2:
                                //跳转机构首页
                                getView().jumpToOrganMain();
                                break;
                            default:
                                //跳转学校首页
                                getView().jumpToSchoolMain();
                                break;
                        }
                        break;
                }
            }

            @Override
            public void onFinish() {
                getView().hideLoading();
            }
        });
    }

}
