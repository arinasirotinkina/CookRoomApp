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
import com.example.cookroom.db.recipes.RecipeIntentConstants
import com.example.cookroom.db.recipes.RecipesDbManager
import com.example.cookroom.models.ProdItem
import org.json.JSONException
import org.json.JSONObject

class EditRecipeActivity : AppCompatActivity() {
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

    private fun getMyIntents() {
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
        val stringRequest = object : StringRequest(
            Method.POST, URL_SEARCH,
            Response.Listener<String> { response ->
                try {
                    val jsonObject = JSONObject(response.toString())
                    val success = jsonObject.getString("success")
                    val jsonArray = jsonObject.getJSONArray("recipe")
                    if (success.equals("1")) {
                        val obj = jsonArray.getJSONObject(0)
                        val ids = obj.getString("id").trim()
                        val i = Intent(this, AddIngredActivity::class.java)
                        i.putExtra("CHOSEN", ids)
                        startActivity(i)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener {
                //Toast.makeText(this, error?.message, Toast.LENGTH_LONG).show()
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
                            val minus_list = ArrayList<ProdItem>()
                            if (success.equals("1")) {
                                for (i in 0 until jsonArray.length()) {
                                    val obj = jsonArray.getJSONObject(i)
                                    val item = ProdItem()
                                    item.title = obj.getString("title").trim()
                                    item.category = obj.getString("category").trim()
                                    item.amount = obj.getString("amount").trim().toDouble()
                                    item.measure = obj.getString("measure").trim()
                                    item.id = obj.getString("id").trim().toInt()
                                    for (ik in list) {
                                        if (ik.id == item.id) {
                                            var temp = ProdItem()
                                            temp.id = ik.id
                                            temp.title = ik.title
                                            temp.category = item.category
                                            if (ik.measure == item.measure) {
                                                temp.measure = item.measure
                                                temp.amount = item.amount!! - ik.amount!!
                                            } else if ((ik.measure == "г"  && item.measure == "кг")
                                                || (ik.measure == "мл"  && item.measure == "л")) {
                                                temp.measure = ik.measure
                                                temp.amount = item.amount!! * 1000 - ik.amount!!
                                            } else if ((ik.measure == "кг"  && item.measure == "г")
                                                    || (ik.measure == "л"  && item.measure == "мл")) {
                                                temp.measure = item.measure
                                                temp.amount = item.amount!! - ik.amount!! * 1000
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
                    Response.ErrorListener {
                        //Toast.makeText(this, error?.message, Toast.LENGTH_LONG).show()
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
                                val item = ProdItem()
                                item.title = obj.getString("title").trim()
                                item.amount = obj.getString("amount").trim().toDouble()
                                item.measure = obj.getString("measure").trim()
                                item.id =  obj.getString("product_id").trim().toInt()
                                item.category = ""
                                list.add(item)
                            }
                            checkList(list, recipe_id)
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener {
                    //Toast.makeText(this, error?.message, Toast.LENGTH_LONG).show()
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
            Response.ErrorListener {
                //Toast.makeText(this, error?.message.toString(), Toast.LENGTH_LONG).show()
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
