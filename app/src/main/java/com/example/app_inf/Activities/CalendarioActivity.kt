package com.example.app_inf.Activities

import com.example.app_inf.R
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class CalendarioActivity : AppCompatActivity() {

    private var selectedYear = 2023
    private var selectedMonth = Calendar.DECEMBER
    private val fechasPrueba = arrayOf(
        createDate(2023, 12, 17),
        createDate(2023, 12, 18),
        createDate(2023, 12, 15)
        // Agrega más fechas aquí según necesites
    )
    private fun createDate(year: Int, month: Int, day: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, day) // Mes se cuenta desde 0 (enero)
        return calendar.time
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendario)
        val monthTextView = findViewById<TextView>(R.id.monthTextView)
        val tableLayout = findViewById<TableLayout>(R.id.tableLayout)
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, selectedYear)
        calendar.set(Calendar.MONTH, selectedMonth)
        calendar.set(Calendar.DAY_OF_MONTH, 1)


        val monthName = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(calendar.time)
        monthTextView.text = monthName

        val diasSemana = arrayOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")
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
        for (i in 1 until primerDiaSemana) {
            val emptyCell = createTextView(" ")
            fila.addView(emptyCell)

            val pCell = createTextView(" ")
            pRow.addView(pCell)
        }

        while (calendar.get(Calendar.MONTH) == selectedMonth) {
            val textView = TextView(this)
            fila.addView(createTextView(dia.toString()))

            val pCell = TextView(this)
            pRow.addView(createTextView(""))

            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) { // Cambia Calendar.SATURDAY a Calendar.SUNDAY
                tableLayout.addView(fila)
                tableLayout.addView(pRow)
                fila = TableRow(this)
                pRow = TableRow(this)
            }

            calendar.add(Calendar.DAY_OF_MONTH, 1)
            dia++
        }

        // Llenar las celdas vacías restantes en la última fila
        while (fila.childCount < 7) {
            val emptyCell = TextView(this)

            fila.addView(createTextView(""))

            val pCell = TextView(this)
            pRow.addView(createTextView(""))
        }

        if (fila.childCount > 0) {
            tableLayout.addView(fila)
            tableLayout.addView(pRow)
        }
        markAttendance(tableLayout, selectedMonth)
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


    // Método para cambiar el mes seleccionado
    private fun changeSelectedMonth(newMonth: Int) {
        selectedMonth = newMonth
        recreate() // Vuelve a crear la actividad para actualizar el calendario
    }

    private fun markAttendance(tableLayout: TableLayout, selectedMonth: Int) {
        val calendar = Calendar.getInstance()

        for (fecha in fechasPrueba) {
            calendar.time = fecha

            if (calendar.get(Calendar.MONTH) == selectedMonth) {
                var weekIndex = calendar.get(Calendar.WEEK_OF_MONTH)
                var dayIndex = calendar.get(Calendar.DAY_OF_WEEK) - 2

                if(dayIndex < 0) {
                    weekIndex -= 1

                    if (dayIndex == -1) {
                        dayIndex = 6
                    }
                }
                
                val rowAsistio = tableLayout.getChildAt(weekIndex * 2) as TableRow
                val textView = rowAsistio.getChildAt(dayIndex) as TextView
                textView.text = "Asistio"

            }
        }
    }



}
//////////