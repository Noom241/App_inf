package com.example.app_inf.Activities

import android.os.Bundle
import android.view.Gravity
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.app_inf.R
import java.util.Calendar
import java.util.Date

class CalendarioActivity : AppCompatActivity() {
    private val DAYS_OF_WEEK = arrayOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")
    private val YEAR = 2023

    private val fechasPrueba = arrayOf(
        createDate(2023, 1, 29),
        createDate(2023, 1, 10),
        createDate(2023, 1, 15)
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
        val tableLayout = findViewById<TableLayout>(R.id.tl_month)

        // Agregar días de la semana en la primera fila
        val rowDaysOfWeek = TableRow(this)
        for (day in DAYS_OF_WEEK) {
            val textView = createTextView(day)
            rowDaysOfWeek.addView(textView)
        }
        tableLayout.addView(rowDaysOfWeek)

        val calendar = Calendar.getInstance()
        calendar[Calendar.YEAR] = YEAR
        calendar[Calendar.MONTH] = Calendar.JANUARY
        calendar[Calendar.DAY_OF_MONTH] = 1

        // Ajustar el calendario para mostrar días del año anterior si es necesario
        val firstDayOfWeek = calendar[Calendar.DAY_OF_WEEK]
        val daysToAdjust = if (firstDayOfWeek == Calendar.SUNDAY) 6 else firstDayOfWeek - 2
        calendar.add(Calendar.DAY_OF_MONTH, -daysToAdjust)

        while (calendar[Calendar.YEAR] <= YEAR) {
            val rowDate = TableRow(this)
            for (i in 0 until DAYS_OF_WEEK.size) {
                val dayNumber = calendar[Calendar.DAY_OF_MONTH]
                val dayTextView = createTextView(dayNumber.toString())
                rowDate.addView(dayTextView)
                calendar.add(Calendar.DAY_OF_MONTH, 1)
            }
            tableLayout.addView(rowDate)

            val rowAsistio = TableRow(this)
            for (i in 0 until DAYS_OF_WEEK.size) {
                val textView = createTextView("")
                rowAsistio.addView(textView)
            }
            tableLayout.addView(rowAsistio)
        }

        // Marcar la asistencia
        markAttendance(tableLayout)
    }

    private fun createTextView(text: String): TextView {
        val textView = TextView(this)
        textView.text = text
        textView.gravity = Gravity.CENTER
        textView.setBackgroundResource(R.drawable.cell_border)
        return textView
    }

    private fun markAttendance(tableLayout: TableLayout) {
        for (fecha in fechasPrueba) {
            val calendar = Calendar.getInstance()
            calendar.time = fecha
            val weekIndex = calendar.get(Calendar.WEEK_OF_YEAR)
            var dayIndex = calendar.get(Calendar.DAY_OF_WEEK)
            if(dayIndex < 2){
                if(dayIndex == 1){
                    dayIndex = 6

                }
                else{
                    dayIndex = 5
                }
            }
            else{
                var dayIndex = calendar.get(Calendar.DAY_OF_WEEK) - 1
            }

            val rowAsistio = tableLayout.getChildAt(weekIndex * 2 + 2) as TableRow
            val textView = rowAsistio.getChildAt(dayIndex) as TextView
            textView.text = "Asistio"
        }
    }







}
