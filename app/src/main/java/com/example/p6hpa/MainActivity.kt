package com.example.p6hpa

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import android.widget.VideoView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class MainActivity : AppCompatActivity() {

    private lateinit var nombreUsuario : TextInputEditText
    private lateinit var hint: TextInputLayout
    private lateinit var instrucciones : Button
    private lateinit var empezarJuego : Button


    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_main)


        val videoView = findViewById<VideoView>(R.id.videoView)
        val videoUri = ("android.resource://" + packageName + "/" + R.raw.video).toUri()
        videoView.setVideoURI(videoUri)
        videoView.setOnCompletionListener { mediaPlayer ->
            mediaPlayer.start()
        }
        videoView.start()

        nombreUsuario = findViewById(R.id.nombreUsuario)
        instrucciones = findViewById(R.id.instrucciones)
        hint = findViewById(R.id.textInputLayout)

        empezarJuego = findViewById(R.id.empezar)

        hint.hint = getString(R.string.nombre)
        empezarJuego.text = getString(R.string.empezar_juego)
        instrucciones.text = getString(R.string.instrucciones)

        empezarJuego.setOnClickListener {

            if(nombreUsuario.text.toString().length <= 3){

                val toast = Toast.makeText(this,
                    getString(R.string.introduce_un_nombre), Toast.LENGTH_SHORT)
                toast.show()

            } else {
                val intent = Intent(this, PantallaJuego::class.java)
                val extras = Bundle()
                extras.putString("nombreUsuario",nombreUsuario.text.toString())

                intent.putExtras(extras)
                startActivity(intent)
                finish()

            }
        }

        instrucciones.setOnClickListener {
            mostrarInstrucciones()
        }
    }

    override fun onResume() {
        super.onResume()

        val videoView = findViewById<VideoView>(R.id.videoView)
        val videoUri = ("android.resource://" + packageName + "/" + R.raw.video).toUri()
        videoView.setVideoURI(videoUri)
        videoView.setOnCompletionListener { mediaPlayer ->

            mediaPlayer.start()

        }
        videoView.start()
    }

    override fun onStop() {
        super.onStop()
        val videoView = findViewById<VideoView>(R.id.videoView)
        videoView.stopPlayback()
    }

    private fun mostrarInstrucciones(){

        val url = getString(R.string.url_reglas)
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = url.toUri()
        }

        startActivity(intent)
    }
}