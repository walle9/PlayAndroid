package com.walle.playandroid.ui

import android.app.Application
import android.content.ContextWrapper
import dagger.hilt.android.HiltAndroidApp

private lateinit var INSTANCE: Application
class App:Application() {
    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }
}
object AppContext: ContextWrapper(INSTANCE)