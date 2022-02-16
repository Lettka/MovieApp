package com.example.mymovieapplication.model

import android.app.IntentService
import android.content.Intent
import android.util.Log

class MainIntentService : IntentService("MainIntentService") {

    companion object {
        const val TAG = "MainIntentService"
    }

    override fun onHandleIntent(intent: Intent?) {
        Log.d(TAG, "onHandleIntent")
        Log.d(TAG, "current thread: ${Thread.currentThread().name}")

        intent?.getParcelableExtra<Category>("CATEGORY_EXTRA")?.let { category ->
            CategoryLoader.load(category, object : CategoryLoader.OnCategoryLoadListener {

                override fun onLoaded(categoryDTO: CategoryDTO) {
                    applicationContext.sendBroadcast(
                        Intent(applicationContext, MainReceiver::class.java).apply {
                            action = MainReceiver.CATEGORY_LOAD_SUCCESS
                            for (movie in categoryDTO.movies) {
                                val itemMovie = Movie(
                                    movie.id.toString(),
                                    movie.title.toString(),
                                    movie.releaseDate.toString(),
                                    movie.rating,
                                    movie.overview.toString(),
                                    false
                                )
                                category.listOfMovie += itemMovie
                            }
                            putExtra("CATEGORY_EXTRA", category)
                        }
                    )
                }

                override fun onFailed(throwable: Throwable) {
                    applicationContext.sendBroadcast(
                        Intent(applicationContext, MainReceiver::class.java).apply {
                            action = MainReceiver.CATEGORY_LOAD_FAILED
                        }
                    )
                }

            })
        }
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand $intent")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }

}