package com.example.app_inf


import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        val btnAsesores = findViewById<Button>(R.id.btnAsesores)
        btnAsesores.setOnClickListener {
            val intent = Intent(this, AsesoresActivity::class.java)
            startActivity(intent)
        }
    }
}

