package com.example.cookroom

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.cookroom.db.products.ProductsDbManager
import com.example.cookroom.models.ProdItem
import org.json.JSONException
import org.json.JSONObject

class AbleCookActivity : AppCompatActivity() {

    var productsDbManager = ProductsDbManager()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_able_cook)
        val kt = intent

    }

    fun onClickCancel(view: View) {
        finish()
    }
    fun onClickAccept(view: View) {
        val kt = intent
        val minus_list = kt.getParcelableArrayListExtra<ProdItem>("minus_list")
        val URL_SELECT = "https://cookroom.site/products_select.php"
        val pref = this.getSharedPreferences("User_Id", MODE_PRIVATE)
        val user_id = pref.getString("user_id", "-1")
        var stringRequest = object : StringRequest(
            Method.POST, URL_SELECT,
            Response.Listener<String> { response ->
                try {
                    val jsonObject = JSONObject(response.toString())
                    val success = jsonObject.getString("success")
                    val jsonArray = jsonObject.getJSONArray("product")
                    if (success.equals("1")) {
                        for (i in 0 until jsonArray.length()) {
                            var obj = jsonArray.getJSONObject(i)
                            val id = obj.getString("id").trim()
                            var title = obj.getString("title").trim()
                            var category = obj.getString("category").trim()
                            var amount = obj.getString("amount").trim()
                            var measure = obj.getString("measure").trim()

                            for (ik in minus_list!!) {
                                if (ik.id.toString() == id) {
                                    productsDbManager.updateToDB(this, ik.title!!, ik.category!!, ik.amount!!.toInt().toString(), ik.measure!!, user_id!!, ik.id.toString())
                                }
                            }
                        }
                    }
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
                params.put("user_id", user_id!!)
                return params
            }
        }
        var requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
        finish()
    }
}