package com.mrt.androidrecipesapp.di

import android.content.Context
import androidx.room.Room
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.mrt.androidrecipesapp.data.AppDatabase
import com.mrt.androidrecipesapp.data.BASE_URL
import com.mrt.androidrecipesapp.data.CategoriesDao
import com.mrt.androidrecipesapp.data.FavoritesDao
import com.mrt.androidrecipesapp.data.RecipeApiService
import com.mrt.androidrecipesapp.data.RecipesListDao
import com.mrt.androidrecipesapp.data.RecipesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

class AppContainer(context: Context) {

    private val db: AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "database"
    )
        .fallbackToDestructiveMigration()
        .build()

    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
    private val categoriesDao: CategoriesDao = db.categoriesDao()
    private val recipesListDao: RecipesListDao = db.recipesListDao()
    private val favoritesDao: FavoritesDao = db.favoritesDao()

    private val loggingInterceptor = HttpLoggingInterceptor()
        .apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    private val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val contentType = "application/json".toMediaType()
    private var retrofit: Retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(BASE_URL)
        .addConverterFactory(Json.asConverterFactory(contentType))
        .build()

    private var service: RecipeApiService = retrofit.create(RecipeApiService::class.java)

    val repository = RecipesRepository(
        recipesListDao = recipesListDao,
        categoriesDao = categoriesDao,
        favoritesDao = favoritesDao,
        service = service,
        defaultDispatcher = defaultDispatcher,
    )

    val categoriesListViewModelFactory = CategoriesListViewModelFactory(repository)
    val recipesListViewModelFactory = RecipesListViewModelFactory(repository)
    val favoritesViewModelFactory = FavoritesViewModelFactory(repository)
    val recipeViewModelFactory = RecipeViewModelFactory(repository)
}