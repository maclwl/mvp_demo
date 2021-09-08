package com.taidii.diibot.entity.school;

import java.io.Serializable;

public class CovidInfoBean implements Serializable {

    private int id;
    private int visitor;
    private boolean have_travel;
    private boolean have_fever;
    private boolean cough;
    private boolean sore_throat;
    private boolean runny_nose;
    private boolean shortness_breath;
    private boolean sense_smell;
    private boolean unwell;
    private String unwell_content;
    private boolean adult_trouble;
    private boolean readed;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVisitor() {
        return visitor;
    }

    public void setVisitor(int visitor) {
        this.visitor = visitor;
    }

    public boolean isHave_travel() {
        return have_travel;
    }

    public void setHave_travel(boolean have_travel) {
        this.have_travel = have_travel;
    }

    public boolean isHave_fever() {
        return have_fever;
    }

    public void setHave_fever(boolean have_fever) {
        this.have_fever = have_fever;
    }

    public boolean isCough() {
        return cough;
    }

    public void setCough(boolean cough) {
        this.cough = cough;
    }

    public boolean isSore_throat() {
        return sore_throat;
    }

    public void setSore_throat(boolean sore_throat) {
        this.sore_throat = sore_throat;
    }

    public boolean isRunny_nose() {
        return runny_nose;
    }

    public void setRunny_nose(boolean runny_nose) {
        this.runny_nose = runny_nose;
    }

    public boolean isShortness_breath() {
        return shortness_breath;
    }

    public void setShortness_breath(boolean shortness_breath) {
        this.shortness_breath = shortness_breath;
    }

    public boolean isSense_smell() {
        return sense_smell;
    }

    public void setSense_smell(boolean sense_smell) {
        this.sense_smell = sense_smell;
    }

    public boolean isUnwell() {
        return unwell;
    }

    public void setUnwell(boolean unwell) {
        this.unwell = unwell;
    }

    public String getUnwell_content() {
        return unwell_content;
    }

    public void setUnwell_content(String unwell_content) {
        this.unwell_content = unwell_content;
    }

    public boolean isAdult_trouble() {
        return adult_trouble;
    }

    public void setAdult_trouble(boolean adult_trouble) {
        this.adult_trouble = adult_trouble;
    }

    public boolean isReaded() {
        return readed;
    }

    public void setReaded(boolean readed) {
        this.readed = readed;
    }
}
