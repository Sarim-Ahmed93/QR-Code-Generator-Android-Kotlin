package com.example.qrcodegenerator

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.content.PermissionChecker.checkSelfPermission
import kotlinx.android.synthetic.main.activity_main.*
import net.glxn.qrgen.core.scheme.VCard
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileOutputStream
import java.time.Duration

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var qrImage : Bitmap? = null
    val EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_text.setOnClickListener(this)
        btn_vCard.setOnClickListener(this)
        btn_save.setOnClickListener(this)
        btn_generateQR.setOnClickListener(this)
        //Check for storage permission

        if (!checkPermissionForExternalStorage()) {
            requestPermissionForExternalStorage()
        }
    }

    override fun onClick(v: View?) {
        when(v?.id)
        {
            R.id.btn_text->
            {
                input_text.visibility = View.VISIBLE
                layout_vCard.visibility = View.GONE
                btn_generateQR.visibility = View.VISIBLE
            }
            R.id.btn_vCard->
            {
                input_text.visibility = View.GONE
                layout_vCard.visibility = View.VISIBLE
                btn_generateQR.visibility = View.VISIBLE

            }
            R.id.btn_generateQR->
            {
                if(layout_vCard.visibility == View.VISIBLE) {

                    if(input_name.text.toString().isNullOrEmpty() && input_email.text.toString().isNullOrEmpty()
                        && input_address.text.toString().isNullOrEmpty() && input_phoneNumber.text.toString().isNullOrEmpty()
                        && input_website.text.toString().isNullOrEmpty())
                    {
                        Toast.makeText(applicationContext,"All fields cannot be empty" , Toast.LENGTH_SHORT).show()
                    }
                    else
                    {
                        generateQRCode()
                    }
                }
                else if(input_text.visibility == View.VISIBLE) {
                    if(!input_text.text.toString().isNullOrEmpty())
                    {
                        generateQRCode()
                    }
                    else
                    {
                        input_text.setError("This field is required")
                    }
                }
            }
            R.id.btn_save->
            {
                if (!checkPermissionForExternalStorage()) {
                    Toast.makeText(this, "External Storage permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show()
                }
                else
                {
                    if(qrImage != null){saveImage(qrImage!!)}
                }
            }
        }
    }
    //Function for Generating QR code
    fun generateQRCode()
    {
        if(layout_vCard.visibility == View.VISIBLE)
        {
            val vCard = VCard(input_name.text.toString())
                .setEmail(input_email.text.toString())
                .setAddress(input_address.text.toString())
                .setPhoneNumber(input_phoneNumber.text.toString())
                .setWebsite(input_website.text.toString())
            qrImage =
                net.glxn.qrgen.android.QRCode.from(vCard).bitmap()
            if(qrImage != null)
            {
                imageView_qrCode.setImageBitmap(qrImage)
                btn_save.visibility = View.VISIBLE
            }
        }
        else if(input_text.visibility == View.VISIBLE)
        {
            qrImage = net.glxn.qrgen.android.QRCode.from(input_text.text.toString()).bitmap()
            if(qrImage != null)
            {
                imageView_qrCode.setImageBitmap(qrImage)
                btn_save.visibility = View.VISIBLE
            }
        }
    }
    //function for requesting storage access
    fun requestPermissionForExternalStorage() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(this, "External Storage permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf<String>(Manifest.permission.WRITE_EXTERNAL_STORAGE), EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE)
        }
    }
    //fuunction for checking storage permission
    fun checkPermissionForExternalStorage(): Boolean {

        val result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true
        } else {
            return false
        }
    }
    //funtion for saving image into gallery
    fun saveImage(image: Bitmap): String {
        var savedImagePath: String? = null

        var imageFileName = "QR" + getTimeStamp() + ".jpg"
        var storageDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "/QRGenerator")
        var success = true
        if (!storageDir.exists()) {
            success = storageDir.mkdirs()
        }
        if (success) {
            var imageFile = File(storageDir, imageFileName)
            savedImagePath = imageFile.getAbsolutePath()
            try {
                var fOut = FileOutputStream(imageFile)
                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
                fOut.close()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }

            // Add the image to the system gallery
            var mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            var f = File(savedImagePath)
            var contentUri = Uri.fromFile(f)
            mediaScanIntent.setData(contentUri)
            sendBroadcast(mediaScanIntent)
            Toast.makeText(this,"QR Image saved into folder: QRGenerator in Gallery",Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this,"ERROR SAVING IMAGE",Toast.LENGTH_SHORT).show()
        }
        return savedImagePath!!
    }


    fun getTimeStamp(): String? {
        val tsLong = System.currentTimeMillis() / 1000
        val ts = tsLong.toString()

        return ts
    }
}