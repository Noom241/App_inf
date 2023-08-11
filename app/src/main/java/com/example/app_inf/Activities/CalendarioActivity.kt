package com.example.app_inf.Activities


import MySQLConnection
import MySQLConnection.obtenerAsistenciaDeEstudianteAsync
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.app_inf.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import android.os.AsyncTask


class CalendarioActivity : AppCompatActivity() {
    private var selectedYear = 2023
    val idEstudiante = 1 // Reemplaza con el ID del estudiante deseado


    private var fechasPrueba = mutableListOf<Pair<Date, Boolean>>()
    private fun agregarNuevaFecha(idEstudiante: Int) {
        val nuevaLista = fechasPrueba.toMutableList()

        obtenerAsistenciaDeEstudianteAsync(idEstudiante, object : MySQLConnection.OnAsistenciaObtenidaListener {
            override fun onAsistenciaObtenida(asistencia: List<Pair<Date, Boolean>>) {
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
        })
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
        setContentView(R.layout.activity_calendario)

        val container = findViewById<LinearLayout>(R.id.container)

        for (month in Calendar.JANUARY..Calendar.DECEMBER) {
            val monthView = createMonthView(month)
            container.addView(monthView)
        }
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
            if(isFirstDaySunday){
                weekIndex++
            }

            if(dayIndex < 0) {
                weekIndex -= 1

                if (dayIndex == -1) {
                    dayIndex = 6
                }
                if(dayIndex == - 2){
                    dayIndex = 5
                }

            }
            

            val rowAsistio = tableLayout.getChildAt(weekIndex * 2 ) as TableRow
            val textView = rowAsistio.getChildAt(dayIndex) as TextView
            when {
                currentDate < fecha -> {
                    textView.text = "clase"
                }
                boolean -> {
                    textView.text = "Asistio"
                }
                else -> {
                    textView.text = "Falta"
                }
            }
        }
    }



}
//////////