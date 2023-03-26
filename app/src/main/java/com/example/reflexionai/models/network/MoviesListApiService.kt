package com.example.reflexionai.models.network

import com.example.reflexionai.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MoviesListApiService {

    val api : MovieListAPI by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MovieListAPI::class.java)
    }
}