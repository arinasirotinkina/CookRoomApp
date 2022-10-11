package com.example.cookroom

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cookroom.db.products.ProdDbManager
import com.example.cookroom.models.ProdItem
import com.example.cookroom.adapters.ShopItemAdapter


class ShoplistFragment : Fragment() {
    private var arrayList: ArrayList<ProdItem>? = null
    private var rcView: RecyclerView? = null
    var tvNoElem: TextView? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_shoplist, container, false)
        tvNoElem = view?.findViewById(R.id.tvNoElem)
        rcView = view?.findViewById(R.id.rcView)
        return view
    }


    var searchView: SearchView? = null
    var prodCategory: String? = null


    override fun onStart() {
        super.onStart()
        arrayList = ArrayList()
        val myDbManager = ProdDbManager(requireActivity())
        myDbManager.openDb()
        val list = myDbManager.readDbData("", "")
        val myAdapter = ShopItemAdapter(list, requireContext())
        rcView?.adapter = myAdapter
        rcView?.layoutManager = LinearLayoutManager(requireActivity())


        if (list.size > 0) {
            tvNoElem?.visibility = View.GONE
        } else {
            tvNoElem?.visibility = View.VISIBLE
        }
    }

}
/*
    fun fillAdapter() {
        val myDbManager = ProdDbManager(requireActivity())

        myDbManager.openDb()
        val list = myDbManager.readDbData("", "")
        val myAdapter = ShopItemAdapter(requireContext(), list)
        rcView?.adapter = myAdapter
        rcView?.layoutManager = LinearLayoutManager(requireActivity())


        if (list.size > 0) {
            tvNoElem?.visibility = View.GONE
        } else {
            tvNoElem?.visibility = View.VISIBLE
        }

    }


}*/