package com.example.mymovieapplication.model

object RepositoryImpl : Repository {

    private val listeners: MutableList<Repository.OnLoadListener> = mutableListOf()
    private var category: Category? = null

    override fun getCategoryFromServer(): Category? = category

    override fun getCategoryFromLocalStorageRus(): List<Category> = getCategoriesRus()

    override fun getCategoryFromLocalStorageEng(): List<Category> = getCategoriesEng()

    override fun categoryLoaded(category: Category?) {
        this.category = category
        listeners.forEach{it.onLoaded()}
    }

    override fun addLoadListener(listener: Repository.OnLoadListener) {
        listeners.add(listener)
    }

    override fun removeLoadListener(listener: Repository.OnLoadListener) {
        listeners.remove(listener)
    }
}