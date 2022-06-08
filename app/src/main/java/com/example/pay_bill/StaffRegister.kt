package com.example.pay_bill

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.example.assign6.VolleySingleton
import com.example.fatura.R
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

class StaffRegister : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_staff_register)

        //if the user is already logged in we will directly start the MainActivity (profile) activity
        if (SharedPrefManager.getInstance(this).isLoggedIn) {
            finish()
            startActivity(Intent(this, MainActivity::class.java))
            return
        }


        val buttonRegister = findViewById<Button>(R.id.buttonRegister)
        val textViewLogin = findViewById<TextView>(R.id.textViewLogin)

        buttonRegister.setOnClickListener(View.OnClickListener {
            //if user pressed on button register
            //here we will register the user to server
            registerStaff()
        })

        textViewLogin.setOnClickListener(View.OnClickListener {
            finish()
            startActivity(Intent(this@StaffRegister, StaffLogin::class.java))
        })
    }

    private fun registerStaff() {
        val editTextUsername = findViewById<EditText>(R.id.editTextUsername)
        val editTextTc = findViewById<EditText>(R.id.editTextTc)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val username = editTextUsername.text.toString().trim { it <= ' ' }
        val tc = editTextTc.text.toString().trim { it <= ' ' }
        val password = editTextPassword.text.toString().trim { it <= ' ' }

        //first we will do the validations
        if (TextUtils.isEmpty(username)) {
            editTextUsername.error = "Please enter username"
            editTextUsername.requestFocus()
            return
        }
        if (TextUtils.isEmpty(tc)) {
            editTextTc.error = "Please enter your tc"
            editTextTc.requestFocus()
            return
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.error = "Enter a password"
            editTextPassword.requestFocus()
            return
        }
        val URLRegister = URLs.URL_STAFF_REGISTER + "?username="+username+"&tc="+tc+"&password="+password
        println(URLRegister)
        val stringRequest = object : StringRequest(
            Request.Method.POST, URLRegister,
            Response.Listener { response ->
                progressBar.visibility = View.GONE

                try {
                    //converting response to json object
                    val obj = JSONObject(response)
                    //if no error in response
                    if (!obj.getBoolean("error")) {
                        Toast.makeText(applicationContext, obj.getString("message"), Toast.LENGTH_SHORT).show()

                        //getting the user from the response
                        val staffJson = obj.getJSONObject("staff")

                        //creating a new user object
                        val staff = Staff(
                            staffJson.getInt("id"),
                            staffJson.getString("username"),
                            staffJson.getInt("tc"),

                            )

                        //storing the user in shared preferences
                        SharedPrefManager.getInstance(applicationContext).staffLogin(staff)

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
                params["username"] = username
                params["tc"] = tc
                params["password"] = password
                return params
            }
        }
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest)
    }
}