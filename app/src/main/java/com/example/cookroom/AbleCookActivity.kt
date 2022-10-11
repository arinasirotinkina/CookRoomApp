package com.example.cookroom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.cookroom.db.depending.DepDbManager
import com.example.cookroom.db.products.ProdDbManager

class AbleCookActivity : AppCompatActivity() {
    val depDbManager = DepDbManager(this)
    val prodDbManager = ProdDbManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_able_cook)
        val kt = intent
        var recipeId = kt.getIntExtra("id", -1)
    }

    fun onClickCancel(view: View) {
        finish()
    }
    fun onClickAccept(view: View) {
        prodDbManager.openDb()
        depDbManager.openDb()
        val kt = intent
        var recipeId = kt.getIntExtra("id", -1)
        var ingreds = depDbManager.readDbData(recipeId)
        for (item in ingreds) {
            var product = prodDbManager.readIngred(item.product!!.toInt())[0]
            var itemAmount = item.amount
            var itemMeasure = item.measure
            var wholeAmount = product.amount
            var wholeMeasure = product.measure
            if (wholeMeasure != itemMeasure) {
                if (wholeMeasure == "кг") {
                    wholeAmount = wholeAmount!! * 1000
                    wholeMeasure = "г"
                } else if (wholeMeasure == "л") {
                    wholeAmount = wholeAmount!! * 1000
                    wholeMeasure = "мл"
                } else if (itemMeasure == "кг" ||itemMeasure == "л")  {
                    itemAmount = itemAmount!! * 1000
                }
            }
            wholeAmount = wholeAmount!! - itemAmount!!
            prodDbManager.updateItem(product.title!!, product.category!!, wholeAmount, wholeMeasure!!, product.id!!)
        }
        Toast.makeText(this, "r", Toast.LENGTH_LONG).show()
        finish()
    }
}