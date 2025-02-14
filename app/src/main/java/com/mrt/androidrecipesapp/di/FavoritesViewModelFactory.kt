package com.mrt.androidrecipesapp.di

import com.mrt.androidrecipesapp.data.RecipesRepository
import com.mrt.androidrecipesapp.ui.recipes.favorites.FavoritesViewModel

class FavoritesViewModelFactory(
    private val recipesRepository: RecipesRepository,
) : Factory<FavoritesViewModel> {

    override fun create(): FavoritesViewModel {
        return FavoritesViewModel(recipesRepository)
    }

}