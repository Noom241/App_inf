package com.example.app_inf.Activities

import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.app_inf.R
import java.util.Calendar

class CalendarioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendario)

        val tableLayout: TableLayout = findViewById(R.id.tl_mounth)

        // Agregar los días de la semana a la primera columna
        val daysOfWeek = arrayOf("  Lunes  ", " Martes  ", "Miércoles", " Jueves  ", " Viernes ", " Sábado  ", " Domingo ")
        val rowDays = TableRow(this)
        for (day in daysOfWeek) {
            val textView = TextView(this)
            textView.text = day
            textView.gravity = Gravity.CENTER
            textView.textSize = 18f
            textView.setBackgroundResource(R.drawable.cell_border)  // Agregar bordes
            rowDays.addView(textView)
        }
        tableLayout.addView(rowDays)

        // Definir los tamaños de las celdas combinadas
        val cellSizes = intArrayOf(1, 2, 10, 8, 8, 8, 10, 8, 10, 8, 8, 20, 8, 8)

        // Crear las filas con celdas combinadas
        for (size in cellSizes) {
            val rowTableLayout = TableRow(this)
            val textView = TextView(this)
            textView.gravity = Gravity.CENTER
            textView.textSize = 18f
            textView.setBackgroundResource(R.drawable.cell_border)

            // Configurar el tamaño de la celda
            val params = TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, size.toFloat())
            textView.layoutParams = params

            rowTableLayout.addView(textView)
            tableLayout.addView(rowTableLayout)
        }

        // Obtener el último día del año 2022 (Diciembre 31)
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, 2022)
        calendar.set(Calendar.MONTH, Calendar.DECEMBER)
        calendar.set(Calendar.DAY_OF_MONTH, 31)

        // Avanzar hasta el primer lunes (día 2) de la última semana de diciembre
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            calendar.add(Calendar.DAY_OF_MONTH, -1)
        }

        // Agregar las columnas pares con números
        for (columnIndex in 0 until 107) {
            val isEvenColumn = columnIndex % 2 == 0

            val rowTableLayout = TableRow(this)

            // Si la columna es par, agregar números
            if (isEvenColumn) {
                for (i in 0 until 7) {
                    val textView = TextView(this)
                    val dayNumber = getDayNumber(calendar)
                    textView.text = dayNumber.toString()
                    textView.gravity = Gravity.CENTER
                    textView.textSize = 18f
                    textView.setBackgroundResource(R.drawable.cell_border)  // Agregar bordes
                    rowTableLayout.addView(textView)

                    // Avanzar al siguiente día
                    calendar.add(Calendar.DAY_OF_MONTH, 1)
                }
            } else {
                // Columnas impares muestran "maxxen"
                for (i in 0 until 7) {
                    val textView = TextView(this)
                    textView.text = ""
                    textView.gravity = Gravity.CENTER
                    textView.textSize = 20.5f
                    textView.setBackgroundResource(R.drawable.cell_border)  // Agregar bordes
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

    private fun getDayNumber(calendar: Calendar): Int {
        // Calcular el día del mes para imprimir
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        // Solo imprimir números válidos (hasta 31)
        return dayOfMonth.takeIf { it <= 31 } ?: 1
    }
}