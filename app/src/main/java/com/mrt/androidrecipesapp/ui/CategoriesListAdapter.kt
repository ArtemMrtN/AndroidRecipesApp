package com.mrt.androidrecipesapp.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mrt.androidrecipesapp.model.Category
import com.mrt.androidrecipesapp.R
import com.mrt.androidrecipesapp.data.RecipesRepository
import com.mrt.androidrecipesapp.databinding.ItemCategoryBinding

class CategoriesListAdapter(private var dataSet: List<Category>) :
    RecyclerView.Adapter<CategoriesListAdapter.ViewHolder>() {

    private var itemClickListener: OnItemClickListener? = null
    private val recipesRepository = RecipesRepository()

    interface OnItemClickListener {
        fun onItemClick(categoryId: Int)
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

        Glide
            .with(viewHolder.itemCategoryImage.context)
            .load("${recipesRepository.retrofit.baseUrl()}images/${category.imageUrl}")
            .centerCrop()
            .placeholder(R.drawable.img_placeholder)
            .error(R.drawable.img_error)
            .into(viewHolder.itemCategoryImage)

        viewHolder.itemCategoryImage.contentDescription =
            "${R.string.item_category_image} ${category.title}"

        viewHolder.itemView.setOnClickListener {
            itemClickListener?.onItemClick(category.id)
        }

    }

    override fun getItemCount() = dataSet.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateCategories(newRecipes: List<Category>) {
        dataSet = newRecipes
        notifyDataSetChanged()
    }

}