package com.mrt.androidrecipesapp.ui.recipes.recipe

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mrt.androidrecipesapp.data.RecipesRepository
import com.mrt.androidrecipesapp.model.Ingredient
import com.mrt.androidrecipesapp.model.Recipe
import com.mrt.androidrecipesapp.ui.RecipeFragment.Companion.FAVORITES
import com.mrt.androidrecipesapp.ui.RecipeFragment.Companion.FAVORITES_ID

class RecipeViewModel(private val application: Application) : AndroidViewModel(application) {

    private var _state = MutableLiveData(RecipeState())
    val state: LiveData<RecipeState> get() = _state

    @SuppressLint("StaticFieldLeak")
    private val recipesRepository = RecipesRepository()

    init {

        Log.i("!!!", "New state")

        _state.value = _state.value?.copy(isFavorites = true)
    }

    data class RecipeState(
        val recipes: List<Recipe> = emptyList(),
        val quantity: Int? = null,
        val isFavorites: Boolean = false,
        val isLoading: Boolean = false,
        val portionsCount: Int = 1,
        val recipeImage: Drawable? = null,
        val ingredients: List<Ingredient> = emptyList(),
        val baseIngredients: List<Ingredient> = emptyList()
    )

    fun loadRecipe(recipeId: Int): Recipe {
        recipesRepository.threadPool.execute {
            try {
                val recipe = recipesRepository.getRecipeById(recipeId)
                Log.d("!!!", "Выполняю запрос на потоке: ${Thread.currentThread().name}")
                val drawable = try {
                    Drawable.createFromStream(
                        application.applicationContext.assets.open(
                            recipe?.imageUrl ?: "Image not found"
                        ),
                        null
                    )
                } catch (e: Exception) {
                    Log.e("!!!", "Image not found ${recipe?.imageUrl}")
                    null
                }
                Handler(Looper.getMainLooper()).post {
                    if (recipe != null) {
                        _state.value = _state.value?.copy(
                            isFavorites = getFavorites().any { it.toIntOrNull() == recipeId },
                            portionsCount = 1,
                            recipeImage = drawable,
                            ingredients = recipe.ingredients,
                            baseIngredients = recipe.ingredients
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e("!!!", "Ошибка загрузки категорий", e)
            }
        }
    }

    fun getFavorites(): MutableSet<String> {
        val sharedPrefs = application.getSharedPreferences(FAVORITES, Context.MODE_PRIVATE)
        val storedSet = sharedPrefs.getStringSet(FAVORITES_ID, emptySet()) ?: emptySet()

        return HashSet(storedSet)
    }

    fun onFavoritesClicked(recipeId: Int) {
        val favorites = getFavorites()

        if (favorites.any { it.toIntOrNull() == recipeId }) {
            favorites.remove(recipeId.toString())
            _state.value = _state.value?.copy(isFavorites = false)
        } else {
            favorites.add(recipeId.toString())
            _state.value = _state.value?.copy(isFavorites = true)
        }
        saveFavorites(favorites)
    }

    fun updatePortionsCount(newCount: Int) {
        _state.value = _state.value?.copy(
            portionsCount = newCount
        )
    }

    private fun saveFavorites(id: Set<String>) {
        val sharedPrefs = application.getSharedPreferences(FAVORITES, Context.MODE_PRIVATE)
        sharedPrefs.edit()
            .putStringSet(FAVORITES_ID, id)
            .apply()
    }

}