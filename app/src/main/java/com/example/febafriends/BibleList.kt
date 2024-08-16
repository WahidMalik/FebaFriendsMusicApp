package com.example.febafriends

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.febafriends.databinding.ActivityBibleListBinding
import com.google.firebase.firestore.FirebaseFirestore

class BibleList : AppCompatActivity() {

    private lateinit var binding: ActivityBibleListBinding
    private lateinit var adapter: SubcategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBibleListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = resources.getColor(R.color.black)
        setSupportActionBar(binding.toolbarBiblelist)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.toolbarbiblelist.text = "Bible Chapters"
        binding.toolbarBiblelist.navigationIcon?.setTint(resources.getColor(android.R.color.holo_red_dark))

        getBibleData()

    }

    fun getBibleData() {
        FirebaseFirestore.getInstance().collection("Bible")
        .get().addOnSuccessListener { result ->
            val biblelist = result.toObjects(Subcategory::class.java)
            setRecyclerView(biblelist)
        }.addOnFailureListener { exception ->
            // Handle the error here
        }
    }



    private fun setRecyclerView(biblelist: List<Subcategory>) {
        adapter = SubcategoryAdapter(biblelist)
        binding.biblerecycleView.adapter = adapter
        val layoutManager = LinearLayoutManager(this)
        binding.biblerecycleView.layoutManager = layoutManager
    }
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
