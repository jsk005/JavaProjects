package com.link2me.android.notesample.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.link2me.android.notesample.model.Note;

import java.util.List;

@Dao
public interface NoteDao {
    /***
     * DAO는 Room 데이터베이스 상호 작용을 정의하는데 사용되는 클래스이다.
     * 데이터베이스에 포함된 테이블이 여럿일 때는 테이블당 하나씩 DAO 클래스를 두는 것이 가장 좋다.
     */

    @Insert
    void insert(Note note);

    @Update
    void update(Note note);

    @Delete
    void delete(Note note);

    @Query("DELETE FROM note_table")
    void deleteAllNotes();

    @Query("SELECT * FROM note_table ORDER BY priority DESC")
    LiveData<List<Note>> getAllNotes();

    @Query("SELECT * FROM note_table WHERE title LIKE :searchQuery or description LIKE :searchQuery")
    LiveData<List<Note>> searchNotes(String searchQuery);

    /***
     * SQL 쿼리문 내부에 메서도 매개변수를 사용할 때는 매개변수 이름 앞에 콜론(:)을 붙여 참조할 수 있다.
     * 테이블에서 하나 이상의 엔터티를 반환하는 SELECT 쿼리의 경우, 메서드에 지정된 반환 타입으로 쿼리 결과를
     * 반환하는 코드를 ROOM이 자동 생성한다.
     */
}

