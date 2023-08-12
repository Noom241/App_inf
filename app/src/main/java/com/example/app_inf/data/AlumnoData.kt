package com.example.app_inf.data

import java.io.Serializable

data class AlumnoData(
    val nombre: String,
    val apoderado: String,
    val telefonoApoderado: String,
    val colegio: String,
    val modalidad: String,
    val horario: Int,
    val paquete_elegido: String,
    var horario_semana: String
): Serializable