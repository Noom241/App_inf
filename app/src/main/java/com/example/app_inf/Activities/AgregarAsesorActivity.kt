package com.example.app_inf.Activities

import MySQLConnection
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.app_inf.R
import java.sql.SQLException
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class AgregarAsesorActivity : AppCompatActivity() {

    private lateinit var inputNombre: TextInputEditText
    private lateinit var inputTelefono: TextInputEditText
    private lateinit var inputUniversidad: TextInputEditText
    private lateinit var inputHorario: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_asesor)

        // Obtener referencias a los elementos EditText dentro de TextInputLayout
        val nombreLayout = findViewById<TextInputLayout>(R.id.rd_nombre_asesor)
        val telefonoLayout = findViewById<TextInputLayout>(R.id.rd_telefono_asesor)
        val universidadLayout = findViewById<TextInputLayout>(R.id.rd_univ_asesor)
        val horarioLayout = findViewById<TextInputLayout>(R.id.rd_horario_asesor)

        inputNombre = (nombreLayout.editText as TextInputEditText?)!!
        inputTelefono = (telefonoLayout.editText as TextInputEditText?)!!
        inputUniversidad = (universidadLayout.editText as TextInputEditText?)!!
        inputHorario = (horarioLayout.editText as TextInputEditText?)!!

        val btnApply = findViewById<Button>(R.id.btn_add_Asesor2)
        btnApply.setOnClickListener(View.OnClickListener {
            val nombre = inputNombre.text.toString()
            val telefono = inputTelefono.text.toString()
            val universidad = inputUniversidad.text.toString()
            val horario = inputHorario.text.toString()

            if (nombre.isEmpty() || telefono.isEmpty() ||
                universidad.isEmpty() || horario.isEmpty()
            ) {
                Toast.makeText(this@AgregarAsesorActivity, "All fields are required", Toast.LENGTH_SHORT)
                    .show()
                return@OnClickListener
            }

            if (!telefono.matches("\\d+".toRegex())) {
                Toast.makeText(
                    this@AgregarAsesorActivity,
                    "Phone number must contain only digits",
                    Toast.LENGTH_SHORT
                ).show()
                return@OnClickListener
            }

            val nombreCompleto = "$nombre"
            InsertAsesorTask().execute(nombreCompleto, telefono, universidad, horario)
        })

        // Resto de tu código...
    }

    private inner class InsertAsesorTask : AsyncTask<String, Void, Boolean>() {
        override fun doInBackground(vararg params: String?): Boolean {
            val nombreCompleto = params[0]
            val telefono = params[1]
            val universidad = params[2]
            val horario = params[3]

            try {
                val connection = MySQLConnection.getConnection()
                val insertQuery =
                    "INSERT INTO asesores (Nombre, Teléfono, Universidad, Horario) VALUES (?, ?, ?, ?)"
                val preparedStatement = connection.prepareStatement(insertQuery)
                preparedStatement.setString(1, nombreCompleto)
                preparedStatement.setString(2, telefono)
                preparedStatement.setString(3, universidad)
                preparedStatement.setString(4, horario)
                preparedStatement.executeUpdate()
                preparedStatement.close()
                connection.close()
                return true
            } catch (e: SQLException) {
                e.printStackTrace()
                return false
            }
        }

        override fun onPostExecute(success: Boolean) {
            if (success) {
                Toast.makeText(this@AgregarAsesorActivity, "Advisor added successfully", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(this@AgregarAsesorActivity, "Error adding advisor", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    // Resto de tus funciones...
}
