package com.taidii.diibot.module.arc_face.contract;

import com.taidii.diibot.base.mvp.IModel;
import com.taidii.diibot.base.mvp.IView;
import com.taidii.diibot.entity.school.GuardiansBean;
import com.taidii.diibot.entity.school.MainSchoolCollectionModel;
import com.taidii.diibot.entity.school.MainSchoolContentModel;
import com.taidii.diibot.entity.school.QrCodeStudentCollection;
import com.taidii.diibot.entity.school.SchoolMainEnum;
import com.taidii.diibot.module.arc_face.faceserver.CompareResult;

import java.util.List;

public class ArcFaceContract {

    public interface View extends IView{

        void faceResultSingStudent(List<QrCodeStudentCollection> modelResultList);

        void faceResultSignStaff(SchoolMainEnum type, MainSchoolContentModel mainSchoolContentModel);
    }

    public interface Model extends IModel{

    }

    public interface Presenter{

        void mateFace(CompareResult compareResult, List<MainSchoolCollectionModel> allCollectionList,List<GuardiansBean> guardianList);
    }

}
