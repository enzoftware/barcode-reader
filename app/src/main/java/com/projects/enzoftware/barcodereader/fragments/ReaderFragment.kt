package com.projects.enzoftware.barcodereader.fragments


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector

import com.projects.enzoftware.barcodereader.R
import com.projects.enzoftware.barcodereader.utils.saveToDB
import kotlinx.android.synthetic.main.fragment_reader.*
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.longToast
import org.jetbrains.anko.support.v4.toast
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class ReaderFragment : Fragment() {

    private val captureCode = 1578
    private var pictureImagePath = ""

    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater!!.inflate(R.layout.fragment_reader, container, false)
        val btnRequest = view!!.findViewById<Button>(R.id.btnCameraRequest)

        btnRequest.setOnClickListener {
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val imageFileName = "$timeStamp.jpg"
            val storageDir: File = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            pictureImagePath = storageDir.absolutePath + "/" + imageFileName
            val file = File(pictureImagePath)
            val outputFileUri: Uri = Uri.fromFile(file)
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri)
            startActivityForResult(takePictureIntent, captureCode)
        }
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == captureCode && resultCode == Activity.RESULT_OK){

            val imgFile = File(pictureImagePath)
            if (imgFile.exists()){
                val bitmapResult = BitmapFactory.decodeFile(imgFile.absolutePath)
                decodeBarcode(bitmapResult)
            }

        }
    }


    private fun decodeBarcode(barcodeImage : Bitmap){
        val detector = BarcodeDetector.Builder(activity)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build()
        if (!detector.isOperational){
            longToast(R.string.not_setup_detector)
        }

        val frame = Frame.Builder().setBitmap(barcodeImage).build()
        val barcodeList : SparseArray<Barcode> = detector.detect(frame)

        if (barcodeList.size() > 0){
            val thisCode = barcodeList.valueAt(0)
            alert("Hey, tu codigo de barras es ${thisCode.rawValue} , quisieras guardarlo?"){
                yesButton {
                    // TODO : IMPLEMENT SAVE TO DB
                    //saveToDB(thisCode.rawValue,activity)
                }
                noButton  {
                    toast("Sorry :(")
                }
            }.show()

        }else{
            longToast(R.string.barcode_not_found)
        }
    }


}// Required empty public constructor
