package com.example.febafriends

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.febafriends.databinding.FragmentHomeBinding
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var originalCategoryList: List<CategoryModel>

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getCategoryData()

        binding.searchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No action needed here
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // No action needed here
            }

            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()
                val filteredData = if (query.isNotEmpty()) {
                    originalCategoryList.filter { it.name.lowercase().contains(query.lowercase()) }
                } else {
                    originalCategoryList
                }

                if (filteredData.isEmpty()) {
                    binding.categoryRecycleView.visibility = View.GONE
                } else {
                    binding.categoryRecycleView.visibility = View.VISIBLE
                }

                categoryAdapter.categoryList = filteredData
                categoryAdapter.notifyDataSetChanged() // Notify adapter of data change
            }
        })
    }

    private fun getCategoryData() {
        FirebaseFirestore.getInstance().collection("category")
            .get().addOnSuccessListener { result ->
                originalCategoryList = result.toObjects(CategoryModel::class.java)
                setRecyclerView(originalCategoryList)
            }.addOnFailureListener { exception ->
                // Handle the error here
            }
    }

    private fun setRecyclerView(categoryList: List<CategoryModel>) {
        categoryAdapter = CategoryAdapter(categoryList)
        binding.categoryRecycleView.adapter = categoryAdapter
        val layoutManager = LinearLayoutManager(requireContext())
        binding.categoryRecycleView.layoutManager = layoutManager
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
