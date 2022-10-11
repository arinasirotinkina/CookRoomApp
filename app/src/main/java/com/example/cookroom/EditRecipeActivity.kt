package com.example.cookroom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.cookroom.db.depending.DepDbManager
import com.example.cookroom.db.products.ProdDbManager
import com.example.cookroom.db.recipes.RecipeDbManager
import com.example.cookroom.db.recipes.RecipeIntentConstants

class EditRecipeActivity : AppCompatActivity() {
    val myDbManager = RecipeDbManager(this)
    val depDbManager = DepDbManager(this)
    val prodDbManager = ProdDbManager(this)
    var id = 0
    var isEditState = false
    var edTitle : EditText? = null
    var edDesc : EditText? = null
    var addIngred :Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_recipe)
        addIngred = findViewById(R.id.button)
        edTitle = findViewById(R.id.edTitle)
        edDesc = findViewById(R.id.edDesc)
        getMyIntents()
    }


    fun onClickSave(view: View) {
        var intentProd = intent
        val myTitle = edTitle?.text.toString()
        val myDesc = edDesc?.text.toString()
        if (myTitle != "") {
            if (isEditState) {
                myDbManager.updateItem(myTitle, myDesc, id)
            } else {
                myDbManager.insertToDb(myTitle, myDesc)
            }
            finish()
        }
        finish()
    }

    override fun onResume() {
        super.onResume()
        myDbManager.openDb()
        depDbManager.openDb()
        prodDbManager.openDb()

    }
    override fun onDestroy() {
        super.onDestroy()
        myDbManager.closeDb()
    }
    fun getMyIntents() {
        val i = intent
        if (i != null) {
            if(i.getStringExtra(RecipeIntentConstants.I_TITLE_KEY) != null) {
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
        if (myTitle != "") {
            if (isEditState) {
                myDbManager.updateItem(myTitle, myDesc, id)
            } else {
                myDbManager.insertToDb(myTitle, myDesc)
                id = myDbManager.readDbData(myTitle)[0].id!!
                isEditState = true
            }
        }
        val i = Intent(this, AddIngredActivity::class.java)
        i.putExtra("CHOSEN", id)
        startActivity(i)

    }

    fun onClickCook(view: View) {
        val myTitle = edTitle?.text.toString()
        val myDesc = edDesc?.text.toString()
        if (myTitle != "") {
            if (isEditState) {
                myDbManager.updateItem(myTitle, myDesc, id)
            } else {
                myDbManager.insertToDb(myTitle, myDesc)
                id = myDbManager.readDbData(myTitle)[0].id!!
                isEditState = true
            }
        }
        if (check(id)){
            val i = Intent(this, AbleCookActivity::class.java)
            i.putExtra("id", id)
            startActivity(i)
        } else {
            val i = Intent(this, DisableCookActivity::class.java)
            i.putExtra("id", id)
            startActivity(i)
        }

    }
    fun check(id: Int): Boolean {
        var ingreds = depDbManager.readDbData(id)

        for (item in ingreds) {
            var product = prodDbManager.readIngred(item.product!!.toInt())[0]
            var itemAmount = item.amount
            var itemMeasure = item.measure
            var wholeAmount = product.amount
            var wholeMeasure = product.measure
            Toast.makeText(this, "$itemAmount", Toast.LENGTH_LONG).show()
            if (wholeMeasure != itemMeasure) {
                if (wholeMeasure == "кг" || wholeMeasure == "л") {
                    wholeAmount = wholeAmount!! * 1000
                } else if (itemMeasure == "кг" ||itemMeasure == "л")  {
                    itemAmount = itemAmount!! * 1000
                }
            }
            if (itemAmount!! > wholeAmount!!) {
                return false
            }
        }
        return true
    }
}
