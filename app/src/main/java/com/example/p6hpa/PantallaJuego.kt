package com.example.p6hpa

import android.animation.ObjectAnimator
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.widget.TextView
import android.widget.VideoView
import androidx.appcompat.app.AlertDialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random
import android.media.MediaPlayer
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope

const val BOTON_ROJO = 0
const val BOTON_VERDE = 1
const val BOTON_AMARILLO = 2
const val BOTON_AZUL = 3

class PantallaJuego : AppCompatActivity() {


    private var puntos = 0
    private var numRonda = 1
    private var idioma = 0
    private var turnoHabilitado = false
    private var pulsacionesMaquina = mutableListOf<Int>()
    private var pulsacionesUsuario = mutableListOf<Int>()
    private lateinit var pulsadorRojo : ImageView
    private lateinit var pulsadorVerde : ImageView
    private lateinit var pulsadorAmarillo : ImageView
    private lateinit var pulsadorAzul : ImageView
    private lateinit var puntuacion : TextView
    private lateinit var ronda : TextView
    private lateinit var sonidoRojo: MediaPlayer
    private lateinit var sonidoAzul: MediaPlayer
    private lateinit var sonidoAmarillo: MediaPlayer
    private lateinit var sonidoVerde: MediaPlayer
    private lateinit var nombreUsuario: String


    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_pantalla_juego)

        val videoView = findViewById<VideoView>(R.id.videoView2)
        val videoUri = ("android.resource://" + packageName + "/" + R.raw.video).toUri()

        videoView.setVideoURI(videoUri)
        videoView.start()
        videoView.setOnCompletionListener { mediaPlayer ->
            mediaPlayer.start()
        }

        val extras = intent.extras
        if (extras != null){
            nombreUsuario = extras.getString("nombreUsuario", "Random")
        }

        sonidoRojo = MediaPlayer.create(this, R.raw.sonido_rojo)
        sonidoAzul = MediaPlayer.create(this, R.raw.sonido_azul)
        sonidoAmarillo = MediaPlayer.create(this, R.raw.sonido_amarillo)
        sonidoVerde = MediaPlayer.create(this, R.raw.sonido_verde)

        pulsadorRojo = findViewById(R.id.rojo)
        pulsadorVerde = findViewById(R.id.verde)
        pulsadorAmarillo = findViewById(R.id.amarillo)
        pulsadorAzul = findViewById(R.id.azul)
        puntuacion = findViewById(R.id.puntuacion)
        ronda = findViewById(R.id.ronda)

        pulsadorRojo.isSoundEffectsEnabled = false
        pulsadorVerde.isSoundEffectsEnabled = false
        pulsadorAmarillo.isSoundEffectsEnabled = false
        pulsadorAzul.isSoundEffectsEnabled = false

        ronda.text = if (idioma == 0) "RONDA: $numRonda" else "ROUND: $numRonda"

        comprobarPulsadores()
        empezarJuego()

    }


    private fun empezarJuego() {

        puntos = 0
        numRonda = 1
        pulsacionesMaquina.clear()
        pulsacionesUsuario.clear()
        turnoHabilitado = false

        val builder = AlertDialog.Builder(this)

        val titulo = getString(R.string.pulsemaster_vs, nombreUsuario)
        val (msj, botonConfirmar) = getString(R.string.empieza_pulsemaster_buena_suerte) to getString(R.string.aceptar)


        builder.setTitle(titulo)
        builder.setCancelable(false)
        builder.setMessage(msj).setNeutralButton(botonConfirmar) { dialog, id ->
            dialog.dismiss()
            turnoMaquina()
        }
        builder.show()
    }


    private fun turnoMaquina() {

        ronda.text =  getString(R.string.ronda_texto, numRonda)

        establecerBotones(false)
        pulsacionesUsuario.clear() // reiniciamos siempre la secuencia del usuario

        val botonPulsado = Random.nextInt(4)

        lifecycleScope.launch { //Con lifecicleScope hacemos la espera de la corrutina, pero con este metodo no paro la aplicaion

            delay(1000)

            pulsacionesMaquina.add(botonPulsado)



            for (color in pulsacionesMaquina) {
                when (color) {
                    BOTON_ROJO -> {
                        animacionPulsador(pulsadorRojo)
                        reproducirSonido(sonidoRojo)
                    }
                    BOTON_VERDE -> {
                        animacionPulsador(pulsadorVerde)
                        reproducirSonido(sonidoVerde)
                    }
                    BOTON_AMARILLO -> {
                        animacionPulsador(pulsadorAmarillo)
                        reproducirSonido(sonidoAmarillo)
                    }
                    BOTON_AZUL -> {
                        animacionPulsador(pulsadorAzul)
                        reproducirSonido(sonidoAzul)
                    }
                }
                delay(800L) //Esperamos ese tiempo entre boton y boton
            }

            turnoUsuario()
        }
    }

    private fun turnoUsuario(){
        turnoHabilitado = true
        establecerBotones(true)
    }


    private fun comprobarPulsacion(colorPulsado: Int) {

        var derrota = false

        if (!turnoHabilitado) return //si no es el turno del usuario no hace nada

        animacionPulsador(
            when (colorPulsado) {
                BOTON_ROJO -> {
                    pulsadorRojo
                }
                BOTON_VERDE -> {
                    pulsadorVerde
                }
                BOTON_AMARILLO -> {
                    pulsadorAmarillo
                }
                else -> {
                    pulsadorAzul
                }
            }
        )

        pulsacionesUsuario.add(colorPulsado)
        val indiceActual = pulsacionesUsuario.lastIndex //siempre cogemos la ultima posicion, que es el utimo boton a pulsado para comparar

        //Ahora solo comprobamos si falla, porque si acierta tiene que seguir pulsando hasta que pulse la misma cantidad de botones
        if (pulsacionesUsuario[indiceActual] != pulsacionesMaquina[indiceActual]) {

            derrota = true
            val builder = AlertDialog.Builder(this)
            val titulo = getString(R.string.has_fallado)
            val (msj, textoBoton) = getString(R.string.hasta_aqu_ha_llegado_tu_aventura) to getString(R.string.continuar)

            builder.setTitle(titulo)
            builder.setCancelable(false)
            builder.setMessage(msj)
            builder.setNeutralButton(textoBoton) { dialog, id ->
                dialog.dismiss()

                liberarSonidos() //Quito de la memoria los sonidos

                val intent = Intent(this, Clasificacion::class.java)
                val extras = Bundle()
                extras.putString("nombreUsuario",nombreUsuario)
                extras.putInt("puntos",puntos)

                intent.putExtras(extras)
                startActivity(intent)
                finish()
            }
            builder.show()
        }

        if (pulsacionesUsuario.size == pulsacionesMaquina.size && !derrota) {

            puntos += 10
            puntuacion.text = getString(R.string.puntuaci_n, puntos)

            turnoHabilitado = false
            numRonda++
            turnoMaquina()
        }
    }



    private fun comprobarPulsadores() {
        pulsadorRojo.setOnClickListener {
            comprobarPulsacion(BOTON_ROJO)
            reproducirSonido(sonidoRojo)
        }
        pulsadorVerde.setOnClickListener {
            comprobarPulsacion(BOTON_VERDE)
            reproducirSonido(sonidoVerde)
        }
        pulsadorAmarillo.setOnClickListener {
            comprobarPulsacion(BOTON_AMARILLO)
            reproducirSonido(sonidoAmarillo)
        }
        pulsadorAzul.setOnClickListener {
            comprobarPulsacion(BOTON_AZUL)
            reproducirSonido(sonidoAzul)
        }
    }



    private fun animacionPulsador(pulsador: ImageView){

        val animacionEjeY = ObjectAnimator.ofFloat(pulsador, "scaleY", 1.5f)
        val animacionEjeX = ObjectAnimator.ofFloat(pulsador, "scaleX", 1.5f)
        val animacionBrillo = ObjectAnimator.ofFloat(pulsador, "Alpha", 0.2f)

        val animacionCompleta = AnimatorSet()
        animacionCompleta.duration = 200
        animacionCompleta.playTogether(animacionEjeY,animacionEjeX, animacionBrillo)

        animacionCompleta.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                pulsador.scaleY = 1f
                pulsador.scaleX = 1f
                pulsador.alpha = 1f
            }
        })

        animacionCompleta.start()

    }


    private fun reproducirSonido(sonido: MediaPlayer){

        if (sonido.isPlaying) { //Rebobinamos al principio por si pulsa muy rapido
            sonido.pause()
            sonido.seekTo(0)
        }

        sonido.start()
    }


    private fun establecerBotones(onOff: Boolean){

        pulsadorRojo.isEnabled = onOff
        pulsadorVerde.isEnabled = onOff
        pulsadorAmarillo.isEnabled = onOff
        pulsadorAzul.isEnabled = onOff

    }


    private fun liberarSonidos() {
        if (::sonidoRojo.isInitialized) sonidoRojo.release()
        if (::sonidoVerde.isInitialized) sonidoVerde.release()
        if (::sonidoAmarillo.isInitialized) sonidoAmarillo.release()
        if (::sonidoAzul.isInitialized) sonidoAzul.release()
    }



    override fun onRestart() {
        super.onRestart()

        turnoHabilitado = false
        puntuacion.text = ""

        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.juego_reiniciado))
        builder.setMessage(getString(R.string.has_salido_de_la_aplicaci_n_empiezas_de_cero))

        builder.setCancelable(false)

        builder.setNeutralButton(getString(R.string.continuar)) { dialog, id ->
            dialog.dismiss()
            empezarJuego()
        }

        builder.show()
    }


    override fun onResume() {
        super.onResume()

        val videoView = findViewById<VideoView>(R.id.videoView2)
        val videoUri = ("android.resource://" + packageName + "/" + R.raw.video).toUri()
        videoView.setVideoURI(videoUri)
        videoView.setOnCompletionListener { mediaPlayer ->

            mediaPlayer.start()

        }
        videoView.start()
    }

    override fun onStop() {
        super.onStop()
        val videoView = findViewById<VideoView>(R.id.videoView2)
        videoView.stopPlayback()
    }
}