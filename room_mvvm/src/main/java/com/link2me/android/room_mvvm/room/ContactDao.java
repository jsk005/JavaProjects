package com.link2me.android.room_mvvm.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.link2me.android.room_mvvm.model.Contact_Item;

import java.util.List;

@Dao
public interface ContactDao {
    /***
     * DAO는 Room 데이터베이스 상호 작용을 정의하는데 사용되는 클래스이다.
     * 데이터베이스에 포함된 테이블이 여럿일 때는 테이블당 하나씩 DAO 클래스를 두는 것이 가장 좋다.
     * @Insert 를 2개 하면 에러가 발생한다.
     * onConflict = OnConflictStrategy.REPLACE 를 안하면 에러가 발생한다.
     */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Contact_Item> items);

    // 굳이 필요없는 코드
    @Update
    void update(Contact_Item item);

    @Delete
    void delete(Contact_Item item);

    @Query("DELETE FROM contactInfo_table WHERE idx=:idx")
    void deleteByIdx(int idx);

    @Query("DELETE FROM contactInfo_table")
    void deleteAll();

    @Query("SELECT * FROM contactInfo_table ORDER BY idx")
    LiveData<List<Contact_Item>> getAllContactInfo();

}
