package com.example.mymovieapplication.model

interface Repository {
    fun getMovieFromServer(): Movie
    fun getMovieFromLocalStorageRus(): List<Category>
    fun getMovieFromLocalStorageEng(): List<Category>
}