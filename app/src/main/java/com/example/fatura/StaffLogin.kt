package com.example.fatura

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
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

class StaffLogin : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_staff_login)


        val btnStaffLogin = findViewById<Button>(R.id.btnStaffLogin)
        val tvStaffRegister = findViewById<TextView>(R.id.tvStaffRegister)

        //calling the method userLogin() for login the user
        btnStaffLogin.setOnClickListener(View.OnClickListener {
            staffLogin()
        })

        val mainPage = findViewById<TextView>(R.id.mainPage)
        mainPage.setOnClickListener(View.OnClickListener {
            startActivity(Intent(applicationContext, Entry::class.java))
            finish()
        })

        //if user presses on textview it call the activity RegisterActivity
        tvStaffRegister.setOnClickListener(View.OnClickListener {
            finish()
            startActivity(Intent(applicationContext, StaffRegister::class.java))
        })
    }

    private fun staffLogin() {

        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val etStaffTc = findViewById<EditText>(R.id.etStaffTc)
        val etStaffPassword = findViewById<EditText>(R.id.etStaffPassword)
        //first getting the values
        val tc = etStaffTc.text.toString()
        val password = etStaffPassword.text.toString()
        //validating inputs
        if (TextUtils.isEmpty(tc)) {
            etStaffTc.error = "Please enter your tc"
            etStaffTc.requestFocus()
            return
        }

        if (TextUtils.isEmpty(password)) {
            etStaffPassword.error = "Please enter your password"
            etStaffPassword.requestFocus()
            return
        }

        val URLLogin = URLs.URL_STAFF_LOGIN + "?tc="+tc+"&password="+password
        println(URLLogin)

        //if everything is fine
        val stringRequest = object : StringRequest(
            Request.Method.POST, URLLogin,
            Response.Listener { response ->
                progressBar.visibility = View.GONE

                try {
                    //converting response to json object
                    val obj = JSONObject(response)

                    //if no error in response
                    if (!obj.getBoolean("error")) {
                        Toast.makeText(applicationContext, obj.getString("message"), Toast.LENGTH_SHORT).show()

                        //getting the user from the response
                        val userJson = obj.getJSONObject("user")

                        //creating a new user object
                        val user = User(
                            userJson.getInt("id"),
                            userJson.getString("username"),
                            userJson.getInt("tc"),
                        )

                        //storing the user in shared preferences
                        SharedPrefManager.getInstance(applicationContext).userLogin(user)
                        //starting the MainActivity
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
                params["tc"] = tc
                params["password"] = password
                return params
            }
        }

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest)
    }
}