package com.taidii.diibot.module.arc_face.contract;

import com.taidii.diibot.base.mvp.IModel;
import com.taidii.diibot.base.mvp.IView;
import com.taidii.diibot.entity.school.GuardiansBean;
import com.taidii.diibot.entity.school.MainSchoolContentModel;
import com.taidii.diibot.entity.school.MateGuardianBean;

import java.util.List;

public class FaceInfoDetailContract {

    public interface View extends IView {

        void mateGuardianResult(List<MateGuardianBean> mateGuardianBeanList);
    }

    public interface Model extends IModel {

    }

    public interface Presenter{

        void mateGuardian(List<GuardiansBean> guardianList, MainSchoolContentModel mainSchoolContentModel);
    }

}
