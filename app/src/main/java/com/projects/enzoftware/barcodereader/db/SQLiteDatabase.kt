package com.projects.enzoftware.barcodereader.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

/**
 * Created by enzoftware on 3/2/18.
 */

class SampleSqliteDBHelper(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context, name, factory, version) {

    // TODO : CALL AS SimpleSqliteDBHelper(this,DATABASE_NAME,null,DATABASE_VERSION)

    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL("CREATE TABLE "+ BARCODE_TABLE_NAME +" " +
                        "(" + BARCODE_ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                BARCODE_RESULT_CODE + " TEXT" + " )")
        Log.i("SQLITEDB","CREATE SUCCESS")
    }





    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS "+ BARCODE_TABLE_NAME)
        onCreate(db)
    }

    companion object {
        var BARCODE_ID_COLUMN : String = "_id"
        var BARCODE_TABLE_NAME : String = "barcode_table"
        var BARCODE_RESULT_CODE : String = "barcode"
    }

}