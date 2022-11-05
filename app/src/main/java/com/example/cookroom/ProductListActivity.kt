package com.example.cookroom


import java.util.ArrayList;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.cookroom.adapters.ItemProductAdapter
import com.example.cookroom.models.ProdItem
import org.json.JSONException
import org.json.JSONObject

//Активность списка продуктов в категории
class ProductListActivity : AppCompatActivity() {
    val myAdapter = ItemProductAdapter(ArrayList(), this)
    var rcView : RecyclerView? = null
    var tvNoElem : TextView? = null
    var prodCategory: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)
        rcView = findViewById(R.id.rcView)
        tvNoElem = findViewById(R.id.tvNoElem)
        val kt = intent
        prodCategory = kt.getCharSequenceExtra("CHOSEN").toString()
        init()
    }

    override fun onResume() {
        super.onResume()
        readDbData()
    }

    //Слушатель нажатия кнопки добавления продукта
    fun onClickNew(view: View) {
        val i = Intent(this, EditProductActivity::class.java)
        i.putExtra("CHOSEN", prodCategory)
        startActivity(i)
    }

    fun init() {
        rcView?.layoutManager = LinearLayoutManager(this)
        //val swapHelper = getSwapMg()
        //swapHelper.attachToRecyclerView(rcView)
        rcView?.adapter = myAdapter
    }

    //Заполнение RecyclerView
    fun fillAdapter(list: ArrayList<ProdItem>) {
        val kt = intent
        prodCategory = kt.getCharSequenceExtra("CHOSEN").toString()
        myAdapter.updateAdapter(list)
        if (list.size > 0) {
            tvNoElem?.visibility = View.GONE
        } else {
            tvNoElem?.visibility = View.VISIBLE
        }
    }
    /*private fun getSwapMg() : ItemTouchHelper {
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
    }*/
    //Чтение данных из базы
    fun readDbData() {
        val URL_READ = "https://cookroom.site/products_readall.php"
        val kt = intent
        prodCategory = kt.getCharSequenceExtra("CHOSEN").toString()
        val pref = this.getSharedPreferences("User_Id", MODE_PRIVATE)
        val user_id = pref.getString("user_id", "-1")
        val stringRequest = object : StringRequest(
            Method.POST, URL_READ,
            Response.Listener { response ->
                try {
                    val jsonObject = JSONObject(response.toString())
                    val success = jsonObject.getString("success")
                    val jsonArray = jsonObject.getJSONArray("product")
                    val list = ArrayList<ProdItem>()
                    if (success.equals("1")) {
                        for (i in 0 until jsonArray.length()) {
                            val obj = jsonArray.getJSONObject(i)
                            val item = ProdItem()
                            item.title = obj.getString("title").trim()
                            item.category = obj.getString("category").trim()
                            item.amount = obj.getString("amount").trim().toDouble()
                            item.measure = obj.getString("measure").trim()
                            item.id = obj.getString("id").trim().toInt()
                            list.add(item)
                        }
                    }
                    fillAdapter(list)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener {
                //Toast.makeText(this, error.message , Toast.LENGTH_LONG).show()
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                var params : HashMap<String, String> = HashMap()
                params.put("category",prodCategory!!)
                params.put("user_id", user_id!!)
                return params
            }
        }
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }
}