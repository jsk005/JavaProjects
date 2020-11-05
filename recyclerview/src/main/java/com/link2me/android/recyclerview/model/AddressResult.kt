package com.link2me.android.recyclerview.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class AddressResult (
    val status: String,
    val message: String="",
    val addrinfo: List<Address_Item>? = null
): Parcelable