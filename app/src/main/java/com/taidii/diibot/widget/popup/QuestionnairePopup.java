package com.taidii.diibot.widget.popup;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.taidii.diibot.R;
import com.taidii.diibot.entity.school.HealthSafeEntryData;
import com.taidii.diibot.entity.school.QuestionnaireModel;
import com.taidii.diibot.module.school_main.adapter.QuestionnaireAdapter;
import com.taidii.diibot.utils.QuestionnaireDataManage;
import java.util.List;

import razerdp.basepopup.BasePopupWindow;

public class QuestionnairePopup extends BasePopupWindow implements View.OnClickListener{

    private Context mContext;
    private RecyclerView questionnaire_list_recycler;
    private QuestionnaireAdapter questionnaireAdapter;
    private QuestionnairePopupListener questionnairePopupListener;
    private HealthSafeEntryData healthSafeEntryData = new HealthSafeEntryData();//上传信息
    private List<QuestionnaireModel> modelList = QuestionnaireDataManage.getQuestionnaireModelList();

    public QuestionnairePopup(Context context,QuestionnairePopupListener questionnairePopupListener) {
        super(context);
        mContext = context;
        this.questionnairePopupListener = questionnairePopupListener;
        setPopupGravity(Gravity.CENTER);
        initEvent();
    }

    public interface QuestionnairePopupListener{

        void questionnaireResult(HealthSafeEntryData healthSafeEntryData);
    }

    public void setSafeEntryData(HealthSafeEntryData healthSafeEntryData){
        this.healthSafeEntryData = healthSafeEntryData;
        /*赋值网络状态*/
        for (QuestionnaireModel model : modelList){
            if (model.getKey().equals("have_fever")){
                model.setCheck(healthSafeEntryData.isHave_fever());
            }
            if (model.getKey().equals("cough")){
                model.setCheck(healthSafeEntryData.isCough());
            }
            if (model.getKey().equals("sore_throat")){
                model.setCheck(healthSafeEntryData.isSore_throat());
            }
            if (model.getKey().equals("runny_nose")){
                model.setCheck(healthSafeEntryData.isRunny_nose());
            }
            if (model.getKey().equals("shortness_breath")){
                model.setCheck(healthSafeEntryData.isShortness_breath());
            }
            if (model.getKey().equals("sense_smell")){
                model.setCheck(healthSafeEntryData.isSense_smell());
            }
            if (model.getKey().equals("unwell")){
                model.setCheck(healthSafeEntryData.isUnwell());
                model.setContent(healthSafeEntryData.getUnwell_content());
            }
            if (model.getKey().equals("adult_trouble")){
                model.setCheck(healthSafeEntryData.isAdult_trouble());
            }
        }
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getTranslateVerticalAnimation(1f, 0f, 500);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getTranslateVerticalAnimation(0f, 1f, 500);
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_questionnaire);
    }

    private void initEvent(){
        findViewById(R.id.btn_confirm).setOnClickListener(this);
        questionnaire_list_recycler = findViewById(R.id.questionnaire_list_recycler);
        initAdapter();
    }

    private void initAdapter(){
        questionnaireAdapter = new QuestionnaireAdapter();
        questionnaire_list_recycler.setLayoutManager(new LinearLayoutManager(mContext));
        questionnaire_list_recycler.setAdapter(questionnaireAdapter);
        questionnaireAdapter.setDataList(modelList);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_confirm:
                for (QuestionnaireModel model : modelList){
                    if (model.getKey().equals("have_fever")){
                        if (model.isCheck()){
                            healthSafeEntryData.setHave_fever(true);
                        }
                    }
                    if (model.getKey().equals("cough")){
                        if (model.isCheck()){
                            healthSafeEntryData.setCough(true);
                        }
                    }
                    if (model.getKey().equals("sore_throat")){
                        if (model.isCheck()){
                            healthSafeEntryData.setSore_throat(true);
                        }
                    }
                    if (model.getKey().equals("runny_nose")){
                        if (model.isCheck()){
                            healthSafeEntryData.setRunny_nose(true);
                        }
                    }
                    if (model.getKey().equals("shortness_breath")){
                        if (model.isCheck()){
                            healthSafeEntryData.setShortness_breath(true);
                        }
                    }
                    if (model.getKey().equals("sense_smell")){
                        if (model.isCheck()){
                            healthSafeEntryData.setRunny_nose(true);
                        }
                    }
                    if (model.getKey().equals("unwell")){
                        if (model.isCheck()){
                            healthSafeEntryData.setUnwell(true);
                            healthSafeEntryData.setUnwell_content(model.getContent());
                        }
                    }
                    if (model.getKey().equals("adult_trouble")){
                        if (model.isCheck()){
                            healthSafeEntryData.setAdult_trouble(true);
                        }
                    }
                }
                questionnairePopupListener.questionnaireResult(healthSafeEntryData);
                dismiss();
                break;
        }
    }

}
