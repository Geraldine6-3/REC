package com.example.rec.main.perfil

import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.rec.R
import com.example.rec.data.UsuarioRepository
import coil.load
import coil.transform.CircleCropTransformation
import kotlinx.coroutines.launch
import java.io.File

class EditarPerfilFragment : Fragment() {

    private var uriFotoSeleccionada: Uri? = null
    private lateinit var ivEditarFoto: ImageView
    private lateinit var archivoFotoTemp: File

    // Permiso cámara
    private val lanzadorPermisoCamara =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { concedido ->
            if (concedido) abrirCamara()
            else Toast.makeText(requireContext(), "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
        }

    // Tomar foto
    private val lanzadorCamara =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { exito ->
            if (exito) {
                uriFotoSeleccionada = Uri.fromFile(archivoFotoTemp)
                ivEditarFoto.load(uriFotoSeleccionada) {
                    transformations(CircleCropTransformation())
                }
            }
        }

    // Galería
    private val lanzadorGaleria =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                uriFotoSeleccionada = it
                ivEditarFoto.load(it) {
                    transformations(CircleCropTransformation())
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_editar_perfil, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etNombres = view.findViewById<EditText>(R.id.et_editar_nombres)
        val etApellidos = view.findViewById<EditText>(R.id.et_editar_apellidos)
        val etCorreo = view.findViewById<EditText>(R.id.et_editar_correo)
        val btnGuardar = view.findViewById<Button>(R.id.btn_guardar_perfil)
        val btnCambiarFoto = view.findViewById<View>(R.id.btn_cambiar_foto)
        ivEditarFoto = view.findViewById(R.id.iv_foto_perfil_edit)

        // Cargar datos actuales desde Supabase
        viewLifecycleOwner.lifecycleScope.launch {
            val usuario = UsuarioRepository.obtenerUsuarioActual()
            usuario?.let {
                etNombres.setText(it.nombres)
                etApellidos.setText(it.apellidos)
                etCorreo.setText(it.correo ?: "")

                if (!it.foto_url.isNullOrEmpty()) {
                    ivEditarFoto.load(it.foto_url) {
                        transformations(CircleCropTransformation())
                        placeholder(R.drawable.user)
                    }
                }
            }
        }

        btnCambiarFoto.setOnClickListener {
            mostrarOpcionesFoto()
        }

        // Guardar perfil (CRUD: Update)
        btnGuardar.setOnClickListener {
            val nom = etNombres.text.toString()
            val ape = etApellidos.text.toString()

            if (nom.isNotEmpty() && ape.isNotEmpty()) {
                viewLifecycleOwner.lifecycleScope.launch {
                    val exito = UsuarioRepository.actualizarUsuario(
                        requireContext(),
                        nom,
                        ape,
                        uriFotoSeleccionada
                    )

                    if (exito) {
                        Toast.makeText(requireContext(), "Perfil actualizado correctamente", Toast.LENGTH_SHORT).show()
                        findNavController().popBackStack()
                    } else {
                        Toast.makeText(requireContext(), "Error al guardar en la base de datos", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Por favor, completa nombres y apellidos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun mostrarOpcionesFoto() {
        val opciones = arrayOf("Tomar foto", "Elegir de galería")

        android.app.AlertDialog.Builder(requireContext())
            .setTitle("Seleccionar foto de perfil")
            .setItems(opciones) { _, which ->
                when (which) {
                    0 -> lanzadorPermisoCamara.launch(Manifest.permission.CAMERA)
                    1 -> lanzadorGaleria.launch("image/*")
                }
            }
            .show()
    }

    private fun abrirCamara() {
        val directorio = File(requireContext().cacheDir, "images")
        if (!directorio.exists()) directorio.mkdirs()

        archivoFotoTemp = File(directorio, "foto_${System.currentTimeMillis()}.jpg")

        val uri = FileProvider.getUriForFile(
            requireContext(),
            "com.example.rec.fileprovider",
            archivoFotoTemp
        )

        lanzadorCamara.launch(uri)
    }
}