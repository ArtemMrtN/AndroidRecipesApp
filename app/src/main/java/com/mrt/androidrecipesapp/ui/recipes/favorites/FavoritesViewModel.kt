package com.mrt.androidrecipesapp.ui.recipes.favorites

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mrt.androidrecipesapp.data.STUB
import com.mrt.androidrecipesapp.model.Recipe
import com.mrt.androidrecipesapp.ui.RecipeFragment.Companion.FAVORITES
import com.mrt.androidrecipesapp.ui.RecipeFragment.Companion.FAVORITES_ID

class FavoritesViewModel(private val application: Application) :
    AndroidViewModel(application) {

    private var _state = MutableLiveData(FavoritesState())
    val state: LiveData<FavoritesState> get() = _state

    init {
        getFavorites()
    }

    data class FavoritesState(
        val recipes: List<Recipe> = emptyList(),
        val favoritesId: MutableSet<String> = mutableSetOf(),
    )

    fun getFavorites() {
        val sharedPrefs = application.getSharedPreferences(FAVORITES, Context.MODE_PRIVATE)
        val storedSet = sharedPrefs.getStringSet(FAVORITES_ID, emptySet()) ?: emptySet()
        _state.value = _state.value?.copy(
            favoritesId = HashSet(storedSet)
        )
    }

    fun getFavoritesByIds(favoritesId: Set<String>): List<Recipe> {
        val favoritesList = STUB.getRecipesByIds(favoritesId)
        return favoritesList
    }
}