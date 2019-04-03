package com.workfort.base.util.helper

import android.content.Context
import android.widget.Toast

class Toaster(private val context: Context) {
    fun showToast(message: String){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun showToast(message: Int){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}