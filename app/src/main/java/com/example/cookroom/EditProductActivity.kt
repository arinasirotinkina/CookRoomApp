package com.example.cookroom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.strictmode.IntentReceiverLeakedViolation
import android.text.Layout
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.cookroom.R
import com.example.cookroom.db.products.ProdIntentConstants
import com.example.cookroom.db.products.ProdDbManager
import com.google.android.material.floatingactionbutton.FloatingActionButton

class EditProductActivity : AppCompatActivity() {
    val myDbManager = ProdDbManager(this)
    var id = 0
    var isEditState = false
    var edTitle : EditText? = null
    var edAmount : EditText? = null
    var edMeasure : Spinner? = null
    var prodCategory : String  =""
    val measureVals = listOf<String>("кг", "г", "л", "мл")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_product)
        edTitle = findViewById(R.id.edTitle)
        edAmount = findViewById(R.id.edAmount)
        edMeasure = findViewById(R.id.edMeasure)
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
        if (myTitle != "" && myMeasure != "") {
            if (isEditState) {
                myDbManager.updateItem(myTitle, myCategory, myAmount, myMeasure, id)
            } else {
                myDbManager.insertToDb(myTitle, myCategory, myAmount, myMeasure)
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
            if(i.getStringExtra(ProdIntentConstants.I_TITLE_KEY) != null) {
                isEditState = true
                edTitle?.setText(i.getStringExtra(ProdIntentConstants.I_TITLE_KEY))
                edAmount?.setText(i.getStringExtra(ProdIntentConstants.I_AMOUNT_KEY))
                edMeasure?.setSelection(measureVals.indexOf(i.getStringExtra(ProdIntentConstants.I_MEASURE_KEY)))
                id = i.getIntExtra(ProdIntentConstants.I_ID_KEY, 0)
            }
        }
    }

}
