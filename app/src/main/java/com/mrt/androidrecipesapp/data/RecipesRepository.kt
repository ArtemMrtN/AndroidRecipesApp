package com.mrt.androidrecipesapp.data

import android.util.Log
import com.mrt.androidrecipesapp.model.Category
import com.mrt.androidrecipesapp.model.Recipe
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response
import kotlin.coroutines.CoroutineContext

class RecipesRepository(
    private val recipesListDao: RecipesListDao,
    private val categoriesDao: CategoriesDao,
    private val favoritesDao: FavoritesDao,
    private val service: RecipeApiService,
    private val defaultDispatcher: CoroutineContext,
) {

    suspend fun getCategoriesFromCache(): List<Category> =
        withContext(defaultDispatcher) {
            categoriesDao.getAllCategories()
        }

    suspend fun addCategoryToCache(categories: List<Category>) =
        withContext(defaultDispatcher) {
            categoriesDao.addCategory(categories)
        }


    suspend fun getRecipesListFromCache(categoryId: Int): List<Recipe> =
        withContext(defaultDispatcher) {
            recipesListDao.getRecipesByCategory(categoryId)
        }

    suspend fun addRecipesToCache(recipes: List<Recipe>, categoryId: Int) =
        withContext(defaultDispatcher) {
            val updatedRecipes = recipes.map { it.copy(categoryId = categoryId) }
            recipesListDao.addRecipe(updatedRecipes)
        }

    suspend fun getFavoritesRecipesFromCache() =
        withContext(defaultDispatcher) {
            favoritesDao.getFavoritesRecipes().filter { it.isFavorite }
        }

    suspend fun addRecipeToFavoritesToCache(recipeId: Int) =
        withContext(defaultDispatcher) {
            favoritesDao.addRecipeToFavorites(recipeId)
            recipesListDao.updateFavoriteStatus(recipeId, true)
        }

    suspend fun removeRecipeToFavoritesToCache(recipeId: Int) =
        withContext(defaultDispatcher) {
            favoritesDao.deleteRecipe(recipeId)
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