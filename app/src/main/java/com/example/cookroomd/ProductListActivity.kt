package com.example.cookroomd

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity
import android.widget.SearchView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cookroomd.adapters.ItemProductAdapter
import com.example.cookroomd.db.product.ProdDbManager


class ProductListActivity : AppCompatActivity() {
    val myDbManager = ProdDbManager(this)
    val myAdapter = ItemProductAdapter(ArrayList(), this)
    var rcView : RecyclerView? = null
    var tvNoElem : TextView? = null
    var searchView: SearchView? = null
    var prodCategory: String? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)


        rcView = findViewById(R.id.rcView)
        tvNoElem = findViewById(R.id.tvNoElem)
        searchView = findViewById(R.id.searchView)

        val kt = intent
        prodCategory = kt.getCharSequenceExtra("CHOSEN").toString()
        init()
        initSearchView()


    }

    override fun onResume() {
        super.onResume()
        myDbManager.openDb()
        fillAdapter()
    }

    fun onClickNew(view: View) {
        val i = Intent(this, EditProductActivity::class.java)
        i.putExtra("CHOSEN", prodCategory)
        startActivity(i)
    }

    override fun onDestroy() {
        super.onDestroy()
        myDbManager.closeDb()
    }
    fun init() {
        rcView?.layoutManager = LinearLayoutManager(this)
        val swapHelper = getSwapMg()
        swapHelper.attachToRecyclerView(rcView)
        rcView?.adapter = myAdapter
    }
    private fun initSearchView() {
        val kt = intent
        prodCategory = kt.getCharSequenceExtra("CHOSEN").toString()
        searchView?.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val list = myDbManager.readDbData(newText!!, prodCategory!!)
                myAdapter.updateAdapter(list)
                return true
            }
        })
    }
    fun fillAdapter() {
        val kt = intent
        prodCategory = kt.getCharSequenceExtra("CHOSEN").toString()
        val list = myDbManager.readDbData("", prodCategory!!)
        myAdapter.updateAdapter(list)
        if (list.size > 0) {
            tvNoElem?.visibility = View.GONE
        } else {
            tvNoElem?.visibility = View.VISIBLE
        }
    }
    private fun getSwapMg() : ItemTouchHelper {
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