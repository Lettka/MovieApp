package com.example.mymovieapplication.viewmodel

import com.example.mymovieapplication.model.Category

sealed class AppState {
    data class Success(val categoryData: List<Category>) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}

