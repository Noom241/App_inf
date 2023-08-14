package com.example.app_inf


import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import com.example.app_inf.Activities.Alumno_interface_v
import com.example.app_inf.Activities.AsistenciaActivity
import com.example.app_inf.Activities.CalendarioActivity
import com.example.app_inf.Activities.EditarAsesorActivity
import com.example.app_inf.Activities.InformeActivity

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        val btnAsesores = findViewById<Button>(R.id.btnAsesores)
        val btnAsistencia = findViewById<Button>(R.id.btnAsistencia)
        val btnAlumnos = findViewById<Button>(R.id.btnAlumnos)
        val btnInforme = findViewById<Button>(R.id.btnInforme)

        btnInforme.setOnClickListener {
            val intent = Intent(this, CalendarioActivity::class.java)
            startActivity(intent)
        }

        btnAsistencia.setOnClickListener {
            val intent = Intent(this, AsistenciaActivity::class.java)
            startActivity(intent)
        }

        btnAsesores.setOnClickListener {
            val intent = Intent(this, EditarAsesorActivity::class.java)
            startActivity(intent)
        }

        btnAlumnos.setOnClickListener {
            val intent = Intent(this, Alumno_interface_v::class.java)
            startActivity(intent)
        }

    }

}
