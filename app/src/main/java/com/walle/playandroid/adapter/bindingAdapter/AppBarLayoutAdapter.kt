package com.walle.playandroid.adapter.bindingAdapter

import androidx.databinding.BindingAdapter
import com.google.android.material.appbar.AppBarLayout
import kotlin.math.abs

/**
 * 设置appbar偏移监听
 */
@BindingAdapter("offsetChangedListener")
fun offsetChangedListener(appBarLayout: AppBarLayout, isClosed: (Boolean) -> Unit) {

    val totalScrollRange by lazy {
        appBarLayout.totalScrollRange
    }

    appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
        isClosed(
            totalScrollRange == abs(verticalOffset)
        )
    })
}