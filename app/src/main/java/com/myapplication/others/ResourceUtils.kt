package com.myapplication.others

import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import com.myapplication.base.App.Companion.get

object ResourceUtils {

    fun getString(@StringRes stringId: Int): String {
        return get().getString(stringId)
    }

    fun getDrawable(@DrawableRes drawableId: Int): Drawable? {
        return get().resources.getDrawable(drawableId)
    }

    fun getScreenWidthInPixel():Int{
        return get().resources.displayMetrics.widthPixels
        //return get().resources.displayMetrics.widthPixels
    }

    fun getColor(@ColorRes colorId: Int): Int {
        return get().resources.getColor(colorId)
    }

    fun getDimen(@DimenRes dimenId: Int): Int {
        return get().resources.getDimension(dimenId).toInt()
    }

    fun dpToPx(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }

    fun pxToDp(px: Int): Int {
        return (px / Resources.getSystem().displayMetrics.density).toInt()
    }

    fun ImageView.setTint(@ColorRes colorRes: Int) {
        ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(ContextCompat.getColor(context, colorRes)))
    }
}