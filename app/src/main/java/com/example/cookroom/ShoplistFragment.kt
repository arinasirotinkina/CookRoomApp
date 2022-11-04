package com.example.cookroom

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.cookroom.adapters.ItemProductAdapter
import com.example.cookroom.adapters.RecipeAdapter
import com.example.cookroom.adapters.ShopItemAdapter
import com.example.cookroom.models.ProdItem
import org.json.JSONException
import org.json.JSONObject


class ShoplistFragment : Fragment() {
    private var rcView: RecyclerView? = null
    var tvNoElem: TextView? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_shoplist, container, false)
        tvNoElem = view?.findViewById(R.id.tvNoElem)
        rcView = view?.findViewById(R.id.rcView)

        return view
    }

    override fun onStart() {
        super.onStart()
        init()
        //initSearchView()
        readRecipes()

    }
    fun init() {
        rcView?.layoutManager = LinearLayoutManager(requireContext())
        //val swapHelper = getSwapMg()
        //swapHelper.attachToRecyclerView(rcView)
    }
    fun fillAdapter(list: ArrayList<ProdItem>) {
        val myAdapter = ShopItemAdapter(ArrayList(), requireContext())
        myAdapter.updateAdapter(list)
        rcView?.adapter = myAdapter
        if (list.size > 0) {
            tvNoElem?.visibility = View.GONE
        } else {
            tvNoElem?.visibility = View.VISIBLE
        }
    }
    fun readRecipes() {
        val URL_READ1 = "https://cookroom.site/shop_readall.php"
        var pref = requireActivity().getSharedPreferences("User_Id", AppCompatActivity.MODE_PRIVATE)
        var user_id = pref.getString("user_id", "-1")
        val stringRequest = object : StringRequest(
            Method.POST, URL_READ1,
            Response.Listener<String> { response ->
                try {
                    val jsonObject = JSONObject(response.toString())
                    val success = jsonObject.getString("success")
                    val jsonArray = jsonObject.getJSONArray("shop")
                    val list = java.util.ArrayList<ProdItem>()
                    Toast.makeText(context,  success.toString(), Toast.LENGTH_LONG).show()
                    if (success.equals("1")) {
                        for (i in 0 until jsonArray.length()) {
                            val obj = jsonArray.getJSONObject(i)
                            val id = obj.getString("id").trim()
                            var title = obj.getString("title").trim()
                            var amount = obj.getString("amount").trim()
                            var measure = obj.getString("measure").trim()
                            var item = ProdItem()
                            item.id = id.toInt()
                            item.title = title
                            item.category = ""
                            item.amount = amount.toDouble()
                            item.measure = measure
                            list.add(item)
                        }
                    }
                    fillAdapter(list)
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
        var requestQueue = Volley.newRequestQueue(requireContext())
        requestQueue.add(stringRequest)
    }

}
