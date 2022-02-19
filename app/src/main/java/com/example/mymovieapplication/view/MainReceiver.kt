package com.example.mymovieapplication.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.mymovieapplication.model.Category
import com.example.mymovieapplication.repository.RepositoryImpl

private const val CATEGORY_EXTRA = "CATEGORY_EXTRA"

class MainReceiver : BroadcastReceiver() {

    companion object {
        const val CATEGORY_LOAD_SUCCESS = "CATEGORY_LOAD_SUCCESS"
        const val CATEGORY_LOAD_FAILED = "CATEGORY_LOAD_FAILED"
    }

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("MainReceiver", "onReceive $intent")
        when (intent.action) {
            CATEGORY_LOAD_SUCCESS -> RepositoryImpl.categoryLoaded(
                intent.extras?.getParcelable<Category>(
                    CATEGORY_EXTRA
                )
            )
            CATEGORY_LOAD_FAILED -> RepositoryImpl.categoryLoaded(null)
        }
    }
}