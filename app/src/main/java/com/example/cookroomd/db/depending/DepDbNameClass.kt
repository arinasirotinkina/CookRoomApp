package com.example.cookroomd.db.depending

import android.provider.BaseColumns
import com.example.cookroomd.db.product.ProdDbNameClass

object DepDbNameClass {
    const val TABLE_NAME = "depending_table"
    const val COLUMN_NAME_RECIPE= "recipe"
    const val COLUMN_NAME_PRODUCT = "product"
    const val COLUMN_NAME_AMOUNT = "amount"
    const val COLUMN_NAME_MEASURE = "measure"

    const val CREATE_TABLE = "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY, $COLUMN_NAME_RECIPE INTEGER, $COLUMN_NAME_PRODUCT INTEGER," +
            " $COLUMN_NAME_AMOUNT INTEGER, $COLUMN_NAME_MEASURE TEXT)"
    const val SQL_DELETE_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
}
