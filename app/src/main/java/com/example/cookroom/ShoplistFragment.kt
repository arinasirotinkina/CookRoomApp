package com.example.cookroom

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.cookroom.adapters.ShopItemAdapter
import com.example.cookroom.db.DbLinkConstants
import com.example.cookroom.db.ShopDbManager
import com.example.cookroom.models.ProdItem
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONException
import org.json.JSONObject

//Фрагмент списка покупок
class ShoplistFragment : Fragment() {
    private var rcView: RecyclerView? = null
    var tvNoElem: TextView? = null
    var shopDbManager= ShopDbManager()
    var newFB : FloatingActionButton? = null
    var delFB : FloatingActionButton? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_shoplist, container, false)
        tvNoElem = view?.findViewById(R.id.tvNoElem)
        rcView = view?.findViewById(R.id.rcView)
        newFB = view.findViewById(R.id.fbNew)
        delFB = view.findViewById(R.id.fbDel)
        newFB?.setOnClickListener{
            val i = Intent(requireContext(), EditShopItemActivity::class.java)
            startActivity(i)
        }
        delFB?.setOnClickListener {
            readRecipes(false)
        }
        return view
    }

    override fun onStart() {
        super.onStart()
        init()
        readRecipes(true)
        Handler(Looper.getMainLooper()).postDelayed({
            readRecipes(true)
        }, 700)
    }

    fun init() {
        rcView?.layoutManager = LinearLayoutManager(requireContext())
    }

    //Заполнение списка
    fun fillAdapter(list: ArrayList<ProdItem>) {
        val myAdapter = ShopItemAdapter(ArrayList(), requireContext())
        myAdapter.updateAdapter(list)
        val swapHelper = getSwapMg(myAdapter)
        swapHelper.attachToRecyclerView(rcView)
        rcView?.adapter = myAdapter
        if (list.size > 0) {
            tvNoElem?.visibility = View.GONE
        } else {
            tvNoElem?.visibility = View.VISIBLE
        }
    }
    //Удаление по свайпу
    private fun getSwapMg(myAdapter: ShopItemAdapter) : ItemTouchHelper {
        return ItemTouchHelper(object: ItemTouchHelper.
        SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pref = requireActivity().getSharedPreferences("User_Id", AppCompatActivity.MODE_PRIVATE)
                val user_id = pref.getString("user_id", "-1")
                myAdapter.removeItem(viewHolder.adapterPosition, shopDbManager, user_id!!, requireContext())
            }
        })
    }
    //Чтение из базы
    fun readRecipes(fb: Boolean) {
        val pref = requireActivity().getSharedPreferences("User_Id", AppCompatActivity.MODE_PRIVATE)
        val user_id = pref.getString("user_id", "-1")
        val stringRequest = object : StringRequest(
            Method.POST, DbLinkConstants.URL_SHOP_READ,
            Response.Listener { response ->
                try {
                    val jsonObject = JSONObject(response.toString())
                    val success = jsonObject.getString("success")
                    val jsonArray = jsonObject.getJSONArray("shop")
                    val list = java.util.ArrayList<ProdItem>()
                    //Toast.makeText(context,  success.toString(), Toast.LENGTH_LONG).show()
                    if (success.equals("1")) {
                        for (i in 0 until jsonArray.length()) {
                            val obj = jsonArray.getJSONObject(i)
                            val item = ProdItem()
                            item.id = obj.getString("id").trim().toInt()
                            item.title = obj.getString("title").trim()
                            item.category = ""
                            item.amount = obj.getString("amount").trim().toDouble()
                            item.measure = obj.getString("measure").trim()
                            if (!fb) {
                                shopDbManager.deleteFromDb(requireContext(), user_id!!, item.title!!)
                            } else {
                                list.add(item)
                            }
                        }
                    }

                    Handler(Looper.getMainLooper()).postDelayed({
                        fillAdapter(list)
                    }, 500)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener {
                //Toast.makeText(this, error?.message, Toast.LENGTH_LONG).show()
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params : HashMap<String, String> = HashMap()
                params["user_id"] = user_id!!
                return params
            }
        }
        val requestQueue = Volley.newRequestQueue(requireContext())
        requestQueue.add(stringRequest)
    }
}