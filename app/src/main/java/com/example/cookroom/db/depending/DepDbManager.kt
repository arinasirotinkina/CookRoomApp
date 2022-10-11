package com.example.cookroom.db.depending

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import com.example.cookroom.db.depending.DepDbNameClass
import com.example.cookroom.db.products.ProdDbHelper
import com.example.cookroom.db.products.ProdDbNameClass
import com.example.cookroom.db.recipes.RecipeDbNameClass
import com.example.cookroom.models.DepItem

class DepDbManager(context: Context) {
    private val depDbHelper = ProdDbHelper(context)
    var db: SQLiteDatabase? = null

    fun openDb() {
        db = depDbHelper.writableDatabase
    }


    fun insertToDb(recipe: Int, product: Int?, amount: Int, measure: String) {
        val values = ContentValues().apply {
            put(DepDbNameClass.COLUMN_NAME_RECIPE, recipe)
            put(DepDbNameClass.COLUMN_NAME_PRODUCT, product)
            put(DepDbNameClass.COLUMN_NAME_AMOUNT, amount)
            put(DepDbNameClass.COLUMN_NAME_MEASURE, measure)

        }
        db?.insert(DepDbNameClass.TABLE_NAME, null, values)
    }

    fun updateItem(recipe: String, product: String, amount: Int, measure: String, id: Int) {
        val selection = BaseColumns._ID + "=$id"
        val values = ContentValues().apply {
            put(DepDbNameClass.COLUMN_NAME_RECIPE, recipe)
            put(DepDbNameClass.COLUMN_NAME_PRODUCT, product)
            put(DepDbNameClass.COLUMN_NAME_AMOUNT, amount)
            put(DepDbNameClass.COLUMN_NAME_MEASURE, measure)
        }
        db?.update(RecipeDbNameClass.TABLE_NAME, values, selection, null)
    }

    fun removeItemFromDb(id: String) {
        val selection = BaseColumns._ID + "=$id"
        db?.delete(DepDbNameClass.TABLE_NAME, selection, null)
    }


    fun readDbData(recipeId: Int): ArrayList<DepItem> {
        val dataList = ArrayList<DepItem>()
        val selection = "${DepDbNameClass.COLUMN_NAME_RECIPE} like ?"
        val cursor = db?.query(DepDbNameClass.TABLE_NAME, null, selection, arrayOf("$recipeId"), null, null, null)
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val dataRecipe =
                    cursor.getString(cursor.getColumnIndexOrThrow(DepDbNameClass.COLUMN_NAME_RECIPE))
                val dataProduct =
                    cursor.getString(cursor.getColumnIndexOrThrow(DepDbNameClass.COLUMN_NAME_PRODUCT))
                val dataAmount =
                    cursor.getInt(cursor.getColumnIndexOrThrow(DepDbNameClass.COLUMN_NAME_AMOUNT))
                val dataMeasure =
                    cursor.getString(cursor.getColumnIndexOrThrow(DepDbNameClass.COLUMN_NAME_MEASURE))
                val dataId =
                    cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID))
                var item = DepItem()

                item.id = dataId
                item.recipe = dataRecipe
                item.product = dataProduct
                item.amount = dataAmount
                item.measure = dataMeasure
                dataList.add(item)
            }
        }


        cursor?.close()
        return dataList
    }


    fun closeDb() {
        depDbHelper.close()
    }
}