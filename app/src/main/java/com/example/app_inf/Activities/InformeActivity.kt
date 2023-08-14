package com.example.app_inf.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.app_inf.R
import com.example.app_inf.ViewModel.AsistenciaViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class InformeActivity : AppCompatActivity() {
    private lateinit var informeViewModel: AsistenciaViewModel
    private lateinit var autoCompleteTextView: AutoCompleteTextView
    private lateinit var autoCompleteTextViewAlumnos: AutoCompleteTextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_informe)
        informeViewModel = ViewModelProvider(this).get(AsistenciaViewModel::class.java)



    }
    private fun setupAutoCompleteViews() {
        // Fetch and set adapters for AutoCompleteTextViews using coroutines
        GlobalScope.launch(Dispatchers.Main) {
            val nombresProfesores = informeViewModel.obtenerNombresDeProfesores()
            val nombresAlumnos = informeViewModel.obtenerNombresDeAlumnos()

            val adapterProfesores = ArrayAdapter(this@InformeActivity, android.R.layout.simple_dropdown_item_1line, nombresProfesores)
            autoCompleteTextView.setAdapter(adapterProfesores)

            val adapterAlumnos = ArrayAdapter(this@InformeActivity, android.R.layout.simple_dropdown_item_1line, nombresAlumnos)
            autoCompleteTextViewAlumnos.setAdapter(adapterAlumnos)
        }
    }



}