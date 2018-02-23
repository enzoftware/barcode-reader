package com.projects.enzoftware.barcodereader

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
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

class MainActivity : AppCompatActivity() {

    private val captureCode = 1578
    private var bmp : Bitmap ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestPermission(this,android.Manifest.permission.CAMERA)

        btnCameraRequest.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(packageManager) != null){
                startActivityForResult(takePictureIntent, captureCode)
            }
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


        //decodeBarcode()
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

    private fun decodeBarcode(){

        //val thisCode = barcodeList.valueAt(0)

    }

}
