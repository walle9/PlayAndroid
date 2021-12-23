package com.walle.playandroid.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.walle.playandroid.model.Article
import kotlinx.coroutines.flow.Flow

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomePageRepository @Inject constructor() {

    fun getArticleFlow(): Flow<PagingData<Article.Data>> {
        return Pager(
            PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { ArticlePagingSource() }
        ).flow
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 50
    }
}