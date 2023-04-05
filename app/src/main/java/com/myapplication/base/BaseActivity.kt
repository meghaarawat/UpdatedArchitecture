package com.myapplication.base

import android.annotation.SuppressLint
import android.app.Dialog
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.myapplication.others.Loader
import com.myapplication.others.MyUtils

@SuppressLint("Registered")
abstract class BaseActivity() : AppCompatActivity() {
    lateinit var dialogLoader: Dialog
    lateinit var progressView: View
    lateinit var main: View

    protected var tvTitleView: TextView? = null

    var loader: Loader? = null
    protected fun showLoader() {
        loader = null
        loader = this?.let { Loader(it) }
        if (loader != null && !loader?.isShowing!!) {
            loader?.show()
        }

    }

    protected fun hideLoader() {
        if (loader != null && loader?.isShowing!!) {
            loader?.dismiss()
        }
        loader = null

    }

    protected fun initProgressView(view: View) {
        progressView = view
    }

    protected fun initMainView(view: View) {
        main = view
    }

    protected fun showProgressView() {
        MyUtils.viewVisible(progressView)
        MyUtils.viewGone(main)
    }

    protected fun hideProgressView() {
        MyUtils.viewVisible(main)
        MyUtils.viewGone(progressView)
    }

    companion object {
        private const val TAG = "BaseActivity"
    }

    protected fun onBackButton(ivBack: ImageView?){
        ivBack?.setOnClickListener { onBackPressed() }
    }
}