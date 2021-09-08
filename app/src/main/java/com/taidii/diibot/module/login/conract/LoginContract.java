package com.taidii.diibot.module.login.conract;
import com.taidii.diibot.base.mvp.IModel;
import com.taidii.diibot.base.mvp.IView;
import com.taidii.diibot.entity.school.CenterTypeBean;
import com.taidii.diibot.net.ResultCallback;

public class LoginContract {

    public interface View extends IView{

        void jumpToSchoolMain();

        void jumpToOrganMain();
    }

    public interface Model extends IModel {

        void getCenterType(String username,String password,ResultCallback<CenterTypeBean> callBack);
    }

    public interface Presenter {

        void getCenterType(String username,String password);
    }

}
