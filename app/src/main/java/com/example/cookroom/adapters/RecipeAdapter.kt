package com.example.cookroom.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.cookroom.R
import com.example.cookroom.db.recipes.RecipeIntentConstants
import com.example.cookroom.models.RecipeItem
import com.example.cookroom.EditRecipeActivity
import com.example.cookroom.db.ShopDbManager

//адаптер для  рецептов
class RecipeAdapter(listMain: ArrayList<RecipeItem>, contextM: Context): RecyclerView.Adapter<RecipeAdapter.MyHolder>() {
    var listArray = listMain
    var context = contextM
    class MyHolder(itemView: View, contextV: Context) : RecyclerView.ViewHolder(itemView) {
        val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        val tvDesc = itemView.findViewById<TextView>(R.id.tvDesc)
        val context = contextV
        fun setData(item: RecipeItem) {
            tvTitle.text = item.title
            tvDesc.text = item.description
            if (item.description!!.length > 200){
                tvDesc.text = "${item.description.toString().substring(0, 200)}..."
            }
            itemView.setOnClickListener{
                val intent = Intent(context, EditRecipeActivity::class.java).apply {
                    putExtra(RecipeIntentConstants.I_TITLE_KEY, item.title)
                    putExtra(RecipeIntentConstants.I_DESC_KEY, item.description)
                    putExtra(RecipeIntentConstants.I_ID_KEY, item.id)
                }
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MyHolder(inflater.inflate(R.layout.recipe_view_item, parent, false), context)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.setData(listArray.get(position))
    }

    override fun getItemCount(): Int {
        return listArray.size
    }
    fun updateAdapter(listItems: List<RecipeItem>) {
        listArray.clear()
        listArray.addAll(listItems)
        notifyDataSetChanged()
    }
    fun removeItem(pos: Int, context: Context) {
        var title = listArray[pos].title
        Toast.makeText(context, title, Toast.LENGTH_LONG).show()
        listArray.removeAt(pos)
        notifyItemRangeChanged(0, listArray.size)
        notifyItemRemoved(pos)
    }
}