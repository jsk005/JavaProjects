package com.link2me.android.room_mvvm.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddressResult(
    val status: String,
    val message: String="",
    val addrinfo: List<Address_Item>? = null
): Parcelable
