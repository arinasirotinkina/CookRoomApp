package com.example.cookroom.models

//класс перевода
class MeasureTrans {
    fun getCoef(title: String): Double{
        var coef = 1.0
        if (title.lowercase() == "яблоко" || title.lowercase() == "яблоки") {
            coef = 100.0
        } else if (title.lowercase() == "абрикос" || title.lowercase() == "абрикосы") {
            coef = 40.0
        }else if (title.lowercase() == "ананас" || title.lowercase() == "ананасы") {
            coef = 1200.0
        }else if (title.lowercase() == "апельсин" || title.lowercase() == "апельсины") {
            coef = 170.0
        }else if (title.lowercase() == "банан" || title.lowercase() == "бананы") {
            coef = 140.0
        }else if (title.lowercase() == "грейпфрут" || title.lowercase() == "грейпфруты") {
            coef = 350.0
        }else if (title.lowercase() == "груша" || title.lowercase() == "груши") {
            coef = 200.0
        }else if (title.lowercase() == "кабачок" || title.lowercase() == "кабачки") {
            coef = 500.0
        }else if (title.lowercase() == "капуста" || title.lowercase() == "капуста белокочанная"
            || title.lowercase() == "капуста цветная") {
            coef = 1500.0
        }else if (title.lowercase() == "картофель" || title.lowercase() == "картошка") {
            coef = 100.0
        }else if (title.lowercase() == "киви") {
            coef = 85.0
        }else if (title.lowercase() == "лимон" || title.lowercase() == "лимоны") {
            coef = 120.0
        }else if (title.lowercase() == "лук" || title.lowercase() == "лук репчатый") {
            coef = 120.0
        }else if (title.lowercase() == "мандарин" || title.lowercase() == "мандарины") {
            coef = 125.0
        }else if (title.lowercase() == "морковь") {
            coef = 100.0
        }else if (title.lowercase() == "огурец" || title.lowercase() == "огурцы") {
            coef = 150.0
        }else if (title.lowercase() == "перец" || title.lowercase() == "перцы") {
            coef = 130.0
        }else if (title.lowercase() == "персик" || title.lowercase() == "персики") {
            coef = 160.0
        }else if (title.lowercase() == "свекла") {
            coef = 400.0
        }else if (title.lowercase() == "помидор" || title.lowercase() == "поидоры"
            || title.lowercase() == "томат" || title.lowercase() == "томаты") {
            coef = 130.0
        } else if (title.lowercase() == "чеснок") {
            coef = 60.0
        }
        return coef
    }
    fun transFromShtToG(temp: ProdItem) : ProdItem{
        val item = ProdItem()
        val coef = getCoef(temp.title!!)
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
    fun transFromGToSht(temp: ProdItem):ProdItem {
        val item = ProdItem()
        val coef = 1 / getCoef(temp.title!!)
        item.title = temp.title
        item.amount = temp.amount?.times(coef)
        item.measure = "шт"
        return item
    }
    fun transFromKgToSht(temp: ProdItem):ProdItem {
        val item = ProdItem()
        val coef = 1000 / getCoef(temp.title!!)
        item.title = temp.title
        item.amount = temp.amount?.times(coef)
        item.measure = "шт"
        return item
    }
}