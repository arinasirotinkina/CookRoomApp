package com.example.cookroom

import androidx.test.core.app.ActivityScenario
import com.example.cookroom.db.products.ProductsDbManager
import com.example.cookroom.models.CategoryItem
import com.example.cookroom.models.MeasureTrans
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

class TransTesting {
    @Test
    fun shtKgTest() {
        val item = ProdItem()
        item.title = "яблоки"
        item.measure = "шт"
        item.amount = 3.0
        var p = MeasureTrans()
        var k = p.transFromShtToG(item)
        assertAll("category",
            Executable { assertEquals(300.0, k.amount) },
            Executable { assertEquals("г", k.measure) }

        )
    }
}
class TransTestingd {
    @Test
    fun shtKgTest() {
        val item = ProdItem()
        item.title = "яблоки"
        item.measure = "г"
        item.amount = 300.0
        var p = MeasureTrans()
        var k = p.transFromGToSht(item)
        assertAll("category",
            Executable { assertEquals(3.0, k.amount) },
            Executable { assertEquals("шт", k.measure) }

        )
    }
}
class TransTestingds {
    @Test
    fun shtKgTest() {
        val item = ProdItem()
        item.title = "яблоки"
        item.measure = "кг"
        item.amount = 1.0
        var p = MeasureTrans()
        var k = p.transFromKgToSht(item)
        assertAll("category",
            Executable { assertEquals(10.0, k.amount) },
            Executable { assertEquals("шт", k.measure) }

        )
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
