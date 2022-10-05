package com.example.cookroomd.db.recipe

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import com.example.cookroomd.db.product.ProdDbHelper
import com.example.cookroomd.model.RecipeItem

class RecipeDbManager (context: Context) {


    private val recipeDbHelper = ProdDbHelper(context)
    var db: SQLiteDatabase? = null

    fun openDb() {
        db = recipeDbHelper.writableDatabase
    }

    fun insertToDb(title: String, description: String) {
        val values = ContentValues().apply {
            put(RecipeDbNameClass.COLUMN_NAME_TITLE, title)
            put(RecipeDbNameClass.COLUMN_NAME_DESC, description)
        }
        db?.insert(RecipeDbNameClass.TABLE_NAME, null, values)
    }

    fun updateItem(title: String, description: String, id: Int) {
        val selection = BaseColumns._ID + "=$id"
        val values = ContentValues().apply {
            put(RecipeDbNameClass.COLUMN_NAME_TITLE, title)
            put(RecipeDbNameClass.COLUMN_NAME_DESC, description)
        }
        db?.update(RecipeDbNameClass.TABLE_NAME, values, selection, null)
    }

    fun removeItemFromDb(id: String) {
        val selection = BaseColumns._ID + "=$id"
        db?.delete(RecipeDbNameClass.TABLE_NAME, selection, null)
    }


    fun readDbData(searchText : String): ArrayList<RecipeItem> {
        val dataList = ArrayList<RecipeItem>()
        val selection = "${RecipeDbNameClass.COLUMN_NAME_TITLE} like ?"
        val cursor = db?.query(RecipeDbNameClass.TABLE_NAME, null, selection, arrayOf("%$searchText%"), null, null, null)
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val dataTitle =
                    cursor.getString(cursor.getColumnIndexOrThrow(RecipeDbNameClass.COLUMN_NAME_TITLE))
                val dataDesc =
                    cursor.getString(cursor.getColumnIndexOrThrow(RecipeDbNameClass.COLUMN_NAME_DESC))
                val dataId =
                    cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID))
                var item = RecipeItem()
                item.title = dataTitle
                item.description = dataDesc
                item.id = dataId
                dataList.add(item)
            }
        }


        cursor?.close()
        return dataList
    }


    fun closeDb() {
        recipeDbHelper.close()
    }
}