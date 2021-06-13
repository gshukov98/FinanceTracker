package com.georgishukov.financetracker.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import androidx.annotation.RequiresApi
import com.georgishukov.financetracker.model.Cost
import com.georgishukov.financetracker.model.User
import com.georgishukov.financetracker.utilities.SingletonHolder
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


/**
 * Created by Georgi Shukov on 25.5.2021 г..
 */
private const val TAG = "AppDatabaseClass"

private const val DATABASE_NAME = "MyCourses.db"
private const val DATABASE_VERSION = 5

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
        ${CostDB.Columns.TIMESTAMP} VARCHAR NOT NULL)""".replaceIndent(" ")


public class AppDatabase (context : Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_USERS_TABLE_SQL)
        db.execSQL(CREATE_COSTS_TABLE_SQL)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

        if (newVersion > oldVersion) {
            onCreate(db)

        } else {
            // otherwise, create the database
            onCreate(db)
        }
    }

    fun addUser(user: User) {
        val db = this.writableDatabase
        val values = ContentValues()
        //values.put(UserDB.Columns.ID, user.id)
        values.put(UserDB.Columns.USERNAME, user.username)
        values.put(UserDB.Columns.PASSWORD, user.password)

        db.insert(UserDB.TABLE_NAME, null, values)
        db.close()
    }

    fun addCost(cost: Cost) {
        val db = this.writableDatabase
        val values = ContentValues()
        //values.put(CostDB.Columns.ID, cost.id)
        values.put(CostDB.Columns.TYPE, cost.type)
        values.put(CostDB.Columns.DESCRIPTION, cost.description)
        values.put(CostDB.Columns.TIMESTAMP, cost.timestamp.toString())
        values.put(CostDB.Columns.PRICE, cost.price)

        db.insert(CostDB.TABLE_NAME, null, values)
        db.close()
    }

    fun getUsers():List<User>{
        val usersList: ArrayList<User> = ArrayList<User>()

        val selectQuery =
            "SELECT *FROM Users"

        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val user = User(cursor.getString(0).toLong(),cursor.getString(1),cursor.getString(2))

                usersList.add(user)
            } while (cursor.moveToNext())
        }

        return usersList

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCosts():List<Cost>{
        val costsList: ArrayList<Cost> = ArrayList<Cost>()

        val selectQuery =
            "SELECT *FROM Costs"

        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                //get date from table
                val s =
                    cursor.getString(4)
                val dateFormat = SimpleDateFormat(
                    "yyyy-MMM-dd",
                    Locale.getDefault()
                )
                var date: Date = Date()
                try {
                    /*date = dateFormat.parse(s)*/
                    date = convertToDateViaSqlDate(LocalDate.parse(s, DateTimeFormatter.ISO_DATE))
                } catch (e: ParseException) {
                    // TODO Auto-generated catch block
                    e.printStackTrace()
                }


                val cost = Cost(cursor.getString(0).toLong(),cursor.getString(1),cursor.getString(2),cursor.getDouble(3),date)

                costsList.add(cost)
            } while (cursor.moveToNext())
        }

        return costsList

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun convertToDateViaSqlDate(dateToConvert: LocalDate?): Date {
        return java.sql.Date.valueOf(dateToConvert.toString())
    }

    companion object : SingletonHolder<AppDatabase, Context>(::AppDatabase) //sync database

}