package com.mrt.androidrecipesapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mrt.androidrecipesapp.model.Recipe

@Dao
interface FavoritesDao {

    @Query("SELECT * FROM recipe")
    fun getFavoritesRecipes(): List<Recipe>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addRecipe(recipes: List<Recipe>)

}