package com.example.mymovieapplication.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie(
    val id: String = "0",
    val name: String = "",
    val year: String = "",
    var rating: Double? = 0.0,
    val description: String = "",
    var isLike: Boolean = false
) : Parcelable

