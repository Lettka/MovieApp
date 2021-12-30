package com.example.mymovieapplication.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie(
    val name: String = "",
    val year: Int = 0,
    var rating: Double = 0.0,
    val description: String = "",
    var isLike: Boolean = false
) : Parcelable

