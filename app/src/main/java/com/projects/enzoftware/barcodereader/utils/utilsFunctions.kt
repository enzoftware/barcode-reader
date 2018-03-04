package com.projects.enzoftware.barcodereader.utils

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import android.widget.Toast
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.projects.enzoftware.barcodereader.db.SampleSqliteDBHelper
import com.projects.enzoftware.barcodereader.model.Barcode

/**
 * Created by enzoftware on 3/4/18.
 */

private val DATABASE_NAME = "barcode_database"
private val DATABASE_VERSION = 1

fun requestPermission(activity: Activity, permission: String){
    Dexter  .withActivity(activity)
            .withPermission(permission)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    Log.i("Permission",response.toString())
                }

                override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?) {
                    Log.i("Permission",permission.toString())
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    Log.i("Permission",response.toString())
                }
            }).check()
}
 fun saveToDB(barcodeCode: String, context: Context){
    val db: SQLiteDatabase = SampleSqliteDBHelper(context, DATABASE_NAME, null, DATABASE_VERSION).writableDatabase
    val values = ContentValues()
    values.put(SampleSqliteDBHelper.BARCODE_RESULT_CODE,barcodeCode)
    val newRowId = db.insert(SampleSqliteDBHelper.BARCODE_TABLE_NAME, null, values)
    Toast.makeText(context,"The new row ID is $newRowId ",Toast.LENGTH_SHORT).show()
}

fun readFromDB(context: Context): ArrayList<Barcode>{
    val arrayBarcode: ArrayList<Barcode> = ArrayList()
    val db: SQLiteDatabase = SampleSqliteDBHelper(context, DATABASE_NAME, null, DATABASE_VERSION).readableDatabase
    val cursor: Cursor = db.rawQuery("SELECT * FROM barcode_table",null)
    if (cursor.count != 0){
        cursor.moveToFirst()
        do{
            val cgt:String = cursor.getString(0)
            val cgx:String = cursor.getString(1)
            arrayBarcode.add(Barcode(cgt,cgx))
        }while (cursor.moveToNext())
        cursor.close()
    }

    return arrayBarcode
}