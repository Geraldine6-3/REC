package com.example.rec.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.rec.R
import com.example.rec.main.MainActivity

class RegistroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        val btnRegistrar = findViewById<Button>(R.id.btn_registrate)
        val tvVolverLogin = findViewById<TextView>(R.id.tv_volver_login)

        // Acción: Registrarse e ir al Main
        btnRegistrar.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Volver al Login
        tvVolverLogin.setOnClickListener {
            finish()
        }
    }
}