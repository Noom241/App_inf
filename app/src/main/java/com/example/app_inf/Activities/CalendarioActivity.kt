package com.example.app_inf.Activities

import android.os.Bundle
import android.view.Gravity
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.app_inf.R
import java.util.Calendar

class CalendarioActivity : AppCompatActivity() {
    private val DAYS_OF_WEEK = arrayOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")
    private val YEAR = 2023

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
            val rowDate1 = TableRow(this)
            val rowDate2 = TableRow(this)
            for (i in 0 until DAYS_OF_WEEK.size) {
                val dayNumber = calendar[Calendar.DAY_OF_MONTH]
                val dayTextView1 = createTextView(dayNumber.toString())
                val dayTextView2 = createTextView("---")
                rowDate1.addView(dayTextView1)
                rowDate2.addView(dayTextView2)
                calendar.add(Calendar.DAY_OF_MONTH, 1)
            }
            tableLayout.addView(rowDate1)
            tableLayout.addView(rowDate2)
        }
    }

    private fun createTextView(text: String): TextView {
        val textView = TextView(this)
        textView.text = text
        textView.gravity = Gravity.CENTER
        textView.setBackgroundResource(R.drawable.cell_border)
        return textView
    }
}
