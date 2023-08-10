package com.example.app_inf.Activities
import com.example.app_inf.R
import android.os.Bundle
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

        val diasSemana = arrayOf("Domingo", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado")
        val primerDiaSemana = calendar.get(Calendar.DAY_OF_WEEK) - 1

        var dia = 1
        var fila = TableRow(this)

        val columnWidth = resources.getDimensionPixelSize(R.dimen.cell_width)

        // Agregar encabezado de días de la semana
        for (i in diasSemana.indices) {
            val textView = TextView(this)
            textView.text = diasSemana[i]
            textView.gravity = Gravity.CENTER
            textView.setBackgroundResource(R.drawable.cell_border)
            textView.width = columnWidth
            fila.addView(textView)
        }
        tableLayout.addView(fila)

        fila = TableRow(this)

        // Llenar las celdas vacías hasta el primer día de la semana
        for (i in 0 until primerDiaSemana) {
            val emptyCell = TextView(this)
            emptyCell.text = " "
            emptyCell.gravity = Gravity.CENTER
            emptyCell.setBackgroundResource(R.drawable.cell_border)
            emptyCell.width = columnWidth
            fila.addView(emptyCell)
        }

        while (calendar.get(Calendar.MONTH) == selectedMonth) {
            val textView = TextView(this)
            textView.text = dia.toString()
            textView.gravity = Gravity.CENTER
            textView.setBackgroundResource(R.drawable.cell_border)
            textView.width = columnWidth
            fila.addView(textView)

            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
                tableLayout.addView(fila)
                fila = TableRow(this)
            }

            calendar.add(Calendar.DAY_OF_MONTH, 1)
            dia++
        }

        // Llenar las celdas vacías restantes en la última fila
        while (fila.childCount < 7) {
            val emptyCell = TextView(this)
            emptyCell.text = " "
            emptyCell.gravity = Gravity.CENTER
            emptyCell.setBackgroundResource(R.drawable.cell_border)
            emptyCell.width = columnWidth
            fila.addView(emptyCell)
        }

        if (fila.childCount > 0) {
            tableLayout.addView(fila)
        }
    }

    // Método para cambiar el mes seleccionado
    private fun changeSelectedMonth(newMonth: Int) {
        selectedMonth = newMonth
        recreate() // Vuelve a crear la actividad para actualizar el calendario
    }
}
