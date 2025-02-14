package com.mrt.androidrecipesapp.ui.recipes.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrt.androidrecipesapp.data.RecipesRepository
import com.mrt.androidrecipesapp.model.Recipe
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val recipesRepository: RecipesRepository,
) : ViewModel() {

    private var _state = MutableLiveData(FavoritesState())
    val state: LiveData<FavoritesState> get() = _state

    init {
        getFavoritesByIds()
    }

    data class FavoritesState(
        val recipes: List<Recipe>? = null,
        val isShowError: Boolean = false,
    )

    private fun getFavoritesByIds() {
        viewModelScope.launch {
            val favorites = recipesRepository.getFavoritesRecipesFromCache()

            if (favorites.isEmpty()) {
                _state.postValue(
                    _state.value?.copy(
                        isShowError = true
                    )
                )
            } else {
                _state.postValue(
                    _state.value?.copy(
                        recipes = favorites
                    )
                )
            }
        }
    }
}