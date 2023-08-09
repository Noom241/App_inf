package com.example.app_inf.Activities

import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.app_inf.R
import java.util.Calendar
import java.util.Date
import java.util.Locale

class CalendarioActivity : AppCompatActivity() {
    private val DAYS_OF_WEEK = arrayOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")
    private val YEAR = 2023

    private val fechasPrueba = arrayOf(
        createDate(2023, 2, 1),
        createDate(2023, 3, 1),
        createDate(2023, 3, 5),
        createDate(2023, 3, 12)
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

        val calendar = Calendar.getInstance()
        calendar[Calendar.YEAR] = YEAR
        calendar[Calendar.MONTH] = Calendar.JANUARY
        calendar[Calendar.DAY_OF_MONTH] = 1

        // Ajustar el calendario para mostrar días del año anterior si es necesario
        val firstDayOfWeek = calendar[Calendar.DAY_OF_WEEK]
        val daysToAdjust = if (firstDayOfWeek == Calendar.SUNDAY) 6 else firstDayOfWeek - 2
        calendar.add(Calendar.DAY_OF_MONTH, -daysToAdjust)

        var previousMonth = -1

        while (calendar[Calendar.YEAR] <= YEAR) {
            val currentMonth = calendar[Calendar.MONTH]
            if (previousMonth != currentMonth) {
                tableLayout.addView(createMonthHeader(calendar))
                val rowDaysOfWeek = TableRow(this)
                for (day in DAYS_OF_WEEK) {
                    val textView = createTextView(day)
                    rowDaysOfWeek.addView(textView)
                }
                tableLayout.addView(rowDaysOfWeek)
                previousMonth = currentMonth
            }

            val rowDate = TableRow(this)
            val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
            for (i in 0 until DAYS_OF_WEEK.size) {
                if (calendar.get(Calendar.DAY_OF_MONTH) <= daysInMonth) {
                    val dayNumber = calendar[Calendar.DAY_OF_MONTH]
                    val dayTextView = createTextView(dayNumber.toString())
                    rowDate.addView(dayTextView)
                } else {
                    val emptyTextView = createTextView("")
                    rowDate.addView(emptyTextView)
                }
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



    private fun createMonthHeader(calendar: Calendar): TableRow {
        val monthHeaderRow = TableRow(this)
        val monthHeaderTextView = createTextView(calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()))
        monthHeaderTextView.gravity = Gravity.CENTER
        monthHeaderTextView.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_200))
        monthHeaderTextView.setTextColor(Color.WHITE)
        monthHeaderTextView.layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
        monthHeaderRow.addView(monthHeaderTextView)
        return monthHeaderRow
    }


    private fun createTextView(text: String): TextView {
        val textView = TextView(this)
        textView.text = text
        textView.gravity = Gravity.CENTER
        textView.setBackgroundResource(R.drawable.cell_border)

        // Ajustar propiedades aquí
        val paddingValue = resources.getDimensionPixelSize(R.dimen.text_view_padding)
        textView.setPadding(paddingValue, paddingValue, paddingValue, paddingValue)

        val layoutParams = TableRow.LayoutParams(250, TableRow.LayoutParams.WRAP_CONTENT, 1f)


        val marginValue = resources.getDimensionPixelSize(R.dimen.text_view_margin)
        layoutParams.setMargins(marginValue, marginValue, marginValue, marginValue)

        val fontSize = resources.getDimensionPixelSize(R.dimen.text_view_font_size).toFloat()
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize)

        textView.layoutParams = layoutParams

        return textView
    }


    private fun markAttendance(tableLayout: TableLayout) {
        for (fecha in fechasPrueba) {
            val calendar = Calendar.getInstance()
            calendar.time = fecha
            val weekIndex = calendar.get(Calendar.WEEK_OF_YEAR) - 1
            val dayIndex = calendar.get(Calendar.DAY_OF_WEEK) - 2

            val rowAsistio = tableLayout.getChildAt((weekIndex + 2) * 3 + 2) as? TableRow

            if (rowAsistio != null && dayIndex >= 0 && dayIndex < rowAsistio.childCount) {
                val textView = rowAsistio.getChildAt(dayIndex) as? TextView
                textView?.text = "Asistio"
            }
        }
    }







    private fun firstDayOfMonthOffset(calendar: Calendar): Int {
        val firstDayOfWeek = calendar[Calendar.DAY_OF_WEEK]
        return if (firstDayOfWeek == Calendar.SUNDAY) 6 else firstDayOfWeek - 2
    }



}