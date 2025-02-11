package com.mrt.androidrecipesapp.ui.recipes.favorites

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mrt.androidrecipesapp.data.RecipesRepository
import com.mrt.androidrecipesapp.model.Recipe
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

    private fun getFavoritesByIds() {
        viewModelScope.launch {
            try {
                val favorites = recipesRepository.getFavoritesRecipesFromCache()
                _state.postValue(
                    _state.value?.copy(
                        recipes = favorites
                    )
                )

            } catch (e: Exception) {
                Log.e("!!!", "Не удалось загрузить рецепты")
                Toast.makeText(application, "Ошибка получения данных", Toast.LENGTH_SHORT).show()
            }
        }
    }
}