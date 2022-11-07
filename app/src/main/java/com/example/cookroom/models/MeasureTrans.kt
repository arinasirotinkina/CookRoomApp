package com.example.cookroom.models

class MeasureTrans {
    val measureVals = listOf("яблоки")
    fun getCoef(title: String): Int{
        var coef = 1
        if (title == "яблоки") {
            coef = 100
        }
        return coef
    }
    fun transFromShtToG(temp: ProdItem) : ProdItem{
        var item = ProdItem()
        var coef = getCoef(temp.title!!)
        item.title = temp.title
        item.amount = temp.amount?.times(coef)
        item.measure = "г"
        return item
    }
    fun transFromShtToKg(temp: ProdItem):ProdItem{
        var item = transFromShtToG(temp)
        item.measure = "кг"
        item.amount = item.amount!! / 1000
        return item
    }
    fun transFromGToSht(temp: ProdItem) {

    }
}