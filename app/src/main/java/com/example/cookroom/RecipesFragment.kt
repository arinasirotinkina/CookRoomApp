package com.example.cookroom

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cookroom.adapters.RecipeAdapter
import com.example.cookroom.db.recipes.RecipeDbManager
import com.google.android.material.floatingactionbutton.FloatingActionButton



class RecipesFragment : Fragment() {

    var rcView : RecyclerView? = null
    var tvNoElem : TextView? = null
    var searchView: SearchView? = null
    var prodCategory: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_recipes, container, false)
        rcView = view.findViewById(R.id.rcView)
        tvNoElem = view.findViewById(R.id.tvNoElem)
        searchView = view.findViewById(R.id.searchView)
        var fbNew = view.findViewById<FloatingActionButton>(R.id.fbNew)
        fbNew.setOnClickListener{
            var i = Intent(requireContext(), EditRecipeActivity::class.java)
            startActivity(i)
        }
        return view
    }
    override fun onStart() {
        super.onStart()
        init()
        initSearchView()
        fillAdapter()
    }
    fun init() {
        val myAdapter = RecipeAdapter(ArrayList(), requireContext())
        rcView?.layoutManager = LinearLayoutManager(requireContext())
        val swapHelper = getSwapMg()
        swapHelper.attachToRecyclerView(rcView)
        rcView?.adapter = myAdapter
    }
    private fun initSearchView() {
        val myDbManager = RecipeDbManager(requireContext())
        val myAdapter = RecipeAdapter(ArrayList(), requireContext())
        searchView?.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val list = myDbManager.readDbData(newText!!)
                myAdapter.updateAdapter(list)
                return true
            }
        })
    }
    fun fillAdapter() {
        val myDbManager = RecipeDbManager(requireContext())
        myDbManager.openDb()
        val list = myDbManager.readDbData("")
        val myAdapter = RecipeAdapter(ArrayList(), requireContext())
        myAdapter.updateAdapter(list)
        rcView?.adapter = myAdapter
        if (list.size > 0) {
            tvNoElem?.visibility = View.GONE
        } else {
            tvNoElem?.visibility = View.VISIBLE
        }
    }
    private fun getSwapMg() : ItemTouchHelper {
        val myDbManager = RecipeDbManager(requireContext())
        val myAdapter = RecipeAdapter(ArrayList(), requireContext())
        return ItemTouchHelper(object: ItemTouchHelper.
        SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                myAdapter.removeItem(viewHolder.adapterPosition, myDbManager)
            }
        })
    }



}