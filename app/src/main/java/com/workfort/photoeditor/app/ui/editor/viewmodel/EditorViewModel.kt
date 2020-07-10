package com.workfort.photoeditor.app.ui.editor.viewmodel

import android.text.TextUtils
import androidx.lifecycle.ViewModel
import java.io.File

class EditorViewModel: ViewModel() {
    private val mCropHistory = ArrayList<String>()

    fun addCropHistory(path: String) {
        mCropHistory.add(path)
    }

    fun clearCropHistory() {
        mCropHistory.forEach {
            val file = File(it)
            if(file.exists()) file.delete()
        }
        mCropHistory.clear()
    }
}