package com.projects.enzoftware.barcodereader

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.util.SparseArray
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.projects.enzoftware.barcodereader.utils.readFromDB
import com.projects.enzoftware.barcodereader.utils.requestPermission
import com.projects.enzoftware.barcodereader.utils.saveToDB
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.toast
import org.jetbrains.anko.yesButton
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private val captureCode = 1578
    private var pictureImagePath = ""

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestPermission(this,android.Manifest.permission.CAMERA)
        requestPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

        btnCameraRequest.setOnClickListener {
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val imageFileName = timeStamp + ".jpg"
            val storageDir : File = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            pictureImagePath = storageDir.absolutePath + "/" + imageFileName
            val file = File(pictureImagePath)
            val outputFileUri: Uri = Uri.fromFile(file)
            val takePictureIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,outputFileUri)
            startActivityForResult(takePictureIntent,captureCode)
        }

        btnAppBarLayout.setOnClickListener {
            readFromDB(this)
            startActivity(Intent(this,ListActivity::class.java))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == captureCode && resultCode == Activity.RESULT_OK){

            val imgFile = File(pictureImagePath)
            if (imgFile.exists()){
                val bitmapResult = BitmapFactory.decodeFile(imgFile.absolutePath)
                imgCodeResult.setImageBitmap(bitmapResult)
                decodeBarcode(bitmapResult)
            }

        }
    }

    private fun decodeBarcode(barcodeImage : Bitmap){
        val detector = BarcodeDetector.Builder(applicationContext)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build()
        if (!detector.isOperational){
            barcodeResult.text = R.string.not_setup_detector.toString()
        }

        val frame = Frame.Builder().setBitmap(barcodeImage).build()
        val barcodeList : SparseArray<Barcode> = detector.detect(frame)

        if (barcodeList.size() > 0){
            val thisCode = barcodeList.valueAt(0)
            barcodeResult.text = thisCode.rawValue

            alert("Hey, tu codigo de barras es ${thisCode.rawValue} , quisieras guardarlo?"){
                yesButton {
                    saveToDB(thisCode.rawValue,this@MainActivity)
                }
                noButton  {
                    toast("Sorry :(")
                }
            }.show()

        }else{
            barcodeResult.text = "Codigo de barras no encontrado"
        }
    }

}

