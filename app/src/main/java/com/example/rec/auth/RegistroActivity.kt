package com.example.rec.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.rec.R
import com.example.rec.SupabaseClient
import com.example.rec.main.MainActivity
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

class RegistroActivity : AppCompatActivity() {

    private lateinit var etNombresRegistro: EditText
    private lateinit var etApellidosRegistro: EditText
    private lateinit var etCorreoRegistro: EditText
    private lateinit var etContrasenaRegistro: EditText
    private lateinit var etConfirmaContrasenaRegistro: EditText
    private lateinit var checkTerminos: CheckBox
    private lateinit var btnRegistrate: Button
    private lateinit var tvVolverLogin: TextView

    @Serializable
    data class UsuarioData(
        val id: String,
        val nombres: String,
        val apellidos: String
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        // Referenciación
        etNombresRegistro = findViewById(R.id.et_nombres_registro)
        etApellidosRegistro = findViewById(R.id.et_apellidos_registro)
        etCorreoRegistro = findViewById(R.id.et_correo_registro)
        etContrasenaRegistro = findViewById(R.id.et_contrasena_registro)
        etConfirmaContrasenaRegistro = findViewById(R.id.et_confirmacontrasena_registro)
        checkTerminos = findViewById(R.id.check_terminos)
        btnRegistrate = findViewById(R.id.btn_registrate)
        tvVolverLogin = findViewById(R.id.tv_volver_login)

        btnRegistrate.setOnClickListener {
            val nombres = etNombresRegistro.text.toString().trim()
            val apellidos = etApellidosRegistro.text.toString().trim()
            val correo = etCorreoRegistro.text.toString().trim()
            val contrasena = etContrasenaRegistro.text.toString().trim()
            val confirmaContrasena = etConfirmaContrasenaRegistro.text.toString().trim()

            // Validaciones previas
            if (correo.isEmpty() || contrasena.isEmpty() || nombres.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (contrasena.length < 8) {
                Toast.makeText(this, "La contraseña debe tener al menos 8 caracteres", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (contrasena != confirmaContrasena) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!checkTerminos.isChecked) {
                Toast.makeText(this, "Acepta los términos y condiciones", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // --- REGISTRO EN SUPABASE ---
            lifecycleScope.launch {
                try {
                    // Paso 1: Registrar correo y contraseña en Auth
                    SupabaseClient.client.auth.signUpWith(Email) {
                        email = correo
                        password = contrasena
                    }

                    // Paso 2: Obtener el ID generado y guardar datos adicionales
                    val userId = SupabaseClient.client.auth.currentUserOrNull()?.id ?: ""

                    SupabaseClient.client.postgrest["usuarios"].insert(
                        UsuarioData(
                            id = userId,
                            nombres = nombres,
                            apellidos = apellidos
                        )
                    )

                    // Paso 3: Éxito
                    runOnUiThread {
                        Toast.makeText(this@RegistroActivity, "Registro exitoso", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@RegistroActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                } catch (e: Exception) {
                    runOnUiThread {
                        Toast.makeText(this@RegistroActivity, "Error en el registro: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        // Volver al Login
        tvVolverLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}