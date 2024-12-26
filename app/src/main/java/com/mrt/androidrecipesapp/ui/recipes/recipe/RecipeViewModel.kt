package com.mrt.androidrecipesapp.ui.recipes.recipe

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mrt.androidrecipesapp.data.STUB
import com.mrt.androidrecipesapp.model.Recipe
import com.mrt.androidrecipesapp.ui.RecipeFragment.Companion.FAVORITES
import com.mrt.androidrecipesapp.ui.RecipeFragment.Companion.FAVORITES_ID

class RecipeViewModel(private val application: Application) : AndroidViewModel(application) {

    private var _state = MutableLiveData(RecipeState())
    val state: LiveData<RecipeState> get() = _state

    init {

        Log.i("!!!", "New state")

        _state = MutableLiveData(RecipeState().copy(isFavorites = true))
    }

    data class RecipeState(
        val recipes: List<Recipe> = emptyList(),
        val quantity: Int? = null,
        val isFavorites: Boolean = false,
        val isLoading: Boolean = false,
        val portionsCount: Int = 1
    )

    fun loadRecipe(recipeId: Int): Recipe {
        val recipe = STUB.getRecipeById(recipeId)
            ?: throw IllegalStateException("Recipe with ID $recipeId not found")
        _state = MutableLiveData(RecipeState().copy(
            isFavorites = getFavorites().any { it.toIntOrNull() == recipeId },
            portionsCount = 1
        )
        )
        return recipe
        TODO("load from network")
    }

    fun getFavorites(): MutableSet<String> {
        val sharedPrefs = application.getSharedPreferences(FAVORITES, Context.MODE_PRIVATE)
        val storedSet = sharedPrefs.getStringSet(FAVORITES_ID, emptySet()) ?: emptySet()

        return HashSet(storedSet)
    }

    fun onFavoritesClicked(recipeId: Int) {
        val favorites = getFavorites()

        if (favorites.any { it.toIntOrNull() == recipeId }) {
            favorites.remove(recipeId.toString())
            _state = MutableLiveData(RecipeState().copy(
                isFavorites = false
            )
            )
        } else {
            favorites.add(recipeId.toString())
            _state = MutableLiveData(RecipeState().copy(
                isFavorites = true
            )
            )
        }

        saveFavorites(favorites)
    }

    private fun saveFavorites(id: Set<String>) {
        val sharedPrefs = application.getSharedPreferences(FAVORITES, Context.MODE_PRIVATE)
        sharedPrefs.edit()
            .putStringSet(FAVORITES_ID, id)
            .apply()
    }

}