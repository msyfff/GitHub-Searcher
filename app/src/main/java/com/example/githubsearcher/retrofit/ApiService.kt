package com.example.githubsearcher.retrofit

import com.example.githubsearcher.response.DetailResponse
import com.example.githubsearcher.response.ItemsItem
import com.example.githubsearcher.response.Response
import kotlinx.coroutines.Job
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    fun getApi(
        @Query("q") q: String,
//        @Header("Authorization") token: String = AUTH
    ): Call<Response>

    @GET("users/{username}")
    fun getDetail(
        @Path("username") username: String,
//        @Header("Authorization") token : String = AUTH
    ): Call<DetailResponse>

    @GET("users/{username}/followers")
    fun getFollowers(
        @Path("username") username: String,
//        @Header("Authorization") token : String = AUTH
    ) : Call<List<ItemsItem>>


    @GET("users/{username}/following")
    fun getFollowing(
        @Path("username") username: String,
//        @Header("Authorization") token : String = AUTH
    ) : Call<List<ItemsItem>>

    @GET("search/users")
    fun getFavorite(
        @Query("q") q: String,
//        @Header("Authorization") token: String = AUTH
    ): Call<Response>

//    companion object {
//        const val AUTH = "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"
//    }
}