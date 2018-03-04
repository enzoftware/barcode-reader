package com.projects.enzoftware.barcodereader

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.projects.enzoftware.barcodereader.R
import com.projects.enzoftware.barcodereader.adapter.RecyclerViewAdapter
import com.projects.enzoftware.barcodereader.db.SampleSqliteDBHelper
import com.projects.enzoftware.barcodereader.model.Barcode

class ListActivity : AppCompatActivity() {

    private val DATABASE_NAME = "barcode_database"
    private val DATABASE_VERSION = 1
    private val barcode_list : ArrayList<Barcode> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        readFromDB()
        printBarcodes(barcode_list)
    }


    private fun readFromDB(){
        val db: SQLiteDatabase = SampleSqliteDBHelper(this, DATABASE_NAME, null, DATABASE_VERSION).readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM barcode_table",null)
        if (cursor.count != 0){
            cursor.moveToFirst()
            do{
                val cgt:String = cursor.getString(0)
                val cgx:String = cursor.getString(1)
                barcode_list.add(Barcode(cgt,cgx))
                Log.i("names",cgt + " || "+ cgx)
            }while (cursor.moveToNext())
            cursor.close()
        }
    }

    private fun printBarcodes(list: ArrayList<Barcode>?){
        val recycler = findViewById<RecyclerView>(R.id.recyclerViewBarcodes)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.hasFixedSize()
        recycler.adapter = RecyclerViewAdapter(this,list)
    }
}
