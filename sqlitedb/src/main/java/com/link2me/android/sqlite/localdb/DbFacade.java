package com.link2me.android.sqlite.localdb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.link2me.android.sqlite.model.SQLite_Item;

import java.util.ArrayList;

public class DbFacade {
    private final String TAG = this.getClass().getSimpleName();
    private DBHelper mHelper;
    private Context mContext;

    public DbFacade(Context context) {
        mHelper = DBHelper.getInstance(context); // Helper 객체 생성
        mContext = context;
    }

    public void InsertData_Phone(String idx, String name, String mobileNO, String officeNO, String Team,
                                 String Mission, String Position, String Photo, String Status) {
        SQLiteDatabase db = mHelper.getWritableDatabase(); // 쓰기 가능한 데이터베이스를 가져와 입력

        // 이름 + 휴대폰번호 기준으로 중복 체크
        String query = String.format("SELECT * FROM %s WHERE %s = '%s' and %s = '%s'",
                DBContract.Entry.TABLE_NAME, DBContract.Entry._NAME, name, DBContract.Entry._MobileNO, mobileNO
                );
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst(); // Cursor를 제일 첫행으로 이동
        if (cursor.getCount() == 0) {  // 중복이 없으면 저장하라.
            ContentValues cv = new ContentValues(); // 객체 생성
            cv.put(DBContract.Entry._IDX, idx);
            cv.put(DBContract.Entry._NAME, name);
            cv.put(DBContract.Entry._MobileNO, mobileNO);
            cv.put(DBContract.Entry._telNO, officeNO);
            cv.put(DBContract.Entry._Team, Team);
            cv.put(DBContract.Entry._Mission, Mission);
            cv.put(DBContract.Entry._Position, Position);
            cv.put(DBContract.Entry._Photo, Photo);
            cv.put(DBContract.Entry._Status, Status);

            db.beginTransaction();  // 대량건수 데이터 입력 처리를 고려
            try {
                long rowId = db.insert(DBContract.Entry.TABLE_NAME, null, cv);
                if (rowId < 0) {
                    throw new SQLException("Fail to Insert");
                }
                db.setTransactionSuccessful();
            } catch (Exception e) {
                Log.i(TAG, e.toString());
            } finally {
                db.endTransaction();
                Log.v(TAG, "DB Inserted " + name + " idx =" + idx);
            }
        }
        cursor.close();
        db.close();
    }

    public void InsertData(String idx, String userNM, String mobileNO, String officeNO, String Team,
                           String Mission, String Position, String Photo, String Status) {
        SQLiteDatabase db = mHelper.getWritableDatabase(); // 쓰기 가능한 데이터베이스를 가져와 입력

        String query = String.format("SELECT idx FROM %s WHERE %s = '%s'", DBContract.Entry.TABLE_NAME, DBContract.Entry._IDX, idx);
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst(); // Cursor를 제일 첫행으로 이동
        if (cursor.getCount() == 0) {  // 중복이 없으면 저장하라.
            ContentValues cv = new ContentValues(); // 객체 생성
            cv.put(DBContract.Entry._IDX, idx);
            cv.put(DBContract.Entry._NAME, userNM);
            cv.put(DBContract.Entry._MobileNO, mobileNO);
            cv.put(DBContract.Entry._telNO, officeNO);
            cv.put(DBContract.Entry._Team, Team);
            cv.put(DBContract.Entry._Mission, Mission);
            cv.put(DBContract.Entry._Position, Position);
            cv.put(DBContract.Entry._Photo, Photo);
            cv.put(DBContract.Entry._Status, Status);
            db.beginTransaction();  // 대량건수 데이터 입력 처리를 고려
            try {
                long rowId = db.insert(DBContract.Entry.TABLE_NAME, null, cv);
                if (rowId < 0) {
                    throw new SQLException("Fail to Insert");
                }
                db.setTransactionSuccessful();
            } catch (Exception e) {
                Log.i(TAG, e.toString());
            } finally {
                db.endTransaction();
                Log.v(TAG, "DB Inserted " + userNM + " uid =" + idx);
            }
        }
        cursor.close();
        db.close();
    }

    /* Get the first row Column_ID from the table */
    public int getFirstId() {
        int idToUpdate = 0;
        String query = String.format("SELECT idx FROM %s LIMIT 1", DBContract.Entry.TABLE_NAME);

        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor res = db.rawQuery(query, null);

        if (null != res && res.getCount() > 0) {
            res.moveToFirst(); // Cursor를 제일 첫행으로 이동
            idToUpdate = res.getInt(0);
        }
        return idToUpdate;
    }

    /* Update the table row with Column_ID - id */
    public boolean updateDB(Integer idx, String name, String mobileNO, String officeNO) {
        Log.i(TAG, "Updating Column_ID : " + idx);
        ContentValues cv = new ContentValues();
        cv.put(DBContract.Entry._NAME, name);
        cv.put(DBContract.Entry._MobileNO, mobileNO);
        cv.put(DBContract.Entry._telNO, officeNO);

        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.update(DBContract.Entry.TABLE_NAME, cv, "idx = ? ", new String[]{Integer.toString(idx)});
        return true;
    }

    public boolean updateDB(String idx, String userNM, String mobileNO, String officeNO, String Team,
                            String Mission, String Position, String Photo, String Status) {
        Log.i(TAG, "Updating Column_ID : " + idx);
        ContentValues cv = new ContentValues();
        cv.put(DBContract.Entry._NAME, userNM);
        cv.put(DBContract.Entry._MobileNO, mobileNO);
        cv.put(DBContract.Entry._telNO, officeNO);
        cv.put(DBContract.Entry._Team, Team);
        cv.put(DBContract.Entry._Mission, Mission);
        cv.put(DBContract.Entry._Position, Position);
        cv.put(DBContract.Entry._Photo, Photo);
        cv.put(DBContract.Entry._Status, Status);

        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.update(DBContract.Entry.TABLE_NAME, cv, "idx = ? ", new String[]{idx});
        return true;
    }

    /* Delete the row with Column_ID - id from the employees table */
    public Integer deleteRow(String idx) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        String selection = DBContract.Entry._IDX + " = ?";
        return db.delete(DBContract.Entry.TABLE_NAME, selection, new String[]{idx});
    }

    public int getTableRowCount() {
        String countQuery = String.format("SELECT * FROM %s", DBContract.Entry.TABLE_NAME);
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        Log.i(TAG, "Total Row : " + cursor.getCount());
        cursor.close();
        return cursor.getCount();
    }

    // SQLiteDB 모든 데이터 ArrayList 에 저장하기
    public ArrayList<SQLite_Item> getAllSQLiteData() {
        ArrayList<SQLite_Item> sqliteDBData = new ArrayList<SQLite_Item>();
        // Select All Query
        String selectQuery = String.format("SELECT * FROM %s", DBContract.Entry.TABLE_NAME);
        SQLiteDatabase db = mHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null)
            if (cursor.moveToFirst()) {
                do {
                    SQLite_Item item = new SQLite_Item();
                    item.setIdx(cursor.getString(1));
                    item.setUserNM(cursor.getString(2));
                    item.setMobileNO(cursor.getString(3));
                    item.setTelNO(cursor.getString(4));
                    item.setTeam(cursor.getString(5));
                    item.setMission(cursor.getString(6));
                    item.setPosition(cursor.getString(7));
                    sqliteDBData.add(item); // ArrayList에 추가
                } while (cursor.moveToNext());
            }

        cursor.close();
        return sqliteDBData; // return ArrayList
    }

    public Cursor LoadSQLiteDBCursor() {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        //db.beginTransaction();
        // Select All Query
        String selectQuery = String.format("SELECT idx,userNM,mobileNO,telNO,Team,Mission,Position,photo,status FROM %s ",
                DBContract.Entry.TABLE_NAME);
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(selectQuery, null);
            // db.query 사용시에는 방법이 다르다.
            // https://developer.android.com/training/data-storage/sqlite?hl=ko#java 참조하면 사용법 나온다.
            //db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //db.endTransaction();
        }
        return cursor;
    }

    public Cursor SelectPhoneNO(String Number) {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Number = Number.replaceAll("[^0-9]", ""); // 숫자를 제외한 문자열 제거
        db.beginTransaction();
        String selectQuery = String.format("SELECT userNM,mobileNO,telNO,Team,Mission,Position FROM %s WHERE %s = '%s' or %s = '%s'",
                DBContract.Entry.TABLE_NAME, DBContract.Entry._MobileNO, Number, DBContract.Entry._telNO, Number
        );
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(selectQuery, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return cursor;
    }

}
