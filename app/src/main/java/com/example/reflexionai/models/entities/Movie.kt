package com.example.reflexionai.models.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.reflexionai.utils.Constants.MOVIES_TABLE_NAME
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = MOVIES_TABLE_NAME)
data class Movie(
    @ColumnInfo(name= "cast")  val Cast: String,

    val Director: String,

    val Genres: String,

    @PrimaryKey
    @ColumnInfo(name = "imdbId") val IMDBID: String,

    @SerializedName("Movie Poster")
    @ColumnInfo(name = "moviePoster") val MoviePoster: String,

    val Rating: String,

    @ColumnInfo(name = "runtime") val Runtime: String,

    @SerializedName("Short Summary")
    @ColumnInfo(name = "shortSummary") val ShortSummary: String,

    val Summary: String,

    @ColumnInfo(name = "title") val Title: String,

    val Writers: String,

    @ColumnInfo(name = "year") val Year: String,

    @SerializedName("YouTube Trailer")
    @ColumnInfo(name = "youTubeTrailer") var YouTubeTrailer: String,

    @ColumnInfo(name = "isLiked") var isLiked : Boolean = false,
): Parcelable