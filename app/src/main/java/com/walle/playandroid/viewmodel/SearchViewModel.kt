package com.walle.playandroid.viewmodel

import android.app.Application
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.walle.playandroid.data.SearchHistory
import com.walle.playandroid.data.SearchRepository
import com.walle.playandroid.model.Article
import com.walle.playandroid.model.HotKey
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.*

class SearchViewModel(application: Application) : BaseViewModel(application) {

    private var searchJob: Job? = null
    val isShowSearchRecyclerView = MutableLiveData(false)

    lateinit var searchHistoryList: LiveData<List<SearchHistory>>
    val _article = MutableLiveData<PagingData<Article.Data>>()
    val article: LiveData<PagingData<Article.Data>> = _article

    private val _hotKey = MutableLiveData<List<HotKey>>()
    val hotKey: LiveData<List<HotKey>> = _hotKey

    val searchEdit = MutableLiveData("")

    init {
        viewModelScope.launch {
            searchHistoryList = SearchRepository.getSearchHistory()
                .distinctUntilChanged()
                .map {
                    it.reversed()
                }
                .catch { e ->
                    Toast.makeText(getApplication(), e.localizedMessage, Toast.LENGTH_SHORT).show()
                }.asLiveData()
        }

    }

    fun addKeyWord(keyWord: String) {
        viewModelScope.launch(Dispatchers.IO) {
            SearchRepository.addKeyWord(SearchHistory(keyWord))
        }

    }

    fun clearHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            SearchRepository.ClearHistory()
        }
    }

    fun delete(searchHistory: SearchHistory) {
        viewModelScope.launch(Dispatchers.IO) {
            SearchRepository.deleteHistory(searchHistory)
        }

    }

    val onSearchTextChange: (String) -> Unit = {
        if (TextUtils.isEmpty(it)) {
            isShowSearchRecyclerView.value = false
        }
    }

    val onTextSubmit: (String) -> Unit = {
        addKeyWord(it)
        search(it)

    }


    /**
     * 获取文章
     */
    fun search(keyWord: String) {
        Log.d("TAG", "search: 开始搜索")

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            Log.d("TAG", "search: 开始搜索了")
            SearchRepository.search(keyWord).cachedIn(viewModelScope).collect {
                _article.value = it
                isShowSearchRecyclerView.value = true
            }
        }


    }

    /**
     * 获取搜索热词
     */
    fun getHotKey() {
        launchWithLoading({ SearchRepository.getHotKey() }, { success ->
            _hotKey.value = success.data!!
        }, false)
    }

    init {
        getHotKey()
    }
}