package com.walle.playandroid.viewmodel

import android.app.Application
import android.util.Log
import android.view.MotionEvent
import androidx.databinding.ObservableInt
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.walle.playandroid.data.HomePageRepository
import com.walle.playandroid.model.Article
import com.walle.playandroid.model.Banner
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class WebViewModel(application: Application) : BaseViewModel(application) {
    val progress=ObservableInt()
}