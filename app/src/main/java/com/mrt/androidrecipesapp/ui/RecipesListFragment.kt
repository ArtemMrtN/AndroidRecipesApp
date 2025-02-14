package com.mrt.androidrecipesapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mrt.androidrecipesapp.R
import com.mrt.androidrecipesapp.RecipesApplication
import com.mrt.androidrecipesapp.data.ImageLoader
import com.mrt.androidrecipesapp.databinding.FragmentRecipesListBinding
import com.mrt.androidrecipesapp.ui.recipes.recipes_list.RecipesListViewModel

class RecipesListFragment : Fragment() {

    private var _binding: FragmentRecipesListBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("binding = null")

    private lateinit var viewModel: RecipesListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appContainer = (requireActivity().application as RecipesApplication).appContainer
        viewModel = appContainer.recipesListViewModelFactory.create()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val args: CategoriesListFragmentArgs by navArgs()
        val categoryId = args.Category.id

        viewModel.loadRecipesList(categoryId)
        viewModel.loadCurrentCategory(categoryId)
        initRecycler()

    }

    private fun initRecycler() {
        val recipesListAdapter = RecipesListAdapter(emptyList())
        binding.rvRecipesList.adapter = recipesListAdapter

        viewModel.state.observe(viewLifecycleOwner) { state ->
            binding.recipesListTitle.text = state.currentCategory?.title

            ImageLoader.loadImage(
                requireContext(),
                binding.recipesListImage,
                state.categoryImage
            )

            binding.recipesListImage.contentDescription =
                "${R.string.item_category_image} ${state.currentCategory?.title}"

            if (state.isShowError) {
                Toast.makeText(requireContext(), "Ошибка загрузки товаров", Toast.LENGTH_LONG)
                    .show()
            } else {
                (binding.rvRecipesList.adapter as RecipesListAdapter).updateRecipes(
                    state.recipes ?: emptyList()
                )
            }
        }

        recipesListAdapter.setOnItemClickListener(object :
            RecipesListAdapter.OnItemClickListener {
            override fun onItemClick(recipeId: Int) {
                openRecipeByRecipeId(recipeId)
            }
        })
    }

    private fun openRecipeByRecipeId(recipeId: Int) {

        val action =
            RecipesListFragmentDirections.actionRecipesListFragmentToRecipeFragment(recipeId)
        findNavController().navigate(action)

    }

}