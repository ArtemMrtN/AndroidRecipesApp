package com.mrt.androidrecipesapp.data

import android.util.Log
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.mrt.androidrecipesapp.model.Category
import com.mrt.androidrecipesapp.model.Recipe
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class RecipesRepository {

    val threadPool: ExecutorService = Executors.newFixedThreadPool(10)

    private val contentType = "application/json".toMediaType()

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    private val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private var retrofit: Retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl("https://recipes.androidsprint.ru/api/")
        .addConverterFactory(Json.asConverterFactory(contentType))
        .build()

    var service: RecipeApiService = retrofit.create(RecipeApiService::class.java)

    fun getCategories(): List<Category>? {
        return try {
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

    fun getRecipesByCategoryId(categoryId: Int): List<Recipe>? {
        return try {
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

    fun getCategoryById(categoryId: Int): Category? {
        return try {
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

    fun getRecipeById(recipeId: Int): Recipe? {
        return try {
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

    fun getRecipesByIds(id: String): List<Recipe>? {
        return try {
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