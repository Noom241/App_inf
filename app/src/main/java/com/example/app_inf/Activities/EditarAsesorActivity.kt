package com.example.app_inf.Activities

import MySQLConnection.obtenerAsesorPorNombre
import android.app.AlertDialog
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.text.Layout
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.compose.ui.text.android.TextLayout
import com.example.app_inf.FetchAsesorDataTask
import com.example.app_inf.R
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModelProvider
import com.example.app_inf.ViewModel.AsistenciaViewModel
import com.example.app_inf.data.AsesorData
import com.google.android.material.textfield.TextInputEditText
import kotlin.properties.Delegates

class EditarAsesorActivity : AppCompatActivity() {
    private lateinit var E_Asesor_ViewModel: AsistenciaViewModel
    lateinit var autoCompleteTextView: AutoCompleteTextView
    lateinit var txtNombreAsesor: TextInputLayout
    lateinit var txtTelefonoAsesor: TextInputLayout
    lateinit var txtUniversidadAsesor: TextInputLayout
    lateinit var edit_Text_name: TextInputEditText
    lateinit var edit_Text_telf: TextInputEditText
    lateinit var edit_Text_uni: TextInputEditText
    lateinit var chk_lunes: CheckBox
    lateinit var chk_martes: CheckBox
    lateinit var chk_miercoles: CheckBox
    lateinit var chk_jueves: CheckBox
    lateinit var chk_viernes: CheckBox
    lateinit var btn_add_Asesor: Button
    lateinit var btn_edit_Asesor: Button
    lateinit var btn_delete_Asesor: Button
    lateinit var Status_activity: String
    var id_actual by Delegates.notNull<Int>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Status_activity = "Visualizar"
        setContentView(R.layout.activity_editar_asesor)
        E_Asesor_ViewModel = ViewModelProvider(this).get(AsistenciaViewModel::class.java)

        // Inicializar las vistas después de setContentView
        autoCompleteTextView = findViewById(R.id.autoCompleteTextView3)
        txtNombreAsesor = findViewById(R.id.rd_edit_nombre_asesor)
        txtTelefonoAsesor = findViewById(R.id.rd_edit_teléfono_asesor)
        txtUniversidadAsesor = findViewById(R.id.rd_edit_univ_asesor)
        edit_Text_name = findViewById(R.id.edit_Text_nombre)
        edit_Text_telf = findViewById(R.id.edit_Text_tel)
        edit_Text_uni = findViewById(R.id.edit_Text_uni)
        chk_lunes = findViewById<CheckBox>(R.id.chk_edit_lunes)
        chk_martes = findViewById<CheckBox>(R.id.chk_edit_martes)
        chk_miercoles = findViewById<CheckBox>(R.id.chk_edit_miercoles)
        chk_jueves = findViewById<CheckBox>(R.id.chk_edit_jueves)
        chk_viernes = findViewById<CheckBox>(R.id.chk_edit_viernes)
        btn_add_Asesor = findViewById<Button>(R.id.btn_a)
        btn_edit_Asesor = findViewById<Button>(R.id.btn_b)
        btn_delete_Asesor = findViewById<Button>(R.id.btn_c)
        
        setupAutoCompleteViews()
        silenceall()

        autoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            val selectedName = autoCompleteTextView.adapter.getItem(position) as String
            GlobalScope.launch(Dispatchers.Main) {
                id_actual = E_Asesor_ViewModel.obtenerIDPorNombre(selectedName, "Profesores")
                val asesor_new = E_Asesor_ViewModel.obtenerAsesorPorNombre(selectedName)
                if (asesor_new != null) {
                    edit_Text_name.setText(asesor_new.nombre)
                    edit_Text_telf.setText(asesor_new.telefono)
                    edit_Text_uni.setText(asesor_new.universidad)



                    val diasSeleccionados = asesor_new.Horario_semana.replace(" ", "").split(",")

                    

                    chk_lunes.isChecked = diasSeleccionados.contains("Lunes")
                    chk_martes.isChecked = diasSeleccionados.contains("Martes")
                    chk_miercoles.isChecked = diasSeleccionados.contains("Miércoles")
                    chk_jueves.isChecked = diasSeleccionados.contains("Jueves")
                    chk_viernes.isChecked = diasSeleccionados.contains("Viernes")


                } else {
                    // Handle case where asesor_new is null
                }
            }
        }


        //val btn_edit_Asesor = findViewById<Button>(R.id.btn_edit_Asesor)
        btn_add_Asesor.setOnClickListener {

            if(Status_activity == "Añadir"){
                confirm_and_add()
            }
            else if(Status_activity == "Editar"){
                confirm_edit()
            }
            else{
                add_btn()
            }

        }

        btn_edit_Asesor.setOnClickListener {
            edit_btn()
        }

        btn_delete_Asesor.setOnClickListener {
            if(Status_activity != "Visualizar"){
                back()
            }
            else{
                mostrarDialogoConfirmacion(autoCompleteTextView.text.toString())
            }
        }

    }
    // Ejecutar la tarea de obtener nombres de asesores en segundo plano
    private fun setupAutoCompleteViews() {
        // Fetch and set adapters for AutoCompleteTextViews using coroutines
        GlobalScope.launch(Dispatchers.Main) {
            val nombresProfesores = E_Asesor_ViewModel.obtenerNombresDeProfesores()

            val adapterProfesores = ArrayAdapter(
                this@EditarAsesorActivity,
                android.R.layout.simple_dropdown_item_1line,
                nombresProfesores
            )
            autoCompleteTextView.setAdapter(adapterProfesores)

        }
    }

    private fun silenceall(){
        edit_Text_name.inputType = InputType.TYPE_NULL
        edit_Text_name.isFocusable = false
        edit_Text_name.isClickable = false
        edit_Text_name.isLongClickable = false

        edit_Text_telf.inputType = InputType.TYPE_NULL
        edit_Text_telf.isFocusable = false
        edit_Text_telf.isClickable = false
        edit_Text_telf.isLongClickable = false

        edit_Text_uni.inputType = InputType.TYPE_NULL
        edit_Text_uni.isFocusable = false
        edit_Text_uni.isClickable = false
        edit_Text_uni.isLongClickable = false
    }

    private fun unmute() {
        edit_Text_name.inputType = InputType.TYPE_TEXT_FLAG_MULTI_LINE or InputType.TYPE_CLASS_TEXT
        edit_Text_name.isFocusable = true
        edit_Text_name.isClickable = true
        edit_Text_name.isLongClickable = true

        edit_Text_telf.inputType = InputType.TYPE_CLASS_PHONE
        edit_Text_telf.isFocusable = true
        edit_Text_telf.isClickable = true
        edit_Text_telf.isLongClickable = true

        edit_Text_uni.inputType = InputType.TYPE_TEXT_FLAG_MULTI_LINE or InputType.TYPE_CLASS_TEXT
        edit_Text_uni.isFocusable = true
        edit_Text_uni.isClickable = true
        edit_Text_uni.isLongClickable = true
    }

    private fun confirm_and_Del(){
        btn_add_Asesor.setText("Confirmar")
        btn_edit_Asesor.visibility = View.INVISIBLE
        btn_delete_Asesor.setText("Cancelar")
    }

    private fun add_btn(){
        unmute()
        Status_activity = "Añadir"
        confirm_and_Del()
        autoCompleteTextView.visibility = View.INVISIBLE
    }

    private fun edit_btn(){
        unmute()

        Status_activity = "Editar"

        confirm_and_Del()

    }

    private fun tomar_Datos_instance():AsesorData{
        val name = edit_Text_name.text.toString()
        val telf = edit_Text_telf.text.toString()
        val uni = edit_Text_uni.text.toString()

        // Crear una lista de CheckBox
        val checkBoxes = listOf(chk_lunes, chk_martes, chk_miercoles, chk_jueves, chk_viernes)

        // Crear una lista para almacenar los días seleccionados
        val diasSeleccionados = mutableListOf<String>()

        // Recorrer la lista de CheckBox y agregar los días seleccionados a la lista
        for (checkBox in checkBoxes) {
            if (checkBox.isChecked) {
                diasSeleccionados.add(checkBox.text.toString())
            }
        }
        val diasSeparadosPorComas = diasSeleccionados.joinToString(", ")

        val new_data = AsesorData(name,telf,diasSeparadosPorComas,uni)

        return(new_data)
    }

    private fun confirm_and_add(){
        GlobalScope.launch(Dispatchers.Main) {
            E_Asesor_ViewModel.agregarAsesor(tomar_Datos_instance())
        }
    }

    private fun confirm_edit(){

        GlobalScope.launch(Dispatchers.Main) {
            E_Asesor_ViewModel.actualizarProfesor(id_actual, tomar_Datos_instance().nombre,tomar_Datos_instance().telefono,tomar_Datos_instance().universidad)
        }

    }

    private fun back(){
        silenceall()
        edit_Text_name.setText("")
        edit_Text_telf.setText("")
        edit_Text_uni.setText("")
        val checkBoxes = listOf(chk_lunes, chk_martes, chk_miercoles, chk_jueves, chk_viernes)
        for (checkBox in checkBoxes) {
            checkBox.isChecked = false
        }
        autoCompleteTextView.visibility = View.VISIBLE

        Status_activity = "Visualizar"

        btn_add_Asesor.setText("Añadir")
        btn_edit_Asesor.visibility = View.VISIBLE
        btn_delete_Asesor.setText("Eliminar")
    }

    private fun mostrarDialogoConfirmacion(nombreAlumno: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirmar borrado")
        builder.setMessage("¿Seguro que deseas borrar este alumno: \n$nombreAlumno?")
        builder.setPositiveButton("Sí") { _, _ ->
            GlobalScope.launch(Dispatchers.Main) {
                E_Asesor_ViewModel.borrarAsesor(id_actual)
            }
        }
        builder.setNegativeButton("No", null)
        builder.show()
    }



}