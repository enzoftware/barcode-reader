package com.projects.enzoftware.barcodereader.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.projects.enzoftware.barcodereader.R
import com.projects.enzoftware.barcodereader.db.BarcodeDao
import com.projects.enzoftware.barcodereader.db.BarcodeRoomDatabase
import com.projects.enzoftware.barcodereader.model.Barcode
import kotlinx.android.synthetic.main.card_view_barcode.view.*

/**
 * Created by enzoftware on 3/4/18.
 */


class RecyclerViewAdapter (var context: Context, var list: ArrayList<Barcode>?): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        (holder as Item).bindData(list!![position],context)
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.card_view_barcode,parent,false)
        return Item(v)
    }

    class Item(itemView:View): RecyclerView.ViewHolder(itemView){
        fun bindData(_list: Barcode, context: Context){
            itemView.barcode_code.text = _list.code
            itemView.btnDeleteThis.setOnClickListener {
                Toast.makeText(context,"Deleted item with ID ${_list.id}",Toast.LENGTH_SHORT).show()
                val barcodeDao : BarcodeDao = BarcodeRoomDatabase.getInstance(context).barcode()
                barcodeDao.deleteBarcode(_list)
            }
        }
    }

}