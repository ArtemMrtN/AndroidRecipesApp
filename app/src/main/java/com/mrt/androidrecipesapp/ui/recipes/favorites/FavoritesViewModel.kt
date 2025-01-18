package com.mrt.androidrecipesapp.ui.recipes.favorites

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.mrt.androidrecipesapp.data.STUB
import com.mrt.androidrecipesapp.model.Recipe
import com.mrt.androidrecipesapp.ui.RecipeFragment.Companion.FAVORITES
import com.mrt.androidrecipesapp.ui.RecipeFragment.Companion.FAVORITES_ID

class FavoritesViewModel(private val application: Application) :
    AndroidViewModel(application) {

    fun getFavorites(): MutableSet<String> {
        val sharedPrefs = application.getSharedPreferences(FAVORITES, Context.MODE_PRIVATE)
        val storedSet = sharedPrefs.getStringSet(FAVORITES_ID, emptySet()) ?: emptySet()

        return HashSet(storedSet)
    }

    fun getFavoritesByIds(favoritesId: Set<String>): List<Recipe> {
        val favoritesList = STUB.getRecipesByIds(favoritesId)
        return favoritesList
    }
}