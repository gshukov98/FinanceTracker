package com.georgishukov.financetracker.model

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import java.sql.Timestamp
import java.util.*

/**
 * Created by Georgi Shukov on 16.5.2021 г..
 */

    data class Cost( var id: Int,
                        var type: String,
                        var description: String,
                        var price: Double,
                        var timestamp: Date): Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString().toString(),
            parcel.readString().toString(),
            parcel.readDouble(),
            TODO("timestamp")) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(type)
        parcel.writeString(description)
        parcel.writeDouble(price)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Cost> {
        override fun createFromParcel(parcel: Parcel): Cost {
            return Cost(parcel)
        }

        override fun newArray(size: Int): Array<Cost?> {
            return arrayOfNulls(size)
        }
    }
}
