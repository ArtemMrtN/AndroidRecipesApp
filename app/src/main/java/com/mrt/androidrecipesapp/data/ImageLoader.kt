package com.mrt.androidrecipesapp.data

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.mrt.androidrecipesapp.R

object ImageLoader {
    fun loadImage(context: Context, imageView: ImageView, imageUrl: Any?) {
        Glide.with(context)
            .load(imageUrl ?: R.drawable.img_error)
            .centerCrop()
            .placeholder(R.drawable.img_placeholder)
            .error(R.drawable.img_error)
            .into(imageView)
    }
}