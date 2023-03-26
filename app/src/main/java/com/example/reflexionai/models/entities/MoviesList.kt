package com.example.reflexionai.models.entities

import com.google.gson.annotations.SerializedName

data class MoviesList(
    @SerializedName("Movie List")
    val MovieList: List<Movie>
)