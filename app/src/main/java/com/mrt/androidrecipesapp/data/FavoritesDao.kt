package com.mrt.androidrecipesapp.data

import androidx.room.Dao
import androidx.room.Query
import com.mrt.androidrecipesapp.model.Recipe

@Dao
interface FavoritesDao {

    @Query("SELECT * FROM recipe")
    fun getFavoritesRecipes(): List<Recipe>

    @Query("UPDATE recipe SET isFavorite = 1 WHERE id = :recipeId")
    fun addRecipeToFavorites(recipeId: Int)

    @Query("UPDATE recipe SET isFavorite = 0 WHERE id = :recipeId")
    fun deleteRecipe(recipeId: Int)

}