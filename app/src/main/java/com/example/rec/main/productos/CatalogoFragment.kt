package com.example.rec.main.productos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rec.R
import com.example.rec.productos.Producto

class CatalogoFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_catalogo, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerCatalogo)

        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        val listaEquipos = listOf(
            Producto("Cámara Pro 4K", "Sony Alpha cinematográfica", "$ 12.000.000", R.drawable.camara_de_video),
            Producto("Grabadora H6", "6 canales de audio", "$ 1.800.000", R.drawable.grabadora_portatil),
            Producto("Interfaz 2i2", "Grabación alta fidelidad", "$ 850.000", R.drawable.interfaz_de_audio),
            Producto("Micrófono U87", "Estándar de oro voces", "$ 15.000.000", R.drawable.microfono_condensador)
        )

        recyclerView.adapter = ProductoAdapter(listaEquipos)
        return view
    }
}