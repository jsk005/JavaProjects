package com.link2me.android.sqlite.model;

public class Address_Item {
    // PersonData 정보를 담고 있는 객체 생성
    private String idx;
    private String userNM;
    private String mobileNO;
    private String officeNO;
    private String Photo; // 이미지 경로를 String으로 받기 위해서
    boolean checkBoxState;

    public Address_Item() {
    }

    public String getIdx() {
        return idx;
    }

    public void setIdx(String idx) {
        this.idx = idx;
    }

    public String getUserNM() {
        return userNM;
    }

    public void setUserNM(String userNM) {
        this.userNM = userNM;
    }

    public String getMobileNO() {
        return mobileNO;
    }

    public void setMobileNO(String mobileNO) {
        this.mobileNO = mobileNO;
    }

    public String getOfficeNO() {
        return officeNO;
    }

    public void setOfficeNO(String officeNO) {
        this.officeNO = officeNO;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        Photo = photo;
    }

    public boolean isCheckBoxState() {
        return checkBoxState;
    }

    public void setCheckBoxState(boolean checkBoxState) {
        this.checkBoxState = checkBoxState;
    }
}
