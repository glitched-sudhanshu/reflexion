package com.example.reflexionai.models.database

import androidx.annotation.WorkerThread
import com.example.reflexionai.models.entities.Movie
import kotlinx.coroutines.flow.Flow


class MovieRepository(private val moviesDao: MoviesDao) {

    //updating db
    @WorkerThread
    suspend fun insertMovieData(movie: Movie){
        moviesDao.insertLikedMovieDetails(movie)
    }

    //taking data from db
    val allMoviesList: Flow<List<Movie>> = moviesDao.getAllMovies()

    //updating db
    @WorkerThread
    suspend fun updateMovieData(movie: Movie){
        moviesDao.updateLikedMovieDetails(movie)
    }

    //taking data from db
    val allLikedMovies  : Flow<List<Movie>> = moviesDao.getLikedMovie()
}
