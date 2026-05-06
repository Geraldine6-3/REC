package com.example.rec.data

import android.content.Context
import android.net.Uri
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.storage
import kotlinx.serialization.Serializable
import com.example.rec.SupabaseClient
import java.io.InputStream

object UsuarioRepository {

    @Serializable
    data class UsuarioData(
        val id: String,
        val nombres: String,
        val apellidos: String,
        val correo: String? = null,
        val foto_url: String? = null
    )

    suspend fun obtenerUsuarioActual(): UsuarioData? {
        val userId = SupabaseClient.client.auth.currentUserOrNull()?.id ?: return null
        return try {
            SupabaseClient.client.postgrest["usuarios"]
                .select { filter { eq("id", userId) } }
                .decodeSingle<UsuarioData>()
        } catch (e: Exception) {
            null
        }
    }

    suspend fun actualizarUsuario(
        context: Context,
        nombres: String,
        apellidos: String,
        fotoUri: Uri?
    ): Boolean {
        val userId = SupabaseClient.client.auth.currentUserOrNull()?.id ?: return false
        return try {
            var urlFoto: String? = null

            if (fotoUri != null) {
                try {
                    val inputStream: InputStream? = context.contentResolver.openInputStream(fotoUri)
                    val bytes = inputStream?.readBytes()
                    if (bytes != null) {

                        val bucketName = "AVATARS"
                        val nombreArchivo = "perfil_${userId}.jpg"

                        val storage = SupabaseClient.client.storage.from(bucketName)
                        storage.upload(nombreArchivo, bytes) { upsert = true }
                        urlFoto = storage.publicUrl(nombreArchivo)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }


            SupabaseClient.client.postgrest["usuarios"].update(
                {
                    set("nombres", nombres)
                    set("apellidos", apellidos)
                    if (urlFoto != null) set("foto_url", urlFoto)
                }
            ) { filter { eq("id", userId) } }

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}