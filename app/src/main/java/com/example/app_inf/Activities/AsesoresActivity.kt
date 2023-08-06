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
import com.example.app_inf.FetchAsesorDataTask
import com.example.app_inf.R

class AsesoresActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_asesores)

        val autoCompleteTextView = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView)
        val txtNombreAsesor = findViewById<TextView>(R.id.txt_nombre_asesor)
        val txtTelefonoAsesor = findViewById<TextView>(R.id.txt_telefono_Asesor)
        val txtHorarioAsesor = findViewById<TextView>(R.id.txt_horario_Asesor)
        val txtUniversidadAsesor = findViewById<TextView>(R.id.txt_universidad_asesor)
        val btn_add_Asesor = findViewById<Button>(R.id.btn_add_Asesor)
        btn_add_Asesor.setOnClickListener {
            val intent = Intent(this, AgregarAsesorActivity::class.java)
            startActivity(intent)
        }

        autoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            val selectedName = autoCompleteTextView.adapter.getItem(position) as String
            FetchAsesorDataTask(txtNombreAsesor, txtTelefonoAsesor, txtHorarioAsesor, txtUniversidadAsesor).execute(selectedName)
        }

        val btnDeleteAsesor = findViewById<Button>(R.id.btn_delete_Asesor)
        btnDeleteAsesor.setOnClickListener {
            val autoCompleteTextView = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView)
            mostrarDialogoConfirmacion(autoCompleteTextView.text.toString())
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
            return MySQLConnection.obtenerNombresDeProfesores()
        }

        override fun onPostExecute(names: List<String>) {
            val adapter = ArrayAdapter(this@AsesoresActivity, android.R.layout.simple_dropdown_item_1line, names)
            autoCompleteTextView.setAdapter(adapter)
        }
    }

    private fun mostrarDialogoConfirmacion(nombreAsesor: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirmar borrado")
        builder.setMessage("¿Seguro que deseas borrar este asesor?")
        builder.setPositiveButton("Sí") { _, _ ->
            borrarAsesor(nombreAsesor)
        }
        builder.setNegativeButton("No", null)
        builder.show()
    }

    private fun borrarAsesor(nombreAsesor: String) {
        val txtNombreAsesor = findViewById<TextView>(R.id.txt_nombre_asesor)
        val txtTelefonoAsesor = findViewById<TextView>(R.id.txt_telefono_Asesor)
        val txtHorarioAsesor = findViewById<TextView>(R.id.txt_horario_Asesor)
        val txtUniversidadAsesor = findViewById<TextView>(R.id.txt_universidad_asesor)
        MySQLConnection.borrarAsesorEnSegundoPlano(nombreAsesor, object : MySQLConnection.OnAsesorBorradoListener {
            override fun onAsesorBorrado(borradoExitoso: Boolean) {
                if (borradoExitoso) {
                    mostrarMensaje("Asesor eliminado correctamente")

                    // Limpiar los campos de texto
                    txtNombreAsesor.text = ""
                    txtTelefonoAsesor.text = ""
                    txtHorarioAsesor.text = ""
                    txtUniversidadAsesor.text = ""

                    // Actualizar la lista de sugerencias del AutoCompleteTextView
                    val autoCompleteTextView = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView)
                    FetchNamesTask(autoCompleteTextView).execute()

                    // Aquí puedes realizar cualquier acción necesaria después de la eliminación exitosa
                } else {
                    mostrarMensaje("Error al eliminar el asesor")
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
