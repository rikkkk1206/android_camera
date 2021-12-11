package com.example.android_camera.camera

import android.app.Activity
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.android_camera.R
import kotlinx.android.synthetic.main.response_image_display.*
import kotlinx.coroutines.*

class CreatedImageActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.response_image_display)
        setResult(Activity.RESULT_CANCELED)
        val cancelButton = findViewById<Button>(R.id.cancel_button)

        cancelButton.setOnClickListener {
            finish()
        }

        val uri = intent.extras?.get("get") as Uri

        val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri) as? Bitmap

        photo_display.setImageBitmap(bitmap)
    }
}
