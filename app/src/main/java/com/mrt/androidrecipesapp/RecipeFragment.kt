package com.mrt.androidrecipesapp

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.divider.MaterialDividerItemDecoration
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

        initUI(recipe)

        initRecycler(recipe)

    }

    private fun initUI(recipe: Recipe) {

        binding.recipesItemTitle.text = recipe.title

        val drawable =
            try {
                Drawable.createFromStream(
                    view?.context?.assets?.open(recipe.imageUrl),
                    null
                )
            } catch (e: Exception) {
                Log.e("!!!", "Image not found ${recipe.imageUrl}")
                null
            }
        binding.recipesItemImage.setImageDrawable(drawable)
        binding.recipesItemImage.contentDescription = recipe.title

    }

    private fun initRecycler(recipe: Recipe) {

        binding.rvIngredients.adapter = IngredientsAdapter(recipe.ingredients)

        val divider = MaterialDividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL).apply {
            isLastItemDecorated = false
            dividerInsetStart = resources.getDimensionPixelSize(R.dimen.margin_three_fourths)
            dividerInsetEnd = resources.getDimensionPixelSize(R.dimen.margin_three_fourths)
            dividerColor = resources.getColor(R.color.light_gray)
            dividerThickness = resources.getDimensionPixelSize(R.dimen.margin_xxs)
        }
        binding.rvIngredients.addItemDecoration(divider)

        binding.rvMethod.adapter = MethodAdapter(recipe.method)
        binding.rvMethod.addItemDecoration(divider)

    }
}