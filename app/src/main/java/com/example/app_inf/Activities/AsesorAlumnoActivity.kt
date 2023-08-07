package com.example.app_inf.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.app_inf.R

class AsesorAlumnoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_asesor_alumno)

        val selected_values = intent.getStringExtra("selected_values")
        val horario_hora = intent.getStringExtra("horario_hora")

        val sortedValues = sortValues(selected_values)
        val valoresArray = sortedValues.split(",")

        val dayTextViews = arrayOf(
            findViewById<TextView>(R.id.day_0),
            findViewById<TextView>(R.id.day_1),
            findViewById<TextView>(R.id.day_2),
            findViewById<TextView>(R.id.day_3)
        )

        for (i in dayTextViews.indices) {
            dayTextViews[i].text = if (valoresArray.size > i) valoresArray[i] else "-----"
        }

        //Lista de Asesores dependiendo findViewById<TextView>(R.id.day_x)




        val btnFinalizar = findViewById<Button>(R.id.btn_finalizar)
        btnFinalizar.setOnClickListener {
            val toastMessage = sortedValues
            Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show()
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

        val values = input?.split(",")?.toMutableList() ?: mutableListOf()
        values.removeAll(listOf("", "-----")) // Remove empty and placeholder values
        values.sortWith(compareBy { dayOrder[it] }) // Sort using the custom dayOrder

        return values.joinToString(",")
    }

}
