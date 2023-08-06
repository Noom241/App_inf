package com.example.app_inf
import android.os.AsyncTask
import android.widget.TextView
import com.example.app_inf.data.AlumnoData

class FetchAlumnosDataTask(
    private val txtNombreAlumno: TextView?,
    private val txtApoderadoAlumno: TextView?,
    private val txtTelefonoApoderado: TextView?,
    private val txtColegioAlumno: TextView?,
    private val txtModalidadAlumno: TextView?,
    private val txtHorarioAlumno: TextView?,
    private val txtPaqueteAlumno: TextView?,
) : AsyncTask<String, Void, AlumnoData>() {

    override fun doInBackground(vararg params: String?): AlumnoData {
        val selectedName = params[0] ?: ""
        return MySQLConnection.obtenerEstudiantePorNombre(selectedName) ?: AlumnoData("", "", "", "", "", "", "", emptyList(), emptyMap())
    }

    override fun onPostExecute(alumnoData: AlumnoData) {
        txtNombreAlumno?.text = alumnoData.nombre
        txtApoderadoAlumno?.text = alumnoData.apoderado
        txtTelefonoApoderado?.text = alumnoData.telefonoApoderado
        txtColegioAlumno?.text = alumnoData.colegio
        txtModalidadAlumno?.text = alumnoData.modalidad
        txtHorarioAlumno?.text = alumnoData.horario
        txtPaqueteAlumno?.text = alumnoData.paquete
    }
}