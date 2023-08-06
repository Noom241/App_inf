package com.example.app_inf


import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import com.example.app_inf.Activities.AlumnosActivity
import com.example.app_inf.Activities.AsesoresActivity

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        val btnAsesores = findViewById<Button>(R.id.btnAsesores)
        val btnAlumnos = findViewById<Button>(R.id.btnAlumnos)
        btnAsesores.setOnClickListener {
            val intent = Intent(this, AsesoresActivity::class.java)
            startActivity(intent)
        }
        btnAlumnos.setOnClickListener {
            val intent = Intent(this, AlumnosActivity::class.java)
            startActivity(intent)
        }
    }
}

