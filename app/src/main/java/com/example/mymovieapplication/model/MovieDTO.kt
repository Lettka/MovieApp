package com.example.mymovieapplication.model

import com.google.gson.annotations.SerializedName

data class MovieDTO(
    val id: String?,
    val title: String?,
    val overview: String?,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("release_date") val releaseDate: String?,
    @SerializedName("vote_average") val rating: Double?,
)
