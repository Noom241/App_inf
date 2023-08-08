package com.example.app_inf.Activities
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_inf.R
import java.util.Calendar

class CalendarioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendario)

        val recyclerView = findViewById<RecyclerView>(R.id.monthRecyclerView)
        val monthNames = arrayOf("Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre")

        recyclerView.layoutManager = GridLayoutManager(this, 4) // 4 columnas para los meses
        recyclerView.adapter = MonthAdapter(monthNames)
    }
}

class MonthAdapter(private val monthNames: Array<String>) :
    RecyclerView.Adapter<MonthAdapter.MonthViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_month, parent, false)
        return MonthViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MonthViewHolder, position: Int) {
        val monthName = monthNames[position]
        holder.bind(monthName, position + 1)
    }

    override fun getItemCount(): Int {
        return monthNames.size
    }

    class MonthViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(monthName: String, monthNumber: Int) {
            val monthNameTextView = itemView.findViewById<TextView>(R.id.monthNameTextView)
            monthNameTextView.text = monthName

            val daysGridLayout = itemView.findViewById<GridLayout>(R.id.daysGridLayout)
            val dayHeaders = arrayOf("Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom") // Cambio aquí

            for (header in dayHeaders) {
                val headerView = TextView(itemView.context)
                headerView.text = header
                headerView.gravity = Gravity.CENTER
                daysGridLayout.addView(headerView)
            }

            val daysInMonth = getDaysInMonth(2023, monthNumber)
            val dayOfWeekFirstDay = getDayOfWeek(2023, monthNumber, 1)

            var dayCounter = 1
            for (row in 0 until 5) {
                for (col in 0 until 7) {
                    if ((row == 0 && col < dayOfWeekFirstDay) || dayCounter > daysInMonth) {
                        daysGridLayout.addView(TextView(itemView.context)) // Agregar celdas vacías
                    } else {
                        val dayView = TextView(itemView.context)
                        dayView.text = dayCounter.toString()
                        dayView.gravity = Gravity.CENTER
                        daysGridLayout.addView(dayView)
                        dayCounter++
                    }
                }
            }
        }

        private fun getDayOfWeek(year: Int, month: Int, day: Int): Int {
            val calendar = Calendar.getInstance()
            calendar.set(year, month - 1, day)
            return (calendar.get(Calendar.DAY_OF_WEEK) + 5) % 7 // Ajustar para que el domingo sea 0
        }

        private fun getDaysInMonth(year: Int, month: Int): Int {
            return when (month) {
                2 -> if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) 29 else 28 // Año bisiesto
                4, 6, 9, 11 -> 30
                else -> 31
            }
        }
    }
}



