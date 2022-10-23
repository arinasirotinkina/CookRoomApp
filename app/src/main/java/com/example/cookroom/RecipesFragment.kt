package com.example.cookroom

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.AsyncHttpStack
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.cookroom.adapters.ItemProductAdapter
import com.example.cookroom.adapters.RecipeAdapter
import com.example.cookroom.db.recipes.RecipeDbManager
import com.example.cookroom.models.ProdItem
import com.example.cookroom.models.RecipeItem
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONException
import org.json.JSONObject


class RecipesFragment : Fragment() {

    var rcView : RecyclerView? = null
    var tvNoElem : TextView? = null
    var searchView: SearchView? = null
    var prodCategory: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_recipes, container, false)
        rcView = view.findViewById(R.id.rcView)
        tvNoElem = view.findViewById(R.id.tvNoElem)
        searchView = view.findViewById(R.id.searchView)
        var fbNew = view.findViewById<FloatingActionButton>(R.id.fbNew)
        fbNew.setOnClickListener{
            var i = Intent(requireContext(), EditRecipeActivity::class.java)
            startActivity(i)
        }
        init()
        //initSearchView()
        readRecipes()
        return view
    }
    override fun onStart() {
        super.onStart()
        init()
        //initSearchView()
        readRecipes()
    }
    fun init() {
        val myAdapter = RecipeAdapter(ArrayList(), requireContext())
        rcView?.layoutManager = LinearLayoutManager(requireContext())
        val swapHelper = getSwapMg()
        swapHelper.attachToRecyclerView(rcView)
        rcView?.adapter = myAdapter
    }
    private fun initSearchView() {
        val myDbManager = RecipeDbManager(requireContext())
        val myAdapter = RecipeAdapter(ArrayList(), requireContext())
        searchView?.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val list = myDbManager.readDbData(newText!!)
                myAdapter.updateAdapter(list)
                return true
            }
        })
    }
    fun fillAdapter(list: ArrayList<RecipeItem>) {
        val myDbManager = RecipeDbManager(requireContext())
        myDbManager.openDb()
        //val list = myDbManager.readDbData("")
        val myAdapter = RecipeAdapter(ArrayList(), requireContext())
        myAdapter.updateAdapter(list)
        rcView?.adapter = myAdapter
        if (list.size > 0) {
            tvNoElem?.visibility = View.GONE
        } else {
            tvNoElem?.visibility = View.VISIBLE
        }
    }
    private fun getSwapMg() : ItemTouchHelper {
        val myDbManager = RecipeDbManager(requireContext())
        val myAdapter = RecipeAdapter(ArrayList(), requireContext())
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
                myAdapter.removeItem(viewHolder.adapterPosition, myDbManager)
            }
        })
    }
    fun readRecipes() {
        val URL_READ1 = "http://arinasyw.beget.tech/recipes_readall.php"
        //val list = myDbManager.readDbData("", prodCategory!!)
        var pref = requireActivity().getSharedPreferences("User_Id", AppCompatActivity.MODE_PRIVATE)
        var user_id = pref.getString("user_id", "-1")

        //Toast.makeText(requireContext(), user_id, Toast.LENGTH_LONG).show()
        //val list = productsDbManager.readDbData(this, prodCategory!!, user_id.toString())


        val stringRequest = object : StringRequest(
            Method.POST, URL_READ1,
            Response.Listener<String> { response ->
                try {
                    val jsonObject = JSONObject(response.toString())
                    val success = jsonObject.getString("success")
                    val jsonArray = jsonObject.getJSONArray("recipe")
                    val list = java.util.ArrayList<RecipeItem>()
                    //Toast.makeText(context,  success.toString(), Toast.LENGTH_LONG).show()
                    if (success.equals("1")) {
                        //Toast.makeText(context, jsonArray.length().toString(), Toast.LENGTH_LONG).show()
                        for (i in 0 until jsonArray.length()) {
                            val obj = jsonArray.getJSONObject(i)
                            val id = obj.getString("id").trim()
                            var title = obj.getString("title").trim()
                            var description = obj.getString("description").trim()
                            //Toast.makeText(context, title, Toast.LENGTH_LONG).show()
                            var item = RecipeItem()
                            item.title = title
                            item.description = description
                            item.id = id.toInt()
                            list.add(item)
                        }
                    }
                    fillAdapter(list)
                    //Toast.makeText(context, list.size.toString(), Toast.LENGTH_LONG).show()
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
        //Toast.makeText(context, dataList.size.toString(), Toast.LENGTH_LONG).show()

        var requestQueue = Volley.newRequestQueue(requireContext())
        requestQueue.add(stringRequest)
    }

}