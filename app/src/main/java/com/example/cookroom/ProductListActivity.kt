package com.example.cookroom

import android.content.Context
import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.cookroom.EditProductActivity
import com.example.cookroom.R
import com.example.cookroom.adapters.ItemProductAdapter
import com.example.cookroom.db.products.ProductsDbManager
import com.example.cookroom.models.ProdItem
import org.json.JSONException
import org.json.JSONObject


class ProductListActivity : AppCompatActivity() {
    val myAdapter = ItemProductAdapter(ArrayList(), this)
    var rcView : RecyclerView? = null
    var tvNoElem : TextView? = null
    var searchView: SearchView? = null
    var prodCategory: String? = null
    var productsDbManager = ProductsDbManager()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)


        rcView = findViewById(R.id.rcView)
        tvNoElem = findViewById(R.id.tvNoElem)
        searchView = findViewById(R.id.searchView)

        val kt = intent
        prodCategory = kt.getCharSequenceExtra("CHOSEN").toString()
        init()
        //initSearchView()


    }

    override fun onResume() {
        super.onResume()
        //myDbManager.openDb()
        //fillAdapter()
        readDbData()
    }

    fun onClickNew(view: View) {
        val i = Intent(this, EditProductActivity::class.java)
        i.putExtra("CHOSEN", prodCategory)
        startActivity(i)
    }

    override fun onDestroy() {
        super.onDestroy()
        //myDbManager.closeDb()
    }
    fun init() {
        rcView?.layoutManager = LinearLayoutManager(this)
        //val swapHelper = getSwapMg()
        //swapHelper.attachToRecyclerView(rcView)
        rcView?.adapter = myAdapter
    }
    /*private fun initSearchView() {
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
    }*/
    fun fillAdapter(list: ArrayList<ProdItem>) {
        val kt = intent
        prodCategory = kt.getCharSequenceExtra("CHOSEN").toString()
        //val list = myDbManager.readDbData("", prodCategory!!)
        var pref = this.getSharedPreferences("User_Id", MODE_PRIVATE)
        var user_id = pref.getString("user_id", "-1")

        //Toast.makeText(this, prodCategory, Toast.LENGTH_LONG).show()
        //val list = productsDbManager.readDbData(this, prodCategory!!, user_id.toString())
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
    fun readDbData() {
        val URL_READ = "https://cookroom.site/products_readall.php"
        val kt = intent
        prodCategory = kt.getCharSequenceExtra("CHOSEN").toString()
        //val list = myDbManager.readDbData("", prodCategory!!)
        var pref = this.getSharedPreferences("User_Id", MODE_PRIVATE)
        var user_id = pref.getString("user_id", "-1")

        //Toast.makeText(this, prodCategory, Toast.LENGTH_LONG).show()
        //val list = productsDbManager.readDbData(this, prodCategory!!, user_id.toString())


        var stringRequest = object : StringRequest(
            Method.POST, URL_READ,
            Response.Listener<String> { response ->

                try {
                    var dataList = ArrayList<ProdItem>()

                    var prodAdapter = ItemProductAdapter(ArrayList(), this)
                    val jsonObject = JSONObject(response.toString())
                    val success = jsonObject.getString("success")
                    val jsonArray = jsonObject.getJSONArray("product")
                    val list = ArrayList<ProdItem>()
                    //Toast.makeText(context,  success.toString(), Toast.LENGTH_LONG).show()

                    if (success.equals("1")) {
                        //Toast.makeText(context, jsonArray.length().toString(), Toast.LENGTH_LONG).show()
                        for (i in 0 until jsonArray.length()) {
                            val obj = jsonArray.getJSONObject(i)
                            val id = obj.getString("id").trim()
                            var title = obj.getString("title").trim()
                            var category = obj.getString("category").trim()
                            var amount = obj.getString("amount").trim()
                            var measure = obj.getString("measure").trim()
                            //Toast.makeText(context, title, Toast.LENGTH_LONG).show()
                            var item = ProdItem()
                            item.title = title
                            item.category = category
                            item.amount = amount.toInt()
                            item.measure = measure
                            item.id = id.toInt()
                            list.add(item)


                        }
                    }
                    fillAdapter(list)
                    //Toast.makeText(context, list.size.toString(), Toast.LENGTH_LONG).show()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            object : Response.ErrorListener {
                override fun onErrorResponse(error: VolleyError?) {
                    //Toast.makeText(this, error?.message, Toast.LENGTH_LONG).show()
                }
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String>? {
                var params : HashMap<String, String> = HashMap<String, String>()
                params.put("category",prodCategory!!)
                params.put("user_id", user_id!!)
                return params
            }
        }
        //Toast.makeText(context, dataList.size.toString(), Toast.LENGTH_LONG).show()

        var requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)

    }

}