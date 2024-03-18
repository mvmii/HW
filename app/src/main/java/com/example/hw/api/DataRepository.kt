package com.example.hw.api

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DataRepository {
    interface ResponseCallback<T> {
        fun onSuccess(data: T)
        fun onError(message: String)
    }

    private val apiService = Retrofit.Builder()
        .baseUrl("https://data.taipei/api/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)

    fun fetchData(req : PlantReq, callback: ResponseCallback<List<PlantInfo>>) {
        apiService.fetchData(req.keyWord, req.limit, req.offset).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    callback.onSuccess(response.body()?.result?.list ?: emptyList())
                } else {
                    callback.onError("API Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                callback.onError("Network Error: ${t.message}")
            }
        })
    }
}