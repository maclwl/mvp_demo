package com.taidii.diibot.module.organ_main.contract;

import com.taidii.diibot.base.mvp.IModel;
import com.taidii.diibot.base.mvp.IView;
import com.taidii.diibot.entity.organ.ClassOrderBean;
import com.taidii.diibot.entity.organ.LocalOrganData;
import com.taidii.diibot.entity.organ.MainOrganCollectionModel;
import com.taidii.diibot.entity.organ.StudentsBean;
import com.taidii.diibot.entity.school.DesInfoBean;
import com.taidii.diibot.net.ResultCallback;

import java.util.List;

public class OrganMainContract {

    public interface View extends IView{

        void topBarInfo(String center_name,String center_logo);

        void organCollection(List<MainOrganCollectionModel> classModelList);

        void qrStudent(StudentsBean qrStudent, ClassOrderBean classOrder);

        void signRefreshBack(List<MainOrganCollectionModel> classModelList);
    }

    public interface Model extends IModel{

        void getLocalOrganData(ResultCallback<LocalOrganData> callBack);

        void getDesInfo(ResultCallback<DesInfoBean> callBack);
    }

    public interface Presenter{

        void getLocalOrganData();

        void getDesInfo();

        void decrypt(String result,List<MainOrganCollectionModel> organClassList);

        void signBackRefresh(List<MainOrganCollectionModel> organClassList,StudentsBean studentsBean);
    }

}
