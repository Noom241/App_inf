package com.example.app_inf.Activities

import android.app.AlertDialog
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.example.app_inf.FetchAlumnosDataTask
import com.example.app_inf.R

class AlumnosActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alumnos)

        val autoCompleteTextView = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView2)
        val txtNombreAlumno = findViewById<TextView>(R.id.txt_nombre_alumno)
        val txtApoderadoAlumno = findViewById<TextView>(R.id.txt_apoderado_alumno)
        val txtTelefonoApoderado = findViewById<TextView>(R.id.txt_telefono_apoderado)
        val txtColegioAlumno = findViewById<TextView>(R.id.txt_colegio_alumno)
        val txtModalidadAlumno = findViewById<TextView>(R.id.txt_modalidad_alumno)
        val txtHorarioAlumno = findViewById<TextView>(R.id.txt_horario_alumno)
        val txtPaqueteAlumno = findViewById<TextView>(R.id.txt_paquete_alumno)
        val btn_add_Alumno = findViewById<Button>(R.id.btn_add_Alumno)
        btn_add_Alumno.setOnClickListener {
            val intent = Intent(this, AgregarAlumnoActivity::class.java)
            startActivity(intent)
        }

        autoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            val selectedName = autoCompleteTextView.adapter.getItem(position) as String
            FetchAlumnosDataTask(txtNombreAlumno, txtApoderadoAlumno, txtTelefonoApoderado, txtColegioAlumno, txtModalidadAlumno, txtHorarioAlumno, txtPaqueteAlumno).execute(selectedName)
        }

        val btnDeleteAlumno = findViewById<Button>(R.id.btn_delete_Alumno)
        btnDeleteAlumno.setOnClickListener {
            val autoCompleteTextView = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView2)
            mostrarDialogoConfirmacion(autoCompleteTextView.text.toString())
        }
        // Ejecutar la tarea de obtener nombres de alumnos en segundo plano
        FetchNamesTask(autoCompleteTextView).execute()

        autoCompleteTextView.setOnClickListener {
            autoCompleteTextView.showDropDown()
        }

    }


    private inner class FetchNamesTask(private val autoCompleteTextView: AutoCompleteTextView) :
        AsyncTask<Void, Void, List<String>>() {

        override fun doInBackground(vararg params: Void?): List<String> {
            return MySQLConnection.obtenerNombresDeAlumnos()
        }

        override fun onPostExecute(names: List<String>) {
            val adapter = ArrayAdapter(this@AlumnosActivity, android.R.layout.simple_dropdown_item_1line, names)
            autoCompleteTextView.setAdapter(adapter)
        }
    }
    private fun mostrarDialogoConfirmacion(nombreAlumno: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirmar borrado")
        builder.setMessage("¿Seguro que deseas borrar este alumno?")
        builder.setPositiveButton("Sí") { _, _ ->
            borrarAlumno(nombreAlumno)
        }
        builder.setNegativeButton("No", null)
        builder.show()
    }

    private fun borrarAlumno(nombreAlumno: String) {
        val txtNombreAlumno = findViewById<TextView>(R.id.txt_nombre_alumno)
        val txtApoderadoAlumno = findViewById<TextView>(R.id.txt_apoderado_alumno)
        val txtTelefonoApoderado = findViewById<TextView>(R.id.txt_telefono_apoderado)
        val txtColegioAlumno = findViewById<TextView>(R.id.txt_colegio_alumno)
        val txtModalidadAlumno = findViewById<TextView>(R.id.txt_modalidad_alumno)
        val txtHorarioAlumno = findViewById<TextView>(R.id.txt_horario_alumno)
        val txtPaqueteAlumno = findViewById<TextView>(R.id.txt_paquete_alumno)
        MySQLConnection.borrarAlumnoEnSegundoPlano(nombreAlumno, object : MySQLConnection.OnAlumnoBorradoListener {
            override fun onAlumnoBorrado(borradoExitoso: Boolean) {
                if (borradoExitoso) {
                    mostrarMensaje("Alumno eliminado correctamente")

                    // Limpiar los campos de texto de los detalles del alumno
                    txtNombreAlumno.text = ""
                    txtApoderadoAlumno.text = ""
                    txtTelefonoApoderado.text = ""
                    txtColegioAlumno.text = ""
                    txtModalidadAlumno.text = ""
                    txtHorarioAlumno.text = ""
                    txtPaqueteAlumno.text = ""

                    // Actualizar la lista de sugerencias del AutoCompleteTextView
                    val autoCompleteTextView = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView2)
                    FetchNamesTask(autoCompleteTextView).execute()

                    // Aquí puedes realizar cualquier acción necesaria después de la eliminación exitosa
                } else {
                    mostrarMensaje("Error al eliminar el alumno")
                }
            }
        })
    }


    private fun mostrarMensaje(mensaje: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Mensaje")
        builder.setMessage(mensaje)
        builder.setPositiveButton("OK", null)
        builder.show()
    }
}