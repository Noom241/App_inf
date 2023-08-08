package com.example.app_inf.Activities

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import com.example.app_inf.R

class PaquetesActivity : AppCompatActivity() {

    private lateinit var spinner1: Spinner
    private lateinit var spinner2: Spinner
    private lateinit var spinner3: Spinner
    private lateinit var spinner4: Spinner
    private lateinit var variableString: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paquetes)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        initializeViews()
        setupSpinners()
        setupButtonListeners()
        setupSpinnerState()
    }

    private fun initializeViews() {
        val paquete_key = intent.getStringExtra("paquete_key")
        variableString = paquete_key ?: ""

        spinner1 = findViewById(R.id.spinner_left_top)
        spinner2 = findViewById(R.id.spinner_right_top)
        spinner3 = findViewById(R.id.spinner_left_bot)
        spinner4 = findViewById(R.id.spinner_right_bot)
    }

    private fun setupSpinners() {
        val diasSemana = arrayOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "-----")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, diasSemana)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner1.adapter = adapter
        spinner2.adapter = adapter
        spinner3.adapter = adapter
        spinner4.adapter = adapter
    }

    private fun setupButtonListeners() {
        /*
        val btnBack = findViewById<Button>(R.id.btn_back_alumno1)
        btnBack.setOnClickListener {
            val intent = Intent(this, AgregarAlumnoActivity::class.java)
            startActivity(intent)
        }*/

        val btnNext = findViewById<Button>(R.id.btn_next_alumno2)
        btnNext.setOnClickListener {
            val spinners = listOf(spinner1, spinner2, spinner3, spinner4)
            val selectedValues = spinners.map { it.selectedItem.toString() }

            val isValidSelection = when (variableString) {
                "A" -> selectedValues[0] != "-----" && selectedValues[1] != "-----"
                "B" -> selectedValues[0] != "-----" && selectedValues[1] != "-----" && selectedValues[2] != "-----"
                "C" -> selectedValues.none { it == "-----" }
                else -> false
            }

            if (isValidSelection) {
                val nonEmptySelectedValues = selectedValues.filter { it != "-----" }
                val isNoRepetition = nonEmptySelectedValues.size == nonEmptySelectedValues.toSet().size

                if (isNoRepetition) {
                    val selectedValuesString = selectedValues.filterNot { it == "-----" }.joinToString(",")
                    val intent = Intent(this, AsesorAlumnoActivity::class.java)
                    intent.putExtra("horario_hora", intent.getStringExtra("horario_hora"))
                    intent.putExtra("selected_values", selectedValuesString)
                    startActivity(intent)
                } else {
                    val errorMessage = "No se pueden repetir días."
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                }
            } else {
                val errorMessage = "Selecciona valores válidos."
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupSpinnerState() {
        when (variableString) {
            "A" -> {
                spinner3.isEnabled = false
                spinner4.isEnabled = false
                spinner3.setSelection(5)
                spinner4.setSelection(5)
            }
            "B" -> {
                spinner4.isEnabled = false
                spinner4.setSelection(5)
            }
            // "C" case doesn't require any special setup
        }
    }
}
