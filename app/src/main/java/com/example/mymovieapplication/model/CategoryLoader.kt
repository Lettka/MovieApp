package com.example.mymovieapplication.model

import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.mymovieapplication.BuildConfig
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

object CategoryLoader {

    fun load(category: Category, listener: OnCategoryLoadListener) {

        //val handler = Handler(Looper.myLooper() ?: Looper.getMainLooper())

        var urlConnection: HttpsURLConnection? = null
        try {

            val uri = when (category.name) {
                "Популярные", "Popular" -> URL("https://api.themoviedb.org/3/movie/popular?page=1")
                //"Новинки", "New" -> URL("https://api.themoviedb.org/3/movie/latest?page=1")
                "Смотрят сейчас", "Now Playing" -> URL("https://api.themoviedb.org/3/movie/now_playing?page=1")
                "Ожидаемые", "Upcoming" -> URL("https://api.themoviedb.org/3/movie/upcoming?page=1")
                "Лучшие", "Top Rated" -> URL("https://api.themoviedb.org/3/movie/top_rated?page=1")
                else -> URL("https://api.themoviedb.org/3/movie/popular?page=1")
            }

            urlConnection = uri.openConnection() as HttpsURLConnection
            with(urlConnection) {
                requestMethod = "GET"
                readTimeout = 50000
                connectTimeout = 50000

                addRequestProperty("Authorization", "Bearer ${BuildConfig.MOVIE_API_TOKEN}")

                val reader = BufferedReader(InputStreamReader(inputStream))
                val result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    reader.lines().collect(Collectors.joining("\n"))
                } else {
                    ""
                }
                val categoryDTO = Gson().fromJson(result, CategoryDTO::class.java)
                listener.onLoaded(categoryDTO)
                /*handler.post {
                    listener.onLoaded(categoryDTO)
                }*/
            }
        } catch (e: Exception) {
            listener.onFailed(e)
            /*handler.post {
                listener.onFailed(e)
            }*/
            Log.e("DEBUGLOG", "FAIL CONNECTION", e)
        } finally {
            urlConnection?.disconnect()
        }
    }


    interface OnCategoryLoadListener {
        fun onLoaded(categoryDTO: CategoryDTO)
        fun onFailed(throwable: Throwable)
    }
}