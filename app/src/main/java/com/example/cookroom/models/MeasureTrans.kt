package com.example.cookroom.models

class MeasureTrans {
    fun transFromShtToG(temp: ProdItem) : ProdItem{
        var item = ProdItem()
        var coef = 1
        if (temp.title == "яблоки") {
            coef = 100
        }
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
}