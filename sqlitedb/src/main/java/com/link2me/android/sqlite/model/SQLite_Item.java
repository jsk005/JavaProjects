package com.link2me.android.sqlite.model;

public class SQLite_Item {
    private String idx; // 서버 idx 칼럼
    private String userNM; // 성명
    private String mobileNO; // 휴대폰 번호
    private String telNO; // 사무실 번호
    private String Team; // 팀(소속)
    private String Mission; // 담당업무
    private String Position; // 직위
    private String Photo; // 사진
    private String Status; // 서버 데이터와 동기화를 위한 체크 필드

    public SQLite_Item() {
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

    public String getTelNO() {
        return telNO;
    }

    public void setTelNO(String telNO) {
        this.telNO = telNO;
    }

    public String getTeam() {
        return Team;
    }

    public void setTeam(String team) {
        Team = team;
    }

    public String getMission() {
        return Mission;
    }

    public void setMission(String mission) {
        Mission = mission;
    }

    public String getPosition() {
        return Position;
    }

    public void setPosition(String position) {
        Position = position;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        Photo = photo;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        this.Status = status;
    }
}
