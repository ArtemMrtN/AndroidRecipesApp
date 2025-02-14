package com.mrt.androidrecipesapp.ui.recipes.recipes_list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrt.androidrecipesapp.data.BASE_URL
import com.mrt.androidrecipesapp.data.RecipesRepository
import com.mrt.androidrecipesapp.model.Category
import com.mrt.androidrecipesapp.model.Recipe
import kotlinx.coroutines.launch

class RecipesListViewModel(
    private val recipesRepository: RecipesRepository,
) : ViewModel() {

    private var _state = MutableLiveData(RecipesListState())
    val state: LiveData<RecipesListState> get() = _state

    data class RecipesListState(
        val recipes: List<Recipe>? = null,
        val currentCategory: Category? = null,
        val categoryImage: String? = null,
        val isShowError: Boolean = false,
    )

    fun loadRecipesList(categoryId: Int) {
        viewModelScope.launch {
            val recipesFromCache = recipesRepository.getRecipesListFromCache(categoryId)
            _state.postValue(
                _state.value?.copy(
                    recipes = recipesFromCache
                )
            )

            val recipes = recipesRepository.getRecipesByCategoryId(categoryId) ?: emptyList()

            if (recipes.isEmpty()) {
                _state.postValue(
                    _state.value?.copy(
                        isShowError = true
                    )
                )
            } else {
                val favorites = recipesRepository.getFavoritesRecipesFromCache().map { it.id }
                val updatedRecipes = recipes.map { recipe ->
                    recipe.copy(isFavorite = favorites.contains(recipe.id))
                }

                recipesRepository.addRecipesToCache(updatedRecipes, categoryId)
                _state.postValue(
                    _state.value?.copy(
                        recipes = recipes
                    )
                )
            }
        }
    }

    fun loadCurrentCategory(categoryId: Int) {
        viewModelScope.launch {
            val category = recipesRepository.getCategoryById(categoryId)
            _state.postValue(
                _state.value?.copy(
                    currentCategory = category,
                    categoryImage = "${BASE_URL}images/${category?.imageUrl}"
                ) ?: RecipesListState(
                    currentCategory = category,
                    categoryImage = "${BASE_URL}images/${category?.imageUrl}"
                )
            )
            Log.i("!!!", "state.value?.currentCategory - ${state.value?.currentCategory}")
        }
    }
}