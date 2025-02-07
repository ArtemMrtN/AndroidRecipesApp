package com.mrt.androidrecipesapp.ui.categories

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mrt.androidrecipesapp.data.RecipesRepository
import com.mrt.androidrecipesapp.model.Category
import kotlinx.coroutines.launch

class CategoriesListViewModel(private val application: Application) :
    AndroidViewModel(application) {

    private var _state = MutableLiveData(CategoriesState())
    val state: LiveData<CategoriesState> get() = _state

    @SuppressLint("StaticFieldLeak")
    private val recipesRepository = RecipesRepository(application)

    data class CategoriesState(
        val categories: List<Category>? = null,
    )

    fun loadCategories() {
        viewModelScope.launch {
            try {
                val categories = recipesRepository.getCategories()
                _state.postValue(
                    _state.value?.copy(
                        categories = categories
                    )
                )
            } catch (e: Exception) {
                Log.e("!!!", "Ошибка загрузки категорий", e)
                Toast.makeText(application, "Ошибка получения данных", Toast.LENGTH_SHORT).show()
            }
        }
    }

}