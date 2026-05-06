package com.example.rec.main.productos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.rec.R
import com.example.rec.productos.Producto

class ProductoAdapter(private val lista: List<Producto>) :
    RecyclerView.Adapter<ProductoAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val img: ImageView = view.findViewById(R.id.imgProducto)
        val nombre: TextView = view.findViewById(R.id.txtNombre)
        val precio: TextView = view.findViewById(R.id.txtPrecio)
        val btnAgregar: Button = view.findViewById(R.id.btnAgregarCarrito)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_producto_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val p = lista[position]

        holder.nombre.text = p.nombre
        holder.precio.text = p.precio
        holder.img.setImageResource(p.imagen)

        holder.btnAgregar.setOnClickListener { view ->


            val bundle = Bundle().apply {
                putString("p_nom", p.nombre)
                putString("p_pre", p.precio)
                putInt("p_img", p.imagen)
            }


            view.findNavController().navigate(
                R.id.action_global_carritoFragment,
                bundle
            )

            Toast.makeText(view.context, "Añadido: ${p.nombre}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount() = lista.size
}