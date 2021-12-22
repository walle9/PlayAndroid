package com.walle.playandroid.net.api

import com.walle.playandroid.net.NetResponse
import com.walle.playandroid.net.retrofit
import retrofit2.http.GET
import retrofit2.http.Path

interface HomeApi{
    companion object HomeService:HomeApi by retrofit.create(HomeApi::class.java)

    @GET("article/list/{page}/json")
    suspend fun getArticle(@Path("page")page:Int): NetResponse<Article>
}