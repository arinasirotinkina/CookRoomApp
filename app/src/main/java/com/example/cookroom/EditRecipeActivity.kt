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
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.cookroom.db.recipes.RecipeIntentConstants
import com.example.cookroom.db.recipes.RecipesDbManager
import com.example.cookroom.models.MeasureTrans
import com.example.cookroom.models.ProdItem
import org.json.JSONException
import org.json.JSONObject

//Активность редактирования/добавления рецепта
class EditRecipeActivity : AppCompatActivity() {
    val recipesDbManager = RecipesDbManager()
    var id = 0
    var isEditState = false
    var edTitle: EditText? = null
    var edDesc: EditText? = null
    var addIngred: Button? = null
    var deleteRecipe: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_recipe)
        addIngred = findViewById(R.id.button)
        edTitle = findViewById(R.id.edTitle)
        edDesc = findViewById(R.id.edDesc)
        deleteRecipe = findViewById(R.id.deleteButton)
        getMyIntents()
        val pref = this.getSharedPreferences("User_Id", MODE_PRIVATE)
        val user_id = pref.getString("user_id", "-1")
        deleteRecipe?.setOnClickListener {
            recipesDbManager.deleteFromDb(this, user_id!!, id.toString())
            finish()
        }

    }
    //слушатель нажатий кнопки сохранения
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

    //получение данных итема при нажатии на него для редактирования
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
        if (!isEditState) {
            deleteRecipe?.visibility = View.GONE
        }
    }

    //слушатель нажатий кнопки добавления ингредиентов
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
            Response.Listener { response ->
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
            override fun getParams(): Map<String, String> {
                val params: HashMap<String, String> = HashMap()
                params["user_id"] = user_id!!
                params["title"] = myTitle
                params["description"] = myDesc
                return params
            }
        }
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }
    //слушатель нажатий кнопки приготовления
    fun onClickCook(view: View) {
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
        check(user_id!!, myTitle, myDesc)
    }

    //Проверка возможности приготовления рецепта
    fun check(user_id: String, myTitle: String, myDesc: String)  {

        fun setId(recipe_id: String) {

            fun checkList(list: ArrayList<ProdItem>, recipe_id : String) {
                //Запуск активности в зависимости от возможности приготовить рецепт
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
                //Сравнение количества продукта пользователя и количества ингредиента
                val URL_SELECT = "https://cookroom.site/products_select.php"
                val stringRequest = object : StringRequest(
                    Method.POST, URL_SELECT,
                    Response.Listener { response ->
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
                                            val temp = ProdItem()
                                            temp.id = ik.id
                                            temp.title = ik.title
                                            temp.category = item.category
                                            if (ik.measure == item.measure) {
                                                temp.measure = item.measure
                                                temp.amount = item.amount!! - ik.amount!!
                                            } else if ((ik.measure == "г" && item.measure == "кг")
                                                || (ik.measure == "мл" && item.measure == "л")
                                            ) {
                                                temp.measure = item.measure
                                                temp.amount = item.amount!! - ik.amount!! / 1000
                                                //Toast.makeText(this, temp.amount.toString(), Toast.LENGTH_LONG).show()
                                            } else if ((ik.measure == "кг" && item.measure == "г")
                                                || (ik.measure == "л" && item.measure == "мл")
                                            ) {
                                                temp.measure = item.measure
                                                temp.amount = item.amount!! - ik.amount!! * 1000
                                            } else if (ik.measure == "шт" && item.measure == "г") {
                                                var p = MeasureTrans()
                                                var t = p.transFromShtToG(ik)
                                                temp.amount = item.amount!! - t.amount!!
                                                temp.measure = t.measure
                                            } else if (ik.measure == "шт" && item.measure == "кг") {
                                                var p = MeasureTrans()
                                                var t = p.transFromShtToKg(ik)
                                                temp.amount = item.amount!! - t.amount!!
                                                Toast.makeText(this, temp.amount.toString(), Toast.LENGTH_LONG).show()
                                                temp.measure = t.measure
                                            }else {
                                                temp.measure = ik.measure
                                                temp.amount = -1 * ik.amount!!
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
                        val params : HashMap<String, String> = HashMap<String, String>()
                        params["user_id"] = user_id
                        return params
                    }
                }
                val requestQueue = Volley.newRequestQueue(this)
                requestQueue.add(stringRequest)

            }
            //Получение ингредиентов рецепта
            val URL_READ = "https://cookroom.site/depending_readall.php"
            val pref = this.getSharedPreferences("User_Id", MODE_PRIVATE)
            val user_id = pref.getString("user_id", "-1")
            val stringRequest = object : StringRequest(
                Method.POST, URL_READ,
                Response.Listener { response ->
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
                override fun getParams(): Map<String, String> {
                    val params : HashMap<String, String> = HashMap()
                    params["recipe_id"] = recipe_id
                    params["user_id"] = user_id!!
                    return params
                }
            }
            val requestQueue = Volley.newRequestQueue(this)
            requestQueue.add(stringRequest)

        }

        //Получение id рецепта
        val URL_SEARCH = "https://cookroom.site/recipes_getid.php"
        val stringRequest = object : StringRequest(
            Method.POST, URL_SEARCH,
            Response.Listener { response ->
                try {
                    val jsonObject = JSONObject(response.toString())
                    val success = jsonObject.getString("success")
                    val jsonArray = jsonObject.getJSONArray("recipe")
                    if (success.equals("1")) {
                        val obj = jsonArray.getJSONObject(0)
                        val ids = obj.getString("id").trim()
                        //Вызывается функция выше
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
            override fun getParams(): Map<String, String> {
                val params: HashMap<String, String> = HashMap()
                params["user_id"] = user_id
                params["title"] = myTitle
                params["description"] = myDesc
                return params
            }
        }
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }
}
