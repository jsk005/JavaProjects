package com.link2me.android.retrofitsample.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LoginResult (
    val status: String = "",
    val message: String = "",
    val userinfo: UserInfo? = null
): Parcelable