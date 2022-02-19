package com.example.mymovieapplication.viewmodel

import android.util.Log
import com.example.mymovieapplication.BuildConfig
import com.example.mymovieapplication.model.Category
import com.example.mymovieapplication.model.CategoryAPI
import com.example.mymovieapplication.model.CategoryDTO
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.net.URL
import java.util.concurrent.TimeUnit

object CategoryLoader {

    private val client: OkHttpClient = OkHttpClient.Builder()
        .callTimeout(1000, TimeUnit.SECONDS)
        .connectTimeout(1000, TimeUnit.SECONDS)
        .addInterceptor(Interceptor { chain ->
            chain.proceed(
                chain.request()
                    .newBuilder()
                    .addHeader("Authorization", "Bearer ${BuildConfig.MOVIE_API_TOKEN}")
                    .build()
            )
        })
        .addInterceptor(HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        })
        .build()

    private val categoryAPI = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
        .build()
        .create(CategoryAPI::class.java)

    fun loadOkHttp(category: Category, listener: OnCategoryLoadListener) {

        val uri = when (category.name) {
            "Популярные", "Popular" -> URL("https://api.themoviedb.org/3/movie/popular?page=1")
            "Смотрят сейчас", "Now Playing" -> URL("https://api.themoviedb.org/3/movie/now_playing?page=1")
            "Ожидаемые", "Upcoming" -> URL("https://api.themoviedb.org/3/movie/upcoming?page=1")
            "Лучшие", "Top Rated" -> URL("https://api.themoviedb.org/3/movie/top_rated?page=1")
            else -> URL("https://api.themoviedb.org/3/movie/popular?page=1")
        }

        val request: Request = Request.Builder()
            .get()
            .addHeader("Authorization", "Bearer ${BuildConfig.MOVIE_API_TOKEN}")
            .url(uri)
            .build()

        client.newCall(request)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    listener.onFailed(e)
                    Log.e("DEBUGLOG", "FAIL CONNECTION", e)
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val categoryDTO =
                            Gson().fromJson(response.body?.string(), CategoryDTO::class.java)
                        listener.onLoaded(categoryDTO)
                    } else {
                        listener.onFailed(Exception(response.body?.string()))
                        Log.e("DEBUGLOG", "FAIL CONNECTION $response")
                    }
                }

            })

    }

    fun loadRetrofit(category: Category, listener: OnCategoryLoadListener) {

        val categoryPss = when (category.name) {
            "Популярные", "Popular" -> "popular"
            "Смотрят сейчас", "Now Playing" -> "now_playing"
            "Ожидаемые", "Upcoming" -> "upcoming"
            "Лучшие", "Top Rated" -> "top_rated"
            else -> "popular"
        }

        categoryAPI.getCategory(categoryPss, 1)
            .enqueue(object : retrofit2.Callback<CategoryDTO> {
                override fun onResponse(
                    call: retrofit2.Call<CategoryDTO>,
                    response: retrofit2.Response<CategoryDTO>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { listener.onLoaded(it) }
                    } else {
                        listener.onFailed(Exception(response.message()))
                        Log.e("DEBUGLOG", "FAIL CONNECTION $response")
                    }
                }

                override fun onFailure(call: retrofit2.Call<CategoryDTO>, t: Throwable) {
                    listener.onFailed(t)
                }

            })

    }

    interface OnCategoryLoadListener {
        fun onLoaded(categoryDTO: CategoryDTO)
        fun onFailed(throwable: Throwable)
    }
}

