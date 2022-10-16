package com.link2me.android.room_mvvm.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

/***
 * 어노테이션을 허용함으로써 클래수 변수를 테이블 열로, 메서드는 SQL문으로 연결시켜 준다.
 * 따라서 테이블 및 열 이름을 별도로 유지할 필요가 없으며, 추가, 삭제, 변경, 쿼리를 하는
 * SQL문도 별도로 유지하지 않아도 된다.
 */

/***
 * @Ignore 어노테이션을 지정하면 테이블에 저장하지 않는 필드를 나타낼 수 있다.
 * SQLite 에서 지원되는 기본 데이터 타입은 Boolean, String, integer, long, double 만 사용할 수 있다.
 */

/***
 * SQLite DB가 boolean 변수를 지원하지 않기 때문에 에러가 발생하여 Address_Item 클래스에 Entity를 적용할 수 없다.
 * 그래서 flag 라는 필드를 별도로 만들어서 매핑처리하는 방법으로 코드를 구현했다.
 * 하지만 boolean 처리가 단순히 false 로만 서버에서 넘어오기 때문에 이 필드는 삭제하는 걸 검토할 필요가 있다.
 * 그렇게 되면 간단하게 쉽게 해결될 수 있고 코드도 좀 더 simple 해질 수 있다.
 */

@Entity(tableName = "contactInfo_table")
@Parcelize
data class Contact_Item (
    @PrimaryKey(autoGenerate = true)
    var idx: Int, // 서버 idx 칼럼으로 primaryKey로 사용하는 필드는 Int 형으로 해야 한다. Retrofit 에서 자동 인식함.
    var userNM: String,  // 성명
    var mobileNO: String,  // 휴대폰 번호
    var telNO: String? = null, // 사무실 번호
    var team: String? = null,   // 팀(소속)
    var mission: String? = null, // 담당업무
    var position: String? = null, // 직위
    var photo: String? = null,  // 이미지 경로를 String으로 받기 위해서
    var flag: String = "0"
):Parcelable

/***
 * SQLite DB에는 Boolena 타입이 없기 때문에 String 또는 Integer 로 저장해야 한다.
 * String flag = item.getCheckBoxState()== true ? "1":"0";
 * flag 칼럼은 굳이 추가하지 않아도 될 것 같다.
 */