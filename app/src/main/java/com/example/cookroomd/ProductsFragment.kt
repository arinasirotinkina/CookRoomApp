package com.example.cookroomd

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import android.widget.Toast
import com.example.cookroomd.adapters.ProductAdapter
import com.example.cookroomd.model.CategoryItem


class ProductsFragment : Fragment(), AdapterView.OnItemClickListener {
    private var arrayList:ArrayList<CategoryItem>? = null
    private var gridView:GridView? = null
    private var productAdapter:ProductAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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
        productAdapter = ProductAdapter(requireContext(), arrayList!!)
        gridView?.adapter = productAdapter
        gridView?.onItemClickListener = this

    }

    private fun setDataList(): ArrayList<CategoryItem> {
        var arrayList:ArrayList<CategoryItem> = ArrayList()
        arrayList.add(CategoryItem(R.drawable.cicon, "Хлебобулочные"))
        arrayList.add(CategoryItem(R.drawable.cicon, "Мясные продукты"))
        arrayList.add(CategoryItem(R.drawable.cicon, "Крупы"))
        arrayList.add(CategoryItem(R.drawable.cicon, "Рыбные продукты"))
        arrayList.add(CategoryItem(R.drawable.cicon, "Кондитерские изделия"))
        arrayList.add(CategoryItem(R.drawable.cicon, "Молочные продукты"))
        arrayList.add(CategoryItem(R.drawable.cicon, "Специи"))
        arrayList.add(CategoryItem(R.drawable.cicon, "Крупы2"))
        arrayList.add(CategoryItem(R.drawable.cicon, "Другое"))
        return arrayList
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        var items : CategoryItem = arrayList!!.get(position)
        Toast.makeText(requireContext(), items.name, Toast.LENGTH_LONG).show()
        val intent = Intent(requireContext(), ProductListActivity::class.java)
        intent.putExtra("CHOSEN", items.name)
        startActivity(intent)

    }

}