package com.example.model

import android.os.Parcel
import android.os.Parcelable

data class ParcelableData(var item: String?) : Parcelable {

    override fun toString(): String {
        return "아이템: $item"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(item)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ParcelableData> {
        const val serialVersionUID = 124L

        override fun createFromParcel(parcel: Parcel): ParcelableData {
            val s = parcel.readString()
            return ParcelableData(s)
        }

        override fun newArray(size: Int): Array<ParcelableData?> {
            return arrayOfNulls(size)
        }
    }
}