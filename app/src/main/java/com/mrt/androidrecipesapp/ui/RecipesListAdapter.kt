package com.mrt.androidrecipesapp.ui

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mrt.androidrecipesapp.R
import com.mrt.androidrecipesapp.model.Recipe
import com.mrt.androidrecipesapp.databinding.ItemRecipesBinding

class RecipesListAdapter(private val dataSet: List<Recipe>) :
    RecyclerView.Adapter<RecipesListAdapter.ViewHolder>() {

    private var itemClickListener: OnItemClickListener? = null

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

        val drawable =
            try {
                Drawable.createFromStream(
                    holder.itemView.context.assets.open(recipe.imageUrl),
                    null
                )
            } catch (e: Exception) {
                Log.e("!!!", "Image not found ${recipe.imageUrl}")
                null
            }
        holder.itemRecipeImage.setImageDrawable(drawable)
        holder.itemRecipeImage.contentDescription =
            "${R.string.item_category_image} ${recipe.title}"

        holder.itemView.setOnClickListener {
            itemClickListener?.onItemClick(recipe.id)
        }
    }

    override fun getItemCount() = dataSet.size
}