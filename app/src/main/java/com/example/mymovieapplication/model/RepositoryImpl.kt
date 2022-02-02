package com.example.mymovieapplication.model

class RepositoryImpl : Repository {
    override fun getMovieFromServer(): Movie = Movie()

    override fun getMovieFromLocalStorageRus(): List<Category> = getCategoriesRus()

    override fun getMovieFromLocalStorageEng(): List<Category> = getCategoriesEng()
}