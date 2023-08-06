package com.example.app_inf.Activities

import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.app_inf.R
import com.example.app_inf.data.AsesorData
import java.sql.SQLException
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.Date

class AgregarAsesorActivity : AppCompatActivity() {

    private lateinit var inputNombre: TextInputEditText
    private lateinit var inputTelefono: TextInputEditText
    private lateinit var inputUniversidad: TextInputEditText
    private lateinit var chkLunes: CheckBox
    private lateinit var chkMartes: CheckBox
    private lateinit var chkMiercoles: CheckBox
    private lateinit var chkJueves: CheckBox
    private lateinit var chkViernes: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_asesor)

        // Obtener referencias a los elementos EditText dentro de TextInputLayout
        val nombreLayout = findViewById<TextInputLayout>(R.id.rd_nombre_asesor)
        val telefonoLayout = findViewById<TextInputLayout>(R.id.rd_telefono_asesor)
        val universidadLayout = findViewById<TextInputLayout>(R.id.rd_univ_asesor)

        chkLunes = findViewById(R.id.chk_lunes)
        chkMartes = findViewById(R.id.chk_martes)
        chkMiercoles = findViewById(R.id.chk_miercoles)
        chkJueves = findViewById(R.id.chk_jueves)
        chkViernes = findViewById(R.id.chk_viernes)

        inputNombre = (nombreLayout.editText as TextInputEditText?)!!
        inputTelefono = (telefonoLayout.editText as TextInputEditText?)!!
        inputUniversidad = (universidadLayout.editText as TextInputEditText?)!!

        val btnApply = findViewById<Button>(R.id.btn_add_Asesor2)
        btnApply.setOnClickListener(View.OnClickListener {
            val selectedDays = mutableListOf<String>()

            if (chkLunes.isChecked) {
                selectedDays.add("Lunes")
            }
            if (chkMartes.isChecked) {
                selectedDays.add("Martes")
            }
            if (chkMiercoles.isChecked) {
                selectedDays.add("Miércoles")
            }
            if (chkJueves.isChecked) {
                selectedDays.add("Jueves")
            }
            if (chkViernes.isChecked) {
                selectedDays.add("Viernes")
            }

            val selectedDaysString = selectedDays.joinToString(",")

            val nombre = inputNombre.text.toString()
            val telefono = inputTelefono.text.toString()
            val universidad = inputUniversidad.text.toString()
            val horario = selectedDaysString

            if (nombre.isEmpty() || telefono.isEmpty() || universidad.isEmpty() || horario.isEmpty()) {
                Toast.makeText(this@AgregarAsesorActivity, "All fields are required", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }

            if (!telefono.matches("\\d+".toRegex())) {
                Toast.makeText(this@AgregarAsesorActivity, "Phone number must contain only digits", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }

            val asesor = AsesorData(nombre, telefono, horario, universidad)
            InsertAsesorTask().execute(asesor)
        })

        // Resto de tu código...
    }

    private inner class InsertAsesorTask : AsyncTask<AsesorData, Void, Boolean>() {
        override fun doInBackground(vararg params: AsesorData?): Boolean {
            val asesor = params[0]

            try {
                val success = MySQLConnection.agregarAsesor(asesor)
                return success
            } catch (e: SQLException) {
                e.printStackTrace()
                return false
            }
        }

        override fun onPostExecute(success: Boolean) {
            if (success) {
                Toast.makeText(this@AgregarAsesorActivity, "Advisor added successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@AgregarAsesorActivity, "Error adding advisor", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Resto de tus funciones...
}
