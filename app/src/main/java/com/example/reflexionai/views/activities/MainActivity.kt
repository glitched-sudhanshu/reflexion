package com.example.reflexionai.views.activities

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.reflexionai.R
import com.example.reflexionai.databinding.ActivityMainBinding
import com.example.reflexionai.models.entities.MoviesList
import com.example.reflexionai.models.network.MoviesListApiService
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import java.net.HttpRetryException

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mNavController: NavController



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.moviesFragment, R.id.favouritesFragment, R.id.profileFragment
            )
        )
        mNavController = findNavController(R.id.fragmentContainerView)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        setupActionBarWithNavController(mNavController, appBarConfiguration)
        NavigationUI.setupActionBarWithNavController(this, mNavController)
        mBinding.bottomNavigationView.setupWithNavController(mNavController)






    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(mNavController, null)
    }
}