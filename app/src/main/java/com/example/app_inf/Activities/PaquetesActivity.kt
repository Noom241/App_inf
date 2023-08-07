package com.example.app_inf.Activities


import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import com.example.app_inf.R

class PaquetesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paquetes)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        val alumnoRecibido = intent.getStringExtra("alumno_key")
        val diasSemana = arrayOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "-----")

        val spinner1 = findViewById<Spinner>(R.id.spinner_left_top)
        val spinner2 = findViewById<Spinner>(R.id.spinner_right_top)
        val spinner3 = findViewById<Spinner>(R.id.spinner_left_bot)
        val spinner4 = findViewById<Spinner>(R.id.spinner_right_bot)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, diasSemana)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner1.adapter = adapter
        spinner2.adapter = adapter
        spinner3.adapter = adapter
        spinner4.adapter = adapter

        val variableString = "A"  // Cambiar la variable según tus necesidades

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

            "C" -> {
                // No se necesita hacer nada, todos los spinners son habilitados por defecto
            }
        }

        val btn_back_alumno1 = findViewById<Button>(R.id.btn_back_alumno1)
        btn_back_alumno1.setOnClickListener {
            val intent = Intent(this, AgregarAlumnoActivity::class.java)
            startActivity(intent)
        }

        val btn_next_alumno2 = findViewById<Button>(R.id.btn_next_alumno2)
        btn_next_alumno2.setOnClickListener {
            val spinnerValues = mutableMapOf(
                spinner1.id to spinner1.selectedItem.toString(),
                spinner2.id to spinner2.selectedItem.toString(),
                spinner3.id to spinner3.selectedItem.toString(),
                spinner4.id to spinner4.selectedItem.toString()
            )

            val nonEmptySpinnerValues = spinnerValues.filter { it.value != "-----" }.values

            val isValid = nonEmptySpinnerValues.size == nonEmptySpinnerValues.toSet().size

            if (isValid) {
                val variableString = variableString  // Cambiar la variable según tus necesidades

                when (variableString) {
                    "A" -> {
                        if (spinner1.selectedItem == "-----" || spinner2.selectedItem == "-----") {
                            // Mostrar mensaje de error
                            val errorMessage = "Selecciona valores válidos."
                            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                        } else {
                            val intent = Intent(this, AsesorAlumnoActivity::class.java)
                            startActivity(intent)
                        }
                    }
                    "B" -> {
                        if (spinner1.selectedItem == "-----" || spinner2.selectedItem == "-----" || spinner3.selectedItem == "-----") {
                            // Mostrar mensaje de error
                            val errorMessage = "Selecciona valores válidos."
                            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                        } else {
                            val intent = Intent(this, AsesorAlumnoActivity::class.java)
                            startActivity(intent)
                        }
                    }
                    "C" -> {
                        if (spinner1.selectedItem == "-----" || spinner2.selectedItem == "-----" ||
                            spinner3.selectedItem == "-----" || spinner4.selectedItem == "-----") {
                            // Mostrar mensaje de error
                            val errorMessage = "Selecciona valores válidos."
                            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                        } else {
                            val intent = Intent(this, AsesorAlumnoActivity::class.java)
                            startActivity(intent)
                        }
                    }
                }
            } else {
                // Mostrar mensaje de error general
                val errorMessage = "No se pueden repetir dias."
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }


    }
}