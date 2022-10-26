package com.example.cookroom.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.cookroom.R
import com.example.cookroom.models.CategoryItem

class  CategoryAdapter(var context: Context, var arrayList: ArrayList<CategoryItem>): BaseAdapter() {
    override fun getCount(): Int {
        return arrayList.size
    }
    override fun getItem(position: Int): Any {
        return arrayList.get(position)
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = View.inflate(context, R.layout.card_view_item_grid, null)
        val icons : ImageView = view.findViewById(R.id.icons)
        val names: TextView= view.findViewById(R.id.name_text_view)
        val listItem :CategoryItem = arrayList.get(position)
        icons.setImageResource(listItem.icons !!)
        names.text = listItem.name
        return view
    }

}