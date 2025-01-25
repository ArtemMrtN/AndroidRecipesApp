package com.mrt.androidrecipesapp.ui.categories

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mrt.androidrecipesapp.data.STUB
import com.mrt.androidrecipesapp.model.Category

class CategoriesListViewModel(private val application: Application) :
    AndroidViewModel(application) {

    private var _state = MutableLiveData(CategoriesState())
    val state: LiveData<CategoriesState> get() = _state

    data class CategoriesState(
        val categories: List<Category> = emptyList(),
    )

    fun loadCategories() {
        val categories = STUB.getCategories()

        _state.value = _state.value?.copy(
            categories = categories
        )
    }

}