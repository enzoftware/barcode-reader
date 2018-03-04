package com.projects.enzoftware.barcodereader

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.projects.enzoftware.barcodereader.adapter.RecyclerViewAdapter
import com.projects.enzoftware.barcodereader.db.SampleSqliteDBHelper
import com.projects.enzoftware.barcodereader.model.Barcode
import com.projects.enzoftware.barcodereader.utils.readFromDB

class ListActivity : AppCompatActivity() {
    private var barcode_list : ArrayList<Barcode> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        barcode_list = readFromDB(this)
        printBarcodes(barcode_list)
    }


    private fun printBarcodes(list: ArrayList<Barcode>?){
        val recycler = findViewById<RecyclerView>(R.id.recyclerViewBarcodes)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.hasFixedSize()
        recycler.adapter = RecyclerViewAdapter(this,list)
    }
}
