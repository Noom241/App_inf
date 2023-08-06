package com.example.app_inf

import MySQLConnection
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.sql.SQLException


class add_Activity : AppCompatActivity() {
    private var inputNombre: EditText? = null
    private var inputApellidos: EditText? = null
    private var inputTelefono: EditText? = null
    private var inputUniversidad: EditText? = null
    private var inputHorario: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        // Initialize UI elements
        inputNombre = findViewById(R.id.input_nombre)
        inputApellidos = findViewById(R.id.input_apellidos)
        inputTelefono = findViewById(R.id.input_telefono)
        inputUniversidad = findViewById(R.id.input_universidad)
        inputHorario = findViewById(R.id.input_horario)
        val btnApply = findViewById<Button>(R.id.btn_apply_add_asesor)
        btnApply.setOnClickListener(View.OnClickListener { // Get input data
            val nombre = inputNombre?.text.toString()
            val apellidos = inputApellidos?.text.toString()
            val telefono = inputTelefono?.text.toString()
            val universidad = inputUniversidad?.text.toString()
            val horario = inputHorario?.text.toString()


            // Validate input data
            if (nombre.isEmpty() || apellidos.isEmpty() || telefono.isEmpty() ||
                universidad.isEmpty() || horario.isEmpty()
            ) {
                Toast.makeText(this@add_Activity, "All fields are required", Toast.LENGTH_SHORT)
                    .show()
                return@OnClickListener
            }
            if (!telefono.matches("\\d+".toRegex())) {
                Toast.makeText(
                    this@add_Activity,
                    "Phone number must contain only digits",
                    Toast.LENGTH_SHORT
                ).show()
                return@OnClickListener
            }

            // Combine Nombres and Apellidos
            val nombreCompleto = "$nombre $apellidos"

            // Insert into database
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
                Toast.makeText(this@add_Activity, "Advisor added successfully", Toast.LENGTH_SHORT)
                    .show()
                preparedStatement.close()
                connection.close()
            } catch (e: SQLException) {
                e.printStackTrace()
                Toast.makeText(this@add_Activity, "Error adding advisor", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
