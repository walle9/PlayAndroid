package com.walle.playandroid.adapter.bindingAdapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

/**
 * 设置图片
 */
@BindingAdapter("url")
fun ImageView.setUrl(url:String) {
    Glide.with(this).load(url).into(this)
}