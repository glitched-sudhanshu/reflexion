package com.example.reflexionai.views.fragments

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reflexionai.application.MoviesApplication
import com.example.reflexionai.databinding.FragmentFavouritesBinding
import com.example.reflexionai.models.entities.Movie
import com.example.reflexionai.viewmodels.MoviesViewModel
import com.example.reflexionai.viewmodels.MoviesViewModelFactory
import com.example.reflexionai.views.adapters.MoviesListAdapter

class FavouritesFragment : Fragment() {

    private var mBinding : FragmentFavouritesBinding? = null
    private var _movieList = mutableListOf<Movie>()
    private val mMovieViewModel: MoviesViewModel by viewModels {
        MoviesViewModelFactory((requireActivity().application as MoviesApplication).repository)
    }

    private lateinit var movieAdapter: MoviesListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentFavouritesBinding.inflate(layoutInflater, container, false)
        return mBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        populateRecyclerView()
    }

    private fun setupRecyclerView() = mBinding?.rvMovies?.apply {

        mMovieViewModel.allLikedMovies.observe(viewLifecycleOwner){
            _movieList = it as MutableList<Movie>
            movieAdapter.submit(it)
        }
        movieAdapter = MoviesListAdapter(_movieList, this@FavouritesFragment, ::toggleLikedMovie)
        adapter = movieAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }

    private fun populateRecyclerView() {
        mMovieViewModel.allLikedMovies.observe(viewLifecycleOwner) {
            Log.d(TAG, "populateRecyclerView: ${it.size}")
            _movieList = it as MutableList<Movie>
            movieAdapter.submit(it)
        }
    }

    private fun toggleLikedMovie(movie: Movie) {
        mMovieViewModel.update(movie)
        populateRecyclerView()
    }

}