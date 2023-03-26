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
import com.example.reflexionai.application.MoviesApplication
import com.example.reflexionai.databinding.FragmentFavouritesBinding
import com.example.reflexionai.viewmodels.MoviesViewModel
import com.example.reflexionai.viewmodels.MoviesViewModelFactory

class FavouritesFragment : Fragment() {

    private var mBinding : FragmentFavouritesBinding? = null

    private val mFavMovieViewModel: MoviesViewModel by viewModels {
        MoviesViewModelFactory((requireActivity().application as MoviesApplication).repository)
    }

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

//        val sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
//        var liked = sharedPreferences.getBoolean("movieLast", false)
//        if(liked){
//            Toast.makeText(requireContext(), "hogya bc", Toast.LENGTH_SHORT).show()
//        }

        mFavMovieViewModel.allMoviesList.observe(viewLifecycleOwner){
            movies ->
            run {
                movies.let {
                    for (item in it) {
                        //got our movies
                        Log.d(TAG, "movie: ${item.Title} is ${item.isLiked}")
                    }
                }
            }
        }
    }

}