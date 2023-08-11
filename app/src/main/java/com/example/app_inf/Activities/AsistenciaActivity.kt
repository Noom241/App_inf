package com.example.app_inf.Activities

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.CalendarView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.app_inf.R
import com.example.app_inf.ViewModel.AsistenciaViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

class AsistenciaActivity : AppCompatActivity() {

    private lateinit var asistenciaViewModel: AsistenciaViewModel
    private lateinit var autoCompleteTextView: AutoCompleteTextView
    private lateinit var autoCompleteTextViewAlumnos: AutoCompleteTextView

    private var selectedYear = 0
    private var selectedMonth = 0
    private var selectedDay = 0

    private lateinit var spinnerAsistencia: Spinner


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_asistencia)

        autoCompleteTextView = findViewById(R.id.buscar_asistencia_asesor)
        autoCompleteTextViewAlumnos = findViewById(R.id.buscar_asistencia_alumno)

        asistenciaViewModel = ViewModelProvider(this).get(AsistenciaViewModel::class.java)



        setupAutoCompleteViews()
        setupCalendarView()
        setupConfirmButton()
        setupSpinner()
    }

    private fun setupAutoCompleteViews() {
        // Fetch and set adapters for AutoCompleteTextViews using coroutines
        GlobalScope.launch(Dispatchers.Main) {
            val nombresProfesores = asistenciaViewModel.obtenerNombresDeProfesores()
            val nombresAlumnos = asistenciaViewModel.obtenerNombresDeAlumnos()

            val adapterProfesores = ArrayAdapter(this@AsistenciaActivity, android.R.layout.simple_dropdown_item_1line, nombresProfesores)
            autoCompleteTextView.setAdapter(adapterProfesores)

            val adapterAlumnos = ArrayAdapter(this@AsistenciaActivity, android.R.layout.simple_dropdown_item_1line, nombresAlumnos)
            autoCompleteTextViewAlumnos.setAdapter(adapterAlumnos)
        }
    }

    private fun setupCalendarView() {
        val calendarView: CalendarView = findViewById(R.id.calendarView)
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            // Almacena las selecciones de fecha en las variables
            selectedYear = year
            selectedMonth = month
            selectedDay = dayOfMonth
        }
    }


    private fun setupSpinner() {
        spinnerAsistencia = findViewById(R.id.spn_asistencia) // Mueve esta línea aquí
        val asistenciaOptions = arrayOf("Clase Regular", "Recuperación")
        val asistenciaAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, asistenciaOptions)
        asistenciaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerAsistencia.adapter = asistenciaAdapter
    }


    private fun setupConfirmButton() {
        val confirmButton: Button = findViewById(R.id.btn_asistencia)
        confirmButton.setOnClickListener {
            val selectedDate = createDate(selectedYear, selectedMonth, selectedDay)
            val asesorNombre = autoCompleteTextView.text.toString()
            val alumnoNombre = autoCompleteTextViewAlumnos.text.toString()
            val asistenciaTipo = spinnerAsistencia.selectedItem.toString()
            val tipoValor = when (asistenciaTipo) {
                "Clase Regular" -> "1"
                "Recuperación" -> "2"
                else -> {"3"}
            }

            GlobalScope.launch(Dispatchers.Main) {
                val idAsesor = asistenciaViewModel.obtenerIDPorNombre(asesorNombre, "Profesores")
                val idAlumno = asistenciaViewModel.obtenerIDPorNombre(alumnoNombre, "Estudiantes")
                println("chupetin $idAsesor $idAlumno")
                println("Gaaaaaaaaa $asistenciaTipo")
                println("$idAlumno,---------- $idAsesor,----- $selectedDate,--------- $asistenciaTipo")

                asistenciaViewModel.registrarAsistencia(idAlumno, idAsesor, selectedDate, tipoValor)

                // Mostrar un Toast u otra acción después de registrar la asistencia
                Toast.makeText(this@AsistenciaActivity, "Asistencia registrada", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun createDate(year: Int, month: Int, day: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        return calendar.time
    }

}