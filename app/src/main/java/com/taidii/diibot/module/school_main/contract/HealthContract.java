package com.taidii.diibot.module.school_main.contract;

import com.taidii.diibot.base.mvp.IModel;
import com.taidii.diibot.base.mvp.IView;
import com.taidii.diibot.entity.school.HealthCheckBody;
import com.taidii.diibot.entity.school.HealthCheckResponse;
import com.taidii.diibot.entity.school.HealthCollectionModel;
import com.taidii.diibot.entity.school.HealthContentModel;
import com.taidii.diibot.entity.school.HealthRegionModel;
import com.taidii.diibot.net.ResultCallback;

import java.util.List;

public class HealthContract {

    public interface View extends IView {

        void localChangeData(List<HealthCollectionModel> collectionModelList);

        void regionButtonChange(HealthRegionModel eyeRegion,HealthRegionModel mouthRegion,HealthRegionModel handRegion,
                                HealthRegionModel buttockRegion,HealthRegionModel generalRegion,HealthRegionModel faceRegion,
                                HealthRegionModel neckRegion,HealthRegionModel chestRegion,HealthRegionModel footRegion);

        void symptomListSetChange(String key,List<HealthContentModel> contentModelList);

        void healthCheckSuccess();
    }

    public interface Model extends IModel {

        void healthRequest(HealthCheckBody healthCheckBody, ResultCallback<HealthCheckResponse> callBack);
    }

    public interface Presenter{

        void initGeneralNoDisease(List<HealthCollectionModel> collectionModelList);

        void initGeneralIsDisease(List<HealthCollectionModel> collectionModelList);

        void clickRegionButton(String key,List<HealthCollectionModel> collectionModelList);

        void diseaseListChange(String key,List<HealthContentModel> currentModelList,List<HealthCollectionModel> collectionModelList);

        void healthCheckRequest(String remark, HealthCheckBody healthCheckBody,List<HealthCollectionModel> collectionModelList);
    }

}
