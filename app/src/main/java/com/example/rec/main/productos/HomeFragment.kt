package com.example.rec.main.productos

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.rec.R
import com.example.rec.auth.LoginActivity
import com.example.rec.auth.RegistroActivity

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val btnComienza = view.findViewById<Button>(R.id.btn_comienza)
        val tvRegistrateHome = view.findViewById<TextView>(R.id.tv_registrate)

        btnComienza.setOnClickListener {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }

        tvRegistrateHome.setOnClickListener {
            val intent = Intent(requireContext(), RegistroActivity::class.java)
            startActivity(intent)
        }

        return view
    }
}