package com.example.hw.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("dataset/f18de02f-b6c9-47c0-8cda-50efad621c14?scope=resourceAquire")
    fun fetchData(
        @Query("q") query: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Call<ApiResponse>
}