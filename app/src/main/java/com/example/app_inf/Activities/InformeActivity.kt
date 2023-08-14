package com.example.app_inf.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.app_inf.R
import com.example.app_inf.ViewModel.AsistenciaViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class InformeActivity : AppCompatActivity() {
    private lateinit var informeViewModel: AsistenciaViewModel
    private lateinit var autoCompleteTextView: AutoCompleteTextView
    private lateinit var autoCompleteTextViewAlumnos: AutoCompleteTextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_informe)
        informeViewModel = ViewModelProvider(this).get(AsistenciaViewModel::class.java)

        autoCompleteTextView = findViewById(R.id.AutocompleteAlumnos)
        autoCompleteTextViewAlumnos = findViewById(R.id.AutocompleteAlumnos)

        setupAutoCompleteViews()
        btn_asesor_pdf()
        btn_asesor_calendar()
        btn_alumno_calendar()

        btn_alumno_pdf()


    }
    private fun setupAutoCompleteViews() {
        // Fetch and set adapters for AutoCompleteTextViews using coroutines
        GlobalScope.launch(Dispatchers.Main) {
            val nombresProfesores = informeViewModel.obtenerNombresDeProfesores()
            val nombresAlumnos = informeViewModel.obtenerNombresDeAlumnos()

            val adapterProfesores = ArrayAdapter(this@InformeActivity, android.R.layout.simple_dropdown_item_1line, nombresProfesores)
            autoCompleteTextView.setAdapter(adapterProfesores)

            val adapterAlumnos = ArrayAdapter(this@InformeActivity, android.R.layout.simple_dropdown_item_1line, nombresAlumnos)
            autoCompleteTextViewAlumnos.setAdapter(adapterAlumnos)
        }
    }

    private fun btn_alumno_calendar() {
        val btnAlumnoCalendar: Button = findViewById(R.id.btn_alumno_asistencia)
        btnAlumnoCalendar.setOnClickListener {
            val stringAlumno = autoCompleteTextViewAlumnos.text.toString()
            GlobalScope.launch(Dispatchers.Main) {
                val idAlumno = informeViewModel.obtenerIDPorNombre(stringAlumno, "Estudiantes")

                val intent = Intent(this@InformeActivity, CalendarioActivity::class.java)
                intent.putExtra("alumno_id", idAlumno)
                startActivity(intent)
            }
        }
    }

    private fun btn_asesor_pdf() {
        val btnAsesorPDF: Button = findViewById(R.id.btn_alumno_asistencia)
        btnAsesorPDF.setOnClickListener {
            val stringAsesor = autoCompleteTextView.text.toString()
            GlobalScope.launch(Dispatchers.Main) {
                val idAsesor = informeViewModel.obtenerIDPorNombre(stringAsesor, "Profesores")

                val intent = Intent(this@InformeActivity, CalendarioActivity::class.java)
                intent.putExtra("asesor_id", idAsesor)
                startActivity(intent)
            }
        }
    }

    private fun btn_alumno_pdf() {
        val btnAlumnoPDF: Button = findViewById(R.id.btn_alumno_pdf)
        btnAlumnoPDF.setOnClickListener {
            val stringAlumno = autoCompleteTextViewAlumnos.text.toString()
            GlobalScope.launch(Dispatchers.Main) {
                val idAlumno = informeViewModel.obtenerIDPorNombre(stringAlumno, "Estudiantes")

                val intent = Intent(this@InformeActivity, CalendarioActivity::class.java)
                intent.putExtra("alumno_id", idAlumno)
                startActivity(intent)
            }
        }
    }

    private fun btn_asesor_calendar() {
        val btnAsesorCalendar: Button = findViewById(R.id.btn_alumno_asistencia)
        btnAsesorCalendar.setOnClickListener {
            val stringAsesor = autoCompleteTextView.text.toString()
            GlobalScope.launch(Dispatchers.Main) {
                val idAsesor = informeViewModel.obtenerIDPorNombre(stringAsesor, "Profesores")

                val intent = Intent(this@InformeActivity, CalendarioActivity::class.java)
                intent.putExtra("asesor_id", idAsesor)
                startActivity(intent)
            }
        }
    }

}