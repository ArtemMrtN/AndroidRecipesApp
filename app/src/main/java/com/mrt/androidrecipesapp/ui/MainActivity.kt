package com.mrt.androidrecipesapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
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
            findNavController(R.id.mainContainer).navigate(R.id.categoriesListFragment)
//            supportFragmentManager.commit {
//                replace<CategoriesListFragment>(R.id.mainContainer)
//                setReorderingAllowed(true)
//                addToBackStack(null)
//            }
        }
        binding.buttonFavorites.setOnClickListener {
            findNavController(R.id.mainContainer).navigate(R.id.favoritesFragment)
//            supportFragmentManager.commit {
//                replace<FavoritesFragment>(R.id.mainContainer)
//                setReorderingAllowed(true)
//                addToBackStack(null)
//            }
        }
    }
}