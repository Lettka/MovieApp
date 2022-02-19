package com.example.mymovieapplication.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Category(
    val name: String = "",
    var listOfMovie: List<Movie> = emptyList()
) : Parcelable

fun getCategoriesRus(): List<Category> = listOf(
    Category("Популярные"),
    Category("Смотрят сейчас"),
    Category("Ожидаемые"),
    Category("Лучшие")
)

fun getCategoriesEng(): List<Category> = listOf(
    Category("Popular"),
    Category("Now Playing"),
    Category("Upcoming"),
    Category("Top Rated"),
)