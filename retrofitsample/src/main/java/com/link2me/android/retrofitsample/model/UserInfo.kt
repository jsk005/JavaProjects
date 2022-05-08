package com.link2me.android.retrofitsample.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserInfo (
        val userNM: String = "",
        val mobileNO: String = "",
        val profileImg: String = ""
): Parcelable