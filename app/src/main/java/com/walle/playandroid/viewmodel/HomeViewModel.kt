package com.walle.playandroid.viewmodel

import android.app.Application
import android.util.Log
import android.view.MotionEvent
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

class HomeViewModel(application: Application) : BaseViewModel(application) {

    private var carouselJob: Job? = null

    private val _article = MutableLiveData<PagingData<Article.Data>>()
    val article: LiveData<PagingData<Article.Data>> = _article

    private val _banners = MutableLiveData<List<Banner>>()
    val banners: LiveData<List<Banner>> = _banners

    val bannerCurrentItem= MutableLiveData<Pair<Int,Boolean>>()
    var bannerTempPosition:Int =1

    val appbarLayoutIsClosed=MutableLiveData(false)

    /**
     * 获取文章
     */
    private fun getArticle() {
        viewModelScope.launch {
            HomePageRepository.getArticleFlow().cachedIn(viewModelScope).collect {
                _article.value = it
            }
        }
    }

    /**
     * 获取banner
     */
    fun getBanners() {
        launchWithLoading({ HomePageRepository.getBanner() }, { success ->
            _banners.value = success.data.let {
                arrayListOf(it[it.size - 1]) + it + it[0]
            }
        }, false)
    }

    /**
     * 设置轮播
     * @param isStart treu 开始轮播 false 停止轮播
     */
    fun setCarousel(isStart: Boolean){

        if (carouselJob?.isActive == true) carouselJob?.cancel()

        if (isStart) {
            carouselJob = viewModelScope.launch {
                while (isActive) {
                    delay(5000)
                    bannerCurrentItem.value = ++bannerTempPosition to true
                }

            }
        }
    }

    /**
     * viewpager2滚动监听
     */
    fun onViewPager2PageScrollStateChanged() {
        banners.value?.size?.let {
            if (bannerTempPosition == 0) {
                bannerCurrentItem.value = it-2 to false
            } else if (bannerTempPosition == it - 1) {
                bannerCurrentItem.value = 1 to false
            }
        }
    }

    /**
     * viewpager2滚动监听
     */
    val viewPager2onPageSelected:(position:Int)->Unit ={
        bannerTempPosition = it
    }

    /**
     * appbar偏移监听
     */
    val appBaronOffsetListener:(isClosed:Boolean)->Unit = { isClosed->

        if (appbarLayoutIsClosed.value!=isClosed) {
            appbarLayoutIsClosed.value = isClosed
            setCarousel(!isClosed)
        }

    }

    /**
     * viewpager2触摸监听
     */
    val onViewPager2Touch:(MotionEvent)->Boolean = {
        when(it.action) {
            MotionEvent.ACTION_DOWN ->{
                setCarousel(false)}
            MotionEvent.ACTION_UP,MotionEvent.ACTION_CANCEL-> setCarousel(true)
        }
        false
    }

    init {
        getArticle()
        getBanners()
    }
}