package com.example.android_camera.camera

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.util.Base64
import androidx.core.content.FileProvider
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.nio.ByteBuffer
import java.util.*

class BitmapUtility {
    fun bitmapToBase64(bitmap: Bitmap): String {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val bArray: ByteArray = stream.toByteArray()
        return Base64.encodeToString(bArray, Base64.DEFAULT)
    }
    fun base64ToBitmap(base64: String): Bitmap {
        val decodeBase64 = Base64.decode(base64, Base64.DEFAULT)
        // val bitmap: Bitmap = Bitmap.createBitmap(256, 256, Bitmap.Config.ARGB_8888)
        return BitmapFactory.decodeByteArray(decodeBase64, 0, decodeBase64.size)
    }
    fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }
    fun byteArrayToBitmap(byteArray: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }
    fun bitmapToUri(bitmap: Bitmap, thisCon: Context): Uri {

        // 一時ファイル作成用のキャッシュディレクトリを定義する
        val cacheDir: File = thisCon.cacheDir

        // 現在日時からファイル名を生成する
        val fileName: String = System.currentTimeMillis().toString() + ".jpg"

        // 空のファイルを生成する
        val file = File(cacheDir, fileName)

        // ファイルにバイトデータを書き込み開始する
        val fileOutputStream: FileOutputStream? = FileOutputStream(file)

        // ファイルにbitmapを書き込む
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)

        // ファイルにバイトデータを書き込み終了する
        fileOutputStream?.close()

        // ファイルからcontent://スキーマ形式のuriを取得する
        return FileProvider.getUriForFile(thisCon, "com.example.android_camera.camera.provider", file)
    }
}
//Bitmap.createScaledBitmap(bitmap, 256, 256, true)