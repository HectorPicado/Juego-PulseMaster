package com.example.p6hpa

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class FragmentClasificacion : Fragment() {

    private lateinit var textoRanking: TextView
    private lateinit var filtro: String
    private lateinit var nombreUsuario: String
    private lateinit var textoUsuario: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textoRanking = view.findViewById(R.id.textoRanking)
        textoUsuario = view.findViewById(R.id.textoUsuario)
        filtro = arguments?.getString("filtro") ?: getString(R.string.general)
        nombreUsuario = arguments?.getString("nombre") ?: "Random"
        actualizarLista()

    }

    private fun actualizarLista(){

        val listaMostrar = Ranking.rankingFiltrado(filtro)
        var textoCompleto = ""
        var posicion = 1

        for (jugador in listaMostrar){

            if(jugador.nombre == nombreUsuario){
                textoUsuario.text =
                    getString(R.string.posicion_usuario, jugador.nombre, posicion)
            }

            textoCompleto += posicion.toString() + "º ---- " + jugador.nombre + " ---- " + jugador.puntos + " pts\n\n\n"
            posicion++
        }

        textoRanking.text = textoCompleto
    }
}