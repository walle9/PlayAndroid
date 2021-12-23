package com.walle.playandroid.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.walle.playandroid.data.HomePageRepository
import com.walle.playandroid.model.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val homePageRepository: HomePageRepository):ViewModel() {

    private val _article = MutableLiveData<PagingData<Article.Data>>()
    val article: LiveData<PagingData<Article.Data>> =_article

    private fun getArticleFlow(): Flow<PagingData<Article.Data>> {
        return homePageRepository.getArticleFlow().cachedIn(viewModelScope)
    }

    fun getArticle() {
        viewModelScope.launch {
            getArticleFlow().collect {
                _article.value = it
            }
        }
    }

    init {
        getArticle()
    }
}