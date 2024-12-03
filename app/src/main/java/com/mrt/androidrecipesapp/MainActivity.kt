package com.mrt.androidrecipesapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mrt.androidrecipesapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val categoriesListFragment = CategoriesListFragment()
        val transaction = supportFragmentManager.beginTransaction()

        transaction.replace(R.id.mainContainer, categoriesListFragment)

        transaction.commit()
    }
}