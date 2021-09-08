package com.taidii.diibot.utils;

import com.taidii.diibot.R;
import com.taidii.diibot.app.BaseApplication;
import com.taidii.diibot.entity.school.QuestionnaireModel;

import java.util.ArrayList;
import java.util.List;

public class QuestionnaireDataManage {

    public static final List<QuestionnaireModel> getQuestionnaireModelList(){
        List<QuestionnaireModel> modelList = new ArrayList<>();
        QuestionnaireModel model1 = new QuestionnaireModel(false,false,false,"have_fever", BaseApplication.instance.getResources().getString(R.string.have_fever),"");
        QuestionnaireModel model2 = new QuestionnaireModel(false,false,false,"cough", BaseApplication.instance.getResources().getString(R.string.have_cough),"");
        QuestionnaireModel model3 = new QuestionnaireModel(false,false,false,"sore_throat", BaseApplication.instance.getResources().getString(R.string.sore_throat),"");
        QuestionnaireModel model4 = new QuestionnaireModel(false,false,false,"runny_nose", BaseApplication.instance.getResources().getString(R.string.runny_nose),"");
        QuestionnaireModel model5 = new QuestionnaireModel(false,false,false,"shortness_breath", BaseApplication.instance.getResources().getString(R.string.shortness_breath),"");
        QuestionnaireModel model6 = new QuestionnaireModel(false,false,false,"sense_smell", BaseApplication.instance.getResources().getString(R.string.sense_smell),"");
        QuestionnaireModel model7 = new QuestionnaireModel(false,true,false,"unwell", BaseApplication.instance.getResources().getString(R.string.unwell),"");
        QuestionnaireModel model8 = new QuestionnaireModel(false,true,false,"adult_trouble", BaseApplication.instance.getResources().getString(R.string.adult_trouble),"");
        modelList.add(model1);modelList.add(model2);modelList.add(model3);modelList.add(model4);
        modelList.add(model5);modelList.add(model6);modelList.add(model7);modelList.add(model8);
        return modelList;
    }

}
