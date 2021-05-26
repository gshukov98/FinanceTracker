package com.georgishukov.financetracker.database

import android.content.ContentUris
import android.net.Uri

/**
 * Created by Georgi Shukov on 16.5.2021 г..
 */
object RelationsDB {

    internal const val TABLE_NAME = "Relations"

    /**
     * The URI to access the table
     */
    val CONTENT_URI: Uri = Uri.withAppendedPath(CONTENT_AUTHORITY_URI, TABLE_NAME)

    const val CONTENT_TYPE = "vnd.android.cursor.dir/vnd.$CONTENT_AUTHORITY.${TABLE_NAME}"

    object Columns {
        const val ID_USER = "User_ID"
        const val ID_COST = "Cost_ID"
    }

    fun getId(uri: Uri):Long{
        return ContentUris.parseId(uri)
    }

    fun buildUriFromId(id:Long): Uri {
        return ContentUris.withAppendedId(CONTENT_URI,id)
    }
}