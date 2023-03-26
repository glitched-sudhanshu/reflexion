package com.example.reflexionai.viewmodels

import androidx.lifecycle.*
import com.example.reflexionai.models.database.MovieRepository
import com.example.reflexionai.models.entities.Movie
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class FavMovieViewModel(private val repository: MovieRepository) : ViewModel() {

    fun insert(movie : Movie) = viewModelScope.launch{
        repository.insertMovieData(movie)
    }

    val allMoviesList : LiveData<List<Movie>> = repository.allMoviesList.asLiveData()

}

class FavMovieViewModelFactory(private val repository: MovieRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(FavMovieViewModel::class.java)){
            return FavMovieViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}