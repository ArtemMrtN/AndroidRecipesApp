package com.mrt.androidrecipesapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mrt.androidrecipesapp.model.Category

@Database(entities = [Category::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoriesDao(): CategoriesDao
}