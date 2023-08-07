package com.example.app_inf.ViewModel

import MySQLConnection.obtenerProfesoresDisponibles
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AsesorAlumnoViewModel : ViewModel() {
    fun fetchProfesores(dia: String, horarioHora: String, onProfesoresFetched: (List<String>) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val horarioInt = horarioHora.toIntOrNull() ?: 0
            val profesoresDisponibles = obtenerProfesoresDisponibles(dia, horarioInt)
            onProfesoresFetched(profesoresDisponibles)
        }
    }
}

