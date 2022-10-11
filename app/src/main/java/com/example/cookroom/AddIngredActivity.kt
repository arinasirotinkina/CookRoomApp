package com.example.cookroom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cookroom.adapters.IngredAdapter
import com.example.cookroom.adapters.ItemProductAdapter
import com.example.cookroom.db.depending.DepDbManager
import com.example.cookroom.db.products.ProdDbManager
import com.example.cookroom.models.DepItem
import com.example.cookroom.models.ProdItem


class AddIngredActivity : AppCompatActivity() {
    var addTitle :EditText? = null
    var addAmount : EditText? = null
    var productId:Int? = null
    var productDbManager = ProdDbManager(this)
    var depDbManager = DepDbManager(this)
    var addIngreds :Button? = null
    var rcView: RecyclerView? = null
    val myAdapter = IngredAdapter(ArrayList(), this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_ingred)
        var addMeasure = findViewById<Spinner>(R.id.chooseMeasure)
        var selected : String = addMeasure.selectedItem.toString()
        addIngreds = findViewById(R.id.addButton)
        addTitle = findViewById(R.id.addTitle)
        addAmount = findViewById(R.id.addAmount)
        rcView = findViewById(R.id.rcView)
        val kt = intent
        var recipeId = kt.getIntExtra("CHOSEN", -1)
        init()
        fillAdapter()

    }

    override fun onStart() {
        super.onStart()
        fillAdapter()
    }


    fun AddIngred(view: View) {
        val kt = intent
        var addMeasure = findViewById<Spinner>(R.id.chooseMeasure)
        var selected : String = addMeasure.selectedItem.toString()
        var product = addTitle?.text.toString()
        var amount1 = addAmount?.text.toString()
        var amount = amount1.toInt()
        productDbManager.openDb()
        depDbManager.openDb()
        val list = productDbManager.readProduct(product)
        if (list.size != 0){
            productId = list[0].id
        }
        var recipeId = kt.getIntExtra("CHOSEN", -1)

        if (productId != null && amount1 != "" && recipeId != -1) {
            //NToast.makeText(this, "$amount", Toast.LENGTH_LONG).show()
            depDbManager.insertToDb(recipeId, productId!!, amount, selected)
        }
        fillAdapter()


    }
    fun init() {
        rcView?.layoutManager = LinearLayoutManager(this)
        //val swapHelper = getSwapMg()
        //swapHelper.attachToRecyclerView(rcView)
        rcView?.adapter = myAdapter
    }

    fun fillAdapter() {
        val kt = intent
        var recipeId = kt.getIntExtra("CHOSEN", -1)
        depDbManager.openDb()
        val list = depDbManager.readDbData(recipeId)
        productDbManager.openDb()
        var ingredList  = ArrayList<ProdItem>()
        val prodList = productDbManager.readDbData("", "")
        for (item in list) {
            var temp = ProdItem()
            for (product in prodList){
                if (product.id.toString() == item.product){
                    temp.title = product.title
                    temp.id = productId
                    temp.category = product.category
                }
            }
            temp.amount = item.amount
            temp.measure = item.measure
            ingredList.add(temp)
        }
        myAdapter.updateAdapter(ingredList)
    }
    /*
    private fun getSwapMg() : ItemTouchHelper {
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
                myAdapter.removeItem(viewHolder.adapterPosition, myDbManager)
            }
        })
    }
    */
}