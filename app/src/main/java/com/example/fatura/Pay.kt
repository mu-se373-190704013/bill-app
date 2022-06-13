package com.example.fatura

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Pay: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_pay)

        val btnMain=findViewById<Button>(R.id.btnMain)
        btnMain.setOnClickListener(View.OnClickListener {
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
        })



}
}