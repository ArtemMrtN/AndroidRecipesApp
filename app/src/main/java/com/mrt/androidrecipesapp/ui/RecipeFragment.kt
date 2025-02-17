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
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.mrt.androidrecipesapp.R
import com.mrt.androidrecipesapp.data.BASE_URL
import com.mrt.androidrecipesapp.data.ImageLoader
import com.mrt.androidrecipesapp.databinding.FragmentRecipeBinding
import com.mrt.androidrecipesapp.ui.recipes.recipe.RecipeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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

        val args: RecipeFragmentArgs by navArgs()
        val recipeId = args.recipeId

        viewModel.loadRecipe(recipeId)

        initUI()

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
    private fun initUI() {

        viewModel.state.observe(viewLifecycleOwner) { state ->

            Log.i("!!!", "isFavorite: ${state.isFavorites}")

            if (state.isFavorites) {
                binding.iconFavorites.setImageResource(R.drawable.ic_heart)
            } else {
                binding.iconFavorites.setImageResource(R.drawable.ic_heart_empty)
            }

            ImageLoader.loadImage(
                requireContext(),
                binding.recipesItemImage,
                "${BASE_URL}images/${state.recipe?.imageUrl}"
            )

            binding.recipeNumberOfServings.text = state.portionsCount.toString()
            (binding.rvIngredients.adapter as IngredientsAdapter)
                .updateIngredients(state.portionsCount, state.recipe?.ingredients ?: emptyList())

            binding.recipesItemTitle.text = state.recipe?.title
            binding.recipesItemImage.contentDescription = state.recipe?.title

            binding.iconFavorites.setOnClickListener {
                viewModel.onFavoritesClicked(state.recipe?.id ?: -1)
            }

            binding.rvMethod.adapter = MethodAdapter(state.recipe?.method ?: emptyList())

        }

        binding.recipeSeekBar.setOnSeekBarChangeListener(PortionSeekBarListener { progress ->
            viewModel.updatePortionsCount(progress)
        })

        binding.rvIngredients.adapter = IngredientsAdapter(emptyList())

        val divider =
            MaterialDividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL).apply {
                isLastItemDecorated = false
                dividerInsetStart = resources.getDimensionPixelSize(R.dimen.margin_three_fourths)
                dividerInsetEnd = resources.getDimensionPixelSize(R.dimen.margin_three_fourths)
                dividerColor = resources.getColor(R.color.light_gray)
                dividerThickness = resources.getDimensionPixelSize(R.dimen.margin_xxs)
            }
        binding.rvIngredients.addItemDecoration(divider)

        binding.rvMethod.addItemDecoration(divider)

    }

}