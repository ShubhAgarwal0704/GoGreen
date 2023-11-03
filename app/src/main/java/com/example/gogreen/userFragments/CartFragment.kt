package com.example.gogreen.userFragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.gogreen.databinding.FragmentCartBinding
import com.example.gogreen.roomdb.AppDatabase
import com.example.gogreen.roomdb.ProductModel
import com.example.gogreen.userAdapter.CartAdapter
import com.example.gogreen.user_Activity.checkoutActivity


class CartFragment : Fragment() {
    private lateinit var binding:FragmentCartBinding
    private lateinit var list: ArrayList<String>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(layoutInflater)
       /* val preferences = requireContext().getSharedPreferences("info", AppCompatActivity.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putBoolean("isCart",false)
        editor.apply()*/

        val dao = AppDatabase.getInstance(requireContext()).productDao()


        list = ArrayList()

        dao.getAllProducts().observe(requireActivity()) {
            binding.cartRecycler.adapter = CartAdapter(requireContext(), it)

            list.clear()
            for (data in it) {
                list.add(data.productId)
            }

            totalCost(it)
        }

        return (binding.root)
    }
    private fun totalCost(data: List<ProductModel>?) {
        var total = 0
        for (item in data!!) {
            total += item.productprice!!.toInt()
        }

        binding.textView12.text = "Total item in cart is ${data.size}"
        binding.textView13.text = "Total Cost : $total"

        binding.checkout.setOnClickListener {
            val intent = Intent(context, checkoutActivity::class.java)
            val b = Bundle()
            b.putStringArrayList("productIds", list)
            b.putString("totalCost", total.toString())
            intent.putExtras(b)
            startActivity(intent)



        }

    }
}