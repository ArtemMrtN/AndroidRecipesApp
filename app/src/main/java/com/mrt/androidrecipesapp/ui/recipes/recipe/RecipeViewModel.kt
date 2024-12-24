package com.mrt.androidrecipesapp.ui.recipes.recipe

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mrt.androidrecipesapp.model.Recipe

class RecipeViewModel : ViewModel() {

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
    )

}