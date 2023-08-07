package com.example.app_inf.Activities

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.app_inf.R

class PaquetesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paquetes)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        val btn_back_alumno1 = findViewById<Button>(R.id.btn_back_alumno1)
        btn_back_alumno1.setOnClickListener {
            val intent = Intent(this, AgregarAlumnoActivity::class.java)
            startActivity(intent)
        }
        val btn_next_alumno2 = findViewById<Button>(R.id.btn_next_alumno2)
        btn_next_alumno2.setOnClickListener {
            val intent = Intent(this, AsesorAlumnoActivity::class.java)
            startActivity(intent)
    }
}
}