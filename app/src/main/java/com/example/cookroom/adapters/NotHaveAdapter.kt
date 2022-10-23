package com.example.cookroom.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cookroom.R
import com.example.cookroom.models.ProdItem

class NotHaveAdapter(listMain: ArrayList<ProdItem>, contextM: Context): RecyclerView.Adapter<NotHaveAdapter.MyHolder>() {
    var listArray = listMain
    var context = contextM
    class MyHolder(itemView: View, contextV: Context) : RecyclerView.ViewHolder(itemView) {
        val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        val tvAmount = itemView.findViewById<TextView>(R.id.tvAmount)
        val tvMeasure = itemView.findViewById<TextView>(R.id.tvMeasure)
        val context = contextV
        fun setData(item: ProdItem) {
            tvTitle.text = item.title
            tvAmount.text = item.amount.toString()
            tvMeasure.text = item.measure
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MyHolder(inflater.inflate(R.layout.need_view_item, parent, false), this.context)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.setData(listArray.get(position))
    }

    override fun getItemCount(): Int {
        return listArray.size
    }


}