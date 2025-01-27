package com.mrt.androidrecipesapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.navOptions
import com.mrt.androidrecipesapp.R
import com.mrt.androidrecipesapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("binding = null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonCategory.setOnClickListener {
            findNavController(R.id.mainContainer).navigate(
                R.id.categoriesListFragment,
                null,
                navOptions {
                    launchSingleTop = true
                })
        }
        binding.buttonFavorites.setOnClickListener {
            findNavController(R.id.mainContainer).navigate(
                R.id.favoritesFragment,
                null,
                navOptions {
                    launchSingleTop = true
                }
            )
        }
    }
}