package com.example.reflexionai.viewmodels

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.*
import com.example.reflexionai.models.database.MovieRepository
import com.example.reflexionai.models.entities.Movie
import com.example.reflexionai.models.entities.MoviesList
import com.example.reflexionai.models.network.MoviesListApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.IOException
import java.lang.IllegalArgumentException
import java.net.HttpRetryException

class MoviesViewModel(private val repository: MovieRepository) : ViewModel() {

    private val _movieList = MutableStateFlow<List<Movie>>(emptyList())
    val movieList: StateFlow<List<Movie>> = _movieList

    private val _pageNumber = MutableStateFlow<Int>(1)
    val pageNumber: StateFlow<Int> = _pageNumber

    private val _isLoading = MutableStateFlow<Boolean>(true)
    val isLoading : StateFlow<Boolean> = _isLoading

    val allMoviesList : LiveData<List<Movie>> = repository.allMoviesList.asLiveData()

    val countOfMovies : LiveData<Int> = repository.countOfMovies.asLiveData()

    val allLikedMovies : LiveData<List<Movie>> = repository.allLikedMovies.asLiveData()

    init {
//        loadMovies()
    }

    fun incrementPageNumber() {
        if(_pageNumber.value == 1)
        {
            _pageNumber.value += 1
            loadMovies()
        }
    }

    fun insert(movie : Movie) = viewModelScope.launch{
        repository.insertMovieData(movie)
    }

    fun update(movie : Movie) = viewModelScope.launch {
        repository.updateMovieData(movie)
    }

    fun getIsLikedOrNot(id: String): LiveData<Boolean?> {
        return liveData {
            val movieIsLiked = withContext(Dispatchers.IO) {
                repository.getMovieWithId(id)
            }
            emit(movieIsLiked)
        }
    }

    fun loadMovies() {
        viewModelScope.launch {
            _isLoading.value = true
            val response: Response<MoviesList> = try {
                when (_pageNumber.value) {
                    1 -> MoviesListApiService.api.getMoviesList1()
                    2 -> MoviesListApiService.api.getMoviesList2()
                    else -> return@launch
                }
            }
            catch (e: IOException) {
                _isLoading.value = false
                Log.e(TAG, "onCreate:  ${e.printStackTrace()}")
                return@launch
            }
            catch (e: HttpRetryException) {
                _isLoading.value = false
                Log.e(TAG, "onCreate:  ${e.printStackTrace()}")
                return@launch
            }
            catch (e: Exception) {
                _isLoading.value = false
                Log.e(TAG, "onCreate:  ${e.printStackTrace()}")
                return@launch
            }
            Log.d(TAG, "loadMovies: called")
            if (response.isSuccessful && response.body() != null) {
                val newMovies = response.body()!!.MovieList
                val currentList = _movieList.value ?: emptyList()
                _movieList.value = currentList + newMovies
            } else {
                Log.d(TAG, "Response not successful")
            }
            _isLoading.value = false
        }
    }
}

class MoviesViewModelFactory(private val repository: MovieRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MoviesViewModel::class.java)){
            return MoviesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}