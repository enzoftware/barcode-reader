package com.projects.enzoftware.barcodereader

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.SparseArray
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private val captureCode = 1578
    private val galleryCode = 8751

    private var bmp : Bitmap ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestPermission(this,android.Manifest.permission.CAMERA)

        btnCameraRequest.setOnClickListener {


            /*
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(packageManager) != null){
                startActivityForResult(takePictureIntent, captureCode)
            }
            */

            val intentGallery = Intent()
            intentGallery.type = "image/*"
            intentGallery.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intentGallery,"Select picture"),galleryCode)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == captureCode && resultCode == Activity.RESULT_OK){
            val extras: Bundle = data!!.extras
            val bitmap: Bitmap = extras.get("data") as Bitmap
            bmp = Bitmap.createScaledBitmap(bitmap,360,640,false)
            imgCodeResult.setImageBitmap(bmp)
            val detector = BarcodeDetector.Builder(applicationContext)
                    .setBarcodeFormats(Barcode.ALL_FORMATS)
                    .build()
            if (!detector.isOperational){
                Log.i("operation","not working")
            }else{
                Log.i("operation","is working")
            }

            val frame = Frame.Builder().setBitmap(bmp).build()
            val barcodeList : SparseArray<Barcode> = detector.detect(frame)
            Log.i("barcode",barcodeList.size().toString())
        }

        if (requestCode == galleryCode && resultCode == Activity.RESULT_OK && data != null && data.data != null){
            val uri: Uri = data.data

            try {
                val bitmapGallery = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                imgCodeResult.setImageBitmap(bitmapGallery)
                decodeBarcode(bitmapGallery)
            }catch (e : IOException){
                e.printStackTrace()
            }
        }

    }

    private fun requestPermission(activity: Activity, permission: String){
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

    private fun decodeBarcode(barcodeImage : Bitmap){
        val detector = BarcodeDetector.Builder(applicationContext)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build()
        if (!detector.isOperational){
            barcodeResult.text = R.string.not_setup_detector.toString()
        }

        val frame = Frame.Builder().setBitmap(barcodeImage).build()
        val barcodeList : SparseArray<Barcode> = detector.detect(frame)
        //Log.i("barcode",barcodeList.valueAt(0).rawValue)
        if (barcodeList.size() > 0){
            val thisCode = barcodeList.valueAt(0)
            barcodeResult.text = thisCode.rawValue
        }else{
            barcodeResult.text = R.string.barcode_not_found.toString()
        }

    }

}
