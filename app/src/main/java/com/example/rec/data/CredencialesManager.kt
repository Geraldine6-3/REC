package com.example.rec.data

import android.content.Context
import android.content.SharedPreferences

object CredencialesManager {
    private const val PREFS_NAME = "MisCredencialesRec"
    private const val KEY_CORREO = "correo_usuario"
    private const val KEY_PASSWORD = "password_usuario"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }
    // 1. Guardar los datos cuando el usuario inicia sesión normal por primera vez
    fun guardarCredenciales(context: Context, correo: String, contrasena: String) {
        val editor = getPrefs(context).edit()
        editor.putString(KEY_CORREO, correo)
        editor.putString(KEY_PASSWORD, contrasena)
        editor.apply()
    }

    // 2. Obtener el correo guardado
    fun obtenerCorreo(context: Context): String? {
        return getPrefs(context).getString(KEY_CORREO, null)
    }

    // 3. Obtener la contraseña guardada (para enviarla a Supabase tras la huella)
    fun obtenerPassword(context: Context): String? {
        return getPrefs(context).getString(KEY_PASSWORD, null)
    }

    // 4. Borrar todo cuando el usuario cierra sesión (Logout)
    fun borrarCredenciales(context: Context) {
        val editor = getPrefs(context).edit()
        editor.clear()
        editor.apply()
    }
}