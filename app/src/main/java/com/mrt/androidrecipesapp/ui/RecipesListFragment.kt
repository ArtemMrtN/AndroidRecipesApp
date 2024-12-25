package com.mrt.androidrecipesapp.ui

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.mrt.androidrecipesapp.R
import com.mrt.androidrecipesapp.data.STUB
import com.mrt.androidrecipesapp.databinding.FragmentRecipesListBinding

class RecipesListFragment : Fragment() {

    private var _binding: FragmentRecipesListBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("binding = null")

    private var categoryId: Int? = null
    private var categoryName: String? = null
    private var categoryImageUrl: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipesListBinding.inflate(inflater, container, false)

        initRecycler()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        categoryId = requireArguments().getInt(CategoriesListFragment.ARG_CATEGORY_ID)
        categoryName = requireArguments().getString(CategoriesListFragment.ARG_CATEGORY_NAME)
        categoryImageUrl =
            requireArguments().getString(CategoriesListFragment.ARG_CATEGORY_IMAGE_URL)

        binding.recipesListTitle.text = categoryName
        val drawable =
            try {
                Drawable.createFromStream(
                    view.context.assets.open(categoryImageUrl.toString()),
                    null
                )
            } catch (e: Exception) {
                Log.e("!!!", "Image not found $categoryImageUrl")
                null
            }
        binding.recipesListImage.setImageDrawable(drawable)
        binding.recipesListImage.contentDescription =
            "${R.string.item_category_image} $categoryName"

    }

    private fun initRecycler() {
        categoryId = requireArguments().getInt(CategoriesListFragment.ARG_CATEGORY_ID)
        val recipesListAdapter = RecipesListAdapter(STUB.getRecipesByCategoryId(categoryId ?: 0))
        binding.rvRecipesList.adapter = recipesListAdapter

        recipesListAdapter.setOnItemClickListener(object :
            RecipesListAdapter.OnItemClickListener {
            override fun onItemClick(recipeId: Int) {
                openRecipeByRecipeId(recipeId)
            }
        })
    }

    private fun openRecipeByRecipeId(recipeId: Int) {

        val bundle = Bundle().apply { putInt(RECIPE_ID, recipeId) }

        parentFragmentManager.commit {
            replace<RecipeFragment>(R.id.mainContainer, args = bundle)
            setReorderingAllowed(true)
            addToBackStack(null)
        }
    }

    companion object {
        const val RECIPE_ID = "recipe id"
    }

}