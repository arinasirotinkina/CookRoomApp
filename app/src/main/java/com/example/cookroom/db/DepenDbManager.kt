package com.example.cookroom.db


import android.content.Context
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.cookroom.models.ProdItem

import org.json.JSONException
import org.json.JSONObject

class DepenDbManager {
    //добавление связи в БД
    fun insertToDb(context: Context, recipe_id: String, product_id: String, title: String, amount:String, measure: String, user_id: String) {
        val stringRequest = object : StringRequest(
            Method.POST, DbLinkConstants.URL_DEP_INSERT,
            Response.Listener<String> { response ->
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
                var params : HashMap<String, String> = HashMap()
                params["recipe_id"] = recipe_id
                params["product_id"] = product_id
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
    //удаление связи из БД
    fun removeItemFromDb(context: Context, user_id: String, title: String, recipe_id: String) {
        val stringRequest = object : StringRequest(
            Method.POST, DbLinkConstants.URL_DEP_DELETE,
            Response.Listener<String> { response ->
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
            override fun getParams(): Map<String, String>? {
                var params : HashMap<String, String> = HashMap<String, String>()
                params.put("recipe_id", recipe_id)
                params.put("title", title)
                params.put("user_id", user_id)
                return params
            }
        }
        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)
    }
}
