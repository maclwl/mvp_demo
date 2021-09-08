package com.taidii.diibot.module.school_main.contract;

import com.taidii.diibot.base.mvp.IModel;
import com.taidii.diibot.base.mvp.IView;
import com.taidii.diibot.entity.school.VisitorListBean;
import com.taidii.diibot.entity.school.VisitorsBean;
import com.taidii.diibot.net.ResultCallback;

import java.util.List;

public class VisitorListContract {

    public interface View extends IView {

        void visitorList(List<VisitorsBean> visitorList);
    }

    public interface Model extends IModel {

        void getVisitorList(ResultCallback<VisitorListBean> callBack);
    }

    public interface Presenter {

        void getVisitorList();
    }

}
