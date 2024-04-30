package com.dicoding.asclepius.network

import com.dicoding.asclepius.BuildConfig.API_TOKEN
import com.dicoding.asclepius.datasource.News
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("top-headlines")
    @Headers("UserResponse-Agent: request")
    suspend fun getArticleList(@Query("category") category : String = "health",@Query("pageSize") pageSize : Int = 10, @Query("q") search : String , @Query("apiKey") apiKey : String = API_TOKEN): News
}