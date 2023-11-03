package com.example.gogreen.user_Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.gogreen.R
import com.razorpay.Checkout
import org.json.JSONObject

class checkoutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout2)
        val checkout = Checkout()
        // checkout.setKeyID( "SzzmGYd2srMwXMMYTX0K")
        checkout.setKeyID("rzp_test_YMpRNrRlG6VDeZ")
        try {
            val options = JSONObject()
            options.put("name", "Go Green")
            options.put("description", "Best Ecommerce App For Green Products")
           // options.put("image", "https://drive.google.com/file/d/1U1JuIS_Mf32wYdUjRytvAG8gy8IlspwK/view?usp=drivesdk")
           options.put("image", "https://i.pinimg.com/originals/22/85/63/2285638720d5a004208fb38664aaf4ab.png")

            options.put("theme.color", "#BD9B72")
            options.put("currency", "BDT")
            //  options.put("amount", (price!!.toInt() * 100)) //pass amount in currency subunits
            options.put("prefill.email", "bhoomiag67890@gmail.com.com")
            options.put("prefill.contact", "+9084243046")
            checkout.open(this, options)
        } catch (e: Exception) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
        }


    }

    private fun onPaymentSuccess(p0:String?) {
        Toast.makeText(this, "Payment Success", Toast.LENGTH_SHORT).show()


    }

    private fun onPaymentError(p0:Int, p1:String?) {
        Toast.makeText(this, "Payment error", Toast.LENGTH_SHORT).show()

    }
}
