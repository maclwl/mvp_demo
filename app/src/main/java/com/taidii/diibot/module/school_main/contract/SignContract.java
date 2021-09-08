package com.taidii.diibot.module.school_main.contract;

import com.taidii.diibot.base.mvp.IModel;
import com.taidii.diibot.base.mvp.IView;
import com.taidii.diibot.entity.school.GuardiansBean;
import com.taidii.diibot.entity.school.MainSchoolContentModel;
import com.taidii.diibot.entity.school.MateGuardianBean;
import com.taidii.diibot.entity.school.PictureInputResponse;
import com.taidii.diibot.entity.school.StaffSignBody;
import com.taidii.diibot.entity.school.StudentSignBody;
import com.taidii.diibot.entity.school.SignResponse;
import com.taidii.diibot.net.ResultCallback;

import java.util.List;

public class SignContract {

    public interface View extends IView{

        void mateGuardianResult(List<MateGuardianBean> mateGuardianBeanList);

        void checkSelectGuardianResult(List<MateGuardianBean> mateGuardianBeanList);

        void selectGuard(int id);

        void studentSignSuccess(SignResponse signResponse);

        void staffSignSuccess(SignResponse signResponse);
    }

    public interface Model extends IModel {

        void studentSignRequest(StudentSignBody studentSignBody, ResultCallback<SignResponse> callBack);

        void staffSignRequest(StaffSignBody staffSignBody, ResultCallback<SignResponse> callBack);

        void studentPictureInput(int studentId,String date,String fileName,ResultCallback<PictureInputResponse> callBack);

        void staffPictureInput(int staffId,String date,String fileName,ResultCallback<PictureInputResponse> callBack);
    }

    public interface Presenter{

        void mateGuardian(List<GuardiansBean> guardianList, MainSchoolContentModel mainSchoolContentModel);

        void checkSelectGuardian(List<MateGuardianBean> guardianBeanList, int position);

        void studentSignRequest(StudentSignBody studentSignBody);

        void staffSignRequest(StaffSignBody staffSignBody);

        void studentPictureInput(int studentId,String date,String fileName);

        void staffPictureInput(int staffId,String date,String fileName);
    }

}
