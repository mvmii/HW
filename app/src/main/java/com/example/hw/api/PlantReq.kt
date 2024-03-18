package com.example.hw.api

data class PlantReq(
    val keyWord: String,
    val limit: Int,
    val offset: Int)