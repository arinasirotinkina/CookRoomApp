package com.example.cookroom.db.recipes

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

class RecipesDbManager {
    var URL_INSERT = "https://cookroom.site/recipes_insert.php"
    var URL_READ = "https://cookroom.site/products_readall.php"
    val URL_UPDATE = "https://cookroom.site/recipes_update.php"
    val URL_SEARCH = "https://cookroom.site/recipes_getid.php"
    val URL_DELETE = "https://cookroom.site/recipes_delete.php"
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
                var params: HashMap<String, String> = HashMap<String, String>()
                params.put("title", title)
                params.put("description", description)
                params.put("user_id", user_id)
                return params
            }
        }
        var requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)

    }

    fun updateToDB(
        context: Context,
        title: String,
        description: String,
        user_id: String,
        id: String
    ) {
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
                var params: HashMap<String, String> = HashMap<String, String>()
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
    fun deleteFromDb(context: Context, user_id: String, recipe_id: String) {
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
                params.put("recipe_id", recipe_id)
                params.put("user_id", user_id)
                return params
            }
        }
        var requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)

    }

}