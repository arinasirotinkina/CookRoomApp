package com.example.cookroomd.db.product

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import com.example.cookroomd.model.ProdItem

class ProdDbManager(context: Context) {


    private val prodDbHelper = ProdDbHelper(context)
    var db: SQLiteDatabase? = null

    fun openDb() {
        db = prodDbHelper.writableDatabase
    }

    fun insertToDb(title: String, category: String, amount: Int,  measure: String) {
        val values = ContentValues().apply {
            put(ProdDbNameClass.COLUMN_NAME_TITLE, title)
            put(ProdDbNameClass.COLUMN_NAME_CATEGORY, category)
            put(ProdDbNameClass.COLUMN_NAME_AMOUNT, amount)
            put(ProdDbNameClass.COLUMN_NAME_MEASURE, measure)
        }
        db?.insert(ProdDbNameClass.TABLE_NAME, null, values)
    }

    fun updateItem(title: String, category: String, amount: Int,  measure: String, id: Int){
        val selection = BaseColumns._ID + "=$id"
        val values = ContentValues().apply {
            put(ProdDbNameClass.COLUMN_NAME_TITLE, title)
            put(ProdDbNameClass.COLUMN_NAME_CATEGORY, category)
            put(ProdDbNameClass.COLUMN_NAME_AMOUNT, amount)
            put(ProdDbNameClass.COLUMN_NAME_MEASURE, measure)
        }
        db?.update(ProdDbNameClass.TABLE_NAME,  values, selection, null)
    }

    fun removeItemFromDb(id: String) {
        val selection = BaseColumns._ID + "=$id"
        db?.delete(ProdDbNameClass.TABLE_NAME, selection, null)
    }
    fun readDbData(searchText : String, prodCategory: String): ArrayList<ProdItem> {
        val dataList = ArrayList<ProdItem>()
        val selection = "${ProdDbNameClass.COLUMN_NAME_TITLE} like ? AND ${ProdDbNameClass.COLUMN_NAME_CATEGORY} like ?"
        val cursor = db?.query(ProdDbNameClass.TABLE_NAME, null, selection, arrayOf("%$searchText%", "%$prodCategory%"), null, null, null)
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val dataTitle =
                    cursor.getString(cursor.getColumnIndexOrThrow(ProdDbNameClass.COLUMN_NAME_TITLE))
                val dataCategory =
                    cursor.getString(cursor.getColumnIndexOrThrow(ProdDbNameClass.COLUMN_NAME_CATEGORY))
                val dataAmount =
                    cursor.getInt(cursor.getColumnIndexOrThrow(ProdDbNameClass.COLUMN_NAME_AMOUNT))
                val dataMeasure =
                    cursor.getString(cursor.getColumnIndexOrThrow(ProdDbNameClass.COLUMN_NAME_MEASURE))
                val dataId =
                    cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID))
                var item = ProdItem()
                item.title = dataTitle
                item.category = dataCategory
                item.amount = dataAmount
                item.measure = dataMeasure
                item.id = dataId
                dataList.add(item)
            }
        }


        cursor?.close()
        return dataList
    }
    fun closeDb() {
        prodDbHelper.close()
    }
}