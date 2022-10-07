package com.example.cookroomd.db.product

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.cookroomd.db.depending.DepDbNameClass
import com.example.cookroomd.db.recipe.RecipeDbNameClass

class ProdDbHelper(context: Context) : SQLiteOpenHelper(context, ProdDbNameClass.DATABASE_NAME, null, ProdDbNameClass.DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(ProdDbNameClass.CREATE_TABLE)
        db?.execSQL(RecipeDbNameClass.CREATE_TABLE)
        db?.execSQL(DepDbNameClass.CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(ProdDbNameClass.SQL_DELETE_TABLE)
        db?.execSQL(RecipeDbNameClass.SQL_DELETE_TABLE)
        db?.execSQL(DepDbNameClass.SQL_DELETE_TABLE)
        onCreate(db)
    }

}