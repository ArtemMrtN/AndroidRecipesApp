package com.mrt.androidrecipesapp.ui.recipes.recipes_list

import android.annotation.SuppressLint
import android.app.Application
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mrt.androidrecipesapp.data.RecipesRepository
import com.mrt.androidrecipesapp.model.Category
import com.mrt.androidrecipesapp.model.Recipe

class RecipesListViewModel(private val application: Application) :
    AndroidViewModel(application) {

    private var _state = MutableLiveData(RecipesListState())
    val state: LiveData<RecipesListState> get() = _state

    @SuppressLint("StaticFieldLeak")
    private val recipesRepository = RecipesRepository()

    data class RecipesListState(
        val recipes: List<Recipe>? = null,
        val currentCategory: Category? = null,
        val categoryImage: Drawable? = null,
    )

    fun loadRecipesList(categoryId: Int) {
        recipesRepository.threadPool.execute {
            try {
                val recipes = recipesRepository.getRecipesByCategoryId(categoryId)
                Log.d("!!!", "Выполняю запрос на потоке: ${Thread.currentThread().name}")
                _state.postValue(
                    _state.value?.copy(
                        recipes = recipes
                    ) ?: RecipesListState(recipes = recipes)
                )
            } catch (e: Exception) {
                Log.e("!!!", "Ошибка загрузки категорий", e)
                Toast.makeText(application, "Ошибка получения данных", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun loadCurrentCategory(categoryId: Int) {
        recipesRepository.threadPool.execute {
            try {
                val category = recipesRepository.getCategoryById(categoryId)
                Log.d("!!!", "Выполняю запрос на потоке: ${Thread.currentThread().name}")
                val drawable = try {
                    Drawable.createFromStream(
                        application.applicationContext.assets.open(
                            category?.imageUrl ?: "Image not found"
                        ),
                        null
                    )
                } catch (e: Exception) {
                    Log.e("!!!", "Image not found ${category?.imageUrl}")
                    null
                }
                _state.postValue(
                    _state.value?.copy(
                        currentCategory = category,
                        categoryImage = drawable
                    ) ?: RecipesListState(currentCategory = category, categoryImage = drawable)
                )
            } catch (e: Exception) {
                Log.e("!!!", "Ошибка загрузки категории", e)
                Toast.makeText(application, "Ошибка получения данных", Toast.LENGTH_SHORT).show()
            }
        }
    }
}