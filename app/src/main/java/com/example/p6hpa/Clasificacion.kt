package com.example.p6hpa

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.VideoView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import kotlin.system.exitProcess


class Clasificacion : AppCompatActivity() {

    private lateinit var textoClasificaion: TextView
    private lateinit var general: TextView
    private lateinit var diario: TextView
    private lateinit var semanal: TextView
    private lateinit var reiniciar: Button
    private lateinit var salir: Button
    private lateinit var nombreUsuario: String
    private var puntos = 0



    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_clasificacion)

        val videoView = findViewById<VideoView>(R.id.videoView3)
        val videoUri = ("android.resource://" + packageName + "/" + R.raw.video).toUri()
        videoView.setVideoURI(videoUri)
        videoView.setOnCompletionListener { mediaPlayer ->
            mediaPlayer.start()
        }
        videoView.start()

        val extras = intent.extras
        if (extras != null){
            nombreUsuario = extras.getString("nombreUsuario", "Random")
            puntos = extras.getInt("puntos")
        }

        textoClasificaion = findViewById(R.id.textoClasificacion)
        general = findViewById(R.id.general)
        diario = findViewById(R.id.diario)
        semanal = findViewById(R.id.semanal)
        reiniciar = findViewById(R.id.botonReiniciar)
        salir = findViewById(R.id.botonSalir)

        resaltarPestana(general)

        if(Ranking.ranking.isEmpty()){
            iniciarJugadores()
        }
        Ranking.ranking.add(Jugador(nombreUsuario, puntos,1))

        general.setOnClickListener {
            llamarFragment("GENERAL")
            resaltarPestana(general)
        }
        diario.setOnClickListener {
            llamarFragment("DIARIO")
            resaltarPestana(diario)
        }
        semanal.setOnClickListener {
            llamarFragment("SEMANAL")
            resaltarPestana(semanal)
        }

        if (savedInstanceState == null) {
            llamarFragment("GENERAL")
        }

        reiniciar.setOnClickListener {

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        salir.setOnClickListener {
            exitProcess(0)
        }
    }


    private fun resaltarPestana(pestanaActiva: TextView) {

        val opacidadInactiva = 0.2f
        val opacidadActiva = 1.0f

        //Primero desactivo todas
        general.alpha = opacidadInactiva
        diario.alpha = opacidadInactiva
        semanal.alpha = opacidadInactiva
        //Y activo la que quiero resaltar
        pestanaActiva.alpha = opacidadActiva

    }



    private fun iniciarJugadores(){

        Ranking.ranking.add(Jugador(getString(R.string.daniel), 100, 20))
        Ranking.ranking.add(Jugador(getString(R.string.miguel), 60, 30))
        Ranking.ranking.add(Jugador(getString(R.string.hector), 400, 5))
        Ranking.ranking.add(Jugador(getString(R.string.jesus), 30, 3))
        Ranking.ranking.add(Jugador(getString(R.string.ivan), 20, 1))

    }


    private fun llamarFragment(filtro: String) {

        val fragment = FragmentClasificacion()
        val infoFragment = Bundle().apply {
            putString("filtro", filtro)
            putString("nombre", nombreUsuario)
        }

        fragment.arguments = infoFragment

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, fragment)
            .commit()
    }


    override fun onResume() {
        super.onResume()

        val videoView = findViewById<VideoView>(R.id.videoView3)
        val videoUri = ("android.resource://" + packageName + "/" + R.raw.video).toUri()
        videoView.setVideoURI(videoUri)
        videoView.setOnCompletionListener { mediaPlayer ->

            mediaPlayer.start()

        }
        videoView.start()
    }

    override fun onStop() {
        super.onStop()
        val videoView = findViewById<VideoView>(R.id.videoView3)
        videoView.stopPlayback()
    }
}