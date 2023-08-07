package com.example.app_inf.Activities

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.app_inf.R

class AsesorAlumnoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_asesor_alumno)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        val btn_back_alumno2 = findViewById<Button>(R.id.btn_back_alumno2)
        btn_back_alumno2.setOnClickListener {
            val intent = Intent(this, PaquetesActivity::class.java)
            startActivity(intent)
    }
}
}