package com.mrt.androidrecipesapp.data

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.room.Room
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.mrt.androidrecipesapp.model.Category
import com.mrt.androidrecipesapp.model.Recipe
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit

class RecipesRepository(application: Application) {

    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO

    private val contentType = "application/json".toMediaType()

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    private val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private var retrofit: Retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(BASE_URL)
        .addConverterFactory(Json.asConverterFactory(contentType))
        .build()

    private var service: RecipeApiService = retrofit.create(RecipeApiService::class.java)

    private val db: AppDatabase = Room.databaseBuilder(
        application.applicationContext,
        AppDatabase::class.java,
        "database-categories"
    ).build()

    private val categoriesDao: CategoriesDao = db.categoriesDao()

    suspend fun getCategoriesFromCache(): List<Category> =
        withContext(defaultDispatcher) {
            categoriesDao.getAllCategories()
        }

    suspend fun getCategories(): List<Category>? =
        withContext(defaultDispatcher) {
            try {
                val categoriesCall: Call<List<Category>> = service.getCategories()
                val categoriesResponse: Response<List<Category>> = categoriesCall.execute()
                if (categoriesResponse.isSuccessful) {
                    categoriesResponse.body()
                } else {
                    null
                }
            } catch (e: Exception) {
                Log.e("!!!", "Ошибка загрузки категорий", e)
                null
            }
        }

    suspend fun getRecipesByCategoryId(categoryId: Int): List<Recipe>? =
        withContext(defaultDispatcher) {
            try {
                val recipesCall: Call<List<Recipe>> = service.getRecipesByCategoryId(categoryId)
                val recipesResponse: Response<List<Recipe>> = recipesCall.execute()
                if (recipesResponse.isSuccessful) {
                    recipesResponse.body()
                } else {
                    null
                }
            } catch (e: Exception) {
                Log.e("!!!", "Ошибка загрузки рецептов", e)
                null
            }
        }

    suspend fun getCategoryById(categoryId: Int): Category? =
        withContext(defaultDispatcher) {
            try {
                val categoryCall: Call<Category?> = service.getCategoryById(categoryId)
                val categoryResponse: Response<Category?> = categoryCall.execute()
                if (categoryResponse.isSuccessful) {
                    categoryResponse.body()
                } else {
                    null
                }
            } catch (e: Exception) {
                Log.e("!!!", "Ошибка загрузки категории", e)
                null
            }
        }

    suspend fun getRecipeById(recipeId: Int): Recipe? =
        withContext(defaultDispatcher) {
            try {
                val recipeCall: Call<Recipe?> = service.getRecipeById(recipeId)
                val recipeResponse: Response<Recipe?> = recipeCall.execute()
                if (recipeResponse.isSuccessful) {
                    recipeResponse.body()
                } else {
                    null
                }
            } catch (e: Exception) {
                Log.e("!!!", "Ошибка загрузки Рецепта", e)
                null
            }
        }

    suspend fun getRecipesByIds(id: String): List<Recipe>? =
        withContext(defaultDispatcher) {
            try {
                val recipeCall: Call<List<Recipe>?> = service.getRecipesByIds(id)
                Log.i("!!!", "ids - $id")
                val recipeResponse: Response<List<Recipe>?> = recipeCall.execute()
                if (recipeResponse.isSuccessful) {
                    recipeResponse.body()
                } else {
                    null
                }
            } catch (e: Exception) {
                Log.e("!!!", "Ошибка загрузки Рецептов", e)
                null
            }
        }

}

const val BASE_URL = "https://recipes.androidsprint.ru/api/"