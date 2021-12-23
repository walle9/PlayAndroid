package com.walle.playandroid.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.walle.playandroid.model.Article
import com.walle.playandroid.net.api.HomeApi
import retrofit2.HttpException
import java.io.IOException

private const val STARTING_PAGE_INDEX = 0

class ArticlePagingSource : PagingSource<Int, Article.Data>() {

    // The refresh key is used for subsequent refresh calls to PagingSource.load after the initial load
    override fun getRefreshKey(state: PagingState<Int, Article.Data>): Int? {
        // We need to get the previous key (or next key if previous is null) of the page
        // that was closest to the most recently accessed index.
        // Anchor position is the most recently accessed index
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article.Data> {
        return try {
            // Start refresh at page 1 if undefined.
            val position = params.key ?: STARTING_PAGE_INDEX
            val response = HomeApi.getArticle(position)
            val data = response.data
            LoadResult.Page(
                data = data.datas,
                prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (data.over) null else data.curPage
            )
        } catch (e: IOException) {
            // IOException for network failures.
            LoadResult.Error(e)
        } catch (e: HttpException) {
            // HttpException for any non-2xx HTTP status codes.
            LoadResult.Error(e)
        }
    }
}