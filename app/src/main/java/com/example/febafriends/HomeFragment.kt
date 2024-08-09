package com.example.febafriends

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.febafriends.databinding.FragmentHomeBinding
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment() {

    // Declare a nullable binding variable
    private var _binding: FragmentHomeBinding? = null
    private lateinit var categoryAdapter: CategoryAdapter

    // Use a non-nullable version of the binding with a getter
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment using the binding class
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Return the root view from the binding object
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        getCategoryData()





    }

    fun getCategoryData(){
        FirebaseFirestore.getInstance().collection("category")
            .get().addOnSuccessListener {
                val categoryList = it.toObjects(CategoryModel::class.java)
                setRecycleView(categoryList)


            }.addOnFailureListener {

            }
    }

    fun setRecycleView(categoryList : List<CategoryModel>){
        categoryAdapter = CategoryAdapter(categoryList)
        binding.categoryRecycleView.adapter = categoryAdapter
        val layoutManager = LinearLayoutManager(requireContext())
        binding.categoryRecycleView.layoutManager = layoutManager
    }


}
