package com.example.android_camera.camera

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.android_camera.R
import com.google.android.material.button.MaterialButton

const val REQUEST_IMAGE_CAPTURE = 1
const val PICK_IMAGE_FILE = 2

class SelectMethodActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.select_picture_method)
        // 最初にキャンセルされた結果をセットしておくことで、端末の戻るボタンに対応させる
        setResult(Activity.RESULT_CANCELED) // 無くても動く？
        val cancelButton = findViewById<Button>(R.id.cancel_button)

        cancelButton.setOnClickListener{
            finish()
        }

        val cameraButton = findViewById<MaterialButton>(R.id.camera_method)
        val albumButton = findViewById<MaterialButton>(R.id.album_method)

        //カメラに移動
        cameraButton.setOnClickListener {
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                takePictureIntent.resolveActivity(packageManager)?.also {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }

        albumButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "image/*"
            }
            startActivityForResult(intent, PICK_IMAGE_FILE)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode != REQUEST_IMAGE_CAPTURE && requestCode != PICK_IMAGE_FILE){return}

        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                val imageBitmap = data.extras?.get("data") as Bitmap

                val bitmapUtility = BitmapUtility()
                val uri: Uri = bitmapUtility.bitmapToUri(imageBitmap, this)

                val result = Intent()
                result.putExtra("takenPicture", uri)
                setResult(Activity.RESULT_OK, result)
                finish()
            }else {
                data?.data?.also { uri ->
                    val uriImg: Uri = uri
                    val result = Intent()
                    result.putExtra("takenPicture", uriImg)
                    setResult(Activity.RESULT_OK, result)
                    finish()
                }
            }
        }else if(requestCode == Activity.RESULT_CANCELED){
            //TODO: uiUtilityにダイアログ表示フラグメント作って「キャンセルされました」って表示する
        }
    }
}