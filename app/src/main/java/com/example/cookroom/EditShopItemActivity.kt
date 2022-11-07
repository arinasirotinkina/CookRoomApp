package com.example.cookroom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Spinner
import com.example.cookroom.db.ShopDbManager
import com.example.cookroom.db.products.ProdIntentConstants
import com.example.cookroom.db.products.ProductsDbManager


//Активность редактирования/добавления элемента списка покупок
class EditShopItemActivity : AppCompatActivity() {
    var id = 0
    var isEditState = false
    var edTitle : EditText? = null
    var edAmount : EditText? = null
    var edMeasure : Spinner? = null
    var prodCategory : String  = ""
    val measureVals = listOf("шт", "кг", "г", "л", "мл")
    val productsDbManager = ProductsDbManager()
    var shopDbManager = ShopDbManager()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_shop_item)
        edTitle = findViewById(R.id.edTitle)
        edAmount = findViewById(R.id.edAmount)
        edMeasure = findViewById(R.id.edMeasure)
        getMyIntents()
    }
    //Слушатель нажатий кнопки сохранения
    fun onClickSave(view: View) {
        val intentProd = intent
        var prodCategory = intentProd!!.getCharSequenceExtra("CHOSEN")
        val myTitle = edTitle?.text.toString()
        val myCategory = prodCategory.toString()
        val myAmount = edAmount?.text.toString()
        val myMeasure = edMeasure?.selectedItem.toString()
        val pref = this.getSharedPreferences("User_Id", MODE_PRIVATE)
        val user_id = pref.getString("user_id", "-1")

        if (myTitle != "" && myMeasure != "") {
            shopDbManager.insertToDb(this, myTitle, myAmount, myMeasure, user_id!!)
            finish()
        }
        finish()
    }

    //получение значений выбранного итема при редактировании
    private fun getMyIntents() {
        val i = intent
        if (i != null) {
            if(i.getStringExtra(ProdIntentConstants.I_TITLE_KEY) != null) {
                isEditState = true
                edTitle?.setText(i.getStringExtra(ProdIntentConstants.I_TITLE_KEY))
                edAmount?.setText(i.getStringExtra(ProdIntentConstants.I_AMOUNT_KEY))
                edMeasure?.setSelection(measureVals.indexOf(i.getStringExtra(
                    ProdIntentConstants.I_MEASURE_KEY)))
                prodCategory = i.getStringExtra(ProdIntentConstants.I_CATEGORY_KEY)!!
                id = i.getIntExtra(ProdIntentConstants.I_ID_KEY, 0)
            }
        }
    }

}
