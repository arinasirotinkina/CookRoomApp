package com.example.cookroom

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.cookroom.adapters.NotHaveAdapter
import com.example.cookroom.db.ShopDbManager
import com.example.cookroom.models.ProdItem
import org.json.JSONException
import org.json.JSONObject

//Активность невозможности приготовления рецепта
class DisableCookActivity : AppCompatActivity() {
    var not_h_list : RecyclerView? = null
    var addToShop : Button? = null
    var shopDbManager = ShopDbManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_disable_cook)
        val kt = intent
        val pref = this.getSharedPreferences("User_Id", MODE_PRIVATE)
        val user_id = pref.getString("user_id", "-1")
        val d = ArrayList<ProdItem>()
        val minus_list = kt.getParcelableArrayListExtra<ProdItem>("minus_list")
        for (item in minus_list!!) {
            if (item.amount!!.toInt() < 0) {
                val temp = ProdItem()
                temp.id = item.id
                temp.title = item.title
                temp.category = item.category
                temp.amount = -1 * item.amount!!
                temp.measure = item.measure
                d.add(temp)
            }
        }
        not_h_list = findViewById(R.id.not_having_list)
        addToShop = findViewById(R.id.addToShop)
        addToShop?.setOnClickListener {
            for (item in d) {
                val URL_SELECT = "https://cookroom.site/shop_select.php"
                val stringRequest = object : StringRequest(
                    Method.POST, URL_SELECT,
                    Response.Listener { response ->
                        try {
                            val jsonObject = JSONObject(response.toString())
                            val success = jsonObject.getString("success")
                            val jsonArray = jsonObject.getJSONArray("shop")
                            if (success.equals("1")) {
                                for (i in 0 until jsonArray.length()) {
                                    val obj = jsonArray.getJSONObject(i)
                                    val ik = ProdItem()
                                    val temp = ProdItem()
                                    ik.id = obj.getString("id").trim().toInt()
                                    ik.title = obj.getString("title").trim()
                                    ik.category = ""
                                    ik.amount = obj.getString("amount").trim().toDouble()
                                    ik.measure = obj.getString("measure").trim()
                                    if (ik.measure == item.measure) {
                                        temp.measure = item.measure
                                        temp.amount = item.amount!! + ik.amount!!
                                    } else if ((ik.measure == "г"  && item.measure == "кг")
                                        || (ik.measure == "мл"  && item.measure == "л")) {
                                        temp.measure = ik.measure
                                        temp.amount = item.amount!! * 1000 + ik.amount!!
                                    } else if ((ik.measure == "кг"  && item.measure == "г")
                                        || (ik.measure == "л"  && item.measure == "мл")) {
                                        temp.measure = item.measure
                                        temp.amount = item.amount!! + ik.amount!! * 1000
                                    }
                                    shopDbManager.insertToDb(this, item.title!!,
                                        temp.amount.toString(), temp.measure!!, user_id!!)
                                }
                            } else {
                                shopDbManager.insertToDb(this, item.title!!,
                                    item.amount.toString(), item.measure!!, user_id!!)
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
                    override fun getParams(): Map<String, String> {
                        val params : HashMap<String, String> = HashMap()
                        params["user_id"] = user_id!!
                        params["title"] = item.title!!
                        return params
                    }
                }
                val requestQueue = Volley.newRequestQueue(this)
                requestQueue.add(stringRequest)
            }
        }
        val adapter = NotHaveAdapter(d, this)
        not_h_list?.layoutManager = LinearLayoutManager(this)
        not_h_list!!.adapter = adapter
    }
}