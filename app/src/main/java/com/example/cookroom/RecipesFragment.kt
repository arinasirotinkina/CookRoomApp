package com.example.cookroom

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.cookroom.adapters.RecipeAdapter
import com.example.cookroom.db.DbLinkConstants
import com.example.cookroom.models.RecipeItem
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONException
import org.json.JSONObject

//Фрагмент рецептов со списком рецептов
class RecipesFragment : Fragment() {
    var rcView : RecyclerView? = null
    var tvNoElem : TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recipes, container, false)
        rcView = view.findViewById(R.id.rcView)
        tvNoElem = view.findViewById(R.id.tvNoElem)
        val fbNew = view.findViewById<FloatingActionButton>(R.id.fbNew)
        fbNew.setOnClickListener{
            val i = Intent(requireContext(), EditRecipeActivity::class.java)
            startActivity(i)
        }
        init()
        readRecipes()
        return view
    }
    override fun onStart() {
        super.onStart()
        init()
        readRecipes()
        Handler(Looper.getMainLooper()).postDelayed({
            readRecipes()
        }, 700)
    }
    fun init() {
        val myAdapter = RecipeAdapter(ArrayList(), requireContext())
        rcView?.layoutManager = LinearLayoutManager(requireContext())
        //val swapHelper = getSwapMg()
        //swapHelper.attachToRecyclerView(rcView)
        rcView?.adapter = myAdapter
    }

    //заполнение RecyclerView
    fun fillAdapter(list: ArrayList<RecipeItem>) {
        val myAdapter = RecipeAdapter(ArrayList(), requireContext())
        myAdapter.updateAdapter(list)
        rcView?.adapter = myAdapter
        if (list.size > 0) {
            tvNoElem?.visibility = View.GONE
        } else {
            tvNoElem?.visibility = View.VISIBLE
        }
    }

    //Чтение рецептов из базы
    private fun readRecipes() {
        val pref = requireActivity().getSharedPreferences("User_Id", AppCompatActivity.MODE_PRIVATE)
        val user_id = pref.getString("user_id", "-1")
        val stringRequest = object : StringRequest(
            Method.POST, DbLinkConstants.URL_REC_READ,
            Response.Listener { response ->
                try {
                    val jsonObject = JSONObject(response.toString())
                    val success = jsonObject.getString("success")
                    val jsonArray = jsonObject.getJSONArray("recipe")
                    val list = java.util.ArrayList<RecipeItem>()
                    if (success.equals("1")) {
                        for (i in 0 until jsonArray.length()) {
                            val obj = jsonArray.getJSONObject(i)
                            val item = RecipeItem()
                            item.title = obj.getString("title").trim()
                            item.description = obj.getString("description").trim()
                            item.id = obj.getString("id").trim().toInt()
                            list.add(item)
                        }
                    }
                    fillAdapter(list)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(requireContext(), error?.message, Toast.LENGTH_LONG).show()
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