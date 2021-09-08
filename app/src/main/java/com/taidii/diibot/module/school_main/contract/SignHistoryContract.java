package com.taidii.diibot.module.school_main.contract;

import com.taidii.diibot.base.mvp.IModel;
import com.taidii.diibot.base.mvp.IView;
import com.taidii.diibot.entity.school.DeleteHistoryResponse;
import com.taidii.diibot.entity.school.SignHistoryBean;
import com.taidii.diibot.entity.school.SignHistoryDataModel;
import com.taidii.diibot.net.ResultCallback;

import java.util.List;

public class SignHistoryContract {

    public interface View extends IView{

        void signHistoryList(List<SignHistoryDataModel> signHistoryList);

        void refreshHistoryList(List<SignHistoryDataModel> signHistoryList);

        void deleteSuccess();
    }

    public interface Model extends IModel {

        void getStudentHistory(int studentId, String fromDate, String toDate, ResultCallback<SignHistoryBean> callBack);

        void deleteSignHistory(int id,int type,ResultCallback<DeleteHistoryResponse> callBack);
    }

    public interface Presenter {

        void getStudentHistory(int studentId, String fromDate, String toDate);

        void deleteSignHistory(int id,int type);

        void refreshHistory(int position,List<SignHistoryDataModel> signHistoryList);
    }

}
