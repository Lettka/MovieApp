package com.example.mymovieapplication.model

class RepositoryImpl : Repository {
    override fun getMovieFromServer(): Movie {
        return Movie()
    }

    override fun getMovieFromLocalStorageRus(): List<Category> {
        return getCategoriesRus()
    }

    override fun getMovieFromLocalStorageEng(): List<Category> {
        return getCategoriesEng()
    }
}