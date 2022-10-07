package com.example.cookroomd

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.example.cookroomd.db.product.ProdDbManager
import com.example.cookroomd.db.product.ProdIntentConstants
import com.example.cookroomd.db.recipe.RecipeDbManager
import com.example.cookroomd.db.recipe.RecipeIntentConstants

class EditRecipeActivity : AppCompatActivity() {
    val myDbManager = RecipeDbManager(this)
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
        val i = Intent(this, AddIngredActivity::class.java)
        startActivity(i)
    }

}
