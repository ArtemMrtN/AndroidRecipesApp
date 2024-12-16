package com.mrt.androidrecipesapp

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mrt.androidrecipesapp.databinding.ItemIngrediensBinding

class IngredientsAdapter(private var dataSet: List<Ingredient>) :
    RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {

        private var quantity: Int = 1

    class ViewHolder(binding: ItemIngrediensBinding) : RecyclerView.ViewHolder(binding.root) {

        val ingredientDescription: TextView = binding.ingredientDescription
        val ingredientUnitOfMeasure: TextView = binding.ingredientUnitOfMeasure
        val ingredientQuantity: TextView = binding.ingredientQuantity
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemIngrediensBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipeIngredient = dataSet[position]

        val adjustedQuantity = if (recipeIngredient.quantity.toIntOrNull() == null) {
            (recipeIngredient.quantity.toDouble() * quantity).toString()
        } else {
            (recipeIngredient.quantity.toInt() * quantity).toString()
        }

        holder.ingredientDescription.text = recipeIngredient.description
        holder.ingredientQuantity.text = adjustedQuantity
        holder.ingredientUnitOfMeasure.text = recipeIngredient.unitOfMeasure

    }

    override fun getItemCount() = dataSet.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateIngredients(progress: Int) {
        quantity = progress
        notifyDataSetChanged()
    }
}