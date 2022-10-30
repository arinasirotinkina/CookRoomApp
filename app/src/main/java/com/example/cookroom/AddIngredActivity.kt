package com.example.cookroom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.cookroom.adapters.IngredAdapter
import com.example.cookroom.db.DepenDbManager
import com.example.cookroom.db.products.ProductsDbManager
import com.example.cookroom.models.ProdItem
import org.json.JSONException
import org.json.JSONObject


class AddIngredActivity : AppCompatActivity() {
    var addAmount : EditText? = null
    var addIngreds :Button? = null
    var rcView: RecyclerView? = null
    val myAdapter = IngredAdapter(ArrayList(), this)
    val productsDbManager = ProductsDbManager()
    var depenDbManager = DepenDbManager()
    var addTitle : AutoCompleteTextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_ingred)
        val pref = this.getSharedPreferences("User_Id", MODE_PRIVATE)
        val user_id = pref.getString("user_id", "-1")
        addIngreds = findViewById(R.id.addButton)
        addTitle = findViewById(R.id.addTitle)
        addAmount = findViewById(R.id.addAmount)
        rcView = findViewById(R.id.rcView)
        var selList = ArrayList<String>()
        var selectList = productsDbManager.selector(this, user_id!!, selList)
        val adapterSel = ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, selectList)
        addTitle!!.threshold = 1
        addTitle!!.setAdapter(adapterSel)
        addTitle!!.onFocusChangeListener = View.OnFocusChangeListener{ _, hasFocus ->
            if (hasFocus) {
                addTitle!!.showDropDown()
            }
        }
        addTitle!!.onItemClickListener = AdapterView.OnItemClickListener {parent, _, position, id ->
            val selectedItem = parent.getItemAtPosition(position).toString()
        }
        init()
        readDbData()
    }

    override fun onStart() {
        super.onStart()
        readDbData()
    }


    fun AddIngred(view: View) {
        val kt = intent
        var recipeId = kt.getStringExtra("CHOSEN")
        var addMeasure = findViewById<Spinner>(R.id.chooseMeasure)
        var amount1 = addAmount?.text.toString()
        val myMeasure = addMeasure?.selectedItem.toString()
        val title = addTitle!!.text.toString()
        val pref = this.getSharedPreferences("User_Id", MODE_PRIVATE)
        val user_id = pref.getString("user_id", "-1")

        val URL_SEARCH = "http://arinasyw.beget.tech/products_getid.php"
        var stringRequest = object : StringRequest(
            Method.POST, URL_SEARCH,
            Response.Listener<String> { response ->
                try {
                    val jsonObject = JSONObject(response.toString())
                    val success = jsonObject.getString("success")
                    val jsonArray = jsonObject.getJSONArray("product")
                    if (success.equals("1")) {
                        for (i in 0 until jsonArray.length()) {
                            var obj = jsonArray.getJSONObject(i)
                            var ids = obj.getString("id").trim()
                            depenDbManager.insertToDb(this, recipeId!!, ids, title, amount1, myMeasure, user_id!!)

                        }
                        readDbData()
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
                params.put("title", title)
                return params
            }
        }
        var requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)




    }
    fun init() {
        rcView?.layoutManager = LinearLayoutManager(this)
        //val swapHelper = getSwapMg()
        //swapHelper.attachToRecyclerView(rcView)
        rcView?.adapter = myAdapter
    }

    fun fillAdapter(ingredList: ArrayList<ProdItem>) {
        myAdapter.updateAdapter(ingredList)
    }
    fun readDbData() {
        val URL_READ = "https://cookroom.site/depending_readall.php"
        val kt = intent
        var recipeId = kt.getStringExtra("CHOSEN")
        var pref = this.getSharedPreferences("User_Id", MODE_PRIVATE)
        var user_id = pref.getString("user_id", "-1")
        var stringRequest = object : StringRequest(
            Method.POST, URL_READ,
            Response.Listener<String> { response ->
                try {
                    val jsonObject = JSONObject(response.toString())
                    val success = jsonObject.getString("success")
                    val jsonArray = jsonObject.getJSONArray("product")
                    val list = java.util.ArrayList<ProdItem>()
                    if (success.equals("1")) {
                        for (i in 0 until jsonArray.length()) {
                            val obj = jsonArray.getJSONObject(i)
                            var title = obj.getString("title").trim()
                            var amount = obj.getString("amount").trim()
                            var measure = obj.getString("measure").trim()
                            var item = ProdItem()
                            item.title = title
                            item.amount = amount.toDouble()
                            item.measure = measure
                            item.id = 0
                            item.category = ""

                            list.add(item)

                        }
                        //Toast.makeText(this, list.size.toString(), Toast.LENGTH_LONG).show()

                        fillAdapter(list)
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
                params.put("recipe_id", recipeId!!)
                params.put("user_id", user_id!!)
                return params
            }
        }
        var requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)

    }
    /*
    private fun getSwapMg() : ItemTouchHelper {
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
    */

}