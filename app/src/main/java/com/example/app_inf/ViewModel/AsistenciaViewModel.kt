package com.example.app_inf.ViewModel

import MySQLConnection
import androidx.lifecycle.ViewModel
import com.example.app_inf.data.AlumnoData
import com.example.app_inf.data.AsesorData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Date


class AsistenciaViewModel : ViewModel() {

    suspend fun registrarAsistencia(idEstudiante: Int, idProfesor: Int, fecha: Date?, asistio: String?): Boolean {
        return withContext(Dispatchers.IO) {
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

    suspend fun obtenerAsesorPorNombre(nombre: String): AsesorData? {
        return withContext(Dispatchers.IO) {
            MySQLConnection.obtenerAsesorPorNombre(nombre)
        }
    }

    suspend fun agregarAsesor(asesorData: AsesorData){
        return withContext(Dispatchers.IO) {
            MySQLConnection.agregarAsesor(asesorData)
        }
    }

    suspend fun actualizarProfesor(id: Int, nombre: String, telefono:String, uni:String, horario_semana:String){
        return withContext(Dispatchers.IO) {
            MySQLConnection.actualizarProfesor(id, nombre, telefono, uni, horario_semana)
        }
    }

    suspend fun borrarAsesor(id:Int){
        return withContext(Dispatchers.IO) {
            MySQLConnection.borrarAsesor(id)
        }
    }

    suspend fun obtenerEstudiantePorNombre(nombre: String): AlumnoData? {
        return withContext(Dispatchers.IO) {
            MySQLConnection.obtenerEstudiantePorNombre(nombre)
        }
    }

    suspend fun agregarAlumno(alumno: AlumnoData): Boolean {
        return withContext(Dispatchers.IO) {
            MySQLConnection.agregarAlumno(alumno)
        }
    }

    suspend fun actualizarAlumno(id: Int, alumno: AlumnoData): Boolean {
        return withContext(Dispatchers.IO) {
            MySQLConnection.actualizarAlumno(id, alumno)
        }
    }

    suspend fun borrarAlumno(nombre: String): Boolean {
        return withContext(Dispatchers.IO) {
            MySQLConnection.borrarAlumno(nombre)
        }
    }

    suspend fun obtenerProfesoresDisponibles(dia: String, horarioElegido: Int): List<String> {
        return withContext(Dispatchers.IO) {
            MySQLConnection.obtenerProfesoresDisponibles(dia, horarioElegido)
        }
    }

    suspend fun obtenerAsistenciaDeEstudiante(idEstudiante: Int): List<Pair<Date, String>> {
        return withContext(Dispatchers.IO) {
            MySQLConnection.obtenerAsistenciaDeEstudiante(idEstudiante)
        }
    }



}
