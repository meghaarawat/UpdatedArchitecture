package com.myapplication.base

import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.myapplication.others.Cons

class App : Application(), LifecycleObserver {

    companion object {
        lateinit var instance: App

        @JvmStatic
        fun get(): App {
            return instance
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}