package com.example.mymovieapplication.viewmodel

import com.example.mymovieapplication.model.Movie

sealed class AppState {
    data class Success(val movieData: Movie) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}
