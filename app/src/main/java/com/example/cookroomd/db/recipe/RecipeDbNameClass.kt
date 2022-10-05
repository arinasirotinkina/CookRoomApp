package com.example.cookroomd.db.recipe

import android.provider.BaseColumns

object RecipeDbNameClass {
    const val TABLE_NAME = "recipe_table"
    const val COLUMN_NAME_TITLE = "title"
    const val COLUMN_NAME_DESC = "description"


    const val CREATE_TABLE = "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY, $COLUMN_NAME_TITLE TEXT, $COLUMN_NAME_DESC TEXT)"
    const val SQL_DELETE_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
}