package com.link2me.android.retrofit_mvvm_login.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PasswdResult (
    val status: String = "",
    val message: String = ""
): Parcelable