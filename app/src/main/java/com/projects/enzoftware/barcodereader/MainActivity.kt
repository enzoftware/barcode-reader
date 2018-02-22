package com.projects.enzoftware.barcodereader

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.util.Log
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val captureCode = 1578


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
            imgCodeResult.setImageBitmap(bitmap)
            Log.i("Camarita","Picture taken")
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
}
