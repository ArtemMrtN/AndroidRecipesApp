package com.mrt.androidrecipesapp.ui.recipes.favorites

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mrt.androidrecipesapp.data.RecipesRepository
import com.mrt.androidrecipesapp.model.Recipe
import com.mrt.androidrecipesapp.ui.RecipeFragment.Companion.FAVORITES
import com.mrt.androidrecipesapp.ui.RecipeFragment.Companion.FAVORITES_ID
import kotlinx.coroutines.launch

class FavoritesViewModel(private val application: Application) :
    AndroidViewModel(application) {

    private var _state = MutableLiveData(FavoritesState())
    val state: LiveData<FavoritesState> get() = _state

    @SuppressLint("StaticFieldLeak")
    private val recipesRepository = RecipesRepository(application)

    init {
        getFavoritesByIds()
    }

    data class FavoritesState(
        val recipes: List<Recipe>? = null,
    )

    private fun getFavorites(): HashSet<String> {
        val sharedPrefs = application.getSharedPreferences(FAVORITES, Context.MODE_PRIVATE)
        val storedSet = sharedPrefs.getStringSet(FAVORITES_ID, emptySet()) ?: emptySet()
        return HashSet(storedSet)
    }

    private fun getFavoritesByIds() {
        val favoritesId = getFavorites().joinToString(",")
        viewModelScope.launch {
            try {
                val recipesList = recipesRepository.getRecipesByIds(favoritesId)

                _state.postValue(
                    _state.value?.copy(
                        recipes = recipesList
                    )
                )

            } catch (e: Exception) {
                Log.e("!!!", "Не удалось загрузить рецепты")
                Toast.makeText(application, "Ошибка получения данных", Toast.LENGTH_SHORT).show()
            }
        }
    }
}