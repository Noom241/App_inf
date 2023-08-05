package com.example.app_inf

import MySQLConnection
import android.os.AsyncTask
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.activity.ComponentActivity
import java.sql.SQLException

class AlumnosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alumnos)
    }
}