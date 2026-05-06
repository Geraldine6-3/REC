package com.example.rec.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.rec.R
import com.example.rec.SupabaseClient
import com.example.rec.data.CredencialesManager
import com.example.rec.main.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.providers.builtin.IDToken
import kotlinx.coroutines.launch
import java.util.concurrent.Executor

class LoginActivity : AppCompatActivity() {

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo


    private val googleLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                val idToken = account?.idToken
                if (idToken != null) {
                    completarLoginConSupabaseGoogle(idToken)
                }
            } catch (e: ApiException) {
                Toast.makeText(this, "Error de autenticación: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val txtEmail = findViewById<EditText>(R.id.et_correo_login)
        val txtPassword = findViewById<EditText>(R.id.et_contrasena_login)
        val btnIngresar = findViewById<Button>(R.id.btn_ingresar)
        val btnHuella = findViewById<LinearLayout>(R.id.in_huella)
        val btnGoogle = findViewById<LinearLayout>(R.id.IngresaGoogle)

        configurarVisibilidadHuella(btnHuella)
        setupBiometrico()

        btnIngresar.setOnClickListener {
            val email = txtEmail.text.toString().trim()
            val password = txtPassword.text.toString().trim()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginConSupabase(email, password)
            } else {
                Toast.makeText(this, "Por favor completa los campos", Toast.LENGTH_SHORT).show()
            }
        }

        btnHuella.setOnClickListener {
            biometricPrompt.authenticate(promptInfo)
        }

        btnGoogle.setOnClickListener {
            loginConGoogleNativo()
        }
    }

    private fun configurarVisibilidadHuella(btnHuella: LinearLayout) {
        val correoGuardado = CredencialesManager.obtenerCorreo(this)
        btnHuella.visibility = if (!correoGuardado.isNullOrEmpty()) View.VISIBLE else View.GONE
    }

    private fun setupBiometrico() {
        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    val email = CredencialesManager.obtenerCorreo(this@LoginActivity)
                    val pass = CredencialesManager.obtenerPassword(this@LoginActivity)
                    if (email != null && pass != null) loginConSupabase(email, pass)
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Inicio de sesión")
            .setSubtitle("Usa tu huella para entrar")
            .setNegativeButtonText("Usar contraseña")
            .build()
    }

    private fun loginConSupabase(email: String, pass: String) {
        lifecycleScope.launch {
            try {
                SupabaseClient.client.auth.signInWith(Email) {
                    this.email = email
                    this.password = pass
                }
                CredencialesManager.guardarCredenciales(this@LoginActivity, email, pass)
                irAMain()
            } catch (e: Exception) {
                Toast.makeText(this@LoginActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loginConGoogleNativo() {
        val myClientId = "438394961355-9p8uulk6ov26vk96i9l1a76luo2gg1sa.apps.googleusercontent.com"
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(myClientId)
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        googleSignInClient.signOut().addOnCompleteListener {
            googleLauncher.launch(googleSignInClient.signInIntent)
        }
    }

    private fun completarLoginConSupabaseGoogle(idToken: String) {
        lifecycleScope.launch {
            try {
                SupabaseClient.client.auth.signInWith(IDToken) {
                    this.idToken = idToken
                    provider = Google
                }
                irAMain()
            } catch (e: Exception) {
                Toast.makeText(this@LoginActivity, "Error Supabase: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun irAMain() {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}