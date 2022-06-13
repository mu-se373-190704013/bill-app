package com.example.fatura

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView.OnItemClickListener
import java.util.HashMap;
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.fatura.databinding.ActivityListOfBillBinding
import org.json.JSONArray
import org.json.JSONException


class ListOfBill : AppCompatActivity() {

    private lateinit var binding: ActivityListOfBillBinding
    private lateinit var billList: MutableList<Bills>

    override fun onCreate(savedInstanceState: Bundle?) {
        if (SharedPrefManager.getInstance(this).isLoggedIn) {

            super.onCreate(savedInstanceState)
            binding = ActivityListOfBillBinding.inflate(layoutInflater)
            setContentView(binding.getRoot())

            //getting the recyclerview from xml
            //initializing the productlist
            billList = ArrayList<Bills>()
            //this method will fetch and parse json
            //to display it in recyclerview

            val user = SharedPrefManager.getInstance(this).user
            listBills(user.tc.toString())

        }
        else {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

     fun listBills(Tc:String?) {

        /*
        * Creating a String Request
        * The request type is GET defined by first parameter
        * The URL is defined in the second parameter
        * Then we have a Response Listener and a Error Listener
        * In response listener we will get the JSON response as a String
        * */

         //this is the JSON Data URL
         //make sure you are using the correct ip else it will not work

         val url= "http://192.168.148.87:8080/fatura/billoflist.php"
        val stringRequest = object :StringRequest(
            Method.POST, url,
            { response ->
                try {
                    //converting the string to json array object
                    val array = JSONArray(response)

                    //traversing through all the object
                    for (i in 0 until array.length()) {
       //getting product object from json array

                        val bill = array.getJSONObject(i)

                        //adding the product to product list
                        billList.add(
                            Bills(
                                bill.getString("id"),
                                bill.getString("Tc"),
                                bill.getInt("amount"),
                                bill.getInt("unit"),
                                bill.getInt("debt"),
                                bill.getString("month")
                            )
                        )

                        binding.listview.isClickable = true
                        binding.listview.adapter=BillsAdapter(this,billList)
                        binding.listview.setOnItemClickListener(OnItemClickListener { parent, view, position, id ->

                            val i = Intent(this@ListOfBill, BillDetail::class.java)
                            i.putExtra("amount",billList[position].amount)
                            i.putExtra("unit",billList[position].unit)
                            i.putExtra("debt",billList[position].debt)
                            i.putExtra("month",billList[position].month)
                            i.putExtra("id",billList[position].id)
                            startActivity(i)

                        })

                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error -> Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show() }
        )
        {

            override fun getParams(): HashMap<String, String> {
                val map = HashMap<String, String>()
                map["Tc"] = Tc!!
                return map
            }
        }

        //adding our stringrequest to queue
        Volley.newRequestQueue(this).add(stringRequest)
    }
}
