package com.example.app_inf.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.example.app_inf.R
import com.example.app_inf.ViewModel.AsistenciaViewModel
import com.example.app_inf.data.AlumnoData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.properties.Delegates

class Alumno_interface_v : AppCompatActivity() {
    private lateinit var Alumn_ViewModel: AsistenciaViewModel
    // Declaración de elementos del layout
    private lateinit var editTextNombre: EditText
    private lateinit var editTextTelefono: EditText
    private lateinit var editTextApoderado: EditText
    private lateinit var editTextColegio: EditText

    private lateinit var checkboxLunes: CheckBox
    private lateinit var checkboxMartes: CheckBox
    private lateinit var checkboxMiercoles: CheckBox
    private lateinit var checkboxJueves: CheckBox
    private lateinit var checkboxViernes: CheckBox

    private lateinit var modalidadAdapter: ArrayAdapter<String>
    private lateinit var paqueteAdapter: ArrayAdapter<String>
    private lateinit var horarioAdapter: ArrayAdapter<String>
    private lateinit var modalidadSpinner: Spinner
    private lateinit var horarioSpinner: Spinner
    private lateinit var paqueteSpinner: Spinner

    private lateinit var buttonAgregar: Button
    private lateinit var buttonActualizar: Button
    private lateinit var buttonEliminar: Button
    //private lateinit var buttonRenovar: Button

    private var buttonState = "Preview_alumn" // Variable para almacenar el estado de los botones

    var id_actual by Delegates.notNull<Int>()

    private lateinit var search_alum: AutoCompleteTextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.alumno_activity_v2)

        Alumn_ViewModel = ViewModelProvider(this).get(AsistenciaViewModel::class.java)

        initializeViews()
        //

        //
        setupAutoCompleteViews()
        // Llama al método para configurar los spinners
        setupSpinners()
        //desabilitar
        disble_inputs()
        paqueteSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                enableCheckboxes() // Habilitar checkboxes cuando se selecciona un nuevo paquete
                uncheck()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // No hacer nada cuando no se selecciona nada
            }
        }



        // Ejemplo: Manejar selección en AutoCompleteTextView
        search_alum.setOnItemClickListener { _, _, position, _ ->
            val selectedName = search_alum.adapter.getItem(position) as String

            GlobalScope.launch(Dispatchers.Main) {
                id_actual = Alumn_ViewModel.obtenerIDPorNombre(selectedName, "Estudiantes")
                val estudiante = Alumn_ViewModel.obtenerEstudiantePorNombre(selectedName)
                if (estudiante != null) {
                    editTextNombre.setText(estudiante.nombre)
                    editTextTelefono.setText(estudiante.telefonoApoderado)
                    editTextApoderado.setText(estudiante.apoderado)
                    editTextColegio.setText(estudiante.colegio)

                    val diasSeleccionados = estudiante.horario_semana.replace(" ", "").split(",")

                    checkboxLunes.isChecked = diasSeleccionados.contains("Lunes")
                    checkboxMartes.isChecked = diasSeleccionados.contains("Martes")
                    checkboxMiercoles.isChecked = diasSeleccionados.contains("Miércoles")
                    checkboxJueves.isChecked = diasSeleccionados.contains("Jueves")
                    checkboxViernes.isChecked = diasSeleccionados.contains("Viernes")

                    setSpinnersFromAlumnoData(estudiante)

                } else {
                    // Handle case where estudiante is null
                }
            }
        }

        buttonAgregar.setOnClickListener {
            if (buttonState == "Añadir") {
                GlobalScope.launch(Dispatchers.Main) {
                    // Agregar el nuevo alumno utilizando el ViewModel
                    Alumn_ViewModel.agregarAlumno(createAlumnoDataInstance())
                    id_actual = Alumn_ViewModel.obtenerIDPorNombre(createAlumnoDataInstance().nombre, "Estudiantes")


                    Alumn_ViewModel.agregarFechasAsistenciaSeleccionadas(id_actual, "2",createAlumnoDataInstance().horario_semana)

                    // Limpiar campos y cambiar estado de botones
                    clearFields()
                    search_alum.visibility = View.VISIBLE
                    changeButtonState()
                    disble_inputs()
                }
            }
            if (buttonState == "Actualizar") {

                GlobalScope.launch(Dispatchers.Main) {
                    id_actual = Alumn_ViewModel.obtenerIDPorNombre(search_alum.text.toString(), "Estudiantes")
                    val updatedAlumno = createAlumnoDataInstance()
                    Alumn_ViewModel.actualizarAlumno(id_actual, updatedAlumno)
                    clearFields()
                    changeButtonState()
                    disble_inputs()
                }
            }
            if (buttonState == "Renovar") {

                GlobalScope.launch(Dispatchers.Main) {
                    id_actual = Alumn_ViewModel.obtenerIDPorNombre(search_alum.text.toString(), "Estudiantes")
                    val updatedAlumno = createAlumnoDataInstance()
                    Alumn_ViewModel.actualizarAlumno(id_actual, updatedAlumno)
                    var ultimo_asistio = Alumn_ViewModel.obtenerUltimoAsistioAntesFecha(id_actual)
                        ?.toInt()
                    if (ultimo_asistio != null) {
                        if(ultimo_asistio > 2){
                            ultimo_asistio ++
                        }
                        else{
                            ultimo_asistio = 2
                        }
                    }
                    else{
                        ultimo_asistio = 2
                    }
                    Alumn_ViewModel.agregarFechasAsistenciaSeleccionadas(id_actual, updatedAlumno.horario_semana, ultimo_asistio.toString())
                    // Otras acciones después de actualizar, si es necesario
                    clearFields()
                    changeButtonState()
                    disble_inputs()
                }
            }
            else if(buttonState == "Preview_alumn") {
                // Cambia el estado de los botones
                clearFields()
                changeButtonState()
                enableInputs()
                search_alum.visibility = View.GONE
                buttonState = "Añadir"
            }

        }

        buttonActualizar.setOnClickListener {

            if(buttonState == "Preview_alumn") {
                // Cambia el estado de los botones
                changeButtonState()
                enable_inputs_for_edit()
                buttonState = "Actualizar"
            }


        }
/*
        buttonRenovar.setOnClickListener {

            if(buttonState == "Preview_alumn") {
                // Cambia el estado de los botones
                changeButtonState()
                enableInputs_for_renovar()
                buttonState = "Renovar"
            }


        }*/

        buttonEliminar.setOnClickListener {
            if (buttonState == "Preview_alumn") {
                val selectedName = search_alum.text.toString()
                val dialogBuilder = AlertDialog.Builder(this)
                dialogBuilder.setMessage("¿Deseas borrar al alumno $selectedName?")
                dialogBuilder.setPositiveButton("Sí") { _, _ ->
                    GlobalScope.launch(Dispatchers.Main) {
                        Alumn_ViewModel.borrarAlumno(selectedName)
                        clearFields()
                    }
                }
                dialogBuilder.setNegativeButton("No") { _, _ ->
                    // No hacer nada si se elige "No"
                }
                val alertDialog = dialogBuilder.create()
                alertDialog.show()
            }
            else{
                clearFields()
                changeButtonState()
                search_alum.visibility = View.VISIBLE
                buttonState = "Preview_alumn"
                disble_inputs()

            }


        }
        checkboxLunes.setOnCheckedChangeListener { _, isChecked ->
            onCheckboxChanged(checkboxLunes, isChecked)
        }

        checkboxMartes.setOnCheckedChangeListener { _, isChecked ->
            onCheckboxChanged(checkboxMartes, isChecked)
        }

        checkboxMiercoles.setOnCheckedChangeListener { _, isChecked ->
            onCheckboxChanged(checkboxMiercoles, isChecked)
        }

        checkboxJueves.setOnCheckedChangeListener { _, isChecked ->
            onCheckboxChanged(checkboxJueves, isChecked)
        }

        checkboxViernes.setOnCheckedChangeListener { _, isChecked ->
            onCheckboxChanged(checkboxViernes, isChecked)
        }




    }

    private fun initializeViews() {
        editTextNombre = findViewById(R.id.editTextNombre)
        editTextTelefono = findViewById(R.id.editTextTelefono)
        editTextApoderado = findViewById(R.id.editTextApoderado)
        editTextColegio = findViewById(R.id.editTextColegio)

        checkboxLunes = findViewById(R.id.checkbox_lunes)
        checkboxMartes = findViewById(R.id.checkbox_martes)
        checkboxMiercoles = findViewById(R.id.checkbox_miercoles)
        checkboxJueves = findViewById(R.id.checkbox_jueves)
        checkboxViernes = findViewById(R.id.checkbox_viernes)

        modalidadSpinner = findViewById(R.id.spn_modalidad_alumno_V)
        horarioSpinner = findViewById(R.id.spn_horario_alumno_V)
        paqueteSpinner = findViewById(R.id.spn_paquete_alumno_V)

        buttonAgregar = findViewById(R.id.button_agregar)
        buttonActualizar = findViewById(R.id.button_actualizar)
        buttonEliminar = findViewById(R.id.button_eliminar)
        //buttonRenovar = findViewById(R.id.button_renovar)



        search_alum = findViewById(R.id.search_alum)
    }

    private fun setupSpinners() {
        val modalidadSpinner: Spinner = findViewById(R.id.spn_modalidad_alumno_V)
        val horarioSpinner: Spinner = findViewById(R.id.spn_horario_alumno_V)
        val paqueteSpinner: Spinner = findViewById(R.id.spn_paquete_alumno_V)

        val modalidadItems = arrayOf("Presencial", "Virtual")
        val horarioItems = arrayOf("3:00Pm - 4:30Pm", "4:30Pm - 6:00Pm", "6:00Pm - 7:30Pm")
        val paqueteItems = arrayOf("A", "B", "C")

        modalidadAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, modalidadItems)
        horarioAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, horarioItems)
        paqueteAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, paqueteItems)

        modalidadSpinner.adapter = modalidadAdapter
        horarioSpinner.adapter = horarioAdapter
        paqueteSpinner.adapter = paqueteAdapter
    }

    // Resto de los métodos y declaraciones

    fun disble_inputs(){
        editTextNombre.isEnabled = false
        editTextTelefono.isEnabled = false
        editTextApoderado.isEnabled = false
        editTextColegio.isEnabled = false

        checkboxLunes.isEnabled = false
        checkboxMartes.isEnabled = false
        checkboxMiercoles.isEnabled = false
        checkboxJueves.isEnabled = false
        checkboxViernes.isEnabled = false

        modalidadSpinner.isEnabled = false
        horarioSpinner.isEnabled = false
        paqueteSpinner.isEnabled = false
    }

    private fun enableInputs() {
        // Habilitar elementos
        editTextNombre.isEnabled = true
        editTextTelefono.isEnabled = true
        editTextApoderado.isEnabled = true
        editTextColegio.isEnabled = true

        checkboxLunes.isEnabled = true
        checkboxMartes.isEnabled = true
        checkboxMiercoles.isEnabled = true
        checkboxJueves.isEnabled = true
        checkboxViernes.isEnabled = true

        modalidadSpinner.isEnabled = true
        horarioSpinner.isEnabled = true
        paqueteSpinner.isEnabled = true
    }
    private fun enableInputs_for_renovar() {
        // Habilitar elementos
        checkboxLunes.isEnabled = true
        checkboxMartes.isEnabled = true
        checkboxMiercoles.isEnabled = true
        checkboxJueves.isEnabled = true
        checkboxViernes.isEnabled = true

        modalidadSpinner.isEnabled = true
        horarioSpinner.isEnabled = true
        paqueteSpinner.isEnabled = true
    }
    private fun enable_inputs_for_edit() {
        // Habilitar elementos
        editTextNombre.isEnabled = true
        editTextTelefono.isEnabled = true
        editTextApoderado.isEnabled = true
        editTextColegio.isEnabled = true

    }

    private fun clearFields() {
        // Limpiar campos de texto
        editTextNombre.text.clear()
        editTextTelefono.text.clear()
        editTextApoderado.text.clear()
        editTextColegio.text.clear()
        search_alum.text.clear()

        uncheck()
    }

    private fun uncheck(){
        // Desmarcar todos los checkboxes
        checkboxLunes.isChecked = false
        checkboxMartes.isChecked = false
        checkboxMiercoles.isChecked = false
        checkboxJueves.isChecked = false
        checkboxViernes.isChecked = false
    }


    private fun setupAutoCompleteViews() {
        // Fetch and set adapters for AutoCompleteTextViews using coroutines
        GlobalScope.launch(Dispatchers.Main) {
            val nombresProfesores = Alumn_ViewModel.obtenerNombresDeAlumnos()

            val adapter_alum = ArrayAdapter(
                this@Alumno_interface_v,
                android.R.layout.simple_dropdown_item_1line,
                nombresProfesores
            )
            search_alum.setAdapter(adapter_alum)

        }
    }

    private fun setSpinnersFromAlumnoData(alumnoData: AlumnoData) {
        val modalidadIndex = modalidadAdapter.getPosition(alumnoData.modalidad)
        val paqueteIndex = paqueteAdapter.getPosition(alumnoData.paquete_elegido)
        val horarioOptions: List<String> = listOf("3:00Pm - 4:30Pm", "4:30Pm - 6:00Pm", "6:00Pm - 7:30Pm")
        val horarioString: String = alumnoData.horario.toString() // Supongamos que este es el valor seleccionado
        val horarioInt: Int = horarioOptions.indexOf(horarioString)



        val horarioIndex = horarioInt

        modalidadSpinner.setSelection(modalidadIndex)
        paqueteSpinner.setSelection(paqueteIndex)
        horarioSpinner.setSelection(horarioIndex)


        enableCheckboxes() // Habilitar todos los checkboxes
    }

    private fun changeButtonState() {
        when (buttonState) {
            "Preview_alumn" -> {
                // Cambia los textos y visibilidad de los botones
                disble_inputs()
                buttonAgregar.text = "Confirmar"
                buttonActualizar.visibility = View.GONE
                //buttonRenovar.visibility = View.GONE
                buttonEliminar.text = "Cancelar"

            }
            else -> {
                buttonAgregar.text = "Añadir"
                buttonActualizar.visibility = View.VISIBLE
                //buttonRenovar.visibility = View.VISIBLE
                buttonEliminar.text = "Eliminar"
                buttonState = "Preview_alumn"
            }
        }
    }

    private fun createAlumnoDataInstance(): AlumnoData {
        val nombre = editTextNombre.text.toString()
        val apoderado = editTextApoderado.text.toString()
        val telefonoApoderado = editTextTelefono.text.toString()
        val colegio = editTextColegio.text.toString()

        val modalidad = modalidadSpinner.selectedItem.toString()
        val horario = horarioSpinner.selectedItem.toString()
        val horarioIndex = horarioAdapter.getPosition(horario)
        val horarioInt = horarioIndex + 1

        val paqueteElegido = paqueteSpinner.selectedItem.toString()

        val checkBoxes = listOf(checkboxLunes, checkboxMartes, checkboxMiercoles, checkboxJueves, checkboxViernes)

        val diasSeleccionados = mutableListOf<String>()

        for (checkBox in checkBoxes) {
            if (checkBox.isChecked) {
                diasSeleccionados.add(checkBox.text.toString())
            }
        }
        val diasSeparadosPorComas = diasSeleccionados.joinToString(", ")

        return AlumnoData(nombre, apoderado, telefonoApoderado, colegio, modalidad, horarioInt, paqueteElegido, diasSeparadosPorComas)
    }

    private fun enableCheckboxes() {
        if (buttonState != "Preview_alumn"){

            checkboxLunes.isEnabled = true
            checkboxMartes.isEnabled = true
            checkboxMiercoles.isEnabled = true
            checkboxJueves.isEnabled = true
            checkboxViernes.isEnabled = true
        }
    }

    private fun onCheckboxChanged(checkbox: CheckBox, isChecked: Boolean) {
        if (isChecked) {
            println("${checkbox.text} ha sido activado")
            val selectedPackage = paqueteSpinner.selectedItem.toString()
            val checkedCount = countCheckedCheckboxes()
            when (selectedPackage) {
                "A" -> {
                    if (checkedCount == 2) {
                        disableUncheckedCheckboxes()

                    }
                }
                "B" -> {
                    if (checkedCount == 3) {
                        disableUncheckedCheckboxes()
                    }

                }
                "C" -> {
                    if (checkedCount == 4) {
                        disableUncheckedCheckboxes()
                    }
                }
            }

        } else {
            println("${checkbox.text} ha sido desactivado")
            enableCheckboxes()

        }

    }

    private fun countCheckedCheckboxes(): Int {
        var checkedCount = 0
        val checkboxes = listOf(
            checkboxLunes,
            checkboxMartes,
            checkboxMiercoles,
            checkboxJueves,
            checkboxViernes
        )

        for (checkbox in checkboxes) {
            if (checkbox.isChecked) {
                checkedCount++
            }
        }

        return checkedCount
    }

    private fun disableUncheckedCheckboxes() {
        val checkboxes = listOf(
            checkboxLunes,
            checkboxMartes,
            checkboxMiercoles,
            checkboxJueves,
            checkboxViernes
        )

        for (checkbox in checkboxes) {
            if (!checkbox.isChecked) {
                checkbox.isEnabled = false
            }
        }
    }

    fun generateDatesForNext4Weeks_(Int:Int, input: String, string: String) {
        val stringSinEspacios = input.replace(" ", "")
        val arrayDias = stringSinEspacios.split(",")

        val today = Calendar.getInstance()

        for (dia in arrayDias) {
            val dayOfWeek = when (dia.toLowerCase()) {
                "lunes" -> Calendar.MONDAY
                "martes" -> Calendar.TUESDAY
                "miércoles" -> Calendar.WEDNESDAY
                "jueves" -> Calendar.THURSDAY
                "viernes" -> Calendar.FRIDAY
                "sábado" -> Calendar.SATURDAY
                "domingo" -> Calendar.SUNDAY
                else -> throw IllegalArgumentException("Día desconocido: $dia")
            }

            val nextOccurrence = Calendar.getInstance()
            nextOccurrence.time = today.time
            nextOccurrence.add(Calendar.DAY_OF_WEEK, (dayOfWeek + 7 - today.get(Calendar.DAY_OF_WEEK)) % 7)

            val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            println("Próximas fechas de $dia:")
            for (i in 0..3) {
                if (i > 0) {
                    nextOccurrence.add(Calendar.DAY_OF_WEEK, 7)
                }
                GlobalScope.launch(Dispatchers.Main) {
                    Alumn_ViewModel.registrarAsistencia(Int, 0, nextOccurrence.time, string)
                }
                println(dateFormatter.format(nextOccurrence.time))
            }
        }
    }




    fun generateDatesForNext4Weeks(input: String, idEstudiante: Int, asistio: String) {
        val stringSinEspacios = input.replace(" ", "")
        val arrayDias = stringSinEspacios.split(",")

        val today = Calendar.getInstance()

        for (dia in arrayDias) {
            val dayOfWeek = when (dia.toLowerCase()) {
                "lunes" -> Calendar.MONDAY
                "martes" -> Calendar.TUESDAY
                "miercoles" -> Calendar.WEDNESDAY
                "jueves" -> Calendar.THURSDAY
                "viernes" -> Calendar.FRIDAY
                "sábado" -> Calendar.SATURDAY
                "domingo" -> Calendar.SUNDAY
                else -> throw IllegalArgumentException("Día desconocido: $dia")
            }

            val nextOccurrence = Calendar.getInstance()
            nextOccurrence.time = today.time
            nextOccurrence.add(Calendar.DAY_OF_WEEK, (dayOfWeek + 7 - today.get(Calendar.DAY_OF_WEEK)) % 7)

            val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            println("Próximas fechas de $dia:")
            for (i in 0..3) {
                if (i > 0) {
                    nextOccurrence.add(Calendar.DAY_OF_WEEK, 7)
                }
                val fecha = nextOccurrence.time
                println(dateFormatter.format(fecha))
                GlobalScope.launch(Dispatchers.Main) {
                    Alumn_ViewModel.registrarAsistencia(idEstudiante, 6, fecha, asistio)
                }

            }
        }
    }

    private fun createDate(year: Int, month: Int, day: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        return calendar.time
    }




}