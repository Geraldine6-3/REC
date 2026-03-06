package com.example.rec.activities

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.rec.R

class RegistroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        val tvVolverLogin = findViewById<TextView>(R.id.tv_volver_login)

        // Al hacer clic, cerramos esta pantalla y regresa a la anterior (Login o Home)
        tvVolverLogin.setOnClickListener {
            finish()
        }
    }
}