package com.mrt.androidrecipesapp.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mrt.androidrecipesapp.R
import com.mrt.androidrecipesapp.databinding.ActivityMainBinding
import com.mrt.androidrecipesapp.model.Category
import com.mrt.androidrecipesapp.model.Recipe
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("binding = null")

    private val threadPool: ExecutorService = Executors.newFixedThreadPool(10)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("!!!", "Метод onCreate() выполняется на потоке: ${Thread.currentThread().name}")

        val thread = Thread {

            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val client: OkHttpClient = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()

            val request: Request = Request.Builder()
                .url("https://recipes.androidsprint.ru/api/category")
                .build()

            val json = client.newCall(request).execute().body?.string()

            val listType = object : TypeToken<List<Category>>() {}.type
            val categories: List<Category> = Gson().fromJson(json, listType)

            categories.forEach { Log.d("!!!", "Category: ${it.id} - ${it.title}") }
            val categoriesId = categories.map { it.id }

            Log.d("!!!", "Выполняю запрос на потоке: ${Thread.currentThread().name}")

            categoriesId.forEach {
                threadPool.execute {

                    val loggingInterceptorRecipes = HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    }

                    val clientRecipes = OkHttpClient.Builder()
                        .addInterceptor(loggingInterceptorRecipes)
                        .build()

                    val requestRecipes: Request = Request.Builder()
                        .url("https://recipes.androidsprint.ru/api/category/$it/recipes")
                        .build()

                    val jsonRecipes = clientRecipes.newCall(requestRecipes).execute().body?.string()

                    val listTypeRecipes = object : TypeToken<List<Recipe>>() {}.type
                    val recipes: List<Recipe> = Gson().fromJson(jsonRecipes, listTypeRecipes)

                    println("Рецепты для категории $it: $recipes")

                    Log.d("!!!", "Выполняю запрос на потоке: ${Thread.currentThread().name}")
                }
            }

        }
        thread.start()

        val navController =
            (supportFragmentManager.findFragmentById(R.id.mainContainer) as NavHostFragment).navController

        binding.buttonCategory.setOnClickListener {
            if (navController.currentDestination?.id != R.id.categoriesListFragment) {
                navController.navigate(
                    R.id.action_global_categoriesListFragment,
                    null,
                )
            }
        }
        binding.buttonFavorites.setOnClickListener {
            if (navController.currentDestination?.id != R.id.favoritesFragment) {
                navController.navigate(
                    R.id.action_global_favoritesFragment,
                    null,
                )
            }
        }
    }
}