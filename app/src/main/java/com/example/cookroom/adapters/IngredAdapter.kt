package com.example.cookroom.adapters

import android.content.Context
import android.icu.text.CaseMap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.cookroom.R
import com.example.cookroom.db.DepenDbManager
import com.example.cookroom.db.products.ProductsDbManager
import com.example.cookroom.models.ProdItem

//адаптер для ингредиентов
class IngredAdapter(listMain: ArrayList<ProdItem>, contextM: Context): RecyclerView.Adapter<IngredAdapter.MyHolder>() {
    var listArray = listMain
    var context = contextM
    class MyHolder(itemView: View, contextV: Context) : RecyclerView.ViewHolder(itemView) {
        val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        val tvAmount = itemView.findViewById<TextView>(R.id.tvAmount)
        val tvMeasure = itemView.findViewById<TextView>(R.id.tvMeasure)
        val context = contextV
        fun setData(item: ProdItem) {
            tvTitle.text = item.title
            var amountCeil = item.amount.toString()
            if (item.amount!! % 1.0 == 0.0) {
                amountCeil = item.amount!!.toInt().toString()
            }
            tvAmount.text = amountCeil
            tvTitle.text = item.title
            tvMeasure.text = item.measure
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MyHolder(inflater.inflate(R.layout.product_view_item, parent, false), this.context)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.setData(listArray.get(position))
    }

    override fun getItemCount(): Int {
        return listArray.size
    }

    fun updateAdapter(listItems: List<ProdItem>) {
        listArray.clear()
        listArray.addAll(listItems)
        notifyDataSetChanged()
    }
    fun removeItem(pos: Int, dbManager: DepenDbManager, user_id: String, recipe_id: String, context: Context) {
        var title = listArray[pos].title
        dbManager.removeItemFromDb(context, user_id, title!!, recipe_id)
        listArray.removeAt(pos)
        notifyItemRangeChanged(0, listArray.size)
        notifyItemRemoved(pos)
    }
}