package com.georgishukov.financetracker.model

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by Georgi Shukov on 25.5.2021 г..
 */
@Parcelize
@SuppressLint("ParcelCreator")
data class User( var id: Long,
                 var username: String,
                 var password: String) : Parcelable