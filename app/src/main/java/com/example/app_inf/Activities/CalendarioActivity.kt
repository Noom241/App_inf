package com.example.app_inf.Activities

import android.content.pm.PackageManager
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.Environment
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.widget.Toast
import com.example.app_inf.R
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.lifecycle.ViewModelProvider
import com.example.app_inf.ViewModel.AsistenciaViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CalendarioActivity : AppCompatActivity() {
    private lateinit var Calendar_ViewModel: AsistenciaViewModel
    private var selectedYear = 2023
    private lateinit var search_alum_: AutoCompleteTextView
    private lateinit var but_informes: ImageButton
    private val WRITE_EXTERNAL_STORAGE_PERMISSION_CODE = 123 // Puedes usar cualquier código
    private lateinit var view: View
    private var nombre_est: String = "Carlos"
    private lateinit var alumno_Seleccionado: TextView

    private var fechasPrueba = mutableListOf<Pair<Date, String>>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendario)
        search_alum_ = findViewById<AutoCompleteTextView>(R.id.search_alum_)
        but_informes = findViewById<ImageButton>(R.id.but_informes)
        alumno_Seleccionado = findViewById<TextView>(R.id.papulince)
        setupAutoCompleteViews()
        Calendar_ViewModel = ViewModelProvider(this).get(AsistenciaViewModel::class.java)

        val container = findViewById<LinearLayout>(R.id.container)
        view = findViewById(R.id.padre)

        for (month in Calendar.JANUARY..Calendar.DECEMBER) {
            val monthView = createMonthView(month)
            container.addView(monthView)
        }

        search_alum_.setOnItemClickListener { _, _, position, _ ->
            val selectedName = search_alum_.adapter.getItem(position) as String
            nombre_est = selectedName
            alumno_Seleccionado.text = nombre_est



            GlobalScope.launch(Dispatchers.Main) {
                val alum_id = Calendar_ViewModel.obtenerIDPorNombre(selectedName, "Estudiantes")
                agregarNuevaFecha(alum_id)
            }
        }

        but_informes.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                generateAndDownloadPDF()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    WRITE_EXTERNAL_STORAGE_PERMISSION_CODE
                )
            }
        }

        val width = view.width
        val height = view.height
        try {
            // Obtener el valor del ancho (por ejemplo, desde una vista)
            val width = view.width

            // Obtener el valor del alto (por ejemplo, desde una vista)
            val height = view.height

            if (width > 0 && height > 0) {
                val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(bitmap)
                view.draw(canvas)
                // Resto del código relacionado con el uso del bitmap
                // Por ejemplo, puedes dibujar en el bitmap, aplicar efectos, etc.
            } else {
                // Manejar el caso cuando width o height no sean válidos
                // Por ejemplo, mostrar un mensaje de error
            }
        } catch (e: IllegalArgumentException) {
            // Manejar la excepción, mostrar un mensaje de error, etc.
            e.printStackTrace()
        }


    }

    private fun generateAndDownloadPDF() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            val context = this
            createAndSavePDF(view, context)
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                WRITE_EXTERNAL_STORAGE_PERMISSION_CODE
            )
        }
    }

    private fun setupAutoCompleteViews() {
        // Fetch and set adapters for AutoCompleteTextViews using coroutines
        GlobalScope.launch(Dispatchers.Main) {
            val nombresProfesores = Calendar_ViewModel.obtenerNombresDeAlumnos()

            val adapter_alum = ArrayAdapter(
                this@CalendarioActivity,
                android.R.layout.simple_dropdown_item_1line,
                nombresProfesores
            )
            search_alum_.setAdapter(adapter_alum)

        }
    }

    private fun agregarNuevaFecha(idEstudiante: Int) {
        val nuevaLista = fechasPrueba.toMutableList()

        GlobalScope.launch(Dispatchers.Main) {
            for ((fecha, string) in Calendar_ViewModel.obtenerAsistenciaDeEstudiante(idEstudiante)) { // Cambio en el nombre de la variable boolean a string
                val calendar = Calendar.getInstance()
                calendar.time = fecha
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH) + 1
                val day = calendar.get(Calendar.DAY_OF_MONTH)

                val nuevaFecha = createDate(year, month, day)
                nuevaLista.add(Pair(nuevaFecha, string))
            }

            fechasPrueba = nuevaLista // Actualizar la lista con las fechas obtenidas
            imprimirFechasPrueba()

            // Llamar a markAttendance aquí después de que la lista se haya actualizado
            markAttendanceForAllMonths()
        }

    }


    private fun markAttendanceForAllMonths() {
        val container = findViewById<LinearLayout>(R.id.container)
        for (month in Calendar.JANUARY..Calendar.DECEMBER) {
            val monthView = container.getChildAt(month) as LinearLayout
            val tableLayout = monthView.findViewById<TableLayout>(R.id.tableLayout)
            for ((fecha, string) in fechasPrueba) {
                markAttendance(tableLayout, month, string, fecha)
            }
        }
    }




    private fun imprimirFechasPrueba() {
        println("Contenido de fechasPrueba:")
        for ((fecha, asistio) in fechasPrueba) {
            println("Fecha: $fecha, Asistió: $asistio")
        }
    }





    private fun createDate(year: Int, month: Int, day: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, day) //
        return calendar.time
    }



    private fun createMonthView(month: Int): View {

        val monthView = layoutInflater.inflate(R.layout.month_view, null) as LinearLayout
        val monthTextView = monthView.findViewById<TextView>(R.id.monthTextView)
        val tableLayout = monthView.findViewById<TableLayout>(R.id.tableLayout)
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, selectedYear)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, 1)


        val monthName = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(calendar.time)
        monthTextView.text = monthName

        val diasSemana =
            arrayOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")
        val primerDiaSemana = calendar.get(Calendar.DAY_OF_WEEK) - 1

        var dia = 1
        var fila = TableRow(this)

        val columnWidth = resources.getDimensionPixelSize(R.dimen.cell_width)

        for (i in diasSemana.indices) {
            val textView = createTextView(diasSemana[i])
            fila.addView(textView)
        }
        tableLayout.addView(fila)

        fila = TableRow(this)
        var pRow = TableRow(this)

        // Llenar las celdas vacías y las celdas "P" hasta el primer día de la semana
        if(primerDiaSemana == 0){
            for (i in 1 until 7) {
                val emptyCell = createTextView(" ")
                fila.addView(emptyCell)

                val pCell = createTextView(" ")
                pRow.addView(pCell)
            }
        }
        else{
            for (i in 1 until primerDiaSemana) {
                val emptyCell = createTextView(" ")
                fila.addView(emptyCell)

                val pCell = createTextView(" ")
                pRow.addView(pCell)
            }
        }


        while (calendar.get(Calendar.MONTH) == month) {
            val textView = TextView(this)
            fila.addView(createTextView(dia.toString()))

            val pCell = TextView(this)
            pRow.addView(createTextView(""))

            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY && dia != calendar.getActualMaximum(Calendar.DAY_OF_MONTH)) { // Cambia Calendar.SATURDAY a Calendar.SUNDAY
                tableLayout.addView(fila)
                tableLayout.addView(pRow)
                fila = TableRow(this)
                pRow = TableRow(this)
            }

            calendar.add(Calendar.DAY_OF_MONTH, 1)
            dia++
        }

        // Llenar las celdas vacías restantes en la última fila
        while (fila.childCount < 7 ) {
            val emptyCell = TextView(this)

            fila.addView(createTextView(""))

            val pCell = TextView(this)
            pRow.addView(createTextView(""))
        }

        if (fila.childCount > 0) {
            tableLayout.addView(fila)
            tableLayout.addView(pRow)
        }

        for ((fecha, boolean) in fechasPrueba) {
            markAttendance(tableLayout, month, boolean, fecha)
        }

        return monthView
    }


    private fun createTextView(text: String): TextView {
        val textView = TextView(this)
        textView.text = text
        textView.gravity = Gravity.CENTER
        textView.setBackgroundResource(R.drawable.cell_border)

        // Ajustar propiedades aquí
        val paddingValue = resources.getDimensionPixelSize(R.dimen.text_view_padding)
        textView.setPadding(paddingValue, paddingValue, paddingValue, paddingValue)

        val layoutParams = TableRow.LayoutParams(220, TableRow.LayoutParams.WRAP_CONTENT, 1f)


        val marginValue = resources.getDimensionPixelSize(R.dimen.text_view_margin)
        layoutParams.setMargins(marginValue, marginValue, marginValue, marginValue)

        val fontSize = resources.getDimensionPixelSize(R.dimen.text_view_font_size).toFloat()
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize)

        textView.layoutParams = layoutParams

        return textView
    }




    private fun markAttendance(tableLayout: TableLayout, selectedMonth: Int, string: String, fecha: Date) {

        val calendar = Calendar.getInstance()
        val currentDate = Calendar.getInstance().time
        calendar.time = fecha

        val firstDayOfMonth = Calendar.getInstance()
        firstDayOfMonth.time = fecha
        firstDayOfMonth.set(Calendar.DAY_OF_MONTH, 1)
        val isFirstDaySunday = firstDayOfMonth.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY



        println(fecha)
        if (calendar.get(Calendar.MONTH) == selectedMonth) {
            var weekIndex = calendar.get(Calendar.WEEK_OF_MONTH)
            var dayIndex = calendar.get(Calendar.DAY_OF_WEEK) - 2
            if (isFirstDaySunday) {
                weekIndex++
            }

            if (dayIndex < 0) {
                weekIndex -= 1

                if (dayIndex == -1) {
                    dayIndex = 6
                }
                if (dayIndex == -2) {
                    dayIndex = 5
                }

            }


            val rowAsistio = tableLayout.getChildAt(weekIndex * 2) as TableRow
            val textView = rowAsistio.getChildAt(dayIndex) as TextView
            val cellBackgroundColor = when {
                currentDate < fecha -> R.color.colorPending
                string == "1" -> R.color.colorPresent
                string == "R" -> R.color.colorRecuperation
                string == "2" -> R.color.purple_200
                string == "3" -> R.color.teal_200
                else -> R.color.colorAbsent
            }
            textView.setBackgroundResource(cellBackgroundColor)
        }

    }


    private fun generateAndDownloadPNG() {
        // Verificar si se tienen los permisos de escritura en tiempo de ejecución
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED) {
            // Permiso otorgado, puedes crear y descargar el archivo PNG
            val context = this
            createAndSavePDF(view, context)
        } else {
            // Permiso no otorgado, solicitar permiso en tiempo de ejecución
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                WRITE_EXTERNAL_STORAGE_PERMISSION_CODE
            )
        }
    }

    private fun createAndSavePDF(view: View, context: Context) {
        println("Creating PDF.")

        val pdfFileName = "$nombre_est.pdf"
        val folderName = "informes"

        // Obtener la ruta de la carpeta "Descargas/informes" en el almacenamiento
        val downloadsDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val storageDirectory = File(downloadsDirectory, folderName)
        if (!storageDirectory.exists()) {
            storageDirectory.mkdirs()
        }

        val pdfFile = File(storageDirectory, pdfFileName)

        try {
            val fileOutputStream = FileOutputStream(pdfFile)
            val pdfDocument = PdfDocument()

            val pageInfo = PdfDocument.PageInfo.Builder(view.width, view.height, 1).create()
            val page = pdfDocument.startPage(pageInfo)

            val canvas = page.canvas
            view.draw(canvas)

            pdfDocument.finishPage(page)
            pdfDocument.writeTo(fileOutputStream)
            fileOutputStream.close()
            pdfDocument.close()

            println("PDF created and saved successfully.")

            // Mostrar mensaje de éxito usando Toast
            Toast.makeText(context, "PDF creado y guardado exitosamente.", Toast.LENGTH_SHORT).show()

            // Mostrar mensaje de éxito o abrir el archivo PDF
            // Por ejemplo, puedes abrir el archivo PDF con un lector de PDFs
            // usando una Intención (Intent).
        } catch (e: IOException) {
            // Manejar la excepción, mostrar un mensaje de error, etc.
            e.printStackTrace()
            println("Error while creating or saving PDF.")

            // Mostrar mensaje de error usando Toast
            Toast.makeText(context, "Error al crear o guardar el PDF.", Toast.LENGTH_SHORT).show()
        }
    }
}