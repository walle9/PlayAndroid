package com.walle.playandroid.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.walle.playandroid.model.Article
import com.walle.playandroid.model.Banner
import com.walle.playandroid.net.api.HomeApi
import com.walle.playandroid.response.DataResponse
import kotlinx.coroutines.flow.Flow


private const val NETWORK_PAGE_SIZE = 50
object HomePageRepository:BaseRepository() {

    fun getArticleFlow(): Flow<PagingData<Article.Data>> {

        return Pager(
            PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { ArticlePagingSource() }
        ).flow
    }

    suspend fun getBanner(): Flow<DataResponse<List<Banner>>> {
        return safeCall { HomeApi.getBanner() }
    }

}