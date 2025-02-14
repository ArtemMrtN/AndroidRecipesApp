package com.mrt.androidrecipesapp.ui.recipes.recipe

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrt.androidrecipesapp.data.RecipesRepository
import com.mrt.androidrecipesapp.model.Recipe
import kotlinx.coroutines.launch

class RecipeViewModel(
    private val recipesRepository: RecipesRepository,
) : ViewModel() {

    private var _state = MutableLiveData(RecipeState())
    val state: LiveData<RecipeState> get() = _state

    init {

        Log.i("!!!", "New state")

        _state.value = _state.value?.copy(isFavorites = true)
    }

    data class RecipeState(
        val recipe: Recipe? = null,
        val quantity: Int? = null,
        val isFavorites: Boolean = false,
        val isLoading: Boolean = false,
        val portionsCount: Int = 1,
    )

    fun loadRecipe(recipeId: Int) {
        viewModelScope.launch {
            val recipe = recipesRepository.getRecipeById(recipeId)

            _state.postValue(
                _state.value?.copy(
                    isFavorites = recipesRepository.getFavoritesRecipesFromCache()
                        .any { it.id == recipeId },
                    recipe = recipe,
                )
            )
            Log.i("!!!", "${recipe?.imageUrl}")
        }
    }

    fun onFavoritesClicked(recipeId: Int) {
        viewModelScope.launch {
            val favorites = recipesRepository.getFavoritesRecipesFromCache()
            if (favorites.any { it.id == recipeId }) {
                recipesRepository.removeRecipeToFavoritesToCache(recipeId)
                _state.value = _state.value?.copy(isFavorites = false)
            } else {
                recipesRepository.addRecipeToFavoritesToCache(recipeId)
                _state.value = _state.value?.copy(isFavorites = true)
            }
        }
    }

    fun updatePortionsCount(newCount: Int) {
        _state.value = _state.value?.copy(
            portionsCount = newCount
        )
    }

}