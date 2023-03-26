package com.example.reflexionai.views.fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reflexionai.application.MoviesApplication
import com.example.reflexionai.databinding.FragmentMoviesBinding
import com.example.reflexionai.models.entities.Movie
import com.example.reflexionai.viewmodels.MoviesViewModel
import com.example.reflexionai.viewmodels.MoviesViewModelFactory
import com.example.reflexionai.views.adapters.MoviesListAdapter


class MoviesFragment : Fragment() {

    private var mBinding: FragmentMoviesBinding? = null
    private var _movieList = mutableListOf<Movie>()
    private lateinit var movieAdapter: MoviesListAdapter
    private val mMovieViewModel: MoviesViewModel by viewModels {
        MoviesViewModelFactory((requireActivity().application as MoviesApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        mBinding = FragmentMoviesBinding.inflate(layoutInflater, container, false)
        return mBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()

        var countOfMovies = 0;

        mMovieViewModel.countOfMovies.observe(viewLifecycleOwner) {
            countOfMovies = it
        }

        if (countOfMovies == 0) {
            mMovieViewModel.loadMovies()

            viewLifecycleOwner.lifecycleScope.launchWhenCreated {
                mMovieViewModel.movieList.collect { movies ->
                    for (item in movies) {
                        if (item.YouTubeTrailer == null) item.YouTubeTrailer = "";
                        mMovieViewModel.getIsLikedOrNot(item.IMDBID)
                            .observe(viewLifecycleOwner) { isLiked ->
                                if (isLiked != null) {
                                    item.isLiked = isLiked
                                }
                            }
                        mMovieViewModel.insert(item)
                    }
                    populateRecyclerView()
                }
            }
        } else {
            populateRecyclerView()
        }
    }


    private fun populateRecyclerView() {
        mMovieViewModel.allMoviesList.observe(viewLifecycleOwner) {
            Log.d(TAG, "populateRecyclerView: ${it.size}")
            _movieList = it as MutableList<Movie>
            movieAdapter.submit(it)
        }
    }


    private fun setupRecyclerView() = mBinding?.recyclerView?.apply {

        mMovieViewModel.allMoviesList.observe(viewLifecycleOwner){
            _movieList = it as MutableList<Movie>
        }
        movieAdapter = MoviesListAdapter(_movieList, this@MoviesFragment, ::toggleLikedMovie)
        adapter = movieAdapter
        layoutManager = LinearLayoutManager(requireContext())

        addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastVisibleItemPosition =
                    (layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                val totalItemCount = movieAdapter.itemCount

                if (lastVisibleItemPosition == totalItemCount - 1) {
                    mMovieViewModel.incrementPageNumber()
                }
            }
        })
    }

    private fun toggleLikedMovie(movie: Movie) {
        mMovieViewModel.update(movie)
        populateRecyclerView()
    }
}