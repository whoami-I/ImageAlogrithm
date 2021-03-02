package com.mike.imagealogrithm

import android.app.Application
import timber.log.Timber

class ImageApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}