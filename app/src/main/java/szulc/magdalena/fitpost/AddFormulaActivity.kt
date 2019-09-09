package szulc.magdalena.fitpost

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_add_formula.*
import szulc.magdalena.fitpost.services.UpdateIntentService
import java.io.File
import java.io.FileOutputStream
import java.util.*

class AddFormulaActivity : AppCompatActivity() {

    var imagePath: String? = ""
    var imageBitmapToSend:Bitmap?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_formula)


        // add picture
        addImageButton.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                        this@AddFormulaActivity,
                        arrayOf(Manifest.permission.CAMERA),
                        1
                    )
                }

            }
            if (ActivityCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                ActivityCompat.requestPermissions(
                    this@AddFormulaActivity,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 102
                )

            } else {
                takePhotoByCamera()
            }
        }
        //send message
        postButton.setOnClickListener {
            sendMessage()
            val intentBack = Intent(this, MainActivity::class.java)
            startActivity(intentBack)
        }
    }

    private fun takePhotoByCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, 104)

    }

    private fun sendMessage() {
        val edit = findViewById<EditText>(R.id.editText)
        val message = editText.text.toString()
        Log.d("MASTODON", "message:$message")
        if(imageBitmapToSend==null) {
            UpdateIntentService.startActionSend(this, message)
            //UpdateIntentService.startActionUpdate(this,message)
        }else{
            UpdateIntentService.startActionSendWithImage(this, message,imageBitmapToSend!!)
            captured_image.setImageBitmap(null)
        }
        edit.text.clear()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.d("MASTODON","request:$requestCode result: $resultCode")
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 104) {
                imageBitmapToSend = data?.extras!!.get("data") as Bitmap
                saveImage(imageBitmapToSend!!)
                captured_image.setImageBitmap(imageBitmapToSend)

            }

        } else {
            Log.d("MASTODON", "resultCode:$resultCode")
        }

    }

    private fun saveImage(capturedBitmap: Bitmap) {

        val root = Environment.getExternalStorageDirectory().toString()
        val myDir = File("$root/capture_photo")
        myDir.mkdirs()
        val generator = Random()
        var n = 10000
        n = generator.nextInt(n)
        val Fname = "Image-$n.jpg"
        val file = File(myDir, Fname)
        if (file.exists()) file.delete()
        try {
            val out = FileOutputStream(file)
            capturedBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
            imagePath = file.absolutePath
            out.flush()
            out.close()


        } catch (e: Exception) {
            e.printStackTrace()

        }
    }
}


