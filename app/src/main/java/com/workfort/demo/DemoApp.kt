package com.workfort.demo

import android.content.Context
import androidx.multidex.MultiDex
import com.workfort.base.BaseApp
import timber.log.Timber

open class DemoApp : BaseApp() {
    init {
        sInstance = this
    }

    companion object {
        private lateinit var sInstance: DemoApp

        fun getApplicationContext(): Context {
            return sInstance.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()

//        SystemClock.sleep(300)

        if (applicationContext != null) {
            if (BuildConfig.DEBUG) {
                initiateOnlyInDebugMode()
            }
//            initiate(applicationContext)
        }
    }

    private fun initiateOnlyInDebugMode() {
        Timber.plant(object : Timber.DebugTree() {
            override fun createStackElementTag(element: StackTraceElement): String? {
                return super.createStackElementTag(element) +
                        " - Method:${element.methodName} - Line:${element.lineNumber}"
            }
        })
    }

//    private fun initiate(context: Context) {
//        Prefs.init(context)
//    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}