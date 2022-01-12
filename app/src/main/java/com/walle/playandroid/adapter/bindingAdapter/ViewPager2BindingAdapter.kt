package com.walle.playandroid.adapter.bindingAdapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback

/**
 * 设置viewpager2当前条目
 */
@BindingAdapter("currentItem","smoothScroll", requireAll = true)
fun setCurrentItem(viewPager2: ViewPager2, currentItem: Int,smoothScroll:Boolean) {
    viewPager2.setCurrentItem(currentItem,smoothScroll)
}

/**
 * 设置viewpager2滚动监听
 */
@BindingAdapter("onPageScrollStateChanged", "onPageSelected", requireAll = true)
fun setonPageChanged(viewPager2: ViewPager2, onPageScrollStateChanged: () -> Unit, onPageSelected: (Int) -> Unit) {
    viewPager2.registerOnPageChangeCallback(object : OnPageChangeCallback() {
        override fun onPageScrollStateChanged(state: Int) {
            super.onPageScrollStateChanged(state)
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                onPageScrollStateChanged.invoke()
            }
        }

        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            onPageSelected.invoke(position)
        }
    })
}

/**
 * 设置viewpager2触摸监听
 */
@SuppressLint("ClickableViewAccessibility")
@BindingAdapter("onTouch")
fun setOnTouch(viewPager2: ViewPager2,onTouch:(MotionEvent)->Boolean) {
    val target = viewPager2.getChildAt(0) ?: return
    target.setOnTouchListener { _, ev ->
        onTouch.invoke(ev)
    }
}
