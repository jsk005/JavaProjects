package com.link2me.android.sqlite.localdb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ContactDBHelper extends SQLiteOpenHelper {
    private final String TAG = this.getClass().getSimpleName();

    // 먼저 db 파일을 만들어야 한다. db 파일에 관련 테이블들을 생성 및 저장
    private static final int DATABASE_VERSION = 1; // 데이터베이스의 버전. 스키마가 변경될 때 숫자를 올린다.
    private static final String DATABASE_NAME = "orgChart.db"; // 상속을 금지하기 위해 final 키워드 사용

    // 테이블을 생성하는 쿼리
    private static final String SQL_CREATE_ENTRIES =
            "create table "+ ContactContract.Entry.TABLE_NAME + " (" +
            ContactContract.Entry._ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"+
            ContactContract.Entry._IDX + " TEXT not null unique," +
            ContactContract.Entry._NAME +" TEXT not null," +
            ContactContract.Entry._MobileNO + " TEXT not null," +
            ContactContract.Entry._telNO + " TEXT," +
            ContactContract.Entry._Team + " TEXT," +
            ContactContract.Entry._Mission + " TEXT," +
            ContactContract.Entry._Position + " TEXT," +
            ContactContract.Entry._Photo + " TEXT," +
            ContactContract.Entry._Status + " TEXT);";

    // SQLite의 데이터 타입은 NULL, INTEGER, REAL, TEXT, BLOB 만 지원한다.
    // DB 생성 위치 : /data/data/<application-package-name>/databases/<database-name>

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ContactContract.Entry.TABLE_NAME;

    // db 헬퍼는 싱글톤 패턴으로 구현하는 것이 좋다.
    // 싱글톤 패턴은 프로그램 내에서 객체가 1개로 고정되게 하는 패턴.
    private static ContactDBHelper sInstance = null;

    // 싱글톤 패턴을 구현할 때, 주로 메소드를 getInstance 로 명명한다.
    // 여러 곳에서 동시에 db 를 열면 동기화 문제가 생길 수 있기 때문에 synchronized 키워드를 이용한다.
    public static synchronized ContactDBHelper getInstance(Context context) {
        if(sInstance == null) {  // 객체가 없을 경우에만 객체를 생성한다.
            sInstance = new ContactDBHelper(context);
        }
        return sInstance; // 객체가 이미 존재할 경우, 기존 객체를 리턴.
    }

    // 싱글톤 패턴 구현 시, 해당 클래스의 생성자는 private 로 선언하여 외부에서의 직접 접근을 막아야 한다.
    private ContactDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // db.execSQL(String query) 는 입력한 쿼리문을 실행하는 메소드.
        db.execSQL(SQL_CREATE_ENTRIES);
        Log.v(TAG,"DB Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // onUpgrade 콜백 메서드는 DB의 스키마가 변경되어 DB 버전이 올라갔을 때 호출된다.
        // DB의 스키마가 변경되면 DB의 버전을 올려야 한다.
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db); //새로 생성하기
    }

}
