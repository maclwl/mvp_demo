package com.taidii.diibot.module.arc_face.contract;

import com.taidii.diibot.base.mvp.IModel;
import com.taidii.diibot.base.mvp.IView;
import com.taidii.diibot.entity.face.CloudFaceResponse;
import com.taidii.diibot.entity.face.TokenInfo;
import com.taidii.diibot.entity.school.GuardiansBean;
import com.taidii.diibot.entity.school.MainSchoolCollectionModel;
import com.taidii.diibot.net.ResultCallback;

import java.util.List;

public class FaceSynchronizationContract {

    public interface View extends IView{

        void refreshChangeList(List<MainSchoolCollectionModel> allCollectionList);

        void progress(int progress);

        void initProgress(int max);
    }

    public interface Model extends IModel{

        void getToken(ResultCallback<TokenInfo> callBack);

        void getCloudFaceList(String token,ResultCallback<CloudFaceResponse> callBack);
    }

    public interface Presenter{

        void initFaceList(List<MainSchoolCollectionModel> allCollectionList,List<GuardiansBean> guardianList);

        void getToken(List<MainSchoolCollectionModel> allCollectionList,List<GuardiansBean> allGuardianList);

        void getCloudFaceList(String token,List<MainSchoolCollectionModel> allCollectionList,List<GuardiansBean> allGuardianList);
    }

}
