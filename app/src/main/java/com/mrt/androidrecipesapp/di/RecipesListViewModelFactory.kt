package com.mrt.androidrecipesapp.di

import com.mrt.androidrecipesapp.data.RecipesRepository
import com.mrt.androidrecipesapp.ui.recipes.recipes_list.RecipesListViewModel

class RecipesListViewModelFactory(
    private val recipesRepository: RecipesRepository,
) : Factory<RecipesListViewModel> {

    override fun create(): RecipesListViewModel {
        return RecipesListViewModel(recipesRepository)
    }
    
}