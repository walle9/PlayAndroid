package com.walle.playandroid.net.api

import com.walle.playandroid.model.Article
import com.walle.playandroid.model.Banner
import com.walle.playandroid.response.NetResponse
import com.walle.playandroid.net.retrofit
import retrofit2.http.GET
import retrofit2.http.Path

interface HomeApi{
    companion object HomeService:HomeApi by retrofit.create(HomeApi::class.java)

    /**
     * 获取首页文章列表
     * @param page 从0开始
     */
    @GET("article/list/{page}/json")
    suspend fun getArticle(@Path("page")page:Int): NetResponse<Article>

    /**
     * 获取首页置顶文章
     */
    @GET("article/top/json")
    suspend fun getArticleTop(): NetResponse<List<Article.Data>>

    /**
     * 获取首页banner
     */
    @GET("banner/json")
    suspend fun getBanner(): NetResponse<List<Banner>>
}