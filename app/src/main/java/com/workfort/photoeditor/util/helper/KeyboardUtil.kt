package com.workfort.photoeditor.util.helper

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

class KeyboardUtil {
    companion object {
        fun showKeyboard(context: Context) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm?.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        }

        fun hideKeyboard(context: Context, editText: EditText) {
            editText.clearFocus()
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm?.hideSoftInputFromWindow(editText.windowToken, 0)
        }
    }
}