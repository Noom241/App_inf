package com.example.app_inf.Activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
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

        val monthNames = resources.getStringArray(R.array.month_names)

        setupMonthContainer(R.id.row1, 0, monthNames)
        setupMonthContainer(R.id.row2, 1, monthNames)
        setupMonthContainer(R.id.row3, 2, monthNames)
        setupMonthContainer(R.id.row4, 3, monthNames)
        setupMonthContainer(R.id.row5, 4, monthNames)
        setupMonthContainer(R.id.row6, 5, monthNames)
        setupMonthContainer(R.id.row7, 6, monthNames)
        setupMonthContainer(R.id.row8, 7, monthNames)
        setupMonthContainer(R.id.row9, 8, monthNames)
        setupMonthContainer(R.id.row10, 9, monthNames)
        setupMonthContainer(R.id.row11, 10, monthNames)
        setupMonthContainer(R.id.row12, 11, monthNames)
    }

    private fun setupMonthContainer(containerId: Int, startIndex: Int, monthNames: Array<String>) {
        val container: LinearLayout = findViewById(containerId)
        for (i in startIndex until startIndex + 3) {
            val monthIndex = i % 12
            val monthName = monthNames[monthIndex]
            val monthView = LayoutInflater.from(this).inflate(R.layout.item_month, container, false)
            container.addView(monthView)
            val monthViewHolder = MonthAdapter.MonthViewHolder(monthView)
            monthViewHolder.bind(monthName, monthIndex + 1)
        }
    }
}

// Rest of the code remains the same



class MonthAdapter(private val monthNames: Array<String>) :
    RecyclerView.Adapter<MonthAdapter.MonthViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_month, parent, false)
        return MonthViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MonthViewHolder, position: Int) {
        val monthIndex = position % 12
        val monthName = monthNames[monthIndex]
        holder.bind(monthName, monthIndex + 1)
    }

    override fun getItemCount(): Int = monthNames.size * 4 // 4 containers with 3 months each

    class MonthViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val daysGridLayout: ViewGroup = itemView.findViewById(R.id.daysGridLayout)
        private val dayHeaders = itemView.resources.getStringArray(R.array.day_headers)
        fun bind(monthName: String, monthNumber: Int) {
            itemView.apply {
                findViewById<TextView>(R.id.monthNameTextView).text = monthName

                setupDayHeaders()
                val calendar = Calendar.getInstance()
                val currentYear = calendar.get(Calendar.YEAR)
                val daysInMonth = getDaysInMonth(currentYear, monthNumber)
                val dayOfWeekFirstDay = getDayOfWeek(currentYear, monthNumber)

                populateCalendarGrid(daysGridLayout, dayOfWeekFirstDay, daysInMonth)
            }
        }

        private fun setupDayHeaders() {
            daysGridLayout.removeAllViews()
            for (header in dayHeaders) {
                val headerView = createHeaderView(header)
                daysGridLayout.addView(headerView)
            }
        }

        private fun getDaysInMonth(year: Int, month: Int): Int {
            return when (month) {
                2 -> if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) 29 else 28 // Año bisiesto
                4, 6, 9, 11 -> 30
                else -> 31
            }
        }

        private fun getDayOfWeek(year: Int, month: Int): Int {
            val calendar = Calendar.getInstance()
            calendar.set(year, month - 1, 1) // Establece el primer día del mes
            return (calendar.get(Calendar.DAY_OF_WEEK) + 5) % 7 // Ajustar para que el domingo sea 0
        }




        private fun createHeaderView(headerText: String): TextView {
            return TextView(itemView.context).apply {
                text = headerText
                gravity = View.TEXT_ALIGNMENT_CENTER
            }
        }

        private fun populateCalendarGrid(daysGridLayout: ViewGroup, startDayOfWeek: Int, daysInMonth: Int) {
            val emptyCellCount = (startDayOfWeek + 6) % 7
            val totalCells = 42 // 5 rows of 7 days

            for (day in 1..totalCells) {
                val dayView = createDayView(day, emptyCellCount, daysInMonth)
                daysGridLayout.addView(dayView)
            }
        }

        private fun createDayView(day: Int, emptyCellCount: Int, daysInMonth: Int): TextView {
            val dayView = TextView(itemView.context).apply {
                gravity = View.TEXT_ALIGNMENT_CENTER
            }

            if (day <= emptyCellCount || day > daysInMonth + emptyCellCount) {
                dayView.text = ""
            } else {
                dayView.text = (day - emptyCellCount).toString()
            }

            return dayView
        }

        // Rest of the code remains the same
    }
}