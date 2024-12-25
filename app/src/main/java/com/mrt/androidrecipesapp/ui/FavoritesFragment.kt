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
import com.mrt.androidrecipesapp.data.STUB
import com.mrt.androidrecipesapp.databinding.FragmentFavoritesBinding
import com.mrt.androidrecipesapp.ui.RecipesListFragment.Companion.RECIPE_ID
import com.mrt.androidrecipesapp.ui.recipes.recipe.RecipeViewModel

class FavoritesFragment : Fragment() {

    private var _binding:FragmentFavoritesBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("binding = null")

    private val viewModel: RecipeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        initRecycler()

    }

    private fun initRecycler() {
        val favoritesId = viewModel.getFavorites()
        if (favoritesId.isEmpty()) {
            binding.emptyTextView.visibility = View.VISIBLE
            binding.rvFavoritesList.visibility = View.GONE
        } else {
            val favoritesListAdapter = RecipesListAdapter(STUB.getRecipesByIds(favoritesId))
            binding.rvFavoritesList.adapter = favoritesListAdapter

            favoritesListAdapter.setOnItemClickListener(object :
                RecipesListAdapter.OnItemClickListener {
                override fun onItemClick(recipeId: Int) {
                    openRecipeByRecipeId(recipeId)
                }
            })
        }
    }

    private fun openRecipeByRecipeId(recipeId: Int) {

        val bundle = Bundle().apply { putInt(RECIPE_ID, recipeId) }

        parentFragmentManager.commit {
            replace<RecipeFragment>(R.id.mainContainer, args = bundle)
            setReorderingAllowed(true)
            addToBackStack(null)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}