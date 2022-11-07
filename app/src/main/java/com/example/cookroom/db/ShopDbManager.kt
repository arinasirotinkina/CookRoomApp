package com.example.cookroom.db


import android.content.Context
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

import org.json.JSONException
import org.json.JSONObject

class ShopDbManager {
    //добавление покупки в БД
    fun insertToDb(context: Context, title: String, amount: String,
                   measure: String, user_id: String) {
        var URL_INS = DbLinkConstants.URL_SHOP_INSERT
        if (amount == "0"){
            URL_INS = DbLinkConstants.URL_SHOP_INSERT_NULL
        }
        val stringRequest = object : StringRequest(
            Method.POST, URL_INS,
            Response.Listener { response ->
                try {
                    val obj = JSONObject(response.toString())
                    //Toast.makeText(context, obj.getString("message"), Toast.LENGTH_LONG).show()
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
                params["amount"] = amount
                params["measure"] = measure
                params["user_id"] = user_id
                return params
            }
        }
        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)
    }
    //удаление покупки из БД
    fun deleteFromDb(context: Context, user_id: String, title: String) {
        val stringRequest = object : StringRequest(
            Method.POST, DbLinkConstants.URL_SHOP_DELETE,
            Response.Listener { response ->
                try {
                    val obj = JSONObject(response.toString())
                    //Toast.makeText(context, obj.getString("message"), Toast.LENGTH_LONG).show()
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
                params["user_id"] = user_id
                return params
            }
        }
        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)
    }
}