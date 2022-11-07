package com.example.cookroom.db.recipes

import android.content.Context
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.cookroom.db.DbLinkConstants
import org.json.JSONException
import org.json.JSONObject

class RecipesDbManager {
    //добавление рецепта в БД
    fun insertToDb(context: Context, title: String, description: String, user_id: String) {
        val stringRequest = object : StringRequest(
            Method.POST, DbLinkConstants.URL_REC_INSERT,
            Response.Listener<String> { response ->
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
                val params: HashMap<String, String> = HashMap()
                params["title"] = title
                params["description"] = description
                params["user_id"] = user_id
                return params
            }
        }
        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)

    }
    //изменение рецепта в БД
    fun updateToDB(context: Context, title: String, description: String,
                   user_id: String, id: String) {
        val stringRequest = object : StringRequest(
            Method.POST, DbLinkConstants.URL_REC_UPD,
            Response.Listener<String> { response ->
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
                val params: HashMap<String, String> = HashMap()
                params["title"] = title
                params["description"] = description
                params["user_id"] = user_id
                params["id"] = id
                return params
            }
        }
        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)

    }
    //удаление рецепта из БД
    fun deleteFromDb(context: Context, user_id: String, recipe_id: String) {
        val stringRequest = object : StringRequest(
            Method.POST, DbLinkConstants.URL_REC_DELETE,
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
                params["recipe_id"] = recipe_id
                params["user_id"] = user_id
                return params
            }
        }
        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)
    }
}