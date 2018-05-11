package com.midas.mytimeline.core

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.midas.mytimeline.structure.core.DBContract
import com.midas.mytimeline.structure.core.timeline

/**
 * Created by taejun on 2018. 3. 27..
 */
class DBHelper(pContext: Context):SQLiteOpenHelper(pContext, DATABASE_NAME, null, DATABASE_VERSION)
{

    override fun onCreate(db: SQLiteDatabase)
    {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int)
    {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int)
    {
        onUpgrade(db, oldVersion, newVersion)
    }


    //-----------------------------------------------------------------------------------------
    //set Info
    @Throws(SQLiteConstraintException::class)
    fun insertInfo(pInfo: timeline): Boolean
    {
        // Gets the data repository in write mode
        val db = writableDatabase

        // Create a new map of values, where column names are the keys
        val values = ContentValues()
        values.put(DBContract.timelineEntry.title, pInfo.title)
        values.put(DBContract.timelineEntry.content, pInfo.content)
        values.put(DBContract.timelineEntry.img_path, pInfo.img_path)
        values.put(DBContract.timelineEntry.date, pInfo.date)
        values.put(DBContract.timelineEntry.color_code, pInfo.color_code)

        // Insert the new row, returning the primary key value of the new row
        val newRowId = db.insert(DBContract.timelineEntry.tb_name, null, values)
        return true
    }

    //-----------------------------------------------------------------------------------------
    //get Array
    fun getAllData(): ArrayList<timeline>
    {
        val array = ArrayList<timeline>()
        val db = writableDatabase
        var cursor: Cursor? = null
        try
        {
            cursor = db.rawQuery("select * from " + DBContract.timelineEntry.tb_name, null)
        }
        catch (e: SQLiteException)
        {
            db.execSQL(SQL_CREATE_ENTRIES)
            return ArrayList()
        }

        var seq: String
        var title: String
        var content : String
        var img_path : String
        var date:String
        var color_code:String
        if (cursor!!.moveToFirst())
        {
            while (cursor.isAfterLast == false)
            {
                seq = cursor.getString(cursor.getColumnIndex(DBContract.timelineEntry.seq))
                title = cursor.getString(cursor.getColumnIndex(DBContract.timelineEntry.title))
                content = cursor.getString(cursor.getColumnIndex(DBContract.timelineEntry.content))
                img_path = cursor.getString(cursor.getColumnIndex(DBContract.timelineEntry.img_path))
                date = cursor.getString(cursor.getColumnIndex(DBContract.timelineEntry.date))
                color_code = cursor.getString(cursor.getColumnIndex(DBContract.timelineEntry.color_code))

                array.add(timeline(seq, title, content, img_path, date, color_code))
                cursor.moveToNext()
            }
        }
        return array
    }

    //-----------------------------------------------------------------------------------------
    //delete info
    @Throws(SQLiteConstraintException::class)
    fun deleteInfo(seq: String): Boolean
    {
        // Gets the data repository in write mode
        val db = writableDatabase
        // Define 'where' part of query.
        val selection = DBContract.timelineEntry.seq + " LIKE ?"
        // Specify arguments in placeholder order.
        val selectionArgs = arrayOf(seq)
        // Issue SQL statement.
        db.delete(DBContract.timelineEntry.tb_name, selection, selectionArgs)
        return true
    }
    //-----------------------------------------------------------------------------------------
    //update
    fun updateInfo(pInfo: timeline)
    {
        // Gets the data repository in write mode
        val db = writableDatabase

        // Create a new map of values, where column names are the keys
        val values = ContentValues()
        values.put(DBContract.timelineEntry.title, pInfo.title)
        values.put(DBContract.timelineEntry.content, pInfo.content)
        values.put(DBContract.timelineEntry.img_path, pInfo.img_path)
        values.put(DBContract.timelineEntry.date, pInfo.date)
        values.put(DBContract.timelineEntry.color_code, pInfo.color_code)

        //public int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
        // Insert the new row, returning the primary key value of the new row
        val newRowId = db.update(DBContract.timelineEntry.tb_name, values, "seq=?", arrayOf(pInfo.seq))
    }

    /*
     @Throws(SQLiteConstraintException::class)
    fun deleteUser(userid: String): Boolean {
        // Gets the data repository in write mode
        val db = writableDatabase
        // Define 'where' part of query.
        val selection = DBContract.UserEntry.COLUMN_USER_ID + " LIKE ?"
        // Specify arguments in placeholder order.
        val selectionArgs = arrayOf(userid)
        // Issue SQL statement.
        db.delete(DBContract.UserEntry.TABLE_NAME, selection, selectionArgs)

        return true
    }

    fun readUser(userid: String): ArrayList<UserModel> {
        val users = ArrayList<UserModel>()
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select * from " + DBContract.UserEntry.TABLE_NAME + " WHERE " + DBContract.UserEntry.COLUMN_USER_ID + "='" + userid + "'", null)
        } catch (e: SQLiteException) {
            // if table not yet present, create it
            db.execSQL(SQL_CREATE_ENTRIES)
            return ArrayList()
        }

        var name: String
        var age: String
        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                name = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.COLUMN_NAME))
                age = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.COLUMN_AGE))

                users.add(UserModel(userid, name, age))
                cursor.moveToNext()
            }
        }
        return users
    }
     */

    companion object
    {
        // If you change the database schema, you must increment the database version.
        val DATABASE_VERSION = 1
        val DATABASE_NAME = "mydb.db"

        private val SQL_CREATE_ENTRIES =
                "CREATE TABLE " + DBContract.timelineEntry.tb_name + " (" +
                        DBContract.timelineEntry.seq + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        DBContract.timelineEntry.title + " TEXT," +
                        DBContract.timelineEntry.content + " TEXT," +
                        DBContract.timelineEntry.img_path + " TEXT," +
                        DBContract.timelineEntry.date + " TEXT," +
                        DBContract.timelineEntry.color_code + " TEXT)"

        private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + DBContract.timelineEntry.tb_name
    }

}