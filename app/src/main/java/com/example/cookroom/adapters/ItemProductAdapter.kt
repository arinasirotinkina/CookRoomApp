package com.example.cookroom.adapters


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.cookroom.EditProductActivity
import com.example.cookroom.R
import com.example.cookroom.db.products.ProdIntentConstants
import com.example.cookroom.models.ProdItem

class ItemProductAdapter(listMain: ArrayList<ProdItem>, contextM: Context): RecyclerView.Adapter<ItemProductAdapter.MyHolder>() {
    var listArray = listMain
    var context = contextM
    class MyHolder(itemView: View, contextV: Context) : RecyclerView.ViewHolder(itemView) {
        val context = contextV
        val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        val tvAmount = itemView.findViewById<TextView>(R.id.tvAmount)
        val tvMeasure = itemView.findViewById<TextView>(R.id.tvMeasure)

        fun setData(item: ProdItem) {
            tvTitle.text = item.title
            tvAmount.text = item.amount.toString()
            tvMeasure.text = item.measure
            itemView.setOnClickListener{
                //Toast.makeText(context, item.amount.toString(), Toast.LENGTH_LONG).show()
                val intent = Intent(context, EditProductActivity::class.java).apply {
                    putExtra(ProdIntentConstants.I_TITLE_KEY, item.title)
                    putExtra(ProdIntentConstants.I_CATEGORY_KEY, item.category)
                    putExtra(ProdIntentConstants.I_AMOUNT_KEY, item.amount.toString())
                    putExtra(ProdIntentConstants.I_MEASURE_KEY, item.measure)
                    putExtra(ProdIntentConstants.I_ID_KEY, item.id)
                }
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MyHolder(inflater.inflate(R.layout.product_view_item, parent, false), context)
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
    /*
    fun removeItem(pos: Int, dbManager: ProdDbManager) {
        dbManager.removeItemFromDb(listArray[pos].id.toString())
        listArray.removeAt(pos)
        notifyItemRangeChanged(0, listArray.size)
        notifyItemRemoved(pos)
    }*/
}