package com.example.cookroom.db.products

import android.content.Context
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.cookroom.db.DbLinkConstants
import org.json.JSONException
import org.json.JSONObject

class ProductsDbManager {
//добавление продукта в БД
    fun insertToDb(context: Context, title: String, category: String, amount: String,
                   measure: String, user_id: String){
        val stringRequest = object : StringRequest(
            Method.POST, DbLinkConstants.URL_PROD_INSERT,
            Response.Listener { response ->
                try {
                    val obj = JSONObject(response.toString())
                    Toast.makeText(context, obj.getString("message"), Toast.LENGTH_LONG).show()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(context, error?.message, Toast.LENGTH_LONG).show() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params : HashMap<String, String> = HashMap()
                params["title"] = title
                params["category"] = category
                params["amount"] = amount
                params["measure"] = measure
                params["user_id"] = user_id
                return params
            }
        }
        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)

    }
    //изменение продукта в БД
    fun updateToDB(context: Context, title: String, category: String, amount: String, measure: String, user_id: String, id: String) {
        val stringRequest = object : StringRequest(
            Method.POST, DbLinkConstants.URL_PROD_UPD,
            Response.Listener { response ->
                try {
                    val obj = JSONObject(response.toString())
                    Toast.makeText(context, obj.getString("message"), Toast.LENGTH_LONG).show()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(context, error?.message, Toast.LENGTH_LONG).show() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params : HashMap<String, String> = HashMap()
                params["title"] = title
                params["category"] = category
                params["amount"] = amount
                params["measure"] = measure
                params["user_id"] = user_id
                params["id"] = id
                return params
            }
        }
        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)

    }
    //выбор всех названий продуктов из БД
    fun selector(context: Context, user_id: String, selList: ArrayList<String>) : ArrayList<String> {
        fun setId(ids: String) {
            selList.add(ids)
        }
        val stringRequest = object : StringRequest(
            Method.POST, DbLinkConstants.URL_PROD_SELECT,
            Response.Listener { response ->
                try {
                    val jsonObject = JSONObject(response.toString())
                    val success = jsonObject.getString("success")
                    val jsonArray = jsonObject.getJSONArray("product")
                    if (success.equals("1")) {
                        for (i in 0 until jsonArray.length()) {
                            val obj = jsonArray.getJSONObject(i)
                            val ids = obj.getString("title").trim()
                            setId(ids)
                        }
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(context, error?.message, Toast.LENGTH_LONG).show() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params : HashMap<String, String> = HashMap()
                params["user_id"] = user_id
                return params
            }
        }
        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)
        return selList
    }
}
