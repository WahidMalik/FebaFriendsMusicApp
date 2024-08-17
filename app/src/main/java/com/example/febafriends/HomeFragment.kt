package com.example.febafriends

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore


class HomeFragment : Fragment() {

    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var originalCategoryList: List<CategoryModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchView = view.findViewById<androidx.appcompat.widget.AppCompatEditText>(R.id.searchView)
        val categoryRecycleView = view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.categoryRecycleView)

        getCategoryData()

        searchView.addTextChangedListener(object : TextWatcher {
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
                    categoryRecycleView.visibility = View.GONE
                } else {
                    categoryRecycleView.visibility = View.VISIBLE
                }

                categoryAdapter.categoryList = filteredData
                categoryAdapter.notifyDataSetChanged()
            }
        })
    }

    private fun getCategoryData() {
        FirebaseFirestore.getInstance().collection("category")
            .get().addOnSuccessListener { result ->
                originalCategoryList = result.toObjects(CategoryModel::class.java)
                setRecyclerView(originalCategoryList)
            }.addOnFailureListener { exception ->
                Log.e("HomeFragment", "Failed to get category data", exception)
            }
    }

    private fun setRecyclerView(categoryList: List<CategoryModel>) {
        val categoryRecycleView = view?.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.categoryRecycleView)
        categoryAdapter = CategoryAdapter(categoryList)
        categoryRecycleView?.adapter = categoryAdapter
        val layoutManager = LinearLayoutManager(requireContext())
        categoryRecycleView?.layoutManager = layoutManager
    }
}
