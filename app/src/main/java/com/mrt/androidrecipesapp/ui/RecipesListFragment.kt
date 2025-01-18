package com.mrt.androidrecipesapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import com.mrt.androidrecipesapp.R
import com.mrt.androidrecipesapp.databinding.FragmentRecipesListBinding
import com.mrt.androidrecipesapp.model.Recipe
import com.mrt.androidrecipesapp.ui.recipes.recipes_list.RecipesListViewModel

class RecipesListFragment : Fragment() {

    private var _binding: FragmentRecipesListBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("binding = null")

    private val viewModel: RecipesListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val recipesList = viewModel.loadRecipesList(
            arguments?.getInt(CategoriesListFragment.ARG_CATEGORY_ID) ?: 0
        )

        viewModel.loadCurrentCategory(
            arguments?.getInt(CategoriesListFragment.ARG_CATEGORY_ID) ?: 0
        )
        initRecycler(recipesList)

        viewModel.state.observe(viewLifecycleOwner) { state ->
            binding.recipesListTitle.text = state.currentCategory?.title
            binding.recipesListImage.setImageDrawable(state.categoryImage)
            binding.recipesListImage.contentDescription =
                "${R.string.item_category_image} ${state.currentCategory?.title}"
        }

    }

    private fun initRecycler(recipesList: List<Recipe>) {
        val recipesListAdapter = RecipesListAdapter(recipesList)
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