package com.link2me.android.retrofitsample.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class VersionResult (
        val version: String
): Parcelable