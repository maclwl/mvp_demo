package com.taidii.diibot.module.school_main.contract;

import com.taidii.diibot.base.mvp.IModel;
import com.taidii.diibot.base.mvp.IView;
import com.taidii.diibot.entity.enums.SingTypeEnum;
import com.taidii.diibot.entity.school.DesInfoBean;
import com.taidii.diibot.entity.school.FastBluetoothModel;
import com.taidii.diibot.entity.school.GuardiansBean;
import com.taidii.diibot.entity.school.HealthCheckCacheDate;
import com.taidii.diibot.entity.school.HealthCheckResponse;
import com.taidii.diibot.entity.school.LocalSchoolData;
import com.taidii.diibot.entity.school.MainSchoolCollectionModel;
import com.taidii.diibot.entity.school.MainSchoolContentModel;
import com.taidii.diibot.entity.school.PictureInputResponse;
import com.taidii.diibot.entity.school.QrCodeStudentCollection;
import com.taidii.diibot.entity.school.SchoolMainEnum;
import com.taidii.diibot.entity.school.SignResponse;
import com.taidii.diibot.entity.school.StaffPictureCacheData;
import com.taidii.diibot.entity.school.StaffSignBody;
import com.taidii.diibot.entity.school.StaffSignCacheData;
import com.taidii.diibot.entity.school.StudentPictureCacheData;
import com.taidii.diibot.entity.school.StudentSignBody;
import com.taidii.diibot.entity.school.StudentSignCacheData;
import com.taidii.diibot.net.ResultCallback;

import java.util.List;

public class SchoolMainContract {

    public interface View extends IView {

        void topBarInfo(String center_name,String center_logo);

        void guardiansList(List<GuardiansBean> guardiansList);

        void schoolCollection(List<MainSchoolCollectionModel> classModelList);

        void signBackRefreshList(List<MainSchoolCollectionModel> signCollectionModelList);

        void signBackAllCollectionModelList(List<MainSchoolCollectionModel> allCollectionList);

        void settingCollectionResult(List<MainSchoolCollectionModel> classModelList);

        void quickSignResult(MainSchoolContentModel mainSchoolContentModel);

        void qrResultSingStudent(List<QrCodeStudentCollection> modelResultList);

        void qrResultSignStaff(SchoolMainEnum type, MainSchoolContentModel mainSchoolContentModel);

        void mateSearchResult(List<MainSchoolCollectionModel> resultModelList,String keyword);

        void showBluetoothListPopup();

        void autoConnectionBluetooth(FastBluetoothModel model);

        void setTitleCount(int totalCount,int signInCount,int signOutCount);

        void hintSignInList(List<MainSchoolCollectionModel> signCollectionModelList);
    }

    public interface Model extends IModel{

        void getLocalSchoolData(String username, String password, ResultCallback<LocalSchoolData> callBack);

        void studentSignRequest(StudentSignBody studentSignBody, ResultCallback<SignResponse> callBack);

        void staffSignRequest(StaffSignBody staffSignBody, ResultCallback<SignResponse> callBack);

        void offLineStudentSign(List<StudentSignCacheData> studentSignCacheList, StudentSignCacheData signCacheData, ResultCallback<SignResponse> callBack);

        void offLineStaffSign(List<StaffSignCacheData> staffSignCacheList, StaffSignCacheData signCacheData, ResultCallback<SignResponse> callBack);

        void offLineStudentPicture(List<StudentPictureCacheData> studentPictureCacheList,StudentPictureCacheData pictureCacheData,ResultCallback<PictureInputResponse> callBack);

        void offLineStaffPicture(List<StaffPictureCacheData> staffPictureCacheList, StaffPictureCacheData pictureCacheData, ResultCallback<PictureInputResponse> callBack);

        void offLineHealthCheck(List<HealthCheckCacheDate> healthCheckCacheList,HealthCheckCacheDate healthCheckCacheDate,ResultCallback<HealthCheckResponse> callBack);

        void getDesInfo(ResultCallback<DesInfoBean> callBack);
    }

    public interface Presenter{

        void getLocalSchoolData(String username,String password);
        void getLocalSchoolDataCount(String username,String password);

        void initSettingList(List<MainSchoolCollectionModel> allCollectionList);

        void removeStaff(List<MainSchoolCollectionModel> allCollectionList);

        void spreadSetting(List<MainSchoolCollectionModel> allCollectionList,boolean isCheck);

        void hideSignInSetting(List<MainSchoolCollectionModel> allCollectionList);

        void quickSign(SchoolMainEnum type, SingTypeEnum signType,MainSchoolContentModel mainSchoolContentModel);

        void signBackRefresh(List<MainSchoolCollectionModel> collectionModelList, int collectionPosition,
                             int contentPosition, MainSchoolContentModel signContentModel,List<MainSchoolCollectionModel> allCollectionList);

        void localOffLineInput();

        void getDesInfo();

        void decrypt(String result,List<MainSchoolCollectionModel> allCollectionList,List<GuardiansBean> guardiansList);

        void mateSearch(String keyword,List<MainSchoolCollectionModel> currentModelList);

        void checkConnectBluetooth(List<FastBluetoothModel> resultModelList);
    }

}
