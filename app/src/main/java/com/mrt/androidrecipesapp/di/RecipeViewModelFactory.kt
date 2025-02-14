package com.mrt.androidrecipesapp.di

import com.mrt.androidrecipesapp.data.RecipesRepository
import com.mrt.androidrecipesapp.ui.recipes.recipe.RecipeViewModel

class RecipeViewModelFactory(
    private val recipesRepository: RecipesRepository,
    ) : Factory<RecipeViewModel> {
    override fun create(): RecipeViewModel {
        return RecipeViewModel(recipesRepository)
    }
}