package com.mrt.androidrecipesapp.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mrt.androidrecipesapp.R
import com.mrt.androidrecipesapp.data.RecipesRepository
import com.mrt.androidrecipesapp.model.Recipe
import com.mrt.androidrecipesapp.databinding.ItemRecipesBinding

class RecipesListAdapter(private var dataSet: List<Recipe>) :
    RecyclerView.Adapter<RecipesListAdapter.ViewHolder>() {

    private var itemClickListener: OnItemClickListener? = null
    private val recipesRepository = RecipesRepository()

    interface OnItemClickListener {
        fun onItemClick(recipeId: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.itemClickListener = listener
    }

    class ViewHolder(binding: ItemRecipesBinding) : RecyclerView.ViewHolder(binding.root) {

        val itemRecipeTitle: TextView = binding.recipesItemTitle
        val itemRecipeImage: ImageView = binding.recipesItemImage
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemRecipesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe: Recipe = dataSet[position]
        holder.itemRecipeTitle.text = recipe.title

        Glide
            .with(holder.itemRecipeImage.context)
            .load("${recipesRepository.retrofit.baseUrl()}images/${recipe.imageUrl}")
            .centerCrop()
            .placeholder(R.drawable.img_placeholder)
            .error(R.drawable.img_error)
            .into(holder.itemRecipeImage)

        holder.itemRecipeImage.contentDescription =
            "${R.string.item_category_image} ${recipe.title}"

        holder.itemView.setOnClickListener {
            itemClickListener?.onItemClick(recipe.id)
        }
    }

    override fun getItemCount() = dataSet.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateRecipes(newRecipes: List<Recipe>) {
        dataSet = newRecipes
        notifyDataSetChanged()
    }
}