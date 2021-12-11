package com.example.android_camera.camera

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ImageViewModel : ViewModel() {
    val stackImage: MutableLiveData<Bitmap> by lazy {
        MutableLiveData<Bitmap>()
    }
}