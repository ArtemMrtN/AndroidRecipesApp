package com.mrt.androidrecipesapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.mrt.androidrecipesapp.R
import com.mrt.androidrecipesapp.databinding.FragmentRecipesListBinding
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

        viewModel.loadRecipesList(
            arguments?.getInt(CategoriesListFragment.ARG_CATEGORY_ID) ?: 0
        )

        viewModel.loadCurrentCategory(
            arguments?.getInt(CategoriesListFragment.ARG_CATEGORY_ID) ?: 0
        )
        initRecycler()

    }

    private fun initRecycler() {
        val recipesListAdapter = RecipesListAdapter(emptyList())
        binding.rvRecipesList.adapter = recipesListAdapter

        viewModel.state.observe(viewLifecycleOwner) { state ->
            binding.recipesListTitle.text = state.currentCategory?.title
            binding.recipesListImage.setImageDrawable(state.categoryImage)
            binding.recipesListImage.contentDescription =
                "${R.string.item_category_image} ${state.currentCategory?.title}"
            (binding.rvRecipesList.adapter as RecipesListAdapter).updateRecipes(state.recipes)
        }

        recipesListAdapter.setOnItemClickListener(object :
            RecipesListAdapter.OnItemClickListener {
            override fun onItemClick(recipeId: Int) {
                openRecipeByRecipeId(recipeId)
            }
        })
    }

    private fun openRecipeByRecipeId(recipeId: Int) {

        val bundle = Bundle().apply { putInt(RECIPE_ID, recipeId) }

        findNavController().navigate(
            R.id.action_recipesListFragment_to_recipeFragment,
            bundle,
            navOptions {
                launchSingleTop = true
            }
        )

    }

    companion object {
        const val RECIPE_ID = "recipe id"
    }

}