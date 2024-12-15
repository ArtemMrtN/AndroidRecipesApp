package com.mrt.androidrecipesapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.mrt.androidrecipesapp.databinding.FragmentListCategoriesBinding

class CategoriesListFragment : Fragment() {

    private var _binding: FragmentListCategoriesBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("binding = null")

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
        val categoriesListAdapter = CategoriesListAdapter(STUB.getCategories())
        binding.rvCategories.adapter = categoriesListAdapter

        categoriesListAdapter.setOnItemClickListener(object :
            CategoriesListAdapter.OnItemClickListener {
            override fun onItemClick(categoryId: Int) {
                openRecipesByCategoryId(categoryId)
            }
        })
    }

    private fun openRecipesByCategoryId(categoryId: Int) {

        val categoryName = STUB.getCategories().find { it.id == categoryId }?.title
            ?: throw IllegalStateException("no find category id")
        val categoryImageUrl = STUB.getCategories().find { it.id == categoryId }?.imageUrl
            ?: throw IllegalStateException("no find category id")

        val bundle = bundleOf(
            ARG_CATEGORY_ID to categoryId,
            ARG_CATEGORY_NAME to categoryName,
            ARG_CATEGORY_IMAGE_URL to categoryImageUrl
        )

        parentFragmentManager.commit {
            replace<RecipesListFragment>(R.id.mainContainer, args = bundle)
            setReorderingAllowed(true)
            addToBackStack(null)
        }
    }

    companion object {
        const val ARG_CATEGORY_ID = "categoryId"
        const val ARG_CATEGORY_NAME = "categoryName"
        const val ARG_CATEGORY_IMAGE_URL = "categoryImageUrl"
    }

}