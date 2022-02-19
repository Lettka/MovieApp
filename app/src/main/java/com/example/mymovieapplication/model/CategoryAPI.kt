package com.example.mymovieapplication.model

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CategoryAPI {

    @GET("3/movie/{categoryPass}")
    fun getCategory(
        @Path("categoryPass") categoryPass: String,
        @Query("page") page: Int,
    ): Call<CategoryDTO>
}
