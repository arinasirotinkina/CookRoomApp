package com.example.cookroom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.cookroom.db.products.ProdIntentConstants
import com.example.cookroom.db.products.ProductsDbManager
import com.example.cookroom.db.shop.ShopDbManager

class EditProductActivity : AppCompatActivity() {

    var id = 0
    var isEditState = false
    var edTitle : EditText? = null
    var edAmount : EditText? = null
    var edMeasure : Spinner? = null
    var prodCategory : String  =""
    val measureVals = listOf<String>("кг", "г", "л", "мл")
    val productsDbManager = ProductsDbManager()
    var sessionManager= SessionManager(this)
    var shopDbManager = ShopDbManager()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_product)
        edTitle = findViewById(R.id.edTitle)
        edAmount = findViewById(R.id.edAmount)
        edMeasure = findViewById(R.id.edMeasure)
        sessionManager.sessionManage(this)
        getMyIntents()
    }


    fun onClickSave(view: View) {
        var intentProd = intent
        var prodCategory = intentProd!!.getCharSequenceExtra("CHOSEN")
        val myTitle = edTitle?.text.toString()
        val myCategory = prodCategory.toString()
        val myAmount1 = edAmount?.text.toString()
        val myAmount = myAmount1.toInt()
        val myMeasure = edMeasure?.selectedItem.toString()

        var pref = this.getSharedPreferences("User_Id", MODE_PRIVATE)
        var user_id = pref.getString("user_id", "-1")
        if (myTitle != "" && myMeasure != "") {
            if (isEditState) {
                prodCategory = intentProd!!.getStringExtra(ProdIntentConstants.I_CATEGORY_KEY)









                productsDbManager.updateToDB(this, myTitle, prodCategory.toString(), myAmount1, myMeasure, user_id.toString(), id.toString())

            } else {
                if (myAmount.toInt() == 0) {
                    //shopDbManager.insertToDb()
                }
                productsDbManager.insertToDb(this, myTitle, myCategory, myAmount1,myMeasure, user_id!!)
            }
            finish()
        }
        finish()
    }


    fun getMyIntents() {
        val i = intent
        if (i != null) {
            if(i.getStringExtra(ProdIntentConstants.I_TITLE_KEY) != null) {
                isEditState = true
                edTitle?.setText(i.getStringExtra(ProdIntentConstants.I_TITLE_KEY))
                edAmount?.setText(i.getStringExtra(ProdIntentConstants.I_AMOUNT_KEY))
                edMeasure?.setSelection(measureVals.indexOf(i.getStringExtra(ProdIntentConstants.I_MEASURE_KEY)))
                prodCategory = i.getStringExtra(ProdIntentConstants.I_CATEGORY_KEY)!!
                //Toast.makeText(this, prodCategory, Toast.LENGTH_LONG).show()

                id = i.getIntExtra(ProdIntentConstants.I_ID_KEY, 0)
            }
        }
    }

}
