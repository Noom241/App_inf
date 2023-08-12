package com.example.app_inf.Activities


import MySQLConnection.obtenerAsistenciaDeEstudianteAsync
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


        val container = findViewById<LinearLayout>(R.id.container)
        val view = layoutInflater.inflate(R.layout.activity_informe, null)

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

        //fin id
        imageButton_download.setOnClickListener {
            generateAndDownloadPDF  ()
        }

        for (month in Calendar.JANUARY..Calendar.DECEMBER) {
            val monthView = createMonthView(month)
            container.addView(monthView)
        }

    }

    private fun generateAndDownloadPDF() {
        // Crear un bitmap como se explicó anteriormente
        val bitmap = createBitmap()

        // Crear un documento PDF
        val pdfDocument = PdfDocument()

        // Crear una página en el documento
        val pageInfo = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, 1).create()
        val page = pdfDocument.startPage(pageInfo)

        // Dibujar el bitmap en la página
        val canvas = page.canvas
        canvas.drawBitmap(bitmap, 0f, 0f, null)

        // Finalizar la página
        pdfDocument.finishPage(page)

        // Guardar el archivo PDF en el almacenamiento externo
        val pdfFileName = "informe.pdf"
        val pdfFile = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), pdfFileName)

        try {
            val fileOutputStream = FileOutputStream(pdfFile)
            pdfDocument.writeTo(fileOutputStream)
            fileOutputStream.close()
            pdfDocument.close()

            // Mostrar mensaje de éxito o abrir el archivo PDF
            // Por ejemplo, puedes abrir el archivo PDF con un lector de PDFs
            // usando una Intención (Intent).
        } catch (e: IOException) {
            // Manejar la excepción, mostrar un mensaje de error, etc.
            e.printStackTrace()
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