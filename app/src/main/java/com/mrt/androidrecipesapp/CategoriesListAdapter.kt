package com.mrt.androidrecipesapp

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mrt.androidrecipesapp.databinding.ItemCategoryBinding

class CategoriesListAdapter(private val dataSet: List<Category>) :
    RecyclerView.Adapter<CategoriesListAdapter.ViewHolder>() {

    private var itemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.itemClickListener = listener
    }

    class ViewHolder(binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {

        val itemCategoryTitle: TextView = binding.itemCategoryTitle
        val itemCategoryDescription: TextView = binding.itemCategoryDescription
        val itemCategoryImage: ImageView = binding.itemCategoryImage

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
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
        viewHolder.itemCategoryImage.contentDescription =
            "${R.string.item_category_image} ${category.title}"

        viewHolder.itemView.setOnClickListener {
            itemClickListener?.onItemClick()
        }

    }

    override fun getItemCount() = dataSet.size

}