package com.projects.enzoftware.barcodereader.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.projects.enzoftware.barcodereader.R
import com.projects.enzoftware.barcodereader.adapter.RecyclerViewAdapter
import com.projects.enzoftware.barcodereader.model.Barcode
import com.projects.enzoftware.barcodereader.utils.readFromDB


/**
 * A simple [Fragment] subclass.
 * Use the [ScannedFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ScannedFragment : Fragment() {

    private var barcode_list : ArrayList<Barcode> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_scanned, container, false)
        val recycler = view!!.findViewById<RecyclerView>(R.id.recyclerViewBarcodes)
        barcode_list = readFromDB(activity)
        printBarcodes(barcode_list,recycler)
        return view
    }

    private fun printBarcodes(list: ArrayList<Barcode>?, recycler:RecyclerView){
        recycler.layoutManager = LinearLayoutManager(activity)
        recycler.hasFixedSize()
        recycler.adapter = RecyclerViewAdapter(activity,list)
    }

}// Required empty public constructor

/*

listBarLayout.setOnClickListener {
            cleanDB(this)
            toast("It's clean now")
            //barcode_list = readFromDB(this)
            // printBarcodes(barcode_list)
        }

*/