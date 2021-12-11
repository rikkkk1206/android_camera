package com.example.android_camera

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.android_camera.camera.CameraFragment
import kotlinx.android.synthetic.main.camera_fragment.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val cameraFragment = CameraFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.fragment_container, cameraFragment)
        transaction.commit()
    }
}