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


/**
 * A simple [Fragment] subclass.
 * Use the [ScannedFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ScannedFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_scanned, container, false)
    }

    private fun printBarcodes(list: ArrayList<Barcode>?){
        val recycler = view!!.findViewById<RecyclerView>(R.id.recyclerViewBarcodes)
        recycler.layoutManager = LinearLayoutManager(activity)
        recycler.hasFixedSize()
        recycler.adapter = RecyclerViewAdapter(activity,list)
    }

}// Required empty public constructor
