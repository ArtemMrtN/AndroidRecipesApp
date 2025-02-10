package com.mrt.androidrecipesapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mrt.androidrecipesapp.model.Category
import com.mrt.androidrecipesapp.model.Recipe

@Database(entities = [Category::class, Recipe::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoriesDao(): CategoriesDao

    abstract fun recipesListDao(): RecipesListDao
}