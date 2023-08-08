package com.example.app_inf.Activities

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import com.example.app_inf.R
import MySQLConnection
import MySQLConnection.obtenerProfesoresDisponibles
import android.os.AsyncTask
import android.widget.Button
import androidx.activity.viewModels
import com.example.app_inf.ViewModel.AsesorAlumnoViewModel

class AsesorAlumnoActivity : AppCompatActivity() {
    private val viewModel: AsesorAlumnoViewModel by viewModels()
    private lateinit var day_0: Spinner
    private lateinit var day_1: Spinner
    private lateinit var day_2: Spinner
    private lateinit var day_3: Spinner
    private lateinit var variableString: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_asesor_alumno)
        initializeViews()
        setupSpinnerState(variableString)

        val selectedValues = intent.getStringExtra("selected_values")
        val horarioHora = intent.getStringExtra("horario_hora").toString()

        val sortedValues = sortValues(selectedValues)
        val valoresArray = sortedValues.split(",")

        val dayTextViews = arrayOf(
            findViewById<TextView>(R.id.day_0),
            findViewById<TextView>(R.id.day_1),
            findViewById<TextView>(R.id.day_2),
            findViewById<TextView>(R.id.day_3)
        )

        dayTextViews.forEachIndexed { i, textView ->
            textView.text = valoresArray.getOrNull(i) ?: "-----"
        }

        val diaTextViews = arrayOf(
            findViewById<Spinner>(R.id.asesor_top_l),
            findViewById<Spinner>(R.id.asesor_top_r),
            findViewById<Spinner>(R.id.asesor_bot_l),
            findViewById<Spinner>(R.id.asesor_bot_r)
        )

        diaTextViews.forEachIndexed { i, spinner ->
            val dia = dayTextViews[i].text.toString()
            FetchProfesoresTask(dia, horarioHora, spinner).execute()
        }

        val btnFinalizar = findViewById<Button>(R.id.btn_finalizar)
        btnFinalizar.setOnClickListener {
            // Agregar aquí el código para manejar el evento del botón
        }
    }

    private inner class FetchProfesoresTask(
        private val dia: String,
        private val horarioHora: String?,
        private val spinner: Spinner
    ) : AsyncTask<Void, Void, List<String>>() {

        override fun doInBackground(vararg params: Void): List<String> {
            val horarioInt = horarioHora?.toIntOrNull() ?: 0
            return obtenerProfesoresDisponibles(dia, horarioInt)
        }

        override fun onPostExecute(result: List<String>) {
            super.onPostExecute(result)
            val adapter = ArrayAdapter(
                this@AsesorAlumnoActivity,
                android.R.layout.simple_spinner_item,
                result
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
    }

    private fun sortValues(input: String?): String {
        val dayOrder = mapOf(
            "Lunes" to 1,
            "Martes" to 2,
            "Miércoles" to 3,
            "Jueves" to 4,
            "Viernes" to 5,
            "Sábado" to 6,
            "Domingo" to 7
        )

        return input
            ?.split(",")
            ?.filterNot { it.isEmpty() || it == "-----" }
            ?.sortedBy { dayOrder[it] }
            ?.joinToString(",")
            ?: ""
    }
    private fun setupSpinnerState(input: String?) {
        when (input) {
            "A" -> {
                day_2.isEnabled = false
                day_3.isEnabled = false
                //day_2.setSelection(5)
                //day_3.setSelection(5)
            }
            "B" -> {
                day_3.isEnabled = false
                //spinner4.setSelection(5)
            }
            // "C" case doesn't require any special setup
        }
    }
    private fun initializeViews() {
        val paquete_key = intent.getStringExtra("paquete_key")
        variableString = paquete_key ?: ""

        day_0 = findViewById(R.id.asesor_top_l)
        day_1 = findViewById(R.id.asesor_top_r)
        day_2 = findViewById(R.id.asesor_bot_l)
        day_3 = findViewById(R.id.asesor_bot_r)
    }
}
