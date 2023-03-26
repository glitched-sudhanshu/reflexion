package com.example.reflexionai.views.adapters

import android.app.Activity
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.reflexionai.R
import com.example.reflexionai.databinding.MovieItemBinding
import com.example.reflexionai.models.entities.Movie

class MoviesListAdapter(private var movies: List<Movie>, private val fragment: Fragment) :
    RecyclerView.Adapter<MoviesListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            MovieItemBinding.inflate(LayoutInflater.from(fragment.activity), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val movieItem = movies[position]
        holder.itemMovieTitle.text = movieItem.Title
        holder.itemMovieYear.text = "(${movieItem.Year})"
        holder.itemMovieCast.text = "Cast: ${movieItem.Cast}"
        holder.itemMovieYtTrailer.text = "Youtube Trailer: ${movieItem.YouTubeTrailer}"
        holder.itemMovieRuntime.text = "Runtime: ${movieItem.Runtime}"
        Glide.with(fragment)
            .load(Uri.parse(movieItem.MoviePoster))
            .into(holder.moviePoster)
        holder.moviePoster.clipToOutline = true
        holder.imgFavMovie.setOnClickListener {
            if (movieItem.isLiked) {
                val drawable = ContextCompat.getDrawable(fragment.requireContext(),
                    R.drawable.ic_favorite_border)
                holder.imgFavMovie.setImageDrawable(drawable)
                movieItem.isLiked = false
                Toast.makeText(fragment.requireContext(), "${movieItem.Title} removed from favourites", Toast.LENGTH_SHORT).show()
            } else {
                val drawable = ContextCompat.getDrawable(fragment.requireContext(),
                    R.drawable.ic_favorite)
                holder.imgFavMovie.setImageDrawable(drawable)
                movieItem.isLiked = true
                Toast.makeText(fragment.requireContext(), "${movieItem.Title} added to favourites", Toast.LENGTH_SHORT).show()
            }
        }

        holder.itemMovieTitle.setOnClickListener {

            holder.itemMovieSummary.text = movieItem.ShortSummary
            holder.summaryContainer.isVisible = !holder.summaryContainer.isVisible


        }
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    fun submit(newList : List<Movie>)
    {
        movies = newList
        notifyDataSetChanged()
    }

    inner class MyViewHolder(view: MovieItemBinding) : RecyclerView.ViewHolder(view.root) {
        val itemMovieTitle = view.txtMovieTitle
        val itemMovieYear = view.txtMovieYear
        val itemMovieCast = view.txtMovieCast
        val itemMovieYtTrailer = view.txtMovieYtTrailer
        val itemMovieRuntime = view.txtMovieRuntime
        val moviePoster = view.moviePoster
        val imgFavMovie = view.imgFavMovie
        val itemMovieSummary = view.txtSummary
        val summaryContainer = view.summaryContainer

    }
}
