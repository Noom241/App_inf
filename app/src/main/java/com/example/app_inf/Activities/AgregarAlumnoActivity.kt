package com.example.app_inf.Activities

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import com.example.app_inf.Activities.PaquetesActivity
import com.example.app_inf.R
import com.example.app_inf.data.AlumnoData
import java.sql.SQLException

class AgregarAlumnoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_alumno)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        val btnNextAlumno1 = findViewById<Button>(R.id.btn_next_alumno1)
        // Obtener referencias a los spinners
        val spnModalidadAlumno = findViewById<Spinner>(R.id.spn_modalidad_alumno)
        val spnPaqueteAlumno = findViewById<Spinner>(R.id.spn_paquete_alumno)
        val spnHorarioAlumno = findViewById<Spinner>(R.id.spn_horario_alumno)

        // Crear ArrayAdapter para los valores de Modalidad Alumno (Presencial/Virtual)
        val modalidadArray = arrayOf("Presencial", "Virtual")
        val modalidadAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, modalidadArray)
        spnModalidadAlumno.adapter = modalidadAdapter

        // Crear ArrayAdapter para los valores de Paquete Alumno (A, B, C)
        val paqueteArray = arrayOf("A", "B", "C")
        val paqueteAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, paqueteArray)
        spnPaqueteAlumno.adapter = paqueteAdapter

        // Crear ArrayAdapter para los valores de Horario Alumno (1, 2, 3)
        val horarioArray = arrayOf("1", "2", "3")
        val horarioAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, horarioArray)
        spnHorarioAlumno.adapter = horarioAdapter

        btnNextAlumno1.setOnClickListener {
            val intent = Intent(this, PaquetesActivity::class.java)
            val nombre = findViewById<EditText>(R.id.rd_nombre_alumno).text.toString()
            val apoderado = findViewById<EditText>(R.id.rd_apod_alumno).text.toString()
            val telefonoApoderado = findViewById<EditText>(R.id.rd_telf_alumno).text.toString()
            val colegio = findViewById<EditText>(R.id.rd_colegio_alumno).text.toString()
            val modalidad = findViewById<Spinner>(R.id.spn_modalidad_alumno).selectedItem.toString()
            val horario = findViewById<Spinner>(R.id.spn_horario_alumno).selectedItem.toString()
            val paquete = findViewById<Spinner>(R.id.spn_paquete_alumno).selectedItem.toString()

            val diasList = mutableListOf<String>() // Dejando la lista vacía
            val asesoresMap = mutableMapOf<String, String>() // Dejando el mapa vacío

            val alumnoData = AlumnoData(
                nombre,
                apoderado,
                telefonoApoderado,
                colegio,
                modalidad,
                horario.toInt(),
                paquete,
                horario_semana = ""
            )

            AgregarAlumnoAsyncTask().execute(alumnoData)
            //startActivity(intent)
        }
    }

    private inner class AgregarAlumnoAsyncTask : AsyncTask<AlumnoData, Void, Boolean>() {
        override fun doInBackground(vararg params: AlumnoData?): Boolean {
            val alumno = params[0]
            return try {
                if (alumno != null) {
                    MySQLConnection.agregarAlumno(alumno)
                } else {
                    false
                }
            } catch (e: SQLException) {
                e.printStackTrace()
                false
            }
        }

        override fun onPostExecute(result: Boolean) {
            super.onPostExecute(result)
            if (result) {
                val intent = Intent(this@AgregarAlumnoActivity, PaquetesActivity::class.java)
                val paquete_key = findViewById<Spinner>(R.id.spn_paquete_alumno).selectedItem.toString()
                val horario_hora = findViewById<Spinner>(R.id.spn_horario_alumno).selectedItem.toString()
                intent.putExtra("paquete_key", paquete_key)
                intent.putExtra("horario_hora", horario_hora)
                startActivity(intent)
            } else {
                // Mostrar mensaje de error o tomar alguna acción en caso de fallo
            }
        }
    }
}
