package com.link2me.android.recyclerview.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Address_Item (
    // 결과를 받을 모델 (ArrayList 에 저장하므로 val 로 선언하면 안된다)
    // 서버 SQL의 칼럼명과 일치하게 작성해야 한다.
    var idx: String="",
    var userNM: String="",
    var mobileNO: String?=null,
    var telNO: String?=null,
    var photo: String?=null,
    var checkBoxState: Boolean
): Parcelable