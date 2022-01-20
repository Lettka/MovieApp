package com.example.mymovieapplication.model

import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

object CategoryLoader {

    private const val YOUR_API_KEY = "e27ec23df999c44ebf7368007dc58fa2"

    fun load(category: Category, listener: OnCategoryLoadListener) {

        val handler = Handler(Looper.myLooper() ?: Looper.getMainLooper())

        Thread {
            var urlConnection: HttpsURLConnection? = null
            try {
                val uri = when (category.name) {
                    "Популярные", "Popular" -> URL("https://api.themoviedb.org/3/movie/popular?api_key=$YOUR_API_KEY&page=1")
                    //"Новинки", "New" -> URL("https://api.themoviedb.org/3/movie/latest?api_key=$YOUR_API_KEY&page=1")
                    "Смотрят сейчас", "Now Playing" -> URL("https://api.themoviedb.org/3/movie/now_playing?api_key=$YOUR_API_KEY&page=1")
                    "Ожидаемые", "Upcoming" -> URL("https://api.themoviedb.org/3/movie/upcoming?api_key=$YOUR_API_KEY&page=1")
                    "Лучшие", "Top Rated" -> URL("https://api.themoviedb.org/3/movie/top_rated?api_key=$YOUR_API_KEY&page=1")
                    else -> URL("https://api.themoviedb.org/3/movie/popular?api_key=$YOUR_API_KEY&page=1")
                }


                urlConnection = uri.openConnection() as HttpsURLConnection
                urlConnection.requestMethod = "GET"
                urlConnection.readTimeout = 1000
                urlConnection.connectTimeout = 1000
                val reader = BufferedReader(InputStreamReader(urlConnection.inputStream))
                val result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    reader.lines().collect(Collectors.joining("\n"))
                } else {
                    ""
                }
                val categoryDTO = Gson().fromJson(result, CategoryDTO::class.java)
                handler.post {
                    listener.onLoaded(categoryDTO)
                }
            } catch (e: Exception) {
                handler.post {
                    listener.onFailed(e)
                }
                Log.e("DEBUGLOG", "FAIL CONNECTION", e)
            } finally {
                urlConnection?.disconnect()
            }
        }.start()

    }

    interface OnCategoryLoadListener {
        fun onLoaded(categoryDTO: CategoryDTO)
        fun onFailed(throwable: Throwable)
    }
}