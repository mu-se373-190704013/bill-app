package com.example.pay_bill

import android.os.Bundle
import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.fatura.R

class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (SharedPrefManager.getInstance(this).isLoggedIn) {

            val userName = findViewById<TextView>(R.id.textViewUsername)
            val btnLogout = findViewById<Button>(R.id.buttonLogout)
            val btnList=findViewById<Button>(R.id.Listbill)

            btnList.setOnClickListener(View.OnClickListener {
                startActivity(Intent(applicationContext, ListOfBill::class.java))
                finish()
            })

            val user = SharedPrefManager.getInstance(this).user

            userName.text = user.name
            btnLogout.setOnClickListener(this)


        } else {
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onClick(view: View) {
        val btnLogout = findViewById<Button>(R.id.buttonLogout)
        if (view == btnLogout) {
            SharedPrefManager.getInstance(applicationContext).logout()
        }
    }

}