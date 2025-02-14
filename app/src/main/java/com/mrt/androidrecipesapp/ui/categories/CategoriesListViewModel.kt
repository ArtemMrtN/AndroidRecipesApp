package com.mrt.androidrecipesapp.ui.categories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrt.androidrecipesapp.data.RecipesRepository
import com.mrt.androidrecipesapp.model.Category
import kotlinx.coroutines.launch

class CategoriesListViewModel(
    private val recipesRepository: RecipesRepository,
) : ViewModel() {

    private var _state = MutableLiveData(CategoriesState())
    val state: LiveData<CategoriesState> get() = _state

    data class CategoriesState(
        val categories: List<Category>? = null,
    )

    fun loadCategories() {
        viewModelScope.launch {
            try {
                val categoriesFromCache = recipesRepository.getCategoriesFromCache()
                _state.postValue(
                    _state.value?.copy(
                        categories = categoriesFromCache
                    )
                )
                Log.i("!!!", "categoriesFromCache - ${state.value?.categories}")

                val categories = recipesRepository.getCategories()
                recipesRepository.addCategoryToCache(categories ?: emptyList())
                Log.i("!!!", "categories - ${state.value?.categories}")

            } catch (e: Exception) {
                Log.e("!!!", "Ошибка загрузки категорий", e)
            }
        }
    }
}