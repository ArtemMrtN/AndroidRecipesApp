package com.mrt.androidrecipesapp.ui.categories

import android.annotation.SuppressLint
import android.app.Application
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mrt.androidrecipesapp.data.RecipesRepository
import com.mrt.androidrecipesapp.model.Category

class CategoriesListViewModel(private val application: Application) :
    AndroidViewModel(application) {

    private var _state = MutableLiveData(CategoriesState())
    val state: LiveData<CategoriesState> get() = _state

    @SuppressLint("StaticFieldLeak")
    private val recipesRepository = RecipesRepository()

    data class CategoriesState(
        val categories: List<Category> = emptyList(),
    )

    fun loadCategories() {
        recipesRepository.threadPool.execute {
            try {
                val categories = recipesRepository.getCategories()
                Log.d("!!!", "Выполняю запрос на потоке: ${Thread.currentThread().name}")
                Handler(Looper.getMainLooper()).post {
                    _state.value = _state.value?.copy(categories = categories ?: emptyList())
                }
            } catch (e: Exception) {
                Log.e("!!!", "Ошибка загрузки категорий", e)
            }
        }
    }

}