package com.example.reflexionai.views.fragments

import android.os.Bundle
import android.text.method.MovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

//    private val mMovieViewModel : MoviesViewModel by viewModels()
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

                movieAdapter.submit(movies)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            mMovieViewModel.isLoading.collect{ isLoading ->
                mBinding?.progressBar?.isVisible = isLoading
            }
        }

    }


    private fun setupRecyclerView() = mBinding?.recyclerView?.apply {
        movieAdapter = MoviesListAdapter(movieList, this@MoviesFragment)
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


}