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
import androidx.lifecycle.ViewModelProvider
import com.example.app_inf.FetchAlumnosDataTask
import com.example.app_inf.R
import com.example.app_inf.ViewModel.AsistenciaViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AlumnosActivity : ComponentActivity() {
    private lateinit var AlumnosActivity_ViewModel: AsistenciaViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alumnos)
        AlumnosActivity_ViewModel = ViewModelProvider(this).get(AsistenciaViewModel::class.java)

        val autoCompleteTextView = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView2)
        val txtNombreAlumno = findViewById<TextView>(R.id.txt_nombre_alumno)
        val txtApoderadoAlumno = findViewById<TextView>(R.id.txt_apoderado_alumno)
        val txtTelefonoApoderado = findViewById<TextView>(R.id.txt_telefono_apoderado)
        val txtColegioAlumno = findViewById<TextView>(R.id.txt_colegio_alumno)
        val txtModalidadAlumno = findViewById<TextView>(R.id.txt_modalidad_alumno)
        val txtHorarioAlumno = findViewById<TextView>(R.id.txt_horario_alumno)
        val txtPaqueteAlumno = findViewById<TextView>(R.id.txt_paquete_alumno)
        val btn_edit_Alumno = findViewById<Button>(R.id.btn_edit_Alumno)

        val intent = Intent(this, AgregarAlumnoActivity::class.java)

        btn_edit_Alumno.setOnClickListener {
            intent.putExtra("status_Activity", "Edit")
            startActivity(intent)
        }
        val btn_add_Alumno = findViewById<Button>(R.id.btn_add_Alumno)
        btn_add_Alumno.setOnClickListener {

            startActivity(intent)
        }

        autoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            val selectedName = autoCompleteTextView.adapter.getItem(position) as String
            GlobalScope.launch(Dispatchers.Main) {
                val alumno = AlumnosActivity_ViewModel.obtenerEstudiantePorNombre(selectedName)
                txtNombreAlumno.text = alumno?.nombre ?: ""
                txtApoderadoAlumno.text = alumno?.apoderado ?: ""
                txtTelefonoApoderado.text = alumno?.telefonoApoderado ?: ""
                txtColegioAlumno.text = alumno?.colegio ?: ""
                txtModalidadAlumno.text = alumno?.modalidad ?: ""
                txtHorarioAlumno.text = alumno?.horario.toString()
                txtPaqueteAlumno.text = alumno?.paquete_elegido ?: ""
                //agregar txt para dias de semana que tiene clase
            }
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
            GlobalScope.launch(Dispatchers.Main) {
                AlumnosActivity_ViewModel.borrarAlumno(nombreAlumno)
            }

        }
        builder.setNegativeButton("No", null)
        builder.show()
    }



    private fun mostrarMensaje(mensaje: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Mensaje")
        builder.setMessage(mensaje)
        builder.setPositiveButton("OK", null)
        builder.show()
    }
}