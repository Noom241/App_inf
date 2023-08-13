package com.example.app_inf.Activities

import MySQLConnection.obtenerAsistenciaDeEstudianteAsync
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.Environment
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest
import com.example.app_inf.R
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class InformeActivity : AppCompatActivity() {
    private var selectedYear = 2023
    private val idEstudiante = 1 // Reemplaza con el ID del estudiante deseado

    private val WRITE_EXTERNAL_STORAGE_PERMISSION_CODE = 123 // Puedes usar cualquier código

    private lateinit var view: View


    private var fechasPrueba = mutableListOf<Pair<Date, Boolean>>()

    private fun agregarNuevaFecha(idEstudiante: Int) {
        val nuevaLista = fechasPrueba.toMutableList()

        obtenerAsistenciaDeEstudianteAsync(idEstudiante
        ) { asistencia ->
            for ((fecha, boolean) in asistencia) {
                val calendar = Calendar.getInstance()
                calendar.time = fecha
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH) + 1
                val day = calendar.get(Calendar.DAY_OF_MONTH)

                val nuevaFecha = createDate(year, month, day)
                nuevaLista.add(Pair(nuevaFecha, boolean))
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
            for ((fecha, boolean) in fechasPrueba) {
                markAttendance(tableLayout, month, boolean, fecha)
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


    override fun onCreate(savedInstanceState: Bundle?) {
        agregarNuevaFecha(idEstudiante)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_informe)

        view = findViewById(R.id.container)


        val container = findViewById<LinearLayout>(R.id.container)


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

        val imageButton_download = findViewById<ImageButton>(R.id.imageButton_download)

        imageButton_download.setOnClickListener {
            generateAndDownloadPDF()
        }

        for (month in Calendar.JANUARY..Calendar.DECEMBER) {
            val monthView = createMonthView(month)
            container.addView(monthView)
        }
    }


    private fun generateAndDownloadPDF() {


        // Verificar si se tienen los permisos de escritura en tiempo de ejecución
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED) {
            // Permiso otorgado, puedes crear y descargar el archivo PDF
            println("Permission granted. Creating and saving PDF.")
            createAndSavePDF()
            generateAndDownloadPNG()
        } else {
            while(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
                // Permiso no otorgado, solicitar permiso en tiempo de ejecución
                println("Permission not granted. Requesting permission.")
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    PackageManager.PERMISSION_GRANTED
                )
            }

        }
    }

    private fun generateAndDownloadPNG() {
        // Verificar si se tienen los permisos de escritura en tiempo de ejecución
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED) {
            // Permiso otorgado, puedes crear y descargar el archivo PNG
            createAndSavePNG()
        } else {
            // Permiso no otorgado, solicitar permiso en tiempo de ejecución
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                WRITE_EXTERNAL_STORAGE_PERMISSION_CODE
            )
        }
    }

    private fun createAndSavePNG() {
        // Capturar la vista container como un bitmap
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)

        // Guardar el bitmap como una imagen PNG en el almacenamiento externo
        val pngFileName = "informeaaaa.png"
        val pngFile = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), pngFileName)

        try {
            val fileOutputStream = FileOutputStream(pngFile)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
            fileOutputStream.close()

            // Mostrar mensaje de éxito o abrir la imagen PNG
            // Por ejemplo, puedes abrir la imagen con una aplicación de visor de imágenes
            // usando una Intención (Intent).
        } catch (e: IOException) {
            // Manejar la excepción, mostrar un mensaje de error, etc.
            e.printStackTrace()
        }
    }




    private fun createAndSavePDF() {
        println("Creating PDF.")

        val pdfFileName = "informe.pdf"
        val pdfFile = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), pdfFileName)

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

            // Mostrar mensaje de éxito o abrir el archivo PDF
            // Por ejemplo, puedes abrir el archivo PDF con un lector de PDFs
            // usando una Intención (Intent).
        } catch (e: IOException) {
            // Manejar la excepción, mostrar un mensaje de error, etc.
            e.printStackTrace()
            println("Error while creating or saving PDF.")
        }
    }


    // Manejar la respuesta de solicitud de permisos
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == WRITE_EXTERNAL_STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso otorgado, puedes crear y descargar el archivo PDF
                println("Permission granted. Creating and saving PDF.")
                createAndSavePDF()
            } else {
                // Permiso no otorgado, maneja esta situación como sea necesario
                // Por ejemplo, muestra un mensaje al usuario
                println("Permission not granted. Cannot create PDF.")
            }
        }
    }



    private fun createBitmap():Bitmap {
        val width = 300
        val height = 300
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint()
        paint.color = Color.BLUE
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
        return bitmap
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

    private fun markAttendance(tableLayout: TableLayout, selectedMonth: Int, boolean: Boolean, fecha: Date) {

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
                currentDate < fecha -> R.color.colorPending // Cambiar al recurso deseado para las clases pendientes
                boolean -> R.color.colorPresent // Cambiar al recurso deseado para la asistencia
                else -> R.color.colorAbsent // Cambiar al recurso deseado para las faltas
            }
            textView.setBackgroundResource(cellBackgroundColor)
        }

    }
}