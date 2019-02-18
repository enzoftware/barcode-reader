package com.projects.enzoftware.barcodereader.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.projects.enzoftware.barcodereader.R
import mehdi.sakout.aboutpage.AboutPage
import mehdi.sakout.aboutpage.Element


/**
 * A simple [Fragment] subclass.
 */
class AboutFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_about, container, false)

        val versionElement = Element()
        versionElement.title = "Version 1.0.0"

        val aboutPage = AboutPage(activity)
                .isRTL(false)
                .setDescription("BarcodeEntity Reader is a simple android app for reading barcodes from products and store in a SQLite3 DB, for educational purpose")
                .setImage(R.drawable.eyesopen)
                .addItem(versionElement)
                .addGroup("Connect with me")
                .addEmail("lizama.enzo@gmail.com","Contact me on Gmail")
                .addWebsite("https://www.gitshowcase.com/enzoftware","Visit my website")
                .addFacebook("enzo.lizamaparedes","Follow me on Facebook")
                .addTwitter("enzoftware","Follow me on Twitter")
                .addGitHub("enzoftware","Follow me on Github")
                .create()

        val layout:ViewGroup = view!!.findViewById(R.id.about_container)
        layout.addView(aboutPage)
        return view
    }

}// Required empty public constructor
