package com.example.app_inf.Activities

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.app_inf.CustomCircleView
import com.example.app_inf.R


// Importa las librerías necesarias

// Importa las librerías necesarias

class Calendario : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendario)

        // Encuentra la instancia de CustomCircleView en tu diseño
        val customCircleView: CustomCircleView = findViewById(R.id.customCircleView)




        // Ejemplo de cómo llamar a la función con parámetros de prueba
        val centerX = 143f // Cambia este valor según tu necesidad
        val centerY = 114f // Cambia este valor según tu necesidad
        val circleColor = Color.BLUE // Cambia este valor según tu necesidad

        customCircleView.setCircleLocationAndColor(centerX, centerY, circleColor)
    }
}


