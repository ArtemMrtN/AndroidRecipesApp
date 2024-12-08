package com.mrt.androidrecipesapp

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mrt.androidrecipesapp.databinding.ItemCategoryBinding

class CategoriesListAdapter(private val dataSet: List<Category>) :
    RecyclerView.Adapter<CategoriesListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding = ItemCategoryBinding.bind(view)

        val itemCategoryTitle: TextView = binding.itemCategoryTitle
        val itemCategoryDescription: TextView = binding.itemCategoryDescription
        val itemCategoryImage: ImageView = binding.itemCategoryImage

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_category, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        val category: Category = dataSet[position]
        viewHolder.itemCategoryTitle.text = category.title
        viewHolder.itemCategoryDescription.text = category.description

        val drawable =
            try {
                Drawable.createFromStream(
                    viewHolder.itemView.context.assets.open(category.imageUrl),
                    null
                )
            } catch (e: Exception) {
                Log.e("!!!", "Image not found ${category.imageUrl}")
                null
            }
        viewHolder.itemCategoryImage.setImageDrawable(drawable)

    }

    override fun getItemCount() = dataSet.size

}