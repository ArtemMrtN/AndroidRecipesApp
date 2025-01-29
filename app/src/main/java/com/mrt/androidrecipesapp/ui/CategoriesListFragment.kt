package com.mrt.androidrecipesapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mrt.androidrecipesapp.R
import com.mrt.androidrecipesapp.data.STUB
import com.mrt.androidrecipesapp.databinding.FragmentListCategoriesBinding
import com.mrt.androidrecipesapp.ui.categories.CategoriesListViewModel

class CategoriesListFragment : Fragment() {

    private var _binding: FragmentListCategoriesBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("binding = null")

    private val viewModel: CategoriesListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListCategoriesBinding.inflate(inflater, container, false)

        initRecycler()

        return binding.root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecycler() {
        val categoriesListAdapter = CategoriesListAdapter(emptyList())
        binding.rvCategories.adapter = categoriesListAdapter

        viewModel.state.observe(viewLifecycleOwner) { state ->
            categoriesListAdapter.updateCategories(state.categories)
        }

        categoriesListAdapter.setOnItemClickListener(object :
            CategoriesListAdapter.OnItemClickListener {
            override fun onItemClick(categoryId: Int) {
                openRecipesByCategoryId(categoryId)
            }
        })

        viewModel.loadCategories()
    }

    private fun openRecipesByCategoryId(categoryId: Int) {

        val category = STUB.getCategories().find { it.id == categoryId }
            ?: throw IllegalArgumentException("${R.string.category_with_id} $categoryId ${R.string.no_found}")

        val action = CategoriesListFragmentDirections
            .actionCategoriesListFragmentToRecipesListFragment(category)

        findNavController().navigate(action)

    }

}