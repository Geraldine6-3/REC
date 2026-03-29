package com.example.rec.inicio

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.rec.R
import com.example.rec.auth.LoginActivity
import com.example.rec.auth.RegistroActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)


        Handler(Looper.getMainLooper()).postDelayed({
            setContentView(R.layout.fragment_home)


            val btnComienza = findViewById<Button>(R.id.btn_comienza)
            btnComienza.setOnClickListener {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }


            val tvRegistrate = findViewById<TextView>(R.id.tv_registrate)
            tvRegistrate.setOnClickListener {
                val intent = Intent(this, RegistroActivity::class.java)
                startActivity(intent)
            }
        }, 2000)
    }
}