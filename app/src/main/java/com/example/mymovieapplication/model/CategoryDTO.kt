package com.example.mymovieapplication.model

import com.google.gson.annotations.SerializedName

data class CategoryDTO (
    @SerializedName("page") val page: Int,
    @SerializedName("results") val movies: List<MovieDTO>,
    @SerializedName("total_pages") val pages: Int
)