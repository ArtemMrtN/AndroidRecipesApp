package com.mrt.androidrecipesapp.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.mrt.androidrecipesapp.model.Ingredient

class Converters {
    @TypeConverter
    fun fromIngredientsList(ingredients: List<Ingredient>): String {
        return Gson().toJson(ingredients)
    }

    @TypeConverter
    fun toIngredientsList(data: String): List<Ingredient> {
        return Gson().fromJson(data, Array<Ingredient>::class.java).toList()
    }

    @TypeConverter
    fun fromMethodList(method: List<String>): String {
        return Gson().toJson(method)
    }

    @TypeConverter
    fun toMethodList(data: String): List<String> {
        return Gson().fromJson(data, Array<String>::class.java).toList()
    }
}