package com.example.cookroomd.db.product

import android.provider.BaseColumns

object ProdDbNameClass {
    const val TABLE_NAME = "product_table"
    const val COLUMN_NAME_TITLE = "title"
    const val COLUMN_NAME_CATEGORY = "category"
    const val COLUMN_NAME_AMOUNT = "amount"
    const val COLUMN_NAME_MEASURE = "measure"

    const val DATABASE_VERSION =  7
    const val DATABASE_NAME = "CookRoomDDb.db"

    const val CREATE_TABLE = "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY, $COLUMN_NAME_TITLE TEXT, $COLUMN_NAME_CATEGORY TEXT," +
            "$COLUMN_NAME_AMOUNT INTEGER, $COLUMN_NAME_MEASURE TEXT)"
    const val SQL_DELETE_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
}