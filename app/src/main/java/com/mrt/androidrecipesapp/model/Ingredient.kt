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
data class Ingredient(
    @PrimaryKey val quantity: String,
    @ColumnInfo(name = "unitOfMeasure") val unitOfMeasure: String,
    @ColumnInfo(name = "description") val description: String,
) : Parcelable