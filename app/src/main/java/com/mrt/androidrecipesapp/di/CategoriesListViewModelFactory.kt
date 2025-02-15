package com.mrt.androidrecipesapp.di

import com.mrt.androidrecipesapp.data.RecipesRepository
import com.mrt.androidrecipesapp.ui.categories.CategoriesListViewModel

class CategoriesListViewModelFactory(
    private val recipesRepository: RecipesRepository,
) : Factory<CategoriesListViewModel> {

    override fun create(): CategoriesListViewModel {
        return CategoriesListViewModel(recipesRepository)
    }

}