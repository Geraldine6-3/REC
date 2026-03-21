package com.example.rec.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.rec.R
import com.example.rec.main.MainActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btnIngresar = findViewById<Button>(R.id.btn_ingresar)
        val tvRegistrateLogin = findViewById<TextView>(R.id.Registrate)
        val tvRecuperar = findViewById<TextView>(R.id.Recpassword)
        val btnGoogle = findViewById<LinearLayout>(R.id.IngresaGoogle)

        // Navegación al Main (Interactividad solicitada)
        btnIngresar.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        tvRegistrateLogin.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }

        tvRecuperar.setOnClickListener {
            Toast.makeText(this, "Enlace de recuperación enviado", Toast.LENGTH_SHORT).show()
        }

        btnGoogle.setOnClickListener {
            Toast.makeText(this, "Iniciando sesión con Google", Toast.LENGTH_SHORT).show()
        }
    }
}