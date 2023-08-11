package com.example.app_inf.ViewModel

import MySQLConnection
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Date

class AsistenciaViewModel : ViewModel() {

    suspend fun registrarAsistencia(idEstudiante: Int, idProfesor: Int, fecha: Date?, asistio: String?): Boolean {
        println("Iniciandooooooooooo")
        return withContext(Dispatchers.IO) {
            println("return")
            MySQLConnection.registrarAsistenciaEstudiante(fecha,idEstudiante, idProfesor, asistio)
        }
    }

    suspend fun obtenerIDPorNombre(nombre: String, tabla: String): Int {
        return withContext(Dispatchers.IO) {
            MySQLConnection.obtenerIDPorNombre(nombre, tabla)
        }
    }

    suspend fun obtenerNombresDeProfesores(): List<String> {
        return withContext(Dispatchers.IO) {
            MySQLConnection.obtenerNombresDeProfesores()
        }
    }

    suspend fun obtenerNombresDeAlumnos(): List<String> {
        return withContext(Dispatchers.IO) {
            MySQLConnection.obtenerNombresDeAlumnos()
        }
    }
}
