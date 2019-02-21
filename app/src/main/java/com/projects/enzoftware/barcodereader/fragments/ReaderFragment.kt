package com.projects.enzoftware.barcodereader.fragments


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.projects.enzoftware.barcodereader.R
import com.projects.enzoftware.barcodereader.db.BarcodeDao
import com.projects.enzoftware.barcodereader.db.BarcodeRoomDatabase
import com.projects.enzoftware.barcodereader.model.BarcodeEntity
import org.jetbrains.anko.noButton
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.longToast
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.yesButton
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class ReaderFragment : Fragment() {

    private val captureCode = 1578
    private lateinit var pictureImagePath : String
    private lateinit var barcodeDao : BarcodeDao

    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_reader, container, false)
        val btnRequest = view!!.findViewById<Button>(R.id.btnCameraRequest)
        barcodeDao = BarcodeRoomDatabase.getInstance(ctx).barcode()

        btnRequest.setOnClickListener {

            // For Android N and above.
            val builder : StrictMode.VmPolicy.Builder = StrictMode.VmPolicy.Builder()
            StrictMode.setVmPolicy(builder.build())

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


    private fun decodeBarcode(bitmap : Bitmap){
        val options = FirebaseVisionBarcodeDetectorOptions.Builder()
                .setBarcodeFormats(FirebaseVisionBarcode.FORMAT_ALL_FORMATS)
                .build()
        val detector = FirebaseVision.getInstance().getVisionBarcodeDetector(options)
        val image = FirebaseVisionImage.fromBitmap(bitmap)
        detector.detectInImage(image)
                .addOnSuccessListener {
                    for (firebaseBarcode in it) {

                        when (firebaseBarcode.valueType) {
                            FirebaseVisionBarcode.TYPE_URL -> firebaseBarcode.url
                            FirebaseVisionBarcode.TYPE_CONTACT_INFO -> firebaseBarcode.contactInfo
                            FirebaseVisionBarcode.TYPE_WIFI -> firebaseBarcode.wifi
                        }

                        alert("Hey, tu codigo de barras es ${firebaseBarcode.displayValue} , quisieras guardarlo?"){
                            yesButton {
                                barcodeDao.insertNewBarcode(BarcodeEntity(firebaseBarcode.displayValue!!))
                            }
                            noButton  {
                                toast("Sorry :(")
                            }
                        }.show()
                    }
                }
                .addOnFailureListener {
                    it.printStackTrace()
                    longToast(R.string.barcode_not_found)
                }
                .addOnCompleteListener {
                    toast("Listo!!")
                }
    }
}

