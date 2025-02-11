package com.mrt.androidrecipesapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mrt.androidrecipesapp.model.Recipe

@Dao
interface RecipesListDao {
    @Query("SELECT * FROM recipe")
    fun getAllRecipes(): List<Recipe>

    @Query("SELECT * FROM recipe WHERE categoryId = :categoryId")
    fun getRecipesByCategory(categoryId: Int): List<Recipe>

//    @Query("UPDATE recipe SET categoryId = :categoryId")
//    fun addRecipe(recipes: List<Recipe>, categoryId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addRecipe(recipes: List<Recipe>)

    @Query("UPDATE recipe SET isFavorite = :isFavorite WHERE id = :recipeId")
    fun updateFavoriteStatus(recipeId: Int, isFavorite: Boolean)
}