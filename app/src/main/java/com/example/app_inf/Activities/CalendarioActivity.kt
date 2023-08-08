package com.example.app_inf.Activities

import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.app_inf.R

class CalendarioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendario)

        val tableLayout: TableLayout = findViewById(R.id.tl_mounth)

        // Agregar los días de la semana a la primera columna
        val daysOfWeek = arrayOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")
        val rowDays = TableRow(this)
        for (day in daysOfWeek) {
            val textView = TextView(this)
            textView.text = day
            textView.gravity = Gravity.CENTER
            rowDays.addView(textView)
        }
        tableLayout.addView(rowDays)

        // Agregar las columnas pares con números
        var startNumber = 26
        for (columnIndex in 0 until 107) {
            val rowTableLayout = layoutInflater.inflate(R.layout.row_table, null) as TableRow
            val isEvenColumn = columnIndex % 2 == 0

            // Si la columna es par, agregar números
            if (isEvenColumn) {
                for (i in 0 until 7) {
                    val textView = TextView(this)
                    val dayNumber = if (columnIndex == 2 && i == 0) {
                        // Special case for the first day in column 2
                        startNumber++
                    } else {
                        (startNumber++).takeIf { it <= 31 } ?: 1
                    }
                    textView.text = dayNumber.toString()
                    textView.gravity = Gravity.CENTER
                    rowTableLayout.addView(textView)
                }
            }

            // Agregar la fila a la columna
            val params = TableLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            rowTableLayout.layoutParams = params
            tableLayout.addView(rowTableLayout)
        }
    }
}
