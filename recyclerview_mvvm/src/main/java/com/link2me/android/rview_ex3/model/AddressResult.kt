package com.link2me.android.rview_ex3.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddressResult(
    val status: String,
    val message: String="",
    val addrinfo: List<Address_Item>? = null
): Parcelable
