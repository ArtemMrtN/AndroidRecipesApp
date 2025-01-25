package com.mrt.androidrecipesapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mrt.androidrecipesapp.R
import com.mrt.androidrecipesapp.databinding.FragmentFavoritesBinding
import com.mrt.androidrecipesapp.ui.RecipesListFragment.Companion.RECIPE_ID
import com.mrt.androidrecipesapp.ui.recipes.favorites.FavoritesViewModel

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("binding = null")

    private val viewModel: FavoritesViewModel by viewModels()

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
        val favoritesListAdapter = RecipesListAdapter(emptyList())
        binding.rvFavoritesList.adapter = favoritesListAdapter

        viewModel.state.observe(viewLifecycleOwner) { state ->
            if (state.recipes.isEmpty()) {
                binding.emptyTextView.visibility = View.VISIBLE
                binding.rvFavoritesList.visibility = View.GONE
            } else {
                favoritesListAdapter.updateRecipes(state.recipes)
            }
        }

        favoritesListAdapter.setOnItemClickListener(object :
            RecipesListAdapter.OnItemClickListener {
            override fun onItemClick(recipeId: Int) {
                openRecipeByRecipeId(recipeId)
            }
        })

    }

    private fun openRecipeByRecipeId(recipeId: Int) {

        val bundle = bundleOf(RECIPE_ID to recipeId)
        findNavController().navigate(R.id.recipeFragment, bundle)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}