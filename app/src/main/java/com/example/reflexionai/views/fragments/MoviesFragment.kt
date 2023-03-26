package com.example.reflexionai.views.fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
    private var _countOfMovies = 0;

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


        mMovieViewModel.countOfMovies.observe(viewLifecycleOwner) { countOfMovies ->
            _countOfMovies = countOfMovies
            if (countOfMovies == 0) {
                mMovieViewModel.loadMovies()
                populateApiData()
            } else {
                populateRecyclerView()
            }
        }


        setupRecyclerView()
    }


    private fun populateRecyclerView() {
        mMovieViewModel.allMoviesList.observe(viewLifecycleOwner) { movies ->
            Log.d(TAG, "populateRecyclerView: ${movies.size}")
            _movieList = movies.toMutableList()
            movieAdapter.submit(_movieList)
        }
    }

    private fun populateApiData() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            mMovieViewModel.movieList.collect { movies ->
                for (item in movies) {
                    if (item.YouTubeTrailer == null) item.YouTubeTrailer = ""
                    mMovieViewModel.getIsLikedOrNot(item.IMDBID)
                        .observe(viewLifecycleOwner) { isLiked ->
                            if (isLiked != null) {
                                item.isLiked = isLiked
                            }
                            mMovieViewModel.insert(item)
                        }
                }
                populateRecyclerView()
            }
        }
    }


    private fun setupRecyclerView() = mBinding?.recyclerView?.apply {

        mMovieViewModel.allMoviesList.observe(viewLifecycleOwner) { movies ->
            _movieList = movies.toMutableList()
        }
        movieAdapter = MoviesListAdapter(_movieList, this@MoviesFragment, ::toggleLikedMovie)
        adapter = movieAdapter
        layoutManager = LinearLayoutManager(requireContext())

//        Toast.makeText(requireContext(), "$_countOfMovies", Toast.LENGTH_SHORT).show()
//        if(_countOfMovies!=0) {
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val lastVisibleItemPosition =
                        (layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                    val totalItemCount = movieAdapter.itemCount

                    if (lastVisibleItemPosition == totalItemCount - 1) {
                        if (mMovieViewModel.pageNumber.value == 1) {
                            mMovieViewModel.incrementPageNumber()
                            populateApiData()
                        }

                    }
                }
            })
//        }
    }

    private fun toggleLikedMovie(movie: Movie) {
        mMovieViewModel.update(movie)
        populateRecyclerView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding?.recyclerView?.clearOnScrollListeners()
    }
}