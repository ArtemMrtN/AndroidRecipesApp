package com.mrt.androidrecipesapp.di

interface Factory<T> {
    fun  create(): T
}