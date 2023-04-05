package com.myapplication.others

import android.graphics.Color
import android.text.TextUtils
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.myapplication.R
import com.myapplication.base.App
import com.squareup.picasso.Picasso


object ImageUtils {

    private val glide: RequestManager = Glide.with(App.get())

    fun loadImage(image: ImageView, url: String?) {
        val circularProgressDrawable = CircularProgressDrawable(App.get())
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.setColorSchemeColors(R.color.progress_tint)
        circularProgressDrawable.start()

        glide
            .load(Cons.BASE_URL + url)
            .centerCrop()
            .placeholder(circularProgressDrawable)
            .error(R.mipmap.ic_launcher_round)
            .into(image);
    }

    fun loadDrawableImage(ivImage: ImageView, @DrawableRes userImage: Int) {
        glide
            .load(userImage)
            .centerCrop()
            .into(ivImage);

    }

    fun loadPicassoImage(ivImage: ImageView, userImage:  String?) {
        Picasso.get()
            .load(MyUtils.getWorkoutImageUrl(userImage))
            .centerCrop()
            .fit()
            .noFade()
            .placeholder(R.mipmap.ic_launcher)
            .error(R.drawable.ic_broken_image)
            .into(ivImage)
    }

}