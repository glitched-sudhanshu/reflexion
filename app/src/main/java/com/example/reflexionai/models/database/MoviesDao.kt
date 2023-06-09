package com.example.reflexionai.models.database

import androidx.room.*
import com.example.reflexionai.models.entities.Movie
import com.example.reflexionai.utils.Constants.MOVIES_TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
interface MoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieDetails(movie: Movie)

    @Query("SELECT isLiked FROM $MOVIES_TABLE_NAME WHERE imdbId = :id")
    suspend fun getMovieFromId(id : String) : Boolean?

    @Query("SELECT * FROM $MOVIES_TABLE_NAME ORDER BY imdbId")
    fun getAllMovies(): Flow<List<Movie>>

    @Query("SELECT COUNT(*) FROM $MOVIES_TABLE_NAME")
    fun getRowCount(): Flow<Int>

    @Update
    suspend fun updateLikedMovieDetails(movie: Movie)

    @Query("SELECT * FROM $MOVIES_TABLE_NAME WHERE isLiked = 1")
    fun getLikedMovie(): Flow<List<Movie>>

}