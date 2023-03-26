package com.example.reflexionai.models.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.reflexionai.models.entities.Movie
import com.example.reflexionai.utils.Constants.DATABASE_NAME

@Database(entities = [Movie::class], version = 1)
abstract class MoviesDatabase : RoomDatabase() {

    companion object {
        //        Singleton prevents multiple instances of database opening at the same time.
        @Volatile
        private var INSTANCE: MoviesDatabase? = null

        fun getDatabase(context: Context): MoviesDatabase {
            //if INSTANCE is not null, then return it
            //if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MoviesDatabase::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE = instance
                //return instance
                instance
            }
        }
    }

    abstract fun moviesDao() : MoviesDao
}