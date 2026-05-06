package com.example.rec.main.perfil

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import coil.transform.CircleCropTransformation
import com.example.rec.R
import com.example.rec.data.UsuarioRepository
import kotlinx.coroutines.launch

class PerfilFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_perfil, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvNombre = view.findViewById<TextView>(R.id.tv_perfil_nombre)
        val tvCorreo = view.findViewById<TextView>(R.id.tv_perfil_correo)
        val ivFoto = view.findViewById<ImageView>(R.id.iv_perfil_foto)
        val btnEditar = view.findViewById<Button>(R.id.btn_ir_editar)


        viewLifecycleOwner.lifecycleScope.launch {
            val usuario = UsuarioRepository.obtenerUsuarioActual()

            if (usuario != null) {
                tvNombre.text = "${usuario.nombres} ${usuario.apellidos}"
                tvCorreo.text = usuario.correo ?: "Sin correo"

                if (!usuario.foto_url.isNullOrEmpty()) {
                    ivFoto.load(usuario.foto_url) {
                        transformations(CircleCropTransformation())
                        placeholder(R.drawable.user)
                        error(R.drawable.user)
                    }
                } else {
                    ivFoto.setImageResource(R.drawable.user)
                }
            } else {
                tvNombre.text = "Usuario no disponible"
                tvCorreo.text = ""
                ivFoto.setImageResource(R.drawable.user)
            }
        }

        btnEditar.setOnClickListener {
            findNavController()
                .navigate(R.id.action_perfilFragment_to_editarPerfilFragment)
        }
    }
}