package com.example.app_inf

import android.os.AsyncTask
import android.widget.TextView
import com.example.app_inf.data.AsesorData
import java.sql.SQLException

class FetchAsesorDataTask(
    private val txtNombreAsesor: TextView?,
    private val txtTelefonoAsesor: TextView?,
    private val txtHorarioAsesor: TextView?,
    private val txtUniversidadAsesor: TextView?
) : AsyncTask<String, Void, AsesorData>() {

    override fun doInBackground(vararg params: String?): AsesorData {
        val selectedName = params[0] ?: ""
        return fetchAsesorData(selectedName)
    }

    override fun onPostExecute(asesorData: AsesorData) {
        txtNombreAsesor?.text = asesorData.nombre
        txtTelefonoAsesor?.text = asesorData.telefono
        txtHorarioAsesor?.text = asesorData.horario
        txtUniversidadAsesor?.text = asesorData.universidad
    }

    private fun fetchAsesorData(selectedName: String): AsesorData {
        val asesorData = AsesorData("", "", "", "") // Initialize with empty values

        try {
            val connection = MySQLConnection.getConnection()
            val query = "SELECT * FROM Profesores WHERE Nombre = ?" // Updated table name
            val statement = connection.prepareStatement(query)
            statement.setString(1, selectedName)
            val resultSet = statement.executeQuery()

            if (resultSet.next()) {
                asesorData.nombre = resultSet.getString("Nombre")
                asesorData.telefono = resultSet.getString("Telefono")
                asesorData.horario = resultSet.getString("Horario_disponible")
                asesorData.universidad = resultSet.getString("Universidad")
            }

            resultSet.close()
            statement.close()
            connection.close()
        } catch (e: SQLException) {
            e.printStackTrace()
        }

        return asesorData
    }
}