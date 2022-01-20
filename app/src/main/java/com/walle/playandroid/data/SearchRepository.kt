package com.walle.playandroid.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.walle.db.SearchHistoryDatabase
import com.walle.playandroid.model.Article
import com.walle.playandroid.model.Banner
import com.walle.playandroid.model.HotKey
import com.walle.playandroid.net.api.HomeApi
import com.walle.playandroid.net.api.SearchApi
import com.walle.playandroid.response.DataResponse
import kotlinx.coroutines.flow.Flow

private const val NETWORK_PAGE_SIZE = 50

object SearchRepository : BaseRepository() {

    fun getSearchHistory(): Flow<List<SearchHistory>> {
        return SearchHistoryDatabase.getInstance().searchHistoryDao().getAll()
    }

    fun addKeyWord(searchHistory: SearchHistory) {
        SearchHistoryDatabase.getInstance().searchHistoryDao().insert(searchHistory)
    }

    fun ClearHistory() {
        SearchHistoryDatabase.getInstance().searchHistoryDao().clear()
    }

    fun deleteHistory(searchHistory: SearchHistory) {
        SearchHistoryDatabase.getInstance().searchHistoryDao().delete(searchHistory)
    }


    fun search(key: String): Flow<PagingData<Article.Data>> {

        return Pager(
            PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { SearchPagingSource(key) }
        ).flow
    }

    suspend fun getHotKey(): Flow<DataResponse<List<HotKey>>> {
        return safeCall { SearchApi.getHotKey() }
    }
}