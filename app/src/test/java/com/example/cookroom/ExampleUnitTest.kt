package com.example.cookroom

import androidx.test.core.app.ActivityScenario
import com.example.cookroom.db.products.ProductsDbManager
import com.example.cookroom.models.CategoryItem
import com.example.cookroom.models.ProdItem
import com.example.cookroom.models.RecipeItem
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.function.Executable


class RecipeTest {

    @Test
    fun recTesting() {
        val recipe = RecipeItem()
        recipe.description = "desc"
        recipe.title = "juj"
        assertAll("recipe",
            Executable { assertEquals("juj", recipe.title) },
            Executable { assertEquals("desc", recipe.description) }
        )
    }
}
class ProductTest {

    @Test
    fun prodTesting() {
        val product = ProdItem()
        product.title = "a"
        product.category = "b"
        assertAll("product",
            Executable { assertEquals("a", product.title) },
            Executable { assertEquals("b", product.category) }
        )
    }
}

class CategoryTest {

    @Test
    fun catTesting() {
        val category = CategoryItem(R.drawable.water, "x")
        assertAll("category",
            Executable { assertEquals("x", category.name) })
    }
}

class InsertTest {
    @Test
    fun insTesting() {
        ActivityScenario.launch(EditProductActivity::class.java).onActivity { activity->

            val prodDbManager = ProductsDbManager()
            assertEquals(true, prodDbManager.insertToDb(activity.applicationContext,
                "", "", "", "", ""))
        }

    }

}



class ExampleUnitTest {

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
}
