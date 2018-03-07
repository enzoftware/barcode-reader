package com.projects.enzoftware.barcodereader

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.projects.enzoftware.barcodereader.adapter.RecyclerViewAdapter
import com.projects.enzoftware.barcodereader.model.Barcode
import com.projects.enzoftware.barcodereader.utils.cleanDB
import com.projects.enzoftware.barcodereader.utils.readFromDB
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.app_bar_list.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.toast
import org.jetbrains.anko.yesButton

class ListActivity : AppCompatActivity() {
    private var barcode_list : ArrayList<Barcode> = ArrayList()
    private var textOfClean :String ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        barcode_list = readFromDB(this)
        printBarcodes(barcode_list)

        listBarLayout.setOnClickListener {
            cleanDB(this)
            toast("It's clean now")
            barcode_list = readFromDB(this)
            printBarcodes(barcode_list)
        }
    }


    private fun printBarcodes(list: ArrayList<Barcode>?){
        val recycler = findViewById<RecyclerView>(R.id.recyclerViewBarcodes)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.hasFixedSize()
        recycler.adapter = RecyclerViewAdapter(this,list)
    }

}
