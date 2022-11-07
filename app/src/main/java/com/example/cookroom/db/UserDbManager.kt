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

class UserDbManager {

    fun turnOnAlarm(context: Context, user_id: String, time: String) {
        var stringRequest = object : StringRequest(
            Method.POST, DbLinkConstants.URL_TURN_ON,
            Response.Listener<String> { response ->
                try {
                    val obj = JSONObject(response.toString())
                    Toast.makeText(context, obj.getString("message"), Toast.LENGTH_LONG).show()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error -> Toast.makeText(context, error?.message, Toast.LENGTH_LONG).show() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params : HashMap<String, String> = HashMap()
                params["user_id"] = user_id
                params["time"] = time
                return params
            }
        }
        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)

    }
    fun turnOffAlarm(context: Context, user_id: String) {
        val stringRequest = object : StringRequest(
            Method.POST, DbLinkConstants.URL_TURN_OFF,
            Response.Listener<String> { response ->
                try {
                    val obj = JSONObject(response.toString())
                    Toast.makeText(context, obj.getString("message"), Toast.LENGTH_LONG).show()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error -> Toast.makeText(context, error?.message, Toast.LENGTH_LONG).show() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params : HashMap<String, String> = HashMap()
                params["user_id"] = user_id
                return params
            }
        }
        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)
    }
}