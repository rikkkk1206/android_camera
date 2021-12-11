package com.example.android_camera.camera.http

import android.graphics.Bitmap
import com.example.android_camera.camera.BitmapUtility
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.Result
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import org.json.JSONObject

class Accessor {
    private val header: HashMap<String, String> = hashMapOf("Content-Type" to "application/json")
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private val requestAdapter = moshi.adapter(RequestFormat::class.java)

    fun getJson(url: String) {
        url.httpGet().responseJson { request, response, result ->
            when (result) {
                is Result.Failure -> {
                    val ex = result.getException()
                    JSONObject(ex.toString())
                }
                is Result.Success -> {
                    result.get().obj()
                }
            }
        }
    }

    fun getImagePost(url: String, bitmap: Bitmap) {
        val toArray = BitmapUtility()
        val bitmapToBase64 = toArray.bitmapToBase64(bitmap)
        val request = RequestFormat(
            key = "image",
            pic = bitmapToBase64
        )

        Fuel.post(url).header(header).body(requestAdapter.toJson(request))
            .response { request, response, result ->
                when (result) {
                    is Result.Failure -> {

                        val ex = result.getException()
                        JSONObject(ex.toString())
                    }
                    is Result.Success -> {
                        print("dwsa")
                        // base64をBitMAPにしてViewにいれてみる
                        // 変わってなかったらPythonがおかしい　
                        // 変わってたらOKHttpでやってみる（サンプルが多いので）
                    }
                }
            }
    }

    fun setImageUtility(bitmap: Bitmap): String {
        val toArray = BitmapUtility()
        return toArray.bitmapToBase64(bitmap)
    }
}