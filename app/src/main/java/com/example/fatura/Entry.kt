package com.example.fatura

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Entry : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entry)

        val button = findViewById<Button>(R.id.userrLogin) //we define the page that will be opened by pressing the button
        button.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java) // we define second page
            startActivity(intent)
        }
        val button1 = findViewById<Button>(R.id.staffLogin) //we define the page that will be opened by pressing the button
        button1.setOnClickListener {
            val intent = Intent(this, StaffLogin::class.java) // we define second page
            startActivity(intent)
        }
    }
}