package com.mrt.androidrecipesapp.ui.recipes.recipes_list

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mrt.androidrecipesapp.data.BASE_URL
import com.mrt.androidrecipesapp.data.RecipesRepository
import com.mrt.androidrecipesapp.model.Category
import com.mrt.androidrecipesapp.model.Recipe
import kotlinx.coroutines.launch

class RecipesListViewModel(private val application: Application) :
    AndroidViewModel(application) {

    private var _state = MutableLiveData(RecipesListState())
    val state: LiveData<RecipesListState> get() = _state

    @SuppressLint("StaticFieldLeak")
    private val recipesRepository = RecipesRepository(application)

    data class RecipesListState(
        val recipes: List<Recipe>? = null,
        val currentCategory: Category? = null,
        val categoryImage: String? = null,
    )

    fun loadRecipesList(categoryId: Int) {
        viewModelScope.launch {
            try {
                val recipesFromCache = recipesRepository.getRecipesListFromCache(categoryId)
                _state.postValue(
                    _state.value?.copy(
                        recipes = recipesFromCache
                    )
                )

                val recipes = recipesRepository.getRecipesByCategoryId(categoryId)
                val favorites = recipesRepository.getFavoritesRecipesFromCache().map { it.id }
                val updatedRecipes = recipes?.map { recipe ->
                    recipe.copy(isFavorite = favorites.contains(recipe.id))
                } ?: emptyList()

                recipesRepository.addRecipesToCache(updatedRecipes, categoryId)
                _state.postValue(
                    _state.value?.copy(
                        recipes = recipes
                    )
                )

            } catch (e: Exception) {
                Log.e("!!!", "Ошибка загрузки категорий", e)
                Toast.makeText(application, "Ошибка получения данных", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun loadCurrentCategory(categoryId: Int) {
        viewModelScope.launch {
            try {
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
            } catch (e: Exception) {
                Log.e("!!!", "Ошибка загрузки категории", e)
                Toast.makeText(application, "Ошибка получения данных", Toast.LENGTH_SHORT).show()
            }
        }
    }
}