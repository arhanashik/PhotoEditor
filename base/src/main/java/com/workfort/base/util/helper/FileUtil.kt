package com.workfort.base.util.helper

import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import com.workfort.base.BaseApp
import java.io.File
import java.io.FileOutputStream
import java.util.*

open class FileUtil {
    fun saveBitmap(bitmap: Bitmap): Uri {
        val id = Random().nextInt(10000)
        val file = File(BaseApp.getApplicationContext().cacheDir, "$id.JPEG")

        val fos = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        fos.flush()
        fos.close()

        return Uri.fromFile(file)
    }

    fun createEmptyFile(fileName: String): File {
        return File(BaseApp.getApplicationContext().cacheDir, fileName)
    }

    fun getPath(uri: Uri): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = BaseApp.getApplicationContext()
            .contentResolver.query(uri, projection, null, null, null) ?: return null
        cursor.moveToFirst()
        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)).also {
            cursor.close()
            return it
        }
    }

    fun getFileType(uri: Uri): String? {
        return BaseApp.getApplicationContext().contentResolver?.getType(uri)
    }
}
