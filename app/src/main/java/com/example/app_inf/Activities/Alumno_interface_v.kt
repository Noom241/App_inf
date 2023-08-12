package com.example.app_inf.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Spinner
import com.example.app_inf.R

class Alumno_interface_v : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.alumno_activity_v2)
        val spnModalidadAlumno = findViewById<Spinner>(R.id.spn_modalidad_alumno)
        val spnPaqueteAlumno = findViewById<Spinner>(R.id.spn_paquete_alumno)
        val spnHorarioAlumno = findViewById<Spinner>(R.id.spn_horario_alumno)
    }
}