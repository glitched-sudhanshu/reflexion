package com.example.reflexionai.views.fragments

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.text.method.MovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
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
    private val movieList = mutableListOf<Movie>()
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

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            mMovieViewModel.movieList.collect{ movies ->
//                movieAdapter.submit(movies)
                for(item in movies)
                {
                    if(item.YouTubeTrailer == null)item.YouTubeTrailer = "";
//                    if (mMovieViewModel.getIsLikedOrNot(item.IMDBID).value!! == true) item.isLiked = true
                    mMovieViewModel.insert(item)
                }
                populateRecyclerView()
            }
        }

//        populateRecyclerView()
//        mMovieViewModel.allMoviesList.observe(viewLifecycleOwner){
//            Log.d(TAG, "populateRecyclerView: ${it.size}")
//            movieAdapter.submit(it)
//        }
//        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
//            mMovieViewModel.isLoading.collect{ isLoading ->
//                mBinding?.progressBar?.isVisible = isLoading
//            }
//        }

    }

    private fun populateRecyclerView() {
        mMovieViewModel.allMoviesList.observe(viewLifecycleOwner){
            Log.d(TAG, "populateRecyclerView: ${it.size}")
            movieAdapter.submit(it)
        }
    }


    private fun setupRecyclerView() = mBinding?.recyclerView?.apply {
        movieAdapter = MoviesListAdapter(movieList, this@MoviesFragment, ::toggleLikedMovie)
        adapter = movieAdapter
        layoutManager = LinearLayoutManager(requireContext())

        addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastVisibleItemPosition =
                    (layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                val totalItemCount = movieAdapter.itemCount

                if (lastVisibleItemPosition == totalItemCount - 1) {
                    // We have reached the end of the list, load more movies
                    mMovieViewModel.incrementPageNumber()
                }
            }
        })
    }


    private fun toggleLikedMovie(movie: Movie){
//        Toast.makeText(requireContext(), "Asdfsadf", Toast.LENGTH_SHORT).show()
        mMovieViewModel.update(movie)
//        val sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
//        val editor = sharedPreferences.edit()
//        editor.putBoolean("movieLast", movie.isLiked)
//        editor.apply()
    }
}