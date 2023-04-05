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

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun onAppForegrounded() {
        Log.d("MyApp", "App in foreground")
    }

    fun isConnected(): Boolean {
        val cm = instance.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (cm != null) {
            if (Build.VERSION.SDK_INT < 23) {
                val ni = cm.activeNetworkInfo
                if (ni != null) {
                    return (ni.isConnected && (ni.type == ConnectivityManager.TYPE_WIFI
                            || ni.type == ConnectivityManager.TYPE_MOBILE
                            || ni.type == ConnectivityManager.TYPE_BLUETOOTH))
                }

            } else {
                val n = cm.activeNetwork
                if (n != null) {
                    val nc = cm.getNetworkCapabilities(n)
                    if (nc != null) {
                        return (nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                                || nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                                || nc.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH))
                    }
                }
            }
        }

        return false
    }

    fun getToken(): String {
        return SharedPref(this).get(Cons.token, "")

    }


    fun getWidth(): Int {
        return Resources.getSystem().displayMetrics.widthPixels;
    }
}