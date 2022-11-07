package com.example.cookroom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.cookroom.db.DbLinkConstants
import com.example.cookroom.db.products.ProductsDbManager
import com.example.cookroom.models.ProdItem
import org.json.JSONException
import org.json.JSONObject

//активность положительной возможности проиготовления
class AbleCookActivity : AppCompatActivity() {
    var productsDbManager = ProductsDbManager()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_able_cook)
    }
    //слушатель нажатий кнопки отказа
    fun onClickCancel(view: View) {
        finish()
    }
    //слушатель нажатий кнопки подтверждения
    fun onClickAccept(view: View) {
        val kt = intent
        val minus_list = kt.getParcelableArrayListExtra<ProdItem>("minus_list")
        val pref = this.getSharedPreferences("User_Id", MODE_PRIVATE)
        val user_id = pref.getString("user_id", "-1")
        val stringRequest = object : StringRequest(
            Method.POST, DbLinkConstants.URL_PROD_SELECT,
            Response.Listener { response ->
                try {
                    val jsonObject = JSONObject(response.toString())
                    val success = jsonObject.getString("success")
                    val jsonArray = jsonObject.getJSONArray("product")
                    if (success.equals("1")) {
                        for (i in 0 until jsonArray.length()) {
                            var obj = jsonArray.getJSONObject(i)
                            val id = obj.getString("id").trim()
                            for (ik in minus_list!!) {
                                if (ik.id.toString() == id) {
                                    productsDbManager.updateToDB(this, ik.title!!, ik.category!!, ik.amount!!.toDouble().toString(), ik.measure!!, user_id!!, ik.id.toString())
                                }
                            }
                        }
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, error?.message, Toast.LENGTH_LONG).show()
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params : HashMap<String, String> = HashMap()
                params["user_id"] = user_id!!
                return params
            }
        }
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
        finish()
    }
}