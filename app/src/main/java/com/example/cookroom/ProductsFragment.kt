package com.example.cookroom

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import com.example.cookroom.adapters.CategoryAdapter
import com.example.cookroom.models.CategoryItem

//Фрагмент с категориями продуктов
class ProductsFragment : Fragment(), AdapterView.OnItemClickListener {
    private var arrayList:ArrayList<CategoryItem>? = null
    private var gridView:GridView? = null
    private var productAdapter: CategoryAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_products, container, false)
        return view
    }

    override fun onStart() {
        super.onStart()
        gridView = view?.findViewById(R.id.my_grid_view_list)
        arrayList = ArrayList()
        arrayList = setDataList()
        productAdapter = CategoryAdapter(requireContext(), arrayList!!)
        gridView?.adapter = productAdapter
        gridView?.onItemClickListener = this
    }

    //категории продуктов
    private fun setDataList(): ArrayList<CategoryItem> {
        var arrayList:ArrayList<CategoryItem> = ArrayList()
        arrayList.add(CategoryItem(R.drawable.apple, "Фрукты"))
        arrayList.add(CategoryItem(R.drawable.cabbage, "Овощи"))
        arrayList.add(CategoryItem(R.drawable.milk, "Кисломолочные"))
        arrayList.add(CategoryItem(R.drawable.porridge, "Крупы"))
        arrayList.add(CategoryItem(R.drawable.meat, "Мясо"))
        arrayList.add(CategoryItem(R.drawable.fish, "Рыба"))
        arrayList.add(CategoryItem(R.drawable.bread, "Хлеб"))
        arrayList.add(CategoryItem(R.drawable.cookies, "Кондитерские"))
        arrayList.add(CategoryItem(R.drawable.salt, "Специи"))
        arrayList.add(CategoryItem(R.drawable.can, "Консервы"))
        arrayList.add(CategoryItem(R.drawable.water, "Напитки"))
        arrayList.add(CategoryItem(R.drawable.other, "Другое"))
        return arrayList
    }

    //слушатель нажатий для категорий
    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        var items : CategoryItem = arrayList!!.get(position)
        val intent = Intent(requireContext(), ProductListActivity::class.java)
        intent.putExtra("CHOSEN", items.name)
        startActivity(intent)
    }
}