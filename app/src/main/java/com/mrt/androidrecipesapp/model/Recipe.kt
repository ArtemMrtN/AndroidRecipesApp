package com.mrt.androidrecipesapp.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
@Entity
data class Recipe(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "ingredients") val ingredients: List<Ingredient> = emptyList(),
    @ColumnInfo(name = "method") val method: List<String> = emptyList(),
    @ColumnInfo(name = "imageUrl") val imageUrl: String,
) : Parcelable