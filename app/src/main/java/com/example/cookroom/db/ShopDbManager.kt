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
    var URL_INSERT = "https://cookroom.site/shop_insert.php"
    var URL_DELETE = "https://cookroom.site/shop_delete.php"
    val URL_UPDATE = "https://cookroom.site/products_update.php"
    val URL_SELECT = "https://cookroom.site/products_select.php"
    fun insertToDb(context: Context, title: String, amount: String, measure: String, user_id: String) {
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
                params.put("amount", amount)
                params.put("measure", measure)
                params.put("user_id", user_id)
                return params
            }
        }
        var requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)

    }

    fun deleteFromDb(context: Context, user_id: String, title: String) {
        val stringRequest = object : StringRequest(
            Method.POST, URL_DELETE,
            Response.Listener { response ->
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
                var params : HashMap<String, String> = HashMap()
                params.put("title", title)
                params.put("user_id", user_id)
                return params
            }
        }
        var requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)

    }

}