package com.mrt.androidrecipesapp.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mrt.androidrecipesapp.R
import com.mrt.androidrecipesapp.data.RecipesRepository
import com.mrt.androidrecipesapp.databinding.ActivityMainBinding
import com.mrt.androidrecipesapp.model.Category
import com.mrt.androidrecipesapp.model.Recipe
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("binding = null")

    private val threadPool: ExecutorService = Executors.newFixedThreadPool(10)

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    private val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("!!!", "Метод onCreate() выполняется на потоке: ${Thread.currentThread().name}")

        val thread = Thread {

            try {

                val recipesRepository = RecipesRepository()

                val categoriesCall: Call<List<Category>> = recipesRepository.service.getCategories()
                val categoriesResponse = categoriesCall.execute()
                val categories: List<Category> = categoriesResponse.body() ?: emptyList()

                categories.forEach { Log.d("!!!", "Category: ${it.id} - ${it.title}") }
                val categoriesId = categories.map { it.id }

                Log.d("!!!", "Выполняю запрос на потоке: ${Thread.currentThread().name}")

                categoriesId.forEach {
                    threadPool.execute {

                        try {

                            val requestRecipes: Request = Request.Builder()
                                .url("https://recipes.androidsprint.ru/api/category/$it/recipes")
                                .build()

                            val jsonRecipes =
                                client.newCall(requestRecipes).execute().body?.string()
                                    ?: throw IllegalStateException("Ошибка загрузки товаров")

                            val listTypeRecipes = object : TypeToken<List<Recipe>>() {}.type
                            val recipes: List<Recipe> =
                                Gson().fromJson(jsonRecipes, listTypeRecipes)

                            println("Рецепты для категории $it: $recipes")

                            Log.d(
                                "!!!",
                                "Выполняю запрос на потоке: ${Thread.currentThread().name}"
                            )
                        } catch (e: Exception) {
                            Log.e("!!!", "Ошибка загрузки товаров", e)
                        }
                    }
                }

            } catch (e: Exception) {
                Log.e("!!!", "Ошибка загрузки категорий", e)
            }

        }
//        thread.start()

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