package com.example.app_inf

import MySQLConnection
import android.os.AsyncTask
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.activity.ComponentActivity
import java.sql.SQLException

class AsesoresActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_asesores)

        val autoCompleteTextView = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView)
        val txtNombreAsesor = findViewById<TextView>(R.id.txt_nombre_asesor)
        val txtTelefonoAsesor = findViewById<TextView>(R.id.txt_telefono_Asesor)
        val txtHorarioAsesor = findViewById<TextView>(R.id.txt_horario_Asesor)
        val txtUniversidadAsesor = findViewById<TextView>(R.id.txt_universidad_asesor)

        autoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            val selectedName = autoCompleteTextView.adapter.getItem(position) as String
            FetchAsesorDataTask(txtNombreAsesor, txtTelefonoAsesor, txtHorarioAsesor, txtUniversidadAsesor).execute(selectedName)
        }

        // Ejecutar la tarea de obtener nombres de asesores en segundo plano
        FetchNamesTask(autoCompleteTextView).execute()

        autoCompleteTextView.setOnClickListener {
            autoCompleteTextView.showDropDown()
        }
    }

    private inner class FetchNamesTask(private val autoCompleteTextView: AutoCompleteTextView) :
        AsyncTask<Void, Void, List<String>>() {

        override fun doInBackground(vararg params: Void?): List<String> {
            val names = ArrayList<String>()
            try {
                val connection = MySQLConnection.getConnection()
                val query = "SELECT Nombre FROM asesores"
                val statement = connection.prepareStatement(query)
                val resultSet = statement.executeQuery()

                while (resultSet.next()) {
                    val name = resultSet.getString("nombre")
                    names.add(name)
                }

                resultSet.close()
                statement.close()
                connection.close()
            } catch (e: SQLException) {
                e.printStackTrace()
            }
            return names
        }

        override fun onPostExecute(names: List<String>) {
            val adapter = ArrayAdapter(this@AsesoresActivity, android.R.layout.simple_dropdown_item_1line, names)
            autoCompleteTextView.setAdapter(adapter)
        }
    }
}
