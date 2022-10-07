package com.example.cookroomd

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.cookroomd.adapters.ItemProductAdapter
import com.example.cookroomd.db.product.ProdDbManager
import com.example.cookroomd.db.recipe.RecipeDbManager
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val prodDbManager = ProdDbManager(this)
        val recipeDbManager = RecipeDbManager(this)
        prodDbManager.openDb()
        recipeDbManager.openDb()
    }

    override fun onStart() {
        super.onStart()
        var bottomNavigationView : BottomNavigationView? = null
        bottomNavigationView = findViewById(R.id.nav_view)
        var navController = findNavController(R.id.fragmentContainerView)
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.productsFragment, R.id.recipesFragment, R.id.shoplistFragment))
        /*setupActionBarWithNavController(navController, appBarConfiguration)*/
        bottomNavigationView.setupWithNavController(navController)
    }


}