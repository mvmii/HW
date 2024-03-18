package com.example.hw.api

import com.google.gson.annotations.SerializedName

data class PlantInfo(
    @SerializedName("_id")
    val id: Int,
    @SerializedName("\uFEFFF_Name_Ch")
    val name: String,
    @SerializedName("F_Location")
    val location: String,
    @SerializedName("F_Feature")
    val feature: String,
    @SerializedName("F_Pic01_URL")
    val imgUrl: String)
