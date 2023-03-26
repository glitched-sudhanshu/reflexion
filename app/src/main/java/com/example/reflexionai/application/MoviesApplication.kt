package com.example.reflexionai.application

import android.app.Application
import com.example.reflexionai.models.database.MovieRepository
import com.example.reflexionai.models.database.MoviesDatabase

class MoviesApplication : Application(){

    private val database by lazy{
        MoviesDatabase.getDatabase((this@MoviesApplication))
    }

    val repository by lazy {
        MovieRepository(database.moviesDao())
    }
}