package com.example.reflexionai.models.network

import com.example.reflexionai.models.entities.Movie
import com.example.reflexionai.models.entities.MoviesList
import com.example.reflexionai.utils.Constants
import retrofit2.Response
import retrofit2.http.GET

interface MovieListAPI {

    @GET(Constants.API_ENDPOINT_MOVIE_1)
    suspend fun getMoviesList1() : Response<MoviesList>

    @GET(Constants.API_ENDPOINT_MOVIE_2)
    suspend fun getMoviesList2() : Response<MoviesList>

}