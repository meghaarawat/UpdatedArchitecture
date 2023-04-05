package com.myapplication.base

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.myapplication.others.Loader
import com.myapplication.others.MyUtils

open class BaseFragment : Fragment() {

    companion object {

        lateinit var dialog: Dialog
        lateinit var progressBar: View
        lateinit var mainView: View

    }

    @SuppressLint("ResourceType")
    protected fun initializeProgressView( progressView: View) {
        progressBar =progressView
    }

    @SuppressLint("ResourceType")
    protected fun initializeMainView( main:View) {
        mainView = main
    }

    @SuppressLint("ResourceType")
    protected fun createDialogForFragmentWithLayout(context: Context, @LayoutRes v: Int) {
        dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(v)
        dialog.setCancelable(true)
    }

    protected fun showDialogForFragment() {

        showLoader()
    }

    protected fun hideDialogForFragment() {
        hideLoader()
    }

    var loader: Loader? = null
    protected fun showLoader() {
       try{
           loader = null
           loader = context?.let { Loader(it) }
           if (loader != null && !loader?.isShowing!!) {
               loader?.show()
           }
       }
       catch(e :Exception){

       }

    }

    protected fun hideLoader() {
        if (loader != null && loader?.isShowing!!) {
            loader?.dismiss()
        }
        loader = null
    }

    protected fun showProgressBar() {
        MyUtils.viewGone(mainView)
        MyUtils.viewVisible(progressBar)

    }

    protected fun hideProgressBar() {
        MyUtils.viewGone(progressBar)
        MyUtils.viewVisible(mainView)
    }

}