package com.example.mymovieapplication.repository

import com.example.mymovieapplication.model.Category

interface Repository {
    fun getCategoryFromServer(): Category?
    fun getCategoryFromLocalStorageRus(): List<Category>
    fun getCategoryFromLocalStorageEng(): List<Category>

    fun categoryLoaded(category: Category?)
    fun addLoadListener(listener: OnLoadListener)
    fun removeLoadListener(listener: OnLoadListener)


    fun interface OnLoadListener {
        fun onLoaded()
    }
}