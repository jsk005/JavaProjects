package com.link2me.android.sqlite.localdb;

import android.provider.BaseColumns;

public final class DBContract {
    // 서버 정보를 파싱하기 위한 변수 선언
    public static final String _RESULTS ="result";
    // 전화 수신
    public static final String _CallingNO = "callingNO";
    public static final String _CallingTime = "received_time";

    private DBContract() {
    }

    public static class Entry implements BaseColumns {
        // BaseColumns 인터페이스를 구현함으로써 내부 클래스는 _ID라고 하는 기본 키 필드를 상속할 수 있다.
        public static final String TABLE_NAME = "PBbook";
        public static final String _IDX = "idx"; // 서버 테이블의 실제 필드명
        public static final String _NAME = "userNM";
        public static final String _MobileNO ="mobileNO";
        public static final String _telNO ="telNO";
        public static final String _Team ="Team";
        public static final String _Mission ="Mission";
        public static final String _Position ="Position";
        public static final String _Photo = "photo"; // 이미지 필드
        public static final String _Status ="status";
    }
    // SQLite의 데이터 타입은 NULL, INTEGER, REAL, TEXT, BLOB 만 지원한다.
}
