package com.example.rec.activities

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.rec.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val tvRegistrateLogin = findViewById<TextView>(R.id.Registrate)
        val tvRecuperar = findViewById<TextView>(R.id.Recpassword)
        val btnGoogle = findViewById<LinearLayout>(R.id.IngresaGoogle)

        // Ir a la pantalla de Registro
        tvRegistrateLogin.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }

        // Recuperar contraseña (mensaje de aviso)
        tvRecuperar.setOnClickListener {
            Toast.makeText(this, "Enlace de recuperación enviado", Toast.LENGTH_SHORT).show()
        }

        // Botón Google (mensaje de aviso)
        btnGoogle.setOnClickListener {
            Toast.makeText(this, "Iniciando sesión con Google", Toast.LENGTH_SHORT).show()
        }
    }
}