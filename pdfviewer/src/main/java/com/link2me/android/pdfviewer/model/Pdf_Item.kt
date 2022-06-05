package com.link2me.android.pdfviewer.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

// @Parcelize Annotaion을 data class 위에 추가해주고 뒤에 Parcelable을 implement 시킨다.

@Parcelize
data class Pdf_Item (
    // 결과를 받을 모델 (ArrayList 에 저장하므로 val 로 선언하면 안된다)
    // 서버 SQL의 칼럼명과 일치하게 작성해야 한다.
    var idx: String="",
    var title: String?=null,
    var category: String?=null,
    var pdfurl: String?=null
): Parcelable

// https://developer.android.com/kotlin/parcelize
/*
Parcel 클래스는 직렬화 시 Container 역할을 하는 클래스
안드로이드에서는 프로세스간 통신(IPC)을 위해 Bundle 클래스를 사용하는데,
이러한 Bundle Class는 Map으로 Key와 Value가 있는 형태의 클래스이다.
이러한 Bundle에서 Int나 String 같은 간단한 데이터는 그대로 Value 값으로 넣을 수 있지만,
Java의 POJO나 Beans 혹은 Kotlin의 data class와 같은 것들은 내부에 많은 데이터가 들어 있기 때문에 Map에 Value로 입력하는 것이 어렵다.
Android에서는 더 효율적으로 데이터를 전달하기 위해 리플렉션을 사용하지 않는 Parcelable Interface를 사용하여 Parcel(꾸러미)를 만들어 Bundle(Map)에 넣어준다.

 */
