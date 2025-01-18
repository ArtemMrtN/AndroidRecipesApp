package com.mrt.androidrecipesapp.ui.recipes.recipes_list

import android.app.Application
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mrt.androidrecipesapp.data.STUB
import com.mrt.androidrecipesapp.model.Category
import com.mrt.androidrecipesapp.model.Recipe

class RecipesListViewModel(private val application: Application) :
    AndroidViewModel(application) {

    private var _state = MutableLiveData(RecipesListState())
    val state: LiveData<RecipesListState> get() = _state

    data class RecipesListState(
        val recipes: List<Recipe> = emptyList(),
        val currentCategory: Category? = null,
        val categoryImage: Drawable? = null,
    )

    fun loadRecipesList(categoryId: Int): List<Recipe> {
        val recipesList = STUB.getRecipesByCategoryId(categoryId)

        _state.value = _state.value?.copy(
            recipes = recipesList
        )
        return recipesList
    }

    fun loadCurrentCategory(categoryId: Int): Category {
        val category = STUB.getCategories().find { it.id == categoryId }
        val drawable = try {
            Drawable.createFromStream(
                application.applicationContext.assets.open(category?.imageUrl ?: "Image not found"),
                null
            )
        } catch (e: Exception) {
            Log.e("!!!", "Image not found ${category?.imageUrl}")
            null
        }
        _state.value = _state.value?.copy(
            currentCategory = category,
            categoryImage = drawable,
        )
        return category ?: throw IllegalStateException("Category with ID $categoryId not found")
    }
}