package com.example.cookroom.db.shop


import android.content.ClipDescription
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.cookroom.MainActivity
import com.example.cookroom.adapters.ItemProductAdapter
import com.example.cookroom.models.ProdItem

import org.json.JSONException
import org.json.JSONObject

class ShopDbManager {
    var URL_INSERT = "http://arinasyw.beget.tech/"
    var URL_READ = "http://arinasyw.beget.tech/"
    val URL_UPDATE = "http://arinasyw.beget.tech/"
    val URL_SEARCH = "http://arinasyw.beget.tech/"
    //var id: String = ""
    fun insertToDb(context: Context, title: String, description: String, user_id: String) {
        val stringRequest = object : StringRequest(
            Method.POST, URL_INSERT,
            Response.Listener<String> { response ->
                try {
                    val obj = JSONObject(response.toString())
                    Toast.makeText(context, obj.getString("message"), Toast.LENGTH_LONG).show()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            object : Response.ErrorListener {
                override fun onErrorResponse(error: VolleyError?) {
                    Toast.makeText(context, error?.message, Toast.LENGTH_LONG).show()
                }
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String>? {
                var params : HashMap<String, String> = HashMap<String, String>()
                params.put("title", title)
                params.put("description", description)
                params.put("user_id", user_id)
                return params
            }
        }
        var requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)

    }
    fun updateToDB(context: Context, title: String, description: String, user_id: String, id: String) {
        var stringRequest = object : StringRequest(
            Method.POST, URL_UPDATE,
            Response.Listener<String> { response ->
                try {
                    val obj = JSONObject(response.toString())
                    //Toast.makeText(context, obj.getString("message"), Toast.LENGTH_LONG).show()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            object : Response.ErrorListener {
                override fun onErrorResponse(error: VolleyError?) {
                    Toast.makeText(context, error?.message, Toast.LENGTH_LONG).show()
                }
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String>? {
                var params : HashMap<String, String> = HashMap<String, String>()
                params.put("title", title)
                params.put("description", description)
                //params.put("user_id", user_id)
                params.put("id", id)
                return params
            }
        }
        var requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)

    }
    suspend fun searchForId(context: Context, myTitle: String, myDesc: String, user_id: String, selList: ArrayList<String>) {
        fun setId(ids: String) {
            selList.add(ids)
            Toast.makeText(context, selList.size.toString(), Toast.LENGTH_LONG).show()
        }
        val stringRequest = object : StringRequest(
            Method.POST, URL_SEARCH,
            Response.Listener<String> { response ->
                try {
                    val jsonObject = JSONObject(response.toString())
                    val success = jsonObject.getString("success")
                    val jsonArray = jsonObject.getJSONArray("recipe")
                    if (success.equals("1")) {
                        val obj = jsonArray.getJSONObject(0)
                        var ids = obj.getString("id").trim()
                        //ids??
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            object : Response.ErrorListener {
                override fun onErrorResponse(error: VolleyError?) {
                    Toast.makeText(context, error?.message, Toast.LENGTH_LONG).show()
                }
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String>? {
                var params : HashMap<String, String> = HashMap<String, String>()
                params.put("user_id", user_id)
                params.put("title", myTitle)
                params.put("description", myDesc)
                return params
            }
        }
        Toast.makeText(context, selList.size.toString(), Toast.LENGTH_LONG).show()
        var requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)
        //return selList

    }

/*
    fun readDbData(context: Context, category: String, user_id: String): ArrayList<ProdItem> {
        var dataList = ArrayList<ProdItem>()
        fun readItem(item: ProdItem) {
            dataList.add(item)
        }
        var prodAdapter = ItemProductAdapter(ArrayList(), context)
        var stringRequest = object : StringRequest(
            Method.POST, URL_READ,
            Response.Listener<String> { response ->

                try {
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
                    //Toast.makeText(context, list.size.toString(), Toast.LENGTH_LONG).show()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            object : Response.ErrorListener {
                override fun onErrorResponse(error: VolleyError?) {
                    Toast.makeText(context, error?.message, Toast.LENGTH_LONG).show()
                }
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String>? {
                var params : HashMap<String, String> = HashMap<String, String>()
                params.put("category","Мясные продукты")
                params.put("user_id", "21")
                return params
            }
        }
        //Toast.makeText(context, dataList.size.toString(), Toast.LENGTH_LONG).show()

        var requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)

        return dataList
    }*/

}
