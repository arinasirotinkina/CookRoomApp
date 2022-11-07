package com.example.cookroom.adapters

import android.content.Context
import android.content.Intent
import android.icu.text.CaseMap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.cookroom.EditProductActivity
import com.example.cookroom.EditShopItemActivity
import com.example.cookroom.R
import com.example.cookroom.db.DepenDbManager
import com.example.cookroom.db.ShopDbManager
import com.example.cookroom.db.products.ProdIntentConstants
import com.example.cookroom.models.ProdItem

//адаптер для покупок
class ShopItemAdapter(listMain: ArrayList<ProdItem>, contextM: Context): RecyclerView.Adapter<ShopItemAdapter.MyHolder>() {
    var listArray = listMain
    var context = contextM
    class MyHolder(itemView: View, contextV: Context) : RecyclerView.ViewHolder(itemView) {
        val context = contextV
        val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        val tvAmount = itemView.findViewById<TextView>(R.id.tvAmount)
        val tvMeasure = itemView.findViewById<TextView>(R.id.tvMeasure)

        fun setData(item: ProdItem) {
            tvTitle.text = item.title
            var amountCeil = item.amount.toString()
            if (item.amount!! % 1.0 == 0.0) {
                amountCeil = item.amount!!.toInt().toString()
            }
            tvAmount.text = amountCeil
            tvMeasure.text = item.measure
            if (amountCeil == "0") {
                tvAmount.text = ""
                tvMeasure.text = ""
            }
            itemView.setOnClickListener{
                val intent = Intent(context, EditShopItemActivity::class.java).apply {
                    putExtra(ProdIntentConstants.I_TITLE_KEY, item.title)
                    putExtra(ProdIntentConstants.I_CATEGORY_KEY, item.category)
                    putExtra(ProdIntentConstants.I_AMOUNT_KEY, amountCeil)
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
    fun removeItem(pos: Int, dbManager: ShopDbManager, user_id: String, context: Context) {
        var title = listArray[pos].title
        Toast.makeText(context, title, Toast.LENGTH_LONG).show()
        dbManager.deleteFromDb(context, user_id, title!!)
        listArray.removeAt(pos)
        notifyItemRangeChanged(0, listArray.size)
        notifyItemRemoved(pos)
    }
}


