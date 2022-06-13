package com.example.fatura

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.example.assign6.VolleySingleton
import org.json.JSONException
import org.json.JSONObject


class StaffMain : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_staff_main)
        if (SharedPrefManager.getInstance(this).isLoggedIn) {

            val btnStaffLogout = findViewById<Button>(R.id.buttonStaffLogout)
            btnStaffLogout.setOnClickListener(this)

            val buttonSend=findViewById<Button>(R.id.SendBill)

            buttonSend.setOnClickListener(View.OnClickListener {
                //if user pressed on button register
                //here we will register the user to server
                sendBill()
            })

        }
            else {
            val intent = Intent(this@StaffMain, StaffLogin::class.java)
            startActivity(intent)
            finish()
        }
    }
    private fun sendBill(){
        val textViewUserTc =findViewById<EditText>(R.id.textViewUserTc)
        val textViewUserAmount =findViewById<EditText>(R.id.textViewUserAmount)
        val textViewUnit =findViewById<EditText>(R.id.textViewUnit)
        val textViewDebt =findViewById<EditText>(R.id.textViewDebt)
        val textViewMonth =findViewById<EditText>(R.id.textViewMonth)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        val Tc = textViewUserTc.text.toString().trim { it <= ' ' }
        val amount = textViewUserAmount.text.toString().trim { it <= ' ' }
        val unit = textViewUnit.text.toString().trim { it <= ' ' }
        val debt = textViewDebt.text.toString().trim { it <= ' ' }
        val month = textViewMonth.text.toString().trim { it <= ' ' }

        if (TextUtils.isEmpty(Tc)) {
            textViewUserTc.error = "Please enter user tc"
            textViewUserTc.requestFocus()
            return
        }
        if (TextUtils.isEmpty(amount)) {
            textViewUserAmount .error = "Please enter bill amount"
            textViewUserAmount .requestFocus()
            return
        }
        if (TextUtils.isEmpty(unit)) {
            textViewUnit.error = "Please enter bill unit"
            textViewUnit.requestFocus()
            return
        }
        if (TextUtils.isEmpty(debt)) {
            textViewDebt.error = "Please enter bill debt"
            textViewDebt.requestFocus()
            return
        }
        if (TextUtils.isEmpty(month)) {
            textViewMonth.error = "Please enter bill month"
            textViewMonth.requestFocus()
            return
        }

        val URLBILL = URLs.URL_BILL + "?Tc="+Tc+"&amount="+amount+"&unit="+unit+"&debt="+debt+"&month="+month
        println(URLBILL)
        val stringRequest = object : StringRequest(
            Method.POST, URLBILL,
            Response.Listener { response ->
                progressBar.visibility = View.GONE

                try {
                    //converting response to json object
                    val obj = JSONObject(response)
                    //if no error in response
                    if (!obj.getBoolean("error")) {
                        Toast.makeText(applicationContext, obj.getString("message"), Toast.LENGTH_SHORT).show()

                        //getting the user from the response
                        val billJson = obj.getJSONObject("bill")

                        //creating a new user object
                        val bill = Bills(
                            billJson.getString("id"),
                            billJson.getString("Tc"),
                            billJson.getInt("amount"),
                            billJson.getInt("unit"),
                            billJson.getInt("debt"),
                            billJson.getString("month"),
                            )

                        //storing the user in shared preferences
                        SharedPrefManager.getInstance(applicationContext).sendBill(bill)

                        //starting the MainActivity activity
                        finish()
                        startActivity(Intent(applicationContext, StaffMain::class.java))
                    } else {
                        Toast.makeText(applicationContext, obj.getString("message"), Toast.LENGTH_SHORT).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error -> Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["Tc"] = Tc
                params["amount"] = amount
                params["unit"] = unit
                params["debt"] = debt
                params["month"] = month.toString()
                return params
            }
        }
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest)
    }


    override fun onClick(view: View) {
        val btnStaffLogout = findViewById<Button>(R.id.buttonStaffLogout)
        if (view == btnStaffLogout) {
            SharedPrefManager.getInstance(applicationContext).logout()
        }
    }
}

