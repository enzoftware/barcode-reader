package com.projects.enzoftware.barcodereader

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
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
    private val DATABASE_NAME = "barcode_database"
    private val DATABASE_VERSION = 1

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

        if (barcodeList.size() > 0){
            val thisCode = barcodeList.valueAt(0)
            barcodeResult.text = thisCode.rawValue

            alert("Hi your code message is ${thisCode.rawValue} , you want to save it?"){
                yesButton {
                    saveToDB(thisCode.rawValue)
                }
                noButton  {
                    toast("Sorry :(")
                }
            }.show()

        }else{
            barcodeResult.text = "Codigo de barras no encontrado"
        }
    }

    private fun saveToDB(barcodeCode: String){
        val db: SQLiteDatabase = SampleSqliteDBHelper(this,DATABASE_NAME,null,DATABASE_VERSION).writableDatabase
        val values = ContentValues()
        values.put(SampleSqliteDBHelper.BARCODE_RESULT_CODE,barcodeCode)
        val newRowId = db.insert(SampleSqliteDBHelper.BARCODE_TABLE_NAME, null, values)
        toast("The new row ID is $newRowId ")
    }

}

