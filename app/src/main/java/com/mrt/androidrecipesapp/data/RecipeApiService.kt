package com.mrt.androidrecipesapp.data

import com.mrt.androidrecipesapp.model.Category
import com.mrt.androidrecipesapp.model.Recipe
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RecipeApiService {

    @GET("category")
    fun getCategories(): Call<List<Category>>

    @GET("category/{categoryId}/recipes")
    fun getRecipesByCategoryId(@Path("categoryId") categoryId: Int): Call<List<Recipe>>

    @GET("category/{categoryId}")
    fun getCategoryById(@Path("categoryId") categoryId: Int): Call<Category?>

    @GET("recipe/{recipeId}")
    fun getRecipeById(@Path("recipeId") recipeId: Int): Call<Recipe?>

    @GET("recipes?ids={id}")
    fun getRecipesByIds(@Path("id") id: Set<String>): Call<List<Recipe>>

}