package com.workfort.demo.app.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.workfort.base.util.helper.Toaster
import com.workfort.demo.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Toaster(this).showToast("show toast!")
    }
}
