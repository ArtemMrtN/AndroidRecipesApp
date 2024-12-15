package com.mrt.androidrecipesapp

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mrt.androidrecipesapp.databinding.FragmentRecipeBinding

class RecipeFragment : Fragment() {

    private var _binding: FragmentRecipeBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("binding = null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun  onViewCreated(view: View, savedInstanceState: Bundle?) {

        val recipe: Recipe = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(RecipesListFragment.ARG_RECIPE, Recipe::class.java)
        } else {
            arguments?.getParcelable(RecipesListFragment.ARG_RECIPE)
        } ?: throw IllegalStateException("arguments is missing")

        binding.recipesItemTitle.text = recipe.title

        val drawable =
            try {
                Drawable.createFromStream(
                    view.context.assets.open(recipe.imageUrl),
                    null
                )
            } catch (e: Exception) {
                Log.e("!!!", "Image not found ${recipe.imageUrl}")
                null
            }
        binding.recipesItemImage.setImageDrawable(drawable)
        binding.recipesItemImage.contentDescription = recipe.title

    }
}