package com.link2me.android.rview_ex3.model

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
class Address_Item(
    // 결과를 받을 모델 (ArrayList 에 저장하므로 val 로 선언하면 안된다)
    // 서버 SQL의 칼럼명과 일치하게 작성해야 한다.
    var idx: String="",
    var userNM: String="",
    var mobileNO: String?=null,
    var telNO: String?=null,
    var photo: String?=null,
    var checkBoxState: Boolean
) : Parcelable {
    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as Address_Item
        return idx == that.idx && userNM == that.userNM && mobileNO == that.mobileNO && telNO == that.telNO && photo == that.photo && checkBoxState == that.checkBoxState
    }

    override fun hashCode(): Int {
        return Objects.hash(idx, userNM, mobileNO, telNO, photo, checkBoxState)
    }

    companion object {
        @JvmField
        var itemCallback: DiffUtil.ItemCallback<Address_Item> =
            object : DiffUtil.ItemCallback<Address_Item>() {
                override fun areItemsTheSame(oldItem: Address_Item, newItem: Address_Item): Boolean {
                    return oldItem.idx == newItem.idx
                }

                override fun areContentsTheSame(oldItem: Address_Item, newItem: Address_Item): Boolean {
                    return oldItem == newItem
                }
            }
    }
}