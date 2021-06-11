package com.georgishukov.financetracker.model

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.sql.Timestamp
import java.util.*

/**
 * Created by Georgi Shukov on 16.5.2021 г..
 */

@Parcelize
@SuppressLint("ParcelCreator")
data class Cost( var id: Long,
                 var type: String,
                 var description: String,
                 var price: Double,
                 var timestamp: Date) : Parcelable

