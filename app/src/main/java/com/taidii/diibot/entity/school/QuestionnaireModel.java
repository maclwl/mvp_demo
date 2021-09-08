package com.taidii.diibot.entity.school;

public class QuestionnaireModel {

    private boolean isCheck;//是否选中
    private boolean isHasSpread;//选项是否可以展开
    private boolean isSpread;//是否展开
    private String key;
    private String name;
    private String content;//unwell选项中的输入内容

    public QuestionnaireModel(boolean isCheck, boolean isHasSpread, boolean isSpread, String key, String name, String content) {
        this.isCheck = isCheck;
        this.isHasSpread = isHasSpread;
        this.isSpread = isSpread;
        this.key = key;
        this.name = name;
        this.content = content;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public boolean isHasSpread() {
        return isHasSpread;
    }

    public void setHasSpread(boolean hasSpread) {
        isHasSpread = hasSpread;
    }

    public boolean isSpread() {
        return isSpread;
    }

    public void setSpread(boolean spread) {
        isSpread = spread;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
