package com.example.pay_bill


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.fatura.R
import com.example.fatura.databinding.ActivityBillDetailBinding
import com.google.android.material.button.MaterialButton


class BillDetail: AppCompatActivity() {


   var binding: ActivityBillDetailBinding? = null

    @SuppressLint("SetTextI18n", "WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBillDetailBinding.inflate(layoutInflater)
        setContentView(binding!!.getRoot())

        val intent = this.intent

        if (intent != null){
            val amount =intent.getIntExtra("amount",0)
            val unit =intent.getIntExtra("unit",0)
            val debt =intent.getIntExtra("debt",0)
            val month =intent.getStringExtra("month")


            binding!!.textAmount.setText(Integer.toString(amount))
            binding!!.textUnit.setText(Integer.toString(unit))
            binding!!.textDebt.setText(Integer.toString(debt))
            binding!!.textMonth.text = month

        }

        val btnPay=findViewById<MaterialButton>(R.id.btnPay)

        btnPay.setOnClickListener{
            val id =  intent.getStringExtra("id")
            deleteBill(id.toString())
        }


    }

    private fun deleteBill(id: String?) {

       val url= "http://192.168.148.125:8080/fatura/deletebills.php/"
        val requestQueue= Volley.newRequestQueue(this)
        val stringRequest=object: StringRequest(Request.Method.POST,url,
            { response ->

                Toast.makeText(this,response,Toast.LENGTH_SHORT).show()
startActivity(Intent(this,Pay::class.java))
            },
            {
                error ->
            })
        {
            override fun getParams():HashMap<String,String>{
                val map =HashMap<String,String>()
                map["id"]= id!!
                return map
            }
        }
requestQueue.add(stringRequest)
    }

}
