package com.example.cookroom

import android.content.Context
import android.icu.util.Measure
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.InputType
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.cookroom.adapters.IngredAdapter
import com.example.cookroom.db.DbLinkConstants
import com.example.cookroom.db.DepenDbManager
import com.example.cookroom.db.products.ProductsDbManager
import com.example.cookroom.models.ProdItem
import org.json.JSONException
import org.json.JSONObject

//Активность добавления ингредиентов
class AddIngredActivity : AppCompatActivity() {
    var addAmount : EditText? = null
    var addIngreds :Button? = null
    var rcView: RecyclerView? = null
    val myAdapter = IngredAdapter(ArrayList(), this)
    val productsDbManager = ProductsDbManager()
    var depenDbManager = DepenDbManager()
    var addTitle : AutoCompleteTextView? = null
    var selectList = ArrayList<String>()
    var flag = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_ingred)
        val pref = this.getSharedPreferences("User_Id", MODE_PRIVATE)
        val user_id = pref.getString("user_id", "-1")
        addIngreds = findViewById(R.id.addButton)
        addTitle = findViewById(R.id.addTitle)
        addAmount = findViewById(R.id.addAmount)
        rcView = findViewById(R.id.rcView)
        addAmount?.inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL

        val selList = ArrayList<String>()
        selectList = productsDbManager.selector(this, user_id!!, selList)
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

    //слушатель нажатий кнопки добавления ингредиентов
    fun AddIngred(view: View) {
        val addMeasure = findViewById<Spinner>(R.id.chooseMeasure)
        val addCategory = findViewById<Spinner>(R.id.addCategory)
        val amount = addAmount?.text.toString()
        val measure = addMeasure?.selectedItem.toString()
        val title = addTitle!!.text.toString()
        val pref = this.getSharedPreferences("User_Id", MODE_PRIVATE)
        val user_id = pref.getString("user_id", "-1")
        if (amount.toDoubleOrNull() == null && amount.toIntOrNull() == null) {
            Toast.makeText(this, "Количество введено не в числовом формате", Toast.LENGTH_LONG).show()
        }else if (title !in selectList && !flag) {
            Toast.makeText(this, "", Toast.LENGTH_LONG).show()
            addCategory?.visibility = View.VISIBLE
            flag = true
        }
        else {
            if (addCategory?.selectedItem.toString() != "" && flag) {
                productsDbManager.insertToDb(this, title, addCategory?.selectedItem.toString(),
                    "0", measure, user_id!!)
                flag = false
                addCategory?.visibility = View.GONE
            }
            Handler(Looper.getMainLooper()).postDelayed({
                addToBase(title, amount, measure, user_id!!)
            }, 400)
        }
        Handler(Looper.getMainLooper()).postDelayed({
            readDbData()
        }, 1000)
    }
    fun addToBase(title:String, amount: String, measure: String, user_id: String) {
        //добавление связи в базу
        val kt = intent
        val recipeId = kt.getStringExtra("CHOSEN")
        val stringRequest = object : StringRequest(
            Method.POST, DbLinkConstants.URL_PROD_GETID,
            Response.Listener { response ->
                try {
                    val jsonObject = JSONObject(response.toString())
                    val success = jsonObject.getString("success")
                    val jsonArray = jsonObject.getJSONArray("product")
                    if (success.equals("1")) {
                        for (i in 0 until jsonArray.length()) {
                            val obj = jsonArray.getJSONObject(i)
                            val ids = obj.getString("id").trim()
                            addTitle?.setText("")
                            addAmount?.setText("")
                            depenDbManager.insertToDb(this, recipeId!!, ids, title,
                                amount, measure, user_id)
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
            override fun getParams(): Map<String, String>? {
                val params: HashMap<String, String> = HashMap()
                params["user_id"] = user_id!!
                params["title"] = title
                return params
            }
        }
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }
    //инициализация адаптера
    fun init() {
        rcView?.layoutManager = LinearLayoutManager(this)
        val swapHelper = getSwapMg(this)
        swapHelper.attachToRecyclerView(rcView)
        rcView?.adapter = myAdapter
    }
    //Заполнение списка
    fun fillAdapter(ingredList: ArrayList<ProdItem>) {
        myAdapter.updateAdapter(ingredList)
    }
    //Чтение ингредиентов из базы
    fun readDbData() {
        val kt = intent
        val recipeId = kt.getStringExtra("CHOSEN")
        val pref = this.getSharedPreferences("User_Id", MODE_PRIVATE)
        val user_id = pref.getString("user_id", "-1")
        val stringRequest = object : StringRequest(
            Method.POST, DbLinkConstants.URL_DEP_READ,
            Response.Listener { response ->
                try {
                    val jsonObject = JSONObject(response.toString())
                    val success = jsonObject.getString("success")
                    val jsonArray = jsonObject.getJSONArray("product")
                    val list = java.util.ArrayList<ProdItem>()
                    if (success.equals("1")) {
                        for (i in 0 until jsonArray.length()) {
                            val obj = jsonArray.getJSONObject(i)
                            var item = ProdItem()
                            item.title =  obj.getString("title").trim()
                            item.amount = obj.getString("amount").trim().toDouble()
                            item.measure = obj.getString("measure").trim()
                            item.id = 0
                            item.category = ""
                            list.add(item)
                        }
                        fillAdapter(list)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, error?.message.toString(), Toast.LENGTH_LONG).show()
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params : HashMap<String, String> = HashMap()
                params["recipe_id"] = recipeId!!
                params["user_id"] = user_id!!
                return params
            }
        }
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }

    //Удаление по свайпу
    private fun getSwapMg(context: Context) : ItemTouchHelper {
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
                val kt = intent
                var recipeId = kt.getStringExtra("CHOSEN")
                val pref = getSharedPreferences("User_Id", MODE_PRIVATE)
                val user_id = pref.getString("user_id", "-1")
                myAdapter.removeItem(viewHolder.adapterPosition, depenDbManager, user_id!!, recipeId!!, context)
            }
        })
    }
}