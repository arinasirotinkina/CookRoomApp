package com.example.cookroom

import android.media.audiofx.PresetReverb
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cookroom.adapters.NotHaveAdapter
import com.example.cookroom.models.ProdItem


class DisableCookActivity : AppCompatActivity() {
    var not_h_list : RecyclerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_disable_cook)
        val kt = intent
        var d = ArrayList<ProdItem>()
        val minus_list = kt.getParcelableArrayListExtra<ProdItem>("minus_list")
        for (item in minus_list!!) {
            if (item.amount!!.toInt() < 0) {
                var temp = ProdItem()
                temp.id = item.id
                temp.title = item.title
                temp.category = item.category
                temp.amount = -1 * item.amount!!
                temp.measure = item.measure
                d.add(temp)
            }
        }
        not_h_list = findViewById(R.id.not_having_list)
        var adapter = NotHaveAdapter(d, this)
        not_h_list?.layoutManager = LinearLayoutManager(this)
        not_h_list!!.adapter = adapter
    }
}