package com.example.reflexionai.models.database

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
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

//    val movieWithId : Flow<Movie> = suspend fun  moviesDao.getMovieFromId(id)

    suspend fun getMovieWithId(id : String) : Boolean?{
        return moviesDao.getMovieFromId(id)
    }

//    @WorkerThread
//    suspend fun insertFrom2ndApi(movie: Movie){
//        moviesDao.insertFromSecondAPI(movie)
//    }

    //taking data from db
    val allLikedMovies  : Flow<List<Movie>> = moviesDao.getLikedMovie()
}
