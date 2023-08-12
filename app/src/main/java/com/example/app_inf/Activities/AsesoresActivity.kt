package com.example.app_inf.Activities

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.example.app_inf.FetchAsesorDataTask
import com.example.app_inf.R

class AsesoresActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_asesores)

        val autoCompleteTextView = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView)
        val txtNombreAsesor = findViewById<TextView>(R.id.txt_nombre_asesor)
        val txtTelefonoAsesor = findViewById<TextView>(R.id.txt_telefono_Asesor)
        val txtHorarioAsesor = findViewById<TextView>(R.id.txt_horario_Asesor)
        val txtUniversidadAsesor = findViewById<TextView>(R.id.txt_universidad_asesor)
        val btn_edit_Asesor = findViewById<Button>(R.id.btn_edit_Asesor)
        btn_edit_Asesor.setOnClickListener {
            val intent = Intent(this, EditarAsesorActivity::class.java)
            startActivity(intent)
        }
        val btn_add_Asesor = findViewById<Button>(R.id.btn_add_Asesor)
        btn_add_Asesor.setOnClickListener {
            val intent = Intent(this, AgregarAsesorActivity::class.java)
            startActivity(intent)
        }
        /*

        autoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            val selectedName = autoCompleteTextView.adapter.getItem(position) as String
            FetchAsesorDataTask(txtNombreAsesor, txtTelefonoAsesor, txtHorarioAsesor, txtUniversidadAsesor).execute(selectedName)
        }*/

        // Ejecutar la tarea de obtener nombres de asesores en segundo plano
        //FetchNamesTask(autoCompleteTextView).execute()

        autoCompleteTextView.setOnClickListener {
            autoCompleteTextView.showDropDown()
        }
    }



}