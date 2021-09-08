package com.taidii.diibot.utils;

import com.taidii.diibot.R;
import com.taidii.diibot.app.BaseApplication;
import com.taidii.diibot.entity.school.HealthCollectionModel;
import com.taidii.diibot.entity.school.HealthContentModel;

import java.util.ArrayList;
import java.util.List;

public class HealthDataManage {

    /*整体本地列表数据*/
    public static final List<HealthCollectionModel> getHealthCollectionList(){

        List<HealthCollectionModel> collectionModelList = new ArrayList<>();

        /*眼*/
        HealthCollectionModel healthEye = new HealthCollectionModel();
        healthEye.setKey(BaseApplication.instance.getResources().getString(R.string.eye));
        List<HealthContentModel> eyeList = new ArrayList<>();
        eyeList.add(new HealthContentModel(BaseApplication.instance.getResources().getString(R.string.eyes_discharge),false,"Eyes discharge"));
        eyeList.add(new HealthContentModel(BaseApplication.instance.getResources().getString(R.string.eyes_swollen),false,"Eyes swollen"));
        eyeList.add(new HealthContentModel(BaseApplication.instance.getResources().getString(R.string.eyes_redness),false,"Eyes redness"));
        healthEye.setContentModelList(eyeList);
        /*口*/
        HealthCollectionModel healthMouth = new HealthCollectionModel();
        healthMouth.setKey(BaseApplication.instance.getResources().getString(R.string.mouth));
        List<HealthContentModel> mouthList = new ArrayList<>();
        mouthList.add(new HealthContentModel(BaseApplication.instance.getResources().getString(R.string.ulcers),false,"Ulcers on Mouth"));
        mouthList.add(new HealthContentModel(BaseApplication.instance.getResources().getString(R.string.red_spots),false,"Red spots on Mouth"));
        mouthList.add(new HealthContentModel(BaseApplication.instance.getResources().getString(R.string.hfmd),false,"HFMD on Mouth"));
        healthMouth.setContentModelList(mouthList);
        /*手*/
        HealthCollectionModel healthHand = new HealthCollectionModel();
        healthHand.setKey(BaseApplication.instance.getResources().getString(R.string.hand));
        List<HealthContentModel> handList = new ArrayList<>();
        handList.add(new HealthContentModel(BaseApplication.instance.getResources().getString(R.string.insect_bites),false,"Insect bites on Hand"));
        handList.add(new HealthContentModel(BaseApplication.instance.getResources().getString(R.string.scratches),false,"Scratches on Hand"));
        handList.add(new HealthContentModel(BaseApplication.instance.getResources().getString(R.string.cuts),false,"Cuts on Hand"));
        handList.add(new HealthContentModel(BaseApplication.instance.getResources().getString(R.string.bruises),false,"Bruises on Hand"));
        handList.add(new HealthContentModel(BaseApplication.instance.getResources().getString(R.string.rashes),false,"Rashes on Hand"));
        handList.add(new HealthContentModel(BaseApplication.instance.getResources().getString(R.string.hfmd),false,"HFMD on Hand"));
        healthHand.setContentModelList(handList);
        /*臀*/
        HealthCollectionModel healthButtock = new HealthCollectionModel();
        healthButtock.setKey(BaseApplication.instance.getResources().getString(R.string.buttocks));
        List<HealthContentModel> buttockList = new ArrayList<>();
        buttockList.add(new HealthContentModel(BaseApplication.instance.getResources().getString(R.string.insect_bites),false,"Insect bites on Buttock"));
        buttockList.add(new HealthContentModel(BaseApplication.instance.getResources().getString(R.string.scratches),false,"Scratches on Buttock"));
        buttockList.add(new HealthContentModel(BaseApplication.instance.getResources().getString(R.string.cuts),false,"Cuts on Buttock"));
        buttockList.add(new HealthContentModel(BaseApplication.instance.getResources().getString(R.string.bruises),false,"Bruises on Buttock"));
        buttockList.add(new HealthContentModel(BaseApplication.instance.getResources().getString(R.string.rashes),false,"Rashes on Buttock"));
        healthButtock.setContentModelList(buttockList);
        /*疾病*/
        HealthCollectionModel healthGeneral = new HealthCollectionModel();
        healthGeneral.setKey(BaseApplication.instance.getResources().getString(R.string.disease));
        List<HealthContentModel> generalList = new ArrayList<>();
        generalList.add(new HealthContentModel(BaseApplication.instance.getResources().getString(R.string.fever),false,"Fever"));
        generalList.add(new HealthContentModel(BaseApplication.instance.getResources().getString(R.string.cold),false,"Cold"));
        generalList.add(new HealthContentModel(BaseApplication.instance.getResources().getString(R.string.cough),false,"Cough"));
        generalList.add(new HealthContentModel(BaseApplication.instance.getResources().getString(R.string.vomit),false,"Vomit"));
        generalList.add(new HealthContentModel(BaseApplication.instance.getResources().getString(R.string.diarrhoea),false,"Diarrhoea"));
        generalList.add(new HealthContentModel(BaseApplication.instance.getResources().getString(R.string.running_nose),false,"Running nose"));
        generalList.add(new HealthContentModel(BaseApplication.instance.getResources().getString(R.string.chicken_pox),false,"Chicken pox"));
        generalList.add(new HealthContentModel(BaseApplication.instance.getResources().getString(R.string.stomach_flu),false,"Stomach flu"));
        healthGeneral.setContentModelList(generalList);
        /*面*/
        HealthCollectionModel healthFace = new HealthCollectionModel();
        healthFace.setKey(BaseApplication.instance.getResources().getString(R.string.face));
        List<HealthContentModel> faceList = new ArrayList<>();
        faceList.add(new HealthContentModel(BaseApplication.instance.getResources().getString(R.string.insect_bites),false,"Insect bites on Face"));
        faceList.add(new HealthContentModel(BaseApplication.instance.getResources().getString(R.string.scratches),false,"Scratches on Face"));
        faceList.add(new HealthContentModel(BaseApplication.instance.getResources().getString(R.string.cuts),false,"Cuts on Face"));
        faceList.add(new HealthContentModel(BaseApplication.instance.getResources().getString(R.string.bruises),false,"Bruises on Face"));
        faceList.add(new HealthContentModel(BaseApplication.instance.getResources().getString(R.string.rashes),false,"Rashes on Face"));
        healthFace.setContentModelList(faceList);
        /*颈*/
        HealthCollectionModel healthNeck = new HealthCollectionModel();
        healthNeck.setKey(BaseApplication.instance.getResources().getString(R.string.neck));
        List<HealthContentModel> neckList = new ArrayList<>();
        neckList.add(new HealthContentModel(BaseApplication.instance.getResources().getString(R.string.insect_bites),false,"Insect bites on Neck"));
        neckList.add(new HealthContentModel(BaseApplication.instance.getResources().getString(R.string.scratches),false,"Scratches on Neck"));
        neckList.add(new HealthContentModel(BaseApplication.instance.getResources().getString(R.string.cuts),false,"Cuts on Neck"));
        neckList.add(new HealthContentModel(BaseApplication.instance.getResources().getString(R.string.bruises),false,"Bruises on Neck"));
        neckList.add(new HealthContentModel(BaseApplication.instance.getResources().getString(R.string.rashes),false,"Rashes on Neck"));
        healthNeck.setContentModelList(neckList);
        /*胸*/
        HealthCollectionModel healthChest = new HealthCollectionModel();
        healthChest.setKey(BaseApplication.instance.getResources().getString(R.string.chest));
        List<HealthContentModel> chestList = new ArrayList<>();
        chestList.add(new HealthContentModel(BaseApplication.instance.getResources().getString(R.string.insect_bites),false,"Insect bites on Chest"));
        chestList.add(new HealthContentModel(BaseApplication.instance.getResources().getString(R.string.scratches),false,"Scratches on Chest"));
        chestList.add(new HealthContentModel(BaseApplication.instance.getResources().getString(R.string.cuts),false,"Cuts on Chest"));
        chestList.add(new HealthContentModel(BaseApplication.instance.getResources().getString(R.string.bruises),false,"Bruises on Chest"));
        chestList.add(new HealthContentModel(BaseApplication.instance.getResources().getString(R.string.rashes),false,"Rashes on Chest"));
        healthChest.setContentModelList(chestList);
        /*脚*/
        HealthCollectionModel healthFoot = new HealthCollectionModel();
        healthFoot.setKey(BaseApplication.instance.getResources().getString(R.string.foot));
        List<HealthContentModel> footList = new ArrayList<>();
        footList.add(new HealthContentModel(BaseApplication.instance.getResources().getString(R.string.insect_bites),false,"Insect bites on Foot"));
        footList.add(new HealthContentModel(BaseApplication.instance.getResources().getString(R.string.scratches),false,"Scratches on Foot"));
        footList.add(new HealthContentModel(BaseApplication.instance.getResources().getString(R.string.cuts),false,"Cuts on Foot"));
        footList.add(new HealthContentModel(BaseApplication.instance.getResources().getString(R.string.bruises),false,"Bruises on Foot"));
        footList.add(new HealthContentModel(BaseApplication.instance.getResources().getString(R.string.rashes),false,"Rashes on Foot"));
        footList.add(new HealthContentModel(BaseApplication.instance.getResources().getString(R.string.hfmd),false,"HFMD on Foot"));
        healthFoot.setContentModelList(footList);

        collectionModelList.add(healthEye); collectionModelList.add(healthMouth);
        collectionModelList.add(healthHand); collectionModelList.add(healthButtock);
        collectionModelList.add(healthGeneral); collectionModelList.add(healthFace);
        collectionModelList.add(healthNeck); collectionModelList.add(healthChest);
        collectionModelList.add(healthFoot);
        return collectionModelList;
    }

    /*提交common匹配标准*/
    public static final List<String> getCommonMateList(){
        List<String> commonMateList = new ArrayList<>();
        commonMateList.add(BaseApplication.instance.getResources().getString(R.string.chicken_pox));
        commonMateList.add(BaseApplication.instance.getResources().getString(R.string.hfmd));
        commonMateList.add(BaseApplication.instance.getResources().getString(R.string.fever));
        commonMateList.add(BaseApplication.instance.getResources().getString(R.string.cold));
        commonMateList.add(BaseApplication.instance.getResources().getString(R.string.cough));
        commonMateList.add(BaseApplication.instance.getResources().getString(R.string.vomit));
        commonMateList.add(BaseApplication.instance.getResources().getString(R.string.rashes));
        commonMateList.add(BaseApplication.instance.getResources().getString(R.string.ulcers));
        commonMateList.add(BaseApplication.instance.getResources().getString(R.string.scratches));
        commonMateList.add(BaseApplication.instance.getResources().getString(R.string.running_nose));
        commonMateList.add(BaseApplication.instance.getResources().getString(R.string.stomach_flu));
        commonMateList.add(BaseApplication.instance.getResources().getString(R.string.insect_bites));
        commonMateList.add(BaseApplication.instance.getResources().getString(R.string.cuts));
        commonMateList.add(BaseApplication.instance.getResources().getString(R.string.breathing));
        commonMateList.add(BaseApplication.instance.getResources().getString(R.string.with_family));
        commonMateList.add(BaseApplication.instance.getResources().getString(R.string.holiday));
        return commonMateList;
    }
}
