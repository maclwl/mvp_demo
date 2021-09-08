package com.taidii.diibot.module.school_main.presenter;

import android.text.TextUtils;
import com.taidii.diibot.R;
import com.taidii.diibot.app.BaseApplication;
import com.taidii.diibot.base.mvp.BasePresenter;
import com.taidii.diibot.entity.school.HealthCheckBody;
import com.taidii.diibot.entity.school.HealthCheckResponse;
import com.taidii.diibot.entity.school.HealthCollectionModel;
import com.taidii.diibot.entity.school.HealthContentModel;
import com.taidii.diibot.entity.school.HealthRegionModel;
import com.taidii.diibot.module.school_main.contract.HealthContract;
import com.taidii.diibot.module.school_main.model.HealthModel;
import com.taidii.diibot.net.ResultCallback;
import com.taidii.diibot.utils.HealthDataManage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HealthPresenter extends BasePresenter<HealthContract.Model,HealthContract.View> implements HealthContract.Presenter{

    @Override
    protected HealthContract.Model createModel() {
        return new HealthModel();
    }

    @Override
    public void initGeneralNoDisease(List<HealthCollectionModel> collectionModelList) {
        /*默认部位为疾病（显示疾病列表默认无病症）*/
        for (HealthCollectionModel collectionModel : collectionModelList){
            if (collectionModel.getKey().equals(BaseApplication.instance.getResources().getString(R.string.disease))){
                getView().symptomListSetChange(collectionModel.getKey(),collectionModel.getContentModelList());
            }
        }
        HealthRegionModel generalRegion = new HealthRegionModel();
        generalRegion.setClick(true);
        getView().regionButtonChange(new HealthRegionModel(),new HealthRegionModel(),new HealthRegionModel(),new HealthRegionModel(),generalRegion,
                new HealthRegionModel(),new HealthRegionModel(),new HealthRegionModel(),new HealthRegionModel());
    }

    @Override
    public void initGeneralIsDisease(List<HealthCollectionModel> collectionModelList) {
        /*默认部位为疾病（显示默认病症为发烧）*/
        for (HealthCollectionModel collectionModel : collectionModelList){
            if (collectionModel.getKey().equals(BaseApplication.instance.getResources().getString(R.string.disease))){
                List<HealthContentModel> diseaseList = collectionModel.getContentModelList();
                for (HealthContentModel contentModel : diseaseList){
                    if (contentModel.getName().equals(BaseApplication.instance.getResources().getString(R.string.fever))){
                        contentModel.setCheck(true);
                    }
                }
                getView().symptomListSetChange(collectionModel.getKey(),diseaseList);
            }
        }
        HealthRegionModel generalRegion = new HealthRegionModel();
        generalRegion.setClick(true);
        getView().regionButtonChange(new HealthRegionModel(),new HealthRegionModel(),new HealthRegionModel(),new HealthRegionModel(),generalRegion,
                new HealthRegionModel(),new HealthRegionModel(),new HealthRegionModel(),new HealthRegionModel());
    }

    /*部位监听相关页面逻辑处理*/
    @Override
    public void clickRegionButton(String key, List<HealthCollectionModel> collectionModelList) {
        /*1.选出对应key部位的病症列表 2.查询出各部位的情况（是否疾病）*/
        List<HealthContentModel> eyeList = new ArrayList<>();//眼 -》病症列表
        List<HealthContentModel> mouthList = new ArrayList<>();//口 -》病症列表
        List<HealthContentModel> handList = new ArrayList<>();//手 -》病症列表
        List<HealthContentModel> buttockList = new ArrayList<>();//臀 -》病症列表
        List<HealthContentModel> generalList = new ArrayList<>();//疾病 -》病症列表
        List<HealthContentModel> faceList = new ArrayList<>();//面 -》病症列表
        List<HealthContentModel> neckList = new ArrayList<>();//颈 -》病症列表
        List<HealthContentModel> chestList = new ArrayList<>();//胸 -》病症列表
        List<HealthContentModel> footList = new ArrayList<>();//脚 -》病症列表
        for (HealthCollectionModel collectionModel : collectionModelList){
            if (collectionModel.getKey().equals(key)){
                getView().symptomListSetChange(collectionModel.getKey(),collectionModel.getContentModelList());
            }
            if (collectionModel.getKey().equals(BaseApplication.instance.getResources().getString(R.string.eye))){
                eyeList = collectionModel.getContentModelList();
            }
            if (collectionModel.getKey().equals(BaseApplication.instance.getResources().getString(R.string.mouth))){
                mouthList = collectionModel.getContentModelList();
            }
            if (collectionModel.getKey().equals(BaseApplication.instance.getResources().getString(R.string.hand))){
                handList = collectionModel.getContentModelList();
            }
            if (collectionModel.getKey().equals(BaseApplication.instance.getResources().getString(R.string.buttocks))){
                buttockList = collectionModel.getContentModelList();
            }
            if (collectionModel.getKey().equals(BaseApplication.instance.getResources().getString(R.string.disease))){
                generalList = collectionModel.getContentModelList();
            }
            if (collectionModel.getKey().equals(BaseApplication.instance.getResources().getString(R.string.face))){
                faceList = collectionModel.getContentModelList();
            }
            if (collectionModel.getKey().equals(BaseApplication.instance.getResources().getString(R.string.neck))){
                neckList = collectionModel.getContentModelList();
            }
            if (collectionModel.getKey().equals(BaseApplication.instance.getResources().getString(R.string.chest))){
                chestList = collectionModel.getContentModelList();
            }
            if (collectionModel.getKey().equals(BaseApplication.instance.getResources().getString(R.string.foot))){
                footList = collectionModel.getContentModelList();
            }
        }
        HealthRegionModel eyeRegion = new HealthRegionModel();
        if (key.equals(BaseApplication.instance.getResources().getString(R.string.eye))){
            eyeRegion.setClick(true);
        }else {
            eyeRegion.setClick(false);
        }
        if (eyeList.size()>0){
            for (HealthContentModel model : eyeList){
                if (model.isCheck()){
                    eyeRegion.setDisease(true);
                    break;
                }
            }
        }
        HealthRegionModel mouthRegion = new HealthRegionModel();
        if (key.equals(BaseApplication.instance.getResources().getString(R.string.mouth))){
            mouthRegion.setClick(true);
        }else {
            mouthRegion.setClick(false);
        }
        if (mouthList.size()>0){
            for (HealthContentModel model : mouthList){
                if (model.isCheck()){
                    mouthRegion.setDisease(true);
                    break;
                }
            }
        }
        HealthRegionModel handRegion = new HealthRegionModel();
        if (key.equals(BaseApplication.instance.getResources().getString(R.string.hand))){
            handRegion.setClick(true);
        }else {
            handRegion.setClick(false);
        }
        if (handList.size()>0){
            for (HealthContentModel model : handList){
                if (model.isCheck()){
                    handRegion.setDisease(true);
                    break;
                }
            }
        }
        HealthRegionModel buttockRegion = new HealthRegionModel();
        if (key.equals(BaseApplication.instance.getResources().getString(R.string.buttocks))){
            buttockRegion.setClick(true);
        }else {
            buttockRegion.setClick(false);
        }
        if (buttockList.size()>0){
            for (HealthContentModel model : buttockList){
                if (model.isCheck()){
                    buttockRegion.setDisease(true);
                    break;
                }
            }
        }
        HealthRegionModel generalRegion = new HealthRegionModel();
        if (key.equals(BaseApplication.instance.getResources().getString(R.string.disease))){
            generalRegion.setClick(true);
        }else {
            generalRegion.setClick(false);
        }
        if (generalList.size()>0){
            for (HealthContentModel model : generalList){
                if (model.isCheck()){
                    generalRegion.setDisease(true);
                    break;
                }
            }
        }
        HealthRegionModel faceRegion = new HealthRegionModel();
        if (key.equals(BaseApplication.instance.getResources().getString(R.string.face))){
            faceRegion.setClick(true);
        }else {
            faceRegion.setClick(false);
        }
        if (faceList.size()>0){
            for (HealthContentModel model : faceList){
                if (model.isCheck()){
                    faceRegion.setDisease(true);
                    break;
                }
            }
        }
        HealthRegionModel neckRegion = new HealthRegionModel();
        if (key.equals(BaseApplication.instance.getResources().getString(R.string.neck))){
            neckRegion.setClick(true);
        }else {
            neckRegion.setClick(false);
        }
        if (neckList.size()>0){
            for (HealthContentModel model : neckList){
                if (model.isCheck()){
                    neckRegion.setDisease(true);
                    break;
                }
            }
        }
        HealthRegionModel chestRegion = new HealthRegionModel();
        if (key.equals(BaseApplication.instance.getResources().getString(R.string.chest))){
            chestRegion.setClick(true);
        }else {
            chestRegion.setClick(false);
        }
        if (chestList.size()>0){
            for (HealthContentModel model : chestList){
                if (model.isCheck()){
                    chestRegion.setDisease(true);
                    break;
                }
            }
        }
        HealthRegionModel footRegion = new HealthRegionModel();
        if (key.equals(BaseApplication.instance.getResources().getString(R.string.foot))){
            footRegion.setClick(true);
        }else {
            footRegion.setClick(false);
        }
        if (footList.size()>0){
            for (HealthContentModel model : footList){
                if (model.isCheck()){
                    footRegion.setDisease(true);
                    break;
                }
            }
        }
        getView().regionButtonChange(eyeRegion,mouthRegion,handRegion,buttockRegion,generalRegion,faceRegion,neckRegion,chestRegion,footRegion);
    }

    /*病症列表item点击逻辑处理*/
    @Override
    public void diseaseListChange(String key, List<HealthContentModel> currentModelList, List<HealthCollectionModel> collectionModelList) {
        getView().localChangeData(collectionModelList);
    }

    /*体侧上传请求相关逻辑处理*/
    @Override
    public void healthCheckRequest(String remark, HealthCheckBody healthCheckBody, List<HealthCollectionModel> collectionModelList) {
        /*循环找出病症以及病症详情*/
        Set<String> diseaseSet = new HashSet<>();
        List<String> diseaseDetailList = new ArrayList<>();
        for (HealthCollectionModel model : collectionModelList){
            List<HealthContentModel> contentModelList = model.getContentModelList();
            for (HealthContentModel contentModel : contentModelList){
                if (contentModel.isCheck()){
                    diseaseSet.add(contentModel.getName());
                    diseaseDetailList.add(contentModel.getDetail());
                }
            }
        }
        List<String> diseaseNameList = new ArrayList<>(diseaseSet);
        /*处理remark 与common*/
        if (diseaseNameList.size()>0&&diseaseDetailList.size()>0){
            /*遍历病症详情处理*/
            StringBuilder sb= new StringBuilder();
            for (String s: diseaseDetailList) {
                if (sb.length() > 0){
                    sb.append("；");
                }
                sb.append(s);
            }
            /*判断是否有手动备注  Fever;Cough;Cuts*/
            if (TextUtils.isEmpty(remark)){
                healthCheckBody.setRemarks(sb.toString().trim());
            }else {
                healthCheckBody.setRemarks(remark);
            }
            /*处理common*/
            List<String> commonMateList = HealthDataManage.getCommonMateList();
            List<Integer> mateResult = new ArrayList<>();
            for (String common : commonMateList){
                int result = 0;
                for (String name : diseaseNameList){
                    if (common.equals(name)){
                        result = 1;
                    }
                }
                mateResult.add(result);
            }
            String common = "";
            if (mateResult.size()>0){
                for (Integer integer : mateResult){
                    common = common + integer;
                }
            }
            healthCheckBody.setCommon(common);
        }else {
            if (TextUtils.isEmpty(remark)){
                healthCheckBody.setRemarks("");
            }else {
                healthCheckBody.setRemarks(remark);
            }
            healthCheckBody.setCommon("0000000000000000");
        }
        healthRequest(healthCheckBody);
    }

    private void healthRequest(HealthCheckBody healthCheckBody){
        getModel().healthRequest(healthCheckBody, new ResultCallback<HealthCheckResponse>() {
            @Override
            public void onStart() {
                getView().showLoading();
            }

            @Override
            public void onSuccess(HealthCheckResponse healthCheckResponse) {
                getView().hideLoading();
                switch (healthCheckResponse.getStatusCode()){
                    case 0:

                        break;
                    case 1:
                        getView().healthCheckSuccess();
                        break;
                }
            }

            @Override
            public void onFinish() {
                getView().hideLoading();
            }
        });
    }

}
