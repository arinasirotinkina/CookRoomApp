package com.example.cookroom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.cookroom.adapters.ItemProductAdapter
import com.example.cookroom.db.depending.DepenDbManager
import com.example.cookroom.db.products.ProductsDbManager
import com.example.cookroom.db.recipes.RecipeIntentConstants
import com.example.cookroom.db.recipes.RecipesDbManager
import com.example.cookroom.models.ProdItem
import org.json.JSONException
import org.json.JSONObject

class EditRecipeActivity : AppCompatActivity() {
    //val myDbManager = RecipeDbManager(this)
    val recipesDbManager = RecipesDbManager()
    var id = 0
    var isEditState = false
    var edTitle: EditText? = null
    var edDesc: EditText? = null
    var addIngred: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_recipe)

        addIngred = findViewById(R.id.button)
        edTitle = findViewById(R.id.edTitle)
        edDesc = findViewById(R.id.edDesc)
        getMyIntents()


    }

    fun onClickSave(view: View) {
        val myTitle = edTitle?.text.toString()
        val myDesc = edDesc?.text.toString()
        val pref = this.getSharedPreferences("User_Id", MODE_PRIVATE)
        val user_id = pref.getString("user_id", "-1")
        if (myTitle != "") {
            if (isEditState) {
                recipesDbManager.updateToDB(this, myTitle, myDesc, user_id!!, id.toString())
            } else {
                recipesDbManager.insertToDb(this, myTitle, myDesc, user_id!!)
            }
            finish()
        }
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        //myDbManager.closeDb()
    }

    fun getMyIntents() {
        val i = intent
        if (i != null) {
            if (i.getStringExtra(RecipeIntentConstants.I_TITLE_KEY) != null) {
                isEditState = true
                edTitle?.setText(i.getStringExtra(RecipeIntentConstants.I_TITLE_KEY))
                edDesc?.setText(i.getStringExtra(RecipeIntentConstants.I_DESC_KEY))
                id = i.getIntExtra(RecipeIntentConstants.I_ID_KEY, 0)
            }
        }
    }


    fun onClickAdd(view: View) {
        val myTitle = edTitle?.text.toString()
        val myDesc = edDesc?.text.toString()
        val pref = this.getSharedPreferences("User_Id", MODE_PRIVATE)
        val user_id = pref.getString("user_id", "-1")
        if (myTitle != "") {
            if (isEditState) {
                recipesDbManager.updateToDB(this, myTitle, myDesc, user_id!!, id.toString())
            } else {
                recipesDbManager.insertToDb(this, myTitle, myDesc, user_id!!)
                isEditState = true
            }
        }
        val URL_SEARCH = "https://cookroom.site/recipes_getid.php"
        var stringRequest = object : StringRequest(
            Method.POST, URL_SEARCH,
            Response.Listener<String> { response ->
                try {
                    val jsonObject = JSONObject(response.toString())
                    val success = jsonObject.getString("success")
                    val jsonArray = jsonObject.getJSONArray("recipe")
                    if (success.equals("1")) {
                        var obj = jsonArray.getJSONObject(0)
                        var ids = obj.getString("id").trim()

                        val i = Intent(this, AddIngredActivity::class.java)
                        i.putExtra("CHOSEN", ids)
                        startActivity(i)

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
                var params: HashMap<String, String> = HashMap<String, String>()
                params.put("user_id", user_id!!)
                params.put("title", myTitle)
                params.put("description", myDesc)
                return params
            }
        }
        var requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)


    }

    fun onClickCook(view: View) {
        val myTitle = edTitle?.text.toString()
        val myDesc = edDesc?.text.toString()

        var pref = this.getSharedPreferences("User_Id", MODE_PRIVATE)
        var user_id = pref.getString("user_id", "-1")
        if (myTitle != "") {
            if (isEditState) {
                //myDbManager.updateItem(myTitle, myDesc, id)
                recipesDbManager.updateToDB(this, myTitle, myDesc, user_id!!, id.toString())
            } else {

                recipesDbManager.insertToDb(this, myTitle, myDesc, user_id!!)
                isEditState = true
            }
        }
        check(user_id!!, myTitle, myDesc)

    }

    fun check(user_id: String, myTitle: String, myDesc: String)  {

        fun setId(recipe_id: String) {
            fun checkList(list: ArrayList<ProdItem>, recipe_id : String) {
                fun start(minus_list: ArrayList<ProdItem>, recipe_id: String) {
                    Toast.makeText(this, minus_list.size.toString(), Toast.LENGTH_LONG).show()
                    var flag = true
                    for (item in minus_list) {
                        if (item.amount!! < 0) {
                            flag = false
                            break
                        }
                    }
                    if (flag) {
                        val i = Intent(this, AbleCookActivity::class.java)
                        i.putExtra("minus_list", minus_list)
                        startActivity(i)
                    }
                    else {
                        val i = Intent(this, DisableCookActivity::class.java)
                        i.putExtra("minus_list", minus_list)

                        startActivity(i)
                    }
                }

                val URL_SELECT = "https://cookroom.site/products_select.php"
                var stringRequest = object : StringRequest(
                    Method.POST, URL_SELECT,
                    Response.Listener<String> { response ->
                        try {
                            val jsonObject = JSONObject(response.toString())
                            val success = jsonObject.getString("success")
                            val jsonArray = jsonObject.getJSONArray("product")
                            val list1 = java.util.ArrayList<ProdItem>()
                            val minus_list = ArrayList<ProdItem>()
                            if (success.equals("1")) {
                                for (i in 0 until jsonArray.length()) {
                                    val obj = jsonArray.getJSONObject(i)
                                    val id = obj.getString("id").trim()
                                    var title = obj.getString("title").trim()
                                    var category = obj.getString("category").trim()
                                    var amount = obj.getString("amount").trim()
                                    var measure = obj.getString("measure").trim()
                                    var item = ProdItem()
                                    item.title = title
                                    item.category = category
                                    item.amount = amount.toDouble()
                                    item.measure = measure
                                    item.id = id.toInt()
                                    list1.add(item)
                                    for (ik in list) {
                                        if (ik.id == item.id) {
                                            var temp = ProdItem()
                                            temp.id = ik.id
                                            temp.title = ik.title
                                            temp.category = item.category
                                            if (ik.measure == item.measure) {
                                                temp.measure = item.measure
                                                temp.amount = item.amount!! - ik.amount!!
                                            }
                                            minus_list.add(temp)
                                        }
                                    }

                                }
                                start(minus_list, recipe_id)
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

            }
            val URL_READ = "https://cookroom.site/depending_readall.php"
            val pref = this.getSharedPreferences("User_Id", MODE_PRIVATE)
            val user_id = pref.getString("user_id", "-1")
            val stringRequest = object : StringRequest(
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
                                val title = obj.getString("title").trim()
                                val amount = obj.getString("amount").trim()
                                val measure = obj.getString("measure").trim()
                                val id = obj.getString("product_id").trim()
                                val item = ProdItem()
                                item.title = title
                                item.amount = amount.toDouble()
                                item.measure = measure
                                item.id =  id.toInt()
                                item.category = ""
                                list.add(item)
                            }
                            checkList(list, recipe_id)
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
                    params.put("recipe_id", recipe_id!!)
                    params.put("user_id", user_id!!)
                    return params
                }
            }
            var requestQueue = Volley.newRequestQueue(this)
            requestQueue.add(stringRequest)

        }

        val URL_SEARCH = "https://cookroom.site/recipes_getid.php"
        var stringRequest = object : StringRequest(
            Method.POST, URL_SEARCH,
            Response.Listener<String> { response ->
                try {
                    val jsonObject = JSONObject(response.toString())
                    val success = jsonObject.getString("success")
                    val jsonArray = jsonObject.getJSONArray("recipe")
                    if (success.equals("1")) {
                        var obj = jsonArray.getJSONObject(0)
                        var ids = obj.getString("id").trim()
                        setId(ids)

                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            object : Response.ErrorListener {
                override fun onErrorResponse(error: VolleyError?) {
                    //Toast.makeText(this, error?.message.toString(), Toast.LENGTH_LONG).show()
                }
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String>? {
                var params: HashMap<String, String> = HashMap<String, String>()
                params.put("user_id", user_id)
                params.put("title", myTitle)
                params.put("description", myDesc)
                return params
            }
        }
        var requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)

    }
}
