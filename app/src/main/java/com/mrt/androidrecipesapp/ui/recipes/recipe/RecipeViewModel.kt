package com.mrt.androidrecipesapp.ui.recipes.recipe

import androidx.lifecycle.ViewModel
import com.mrt.androidrecipesapp.model.Recipe

class RecipeViewModel : ViewModel() {

    data class RecipeState(
        val recipes: List<Recipe> = emptyList(),
        var isFavorites: Boolean = false,
        val isLoading: Boolean = false,
    )

}