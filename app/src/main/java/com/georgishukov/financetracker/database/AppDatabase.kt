package com.georgishukov.financetracker.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.georgishukov.financetracker.utilities.SingletonHolder


/**
 * Created by Georgi Shukov on 25.5.2021 г..
 */
private const val TAG = "AppDatabaseClass"

private const val DATABASE_NAME = "MyCourses.db"
private const val DATABASE_VERSION = 1

//tables
private val CREATE_USERS_TABLE_SQL = """CREATE TABLE ${UserDB.TABLE_NAME} (
        ${UserDB.Columns.ID} INTEGER PRIMARY KEY AUTOINCREMENT,
        ${UserDB.Columns.USERNAME} VARCHAR(70) NOT NULL,
        ${UserDB.Columns.PASSWORD} VARCHAR(100) NOT NULL)""".replaceIndent(" ")

private val CREATE_COSTS_TABLE_SQL = """CREATE TABLE ${CostDB.TABLE_NAME} (
        ${CostDB.Columns.ID} INTEGER PRIMARY KEY AUTOINCREMENT,
        ${CostDB.Columns.TYPE} VARCHAR(50) NOT NULL,
        ${CostDB.Columns.DESCRIPTION} VARCHAR(150),
        ${CostDB.Columns.PRICE} DOUBLE NOT NULL,
        ${CostDB.Columns.TIMESTAMP} DATETIME NOT NULL)""".replaceIndent(" ")

private val CREATE_RELATIONS_TABLE_SQL = """CREATE TABLE ${RelationsDB.TABLE_NAME}( 
    ${RelationsDB.Columns.ID_USER} INTEGER NOT NULL,
    ${RelationsDB.Columns.ID_COST} INTEGER NOT NULL,
    FOREIGN KEY(${RelationsDB.Columns.ID_USER}) REFERENCES ${UserDB.TABLE_NAME}(${UserDB.Columns.ID}) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY(${RelationsDB.Columns.ID_COST}) REFERENCES ${CostDB.TABLE_NAME}(${CostDB.Columns.ID}) ON DELETE CASCADE ON UPDATE CASCADE)""".replaceIndent(" ")

internal class AppDatabase (context : Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_USERS_TABLE_SQL)
        db.execSQL(CREATE_COSTS_TABLE_SQL)
        db.execSQL(CREATE_RELATIONS_TABLE_SQL)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

        if (newVersion > oldVersion) {
            onCreate(db)

        } else {
            // otherwise, create the database
            onCreate(db)
        }
    }

    companion object : SingletonHolder<AppDatabase, Context>(::AppDatabase) //sync database

}