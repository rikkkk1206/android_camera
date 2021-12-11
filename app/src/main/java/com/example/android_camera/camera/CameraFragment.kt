package com.example.android_camera.camera

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.android_camera.R
import com.example.android_camera.camera.http.*
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.camera_fragment.*
import kotlinx.coroutines.*
import okhttp3.FormBody
import okhttp3.Headers
import okhttp3.OkHttpClient
import okhttp3.Request

class CameraFragment : Fragment() {
    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    private val viewModel: ImageViewModel by viewModels()
    private var sendBitmap: Bitmap? = null

    private val baseUrl = "http://10.0.2.2:5001"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.camera_fragment, container, false)

        val selectMethodButton = view.findViewById<MaterialButton>(R.id.select_picture)

        selectMethodButton.setOnClickListener {
            val intent = Intent(context, SelectMethodActivity::class.java)
            startActivityForResult(intent,10)
        }

        val imageView = view.findViewById<ImageView>(R.id.photo_display)
        val imageObserver = Observer<Bitmap> { newImage ->
            imageView.setImageBitmap(newImage)
        }
        viewModel.stackImage.observe(viewLifecycleOwner, imageObserver)

        //通信
        val accessorButtonPanel = view.findViewById<MaterialButton>(R.id.create_image_panel)
        val accessorButtonBin = view.findViewById<MaterialButton>(R.id.create_image_bin)
        val accessorButtonInvert = view.findViewById<MaterialButton>(R.id.create_image_invert)

        accessorButtonPanel.setOnClickListener {
            sendBitmap?.let { nonNullBitmap: Bitmap ->
                scope.launch {
                    okHttpPost("$baseUrl/image", nonNullBitmap, requestPanel)
                }
            }
        }
        accessorButtonBin.setOnClickListener {
            sendBitmap?.let { nonNullBitmap: Bitmap ->
                scope.launch {
                    okHttpPost("$baseUrl/image", nonNullBitmap, requestBin)
                }
            }
        }
        accessorButtonInvert.setOnClickListener {
            sendBitmap?.let { nonNullBitmap: Bitmap ->
                scope.launch {
                    okHttpPost("$baseUrl/image", nonNullBitmap, requestInvert)
                }
            }
        }

        return view
    }

    suspend fun okHttpGet(url: String) = withContext(Dispatchers.IO) {
        val client: OkHttpClient = OkHttpClient.Builder().build()
        val request: Request = Request.Builder().url(url).build()

        client.newCall(request).execute().use { response ->
            if (response.isSuccessful) {
                val suc = response.body?.string()

                val intent = Intent(context, CreatedImageActivity::class.java)
                    intent.putExtra("get", suc)
                    startActivityForResult(intent,11)
            } else {
                val err = "failed/ code: ${response.code} / message: ${response.message}"
                err
            }
        }
    }

    suspend fun okHttpPost(url: String, bitmap: Bitmap, request: RequestFormat) = withContext(Dispatchers.IO) {
        val client = OkHttpClient()

        val accessor = Accessor()
        val bitmapToBase64: String = accessor.setImageUtility(bitmap)

        request.pic = bitmapToBase64
        val body: FormBody = FormBody.Builder()
            .add("key", request.key)
            .add("pic", request.pic).build()

        val headers: Headers = Headers.Builder()
            .add("Content-Type", "application/json")
            .add("Connection", "close")
            .add("Transfer-Encoding", "chunked").build()

        val request: Request = Request.Builder().url(url).headers(headers)
            .post(body).build()

        client.newCall(request).execute().use { response ->
            delay(1000)
            if (response.isSuccessful) {
                val suc = response.body?.string()

                val bitmapUtility = BitmapUtility()
                val base64ToBitmap = suc?.let { bitmapUtility.base64ToBitmap(it) }

                val uri: Uri? = base64ToBitmap?.let { nonNullBitmap ->
                    context?.let { nonNullContext ->
                        bitmapUtility.bitmapToUri(nonNullBitmap, nonNullContext)
                    }
                }
                if (uri == null) {
                    print(("err"))
                }

                val intent = Intent(context, CreatedImageActivity::class.java)
                intent.putExtra("get", uri)
                startActivityForResult(intent,11)
            } else {
                val err = "failed/ code: ${response.code} / message: ${response.message}"
                err
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode != 10) { return }

        if (resultCode == Activity.RESULT_OK && data != null) {
            val uri = data.extras?.get("takenPicture") as Uri
            uri?.let { nonNullUri: Uri ->

                // uriをbitmapに変換する
                val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, nonNullUri) as? Bitmap
                photo_display?.setImageBitmap(bitmap)

                if (bitmap != null) {
                    sendBitmap = bitmap
                }
            }
        }
    }
}