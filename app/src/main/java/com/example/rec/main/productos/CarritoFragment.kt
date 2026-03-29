package com.example.rec.main.productos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.rec.R

class CarritoFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_carrito, container, false)

        // Capturamos los datos
        val nombre = arguments?.getString("p_nom")
        val precio = arguments?.getString("p_pre")

        val txtMensaje = view.findViewById<TextView>(R.id.txtMensajeVacio)
        val btnFinalizar = view.findViewById<Button>(R.id.btnFinalizarCompra)

        if (nombre != null) {
            txtMensaje.visibility = View.VISIBLE
            txtMensaje.text = "PRODUCTO EN CARRITO:\n\n$nombre\n$precio"

            txtMensaje.textSize = 20f
            txtMensaje.setTextColor(resources.getColor(R.color.black))
        }

        btnFinalizar.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return view
    }
}