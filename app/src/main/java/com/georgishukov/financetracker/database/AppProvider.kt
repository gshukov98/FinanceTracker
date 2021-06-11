package com.georgishukov.financetracker.database

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri
import android.util.Log

/**
 * Created by Georgi Shukov on 16.5.2021 г..
 * This is the ContentProvider child class
 * This is the only class that know about [AppDatabase]
 */


private const val TAG = "AppProvider"

const val CONTENT_AUTHORITY = "com.georgishukov.financetracker.provider"
val CONTENT_AUTHORITY_URI: Uri = Uri.parse("content://$CONTENT_AUTHORITY")

const val USERS = 100
const val USERS_ID = 101

const val COSTS = 200
const val COSTS_ID = 201



class AppProvider: ContentProvider() {
    private val uriMatcher by lazy { buildUriMatcher() }
    
    private fun buildUriMatcher(): UriMatcher {
        Log.d(TAG, "buildUriMatcher starts")
        val matcher = UriMatcher(UriMatcher.NO_MATCH)

        matcher.addURI(CONTENT_AUTHORITY, UserDB.TABLE_NAME, USERS)
        matcher.addURI(CONTENT_AUTHORITY, "${UserDB.TABLE_NAME}/#", USERS_ID)

        matcher.addURI(CONTENT_AUTHORITY, CostDB.TABLE_NAME, COSTS)
        matcher.addURI(CONTENT_AUTHORITY, "${CostDB.TABLE_NAME}/#", COSTS_ID)

        return matcher
    }

    override fun onCreate(): Boolean {
        return true;
    }

    override fun query(
            uri: Uri, projection: Array<out String>?, selection: String?,
            selectionArgs: Array<out String>?, sortOrder: String?
    ): Cursor? {

        val match = uriMatcher.match(uri)

        val queryBuilder = SQLiteQueryBuilder()

        when (match) {

            USERS -> queryBuilder.tables = UserDB.TABLE_NAME
            USERS_ID -> {
                queryBuilder.tables = UserDB.TABLE_NAME
                val queryId = UserDB.getId(uri)
                queryBuilder.appendWhere("${UserDB.Columns.ID} = ")
                queryBuilder.appendWhereEscapeString("$queryId")
            }

            COSTS -> queryBuilder.tables = CostDB.TABLE_NAME
            COSTS_ID -> {
                queryBuilder.tables = CostDB.TABLE_NAME
                val queryId = CostDB.getId(uri)
                queryBuilder.appendWhere("${CostDB.Columns.ID} = ")
                queryBuilder.appendWhereEscapeString("$queryId")
            }

            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }

        val context = context ?: throw NullPointerException("Context can't be null here")
        val db = AppDatabase.getInstance(context).readableDatabase
        val cursor =
                queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder)

        Log.d(TAG, "query: rows in returned cursor = ${cursor.count}")

        return cursor
    }

    override fun getType(uri: Uri): String? {
        return when (uriMatcher.match(uri)) {

            USERS -> UserDB.CONTENT_TYPE
            USERS_ID -> UserDB.CONTENT_ITEM_TYPE

            COSTS -> CostDB.CONTENT_TYPE
            COSTS_ID -> CostDB.CONTENT_ITEM_TYPE

            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri {
        val match = uriMatcher.match(uri)

        val recordId: Long
        val returnUri: Uri

        val context = context ?: throw NullPointerException("Context can't be null here")

        when (match) {
            USERS -> {
                val db = AppDatabase.getInstance(context).writableDatabase
                recordId = db.insert(UserDB.TABLE_NAME, null, values)
                if (recordId != -1L) {
                    returnUri = UserDB.buildUriFromId(recordId)
                } else {
                    throw SQLException("Failed to insert, Uri was $uri")
                }
            }

            COSTS -> {
                val db = AppDatabase.getInstance(context).writableDatabase
                recordId = db.insert(CostDB.TABLE_NAME,null,values)
                if (recordId != -1L) {
                    returnUri = CostDB.buildUriFromId(recordId)
                } else {
                    throw SQLException("Failed to insert, Uri was $uri")
                }
            }

            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
        if (recordId > 0) {
            context.contentResolver?.notifyChange(uri, null)
        }
        return returnUri
    }


    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        val match = uriMatcher.match(uri)

        val count: Int
        var selectionCriteria: String

        val context = context ?: throw NullPointerException("Context can't be null here")

        when (match) {
            USERS -> {
                val db = AppDatabase.getInstance(context).writableDatabase
                count = db.delete(UserDB.TABLE_NAME, selection, selectionArgs)
            }
            USERS_ID -> {
                val db = AppDatabase.getInstance(context).writableDatabase
                val id = UserDB.getId(uri)
                selectionCriteria = "${UserDB.Columns.ID} = $id"

                if (selection != null && selection.isNotEmpty()) {
                    selectionCriteria += "AND ($selection)"
                }
                count = db.delete(UserDB.TABLE_NAME, selectionCriteria, selectionArgs)
            }

            COSTS -> {
                val db = AppDatabase.getInstance(context).writableDatabase
                count = db.delete(CostDB.TABLE_NAME, selection, selectionArgs)
            }
            COSTS_ID -> {
                val db = AppDatabase.getInstance(context).writableDatabase
                val id = CostDB.getId(uri)
                selectionCriteria = "${CostDB.Columns.ID} = $id"

                if (selection != null && selection.isNotEmpty()) {
                    selectionCriteria += "AND ($selection)"
                }
                count = db.delete(CostDB.TABLE_NAME, selectionCriteria, selectionArgs)
            }


            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }

        if (count > 0) {
            context.contentResolver?.notifyChange(uri, null)
        }
        return count
    }

    override fun update(
            uri: Uri, values: ContentValues?, selection: String?,
            selectionArgs: Array<out String>?
    ): Int {
        val match = uriMatcher.match(uri)

        val count: Int
        var selectionCriteria: String

        val context = context ?: throw NullPointerException("Context can't be null here")

        when (match) {
            USERS -> {
                val db = AppDatabase.getInstance(context).writableDatabase
                count = db.update(UserDB.TABLE_NAME, values, selection, selectionArgs)
            }
            USERS_ID -> {
                val db = AppDatabase.getInstance(context).writableDatabase
                val id = UserDB.getId(uri)
                selectionCriteria = "${UserDB.Columns.ID} = $id"

                if (selection != null && selection.isNotEmpty()) {
                    selectionCriteria += "AND ($selection)"
                }
                count = db.update(UserDB.TABLE_NAME, values, selectionCriteria, selectionArgs)
            }

            COSTS -> {
                val db = AppDatabase.getInstance(context).writableDatabase
                count = db.update(CostDB.TABLE_NAME, values, selection, selectionArgs)
            }
            COSTS_ID -> {
                val db = AppDatabase.getInstance(context).writableDatabase
                val id = CostDB.getId(uri)
                selectionCriteria = "${CostDB.Columns.ID} = $id"

                if (selection != null && selection.isNotEmpty()){
                    selectionCriteria += "AND ($selection)"
                }
                count = db.update(CostDB.TABLE_NAME, values, selectionCriteria, selectionArgs)
            }

            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }

        if (count > 0) {
            context.contentResolver?.notifyChange(uri, null)
        }

        return count
    }


}