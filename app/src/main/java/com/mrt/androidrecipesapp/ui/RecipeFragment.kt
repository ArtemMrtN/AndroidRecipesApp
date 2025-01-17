package com.mrt.androidrecipesapp.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.mrt.androidrecipesapp.R
import com.mrt.androidrecipesapp.model.Recipe
import com.mrt.androidrecipesapp.databinding.FragmentRecipeBinding
import com.mrt.androidrecipesapp.ui.recipes.recipe.RecipeViewModel

class RecipeFragment : Fragment() {

    private var _binding: FragmentRecipeBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("binding = null")

    private val viewModel: RecipeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val recipe = viewModel.loadRecipe(arguments?.getInt(RecipesListFragment.RECIPE_ID) ?: 0)

        initUI(recipe)

    }

    class PortionSeekBarListener(val onChangeIngredients: (Int) -> Unit) :
        SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            onChangeIngredients(progress)
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {}

        override fun onStopTrackingTouch(seekBar: SeekBar?) {}
    }

    @SuppressLint("SetTextI18n")
    private fun initUI(recipe: Recipe) {

        viewModel.state.observe(viewLifecycleOwner) { state ->
            Log.i("!!!", "isFavorite: ${state.isFavorites}")

            if (state.isFavorites) {
                binding.iconFavorites.setImageResource(R.drawable.ic_heart)
            } else {
                binding.iconFavorites.setImageResource(R.drawable.ic_heart_empty)
            }
            binding.recipesItemImage.setImageDrawable(state.recipeImage)

            binding.recipeNumberOfServings.text = state.portionsCount.toString()
            (binding.rvIngredients.adapter as IngredientsAdapter).updateIngredients(state.portionsCount)

        }

        binding.recipeSeekBar.setOnSeekBarChangeListener(PortionSeekBarListener { progress ->
            viewModel.updatePortionsCount(progress)
        })

        binding.rvIngredients.adapter = IngredientsAdapter(recipe.ingredients)

        binding.recipesItemTitle.text = recipe.title

        binding.recipesItemImage.contentDescription = recipe.title

        binding.iconFavorites.setOnClickListener {
            viewModel.onFavoritesClicked(recipe.id)
        }

        val divider =
            MaterialDividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL).apply {
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

    companion object {
        const val FAVORITES = "favorites"
        const val FAVORITES_ID = "favorites_id"
    }

}