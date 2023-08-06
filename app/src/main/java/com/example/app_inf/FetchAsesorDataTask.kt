package com.example.app_inf

import android.os.AsyncTask
import android.widget.TextView
import com.example.app_inf.data.AsesorData

class FetchAsesorDataTask(
    private val txtNombreAsesor: TextView?,
    private val txtTelefonoAsesor: TextView?,
    private val txtHorarioAsesor: TextView?,
    private val txtUniversidadAsesor: TextView?
) : AsyncTask<String, Void, AsesorData>() {

    override fun doInBackground(vararg params: String?): AsesorData {
        val selectedName = params[0] ?: ""
        return MySQLConnection.obtenerAsesorPorNombre(selectedName) ?: AsesorData("", "", "", "")
    }

    override fun onPostExecute(asesorData: AsesorData) {
        txtNombreAsesor?.text = asesorData.nombre
        txtTelefonoAsesor?.text = asesorData.telefono
        txtHorarioAsesor?.text = asesorData.horario
        txtUniversidadAsesor?.text = asesorData.universidad
    }
}