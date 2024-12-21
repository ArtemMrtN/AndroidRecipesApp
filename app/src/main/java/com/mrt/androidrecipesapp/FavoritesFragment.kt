package com.mrt.androidrecipesapp

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.mrt.androidrecipesapp.RecipeFragment.Companion.FAVORITES
import com.mrt.androidrecipesapp.RecipeFragment.Companion.FAVORITES_ID
import com.mrt.androidrecipesapp.databinding.FragmentFavoritesBinding

class FavoritesFragment : Fragment() {

    private var _binding:FragmentFavoritesBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("binding = null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)

        initRecycler()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }

    private fun initRecycler() {
        val favoritesId = getFavorites()
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

        val recipe: Recipe? = STUB.getRecipeById(recipeId)
        val bundle = Bundle().apply { putParcelable(ARG_RECIPE, recipe) }

        parentFragmentManager.commit {
            replace<RecipeFragment>(R.id.mainContainer, args = bundle)
            setReorderingAllowed(true)
            addToBackStack(null)
        }
    }

    private fun getFavorites(): MutableSet<String> {
        val sharedPrefs = requireActivity().getSharedPreferences(FAVORITES, Context.MODE_PRIVATE)
        val storedSet = sharedPrefs.getStringSet(FAVORITES_ID, emptySet()) ?: emptySet()

        return HashSet(storedSet)
    }

    companion object {
        const val ARG_RECIPE = "arg recipe"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}