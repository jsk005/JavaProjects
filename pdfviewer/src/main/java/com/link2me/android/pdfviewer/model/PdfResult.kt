package com.link2me.android.pdfviewer.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PdfResult(
    val status: String,
    val message: String = "",
    val pdfinfo: List<Pdf_Item>? = null
): Parcelable
