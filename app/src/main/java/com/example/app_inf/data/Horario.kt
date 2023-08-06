package com.example.app_inf.data

import java.sql.Time

data class Horario(
    val id: Int,
    val dia: String,
    val horaInicio: Time,
    val horaFin: Time
)