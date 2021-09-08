package com.taidii.diibot.module.organ_main.contract;

import com.taidii.diibot.base.mvp.IModel;
import com.taidii.diibot.base.mvp.IView;
import com.taidii.diibot.entity.organ.OrganDetailBean;
import com.taidii.diibot.entity.organ.OrganSignResponse;
import com.taidii.diibot.entity.organ.OrganStudentSignBody;
import com.taidii.diibot.entity.school.PictureInputResponse;
import com.taidii.diibot.net.ResultCallback;

public class OrganDetailContract {

    public interface View extends IView {

        void OrganDetail(OrganDetailBean organDetailBean);

        void SignBack(OrganSignResponse organSignResponse);
    }

    public interface Model extends IModel {

        void getOrganDetail(int student_id,boolean is_child,ResultCallback<OrganDetailBean> callBack);

        void studentSignRequest(OrganStudentSignBody organStudentSignBody, ResultCallback<OrganSignResponse> callBack);

        void organPictureInput(int studentId,int id,String date,String fileName,ResultCallback<PictureInputResponse> callBack);
    }

    public interface Presenter{

        void getOrganDetail(int student_id,int is_test_child);

        void studentSignRequest(OrganStudentSignBody organStudentSignBody);

        void organPictureInput(int studentId,int id,String date,String fileName);
    }
}
