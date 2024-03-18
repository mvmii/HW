package com.example.hw.api

import com.google.gson.annotations.SerializedName

class ApiResponse(val result: Result) {
    data class Result(
        @SerializedName("limit")
        val limit: Int,
        @SerializedName("offset")
        val offset: Int,
        @SerializedName("count")
        val count: Int,
        @SerializedName("results")
        val list: List<PlantInfo>
    )
}