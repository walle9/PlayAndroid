package com.walle.playandroid.net.api

import com.walle.playandroid.model.Article
import com.walle.playandroid.model.Banner
import com.walle.playandroid.model.HotKey
import com.walle.playandroid.response.NetResponse
import com.walle.playandroid.net.retrofit
import retrofit2.http.*

interface SearchApi{
    companion object SearchService:SearchApi by retrofit.create(SearchApi::class.java)

    /**
     * 获取搜索
     * @param key 搜索关键字
     * @param page 分页 从0开始
     */
    @FormUrlEncoded
    @POST("article/query/{page}/json")
    suspend fun search(@Field("k")key:String,@Path("page") page: Int): NetResponse<Article>

    /**
     * 获取搜索热词
     */
    @GET("hotkey/json")
    suspend fun getHotKey(): NetResponse<List<HotKey>>
}