package com.link2me.android.notesample.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/***
 * 어노테이션을 허용함으로써 클래수 변수를 테이블 열로, 메서드는 SQL문으로 연결시켜 준다.
 * 따라서 테이블 및 열 이름을 별도로 유지할 필요가 없으며, 추가, 삭제, 변경, 쿼리를 하는
 * SQL문도 별도로 유지하지 않아도 된다.
 */
@Entity(tableName = "note_table")
public class Note {
    /***
     * @Ignore 어노테이션을 지정하면 테이블에 저장하지 않는 필드를 나타낼 수 있다.
     * SQLite 에서 지원되는 기본 데이터 타입은 Boolean, String, integer, long, double 만 사용할 수 있다.
     */

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;

    private String description;

    private int priority;

    public Note(String title, String description, int priority) {
        this.title = title;
        this.description = description;
        this.priority = priority;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }
}