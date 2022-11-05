package com.example.cookroom

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

//Главная активность, содержит 4 фрагмента: ProductsFragment, RecipesFragment, ShoplistFragment, UserFragment
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    override fun onStart() {
        super.onStart()
        var bottomNavigationView : BottomNavigationView? = null
        bottomNavigationView = findViewById(R.id.nav_view)
        var navController = findNavController(R.id.fragmentContainerView)
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.productsFragment, R.id.recipesFragment, R.id.shoplistFragment))
        bottomNavigationView.setupWithNavController(navController)
    }
}