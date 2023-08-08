package com.example.app_inf.Activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        val recyclerView: RecyclerView = findViewById(R.id.monthRecyclerView)
        val monthNames = resources.getStringArray(R.array.month_names)


        setupMonthRecyclerView(recyclerView, monthNames)
    }
    private fun setupMonthRecyclerView(recyclerView: RecyclerView, monthNames: Array<String>) {
        recyclerView.apply {
            layoutManager = GridLayoutManager(this@CalendarioActivity, 3)
            adapter = MonthAdapter(monthNames)
        }
    }
}

class MonthAdapter(private val monthNames: Array<String>) :
    RecyclerView.Adapter<MonthAdapter.MonthViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_month, parent, false)
        return MonthViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MonthViewHolder, position: Int) {
        val monthName = monthNames[position]
        holder.bind(monthName, position + 1)
    }

    override fun getItemCount(): Int = monthNames.size

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
                val dayOfWeekFirstDay = getDayOfWeek(currentYear, monthNumber, 1)

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

        private fun getDayOfWeek(year: Int, month: Int, day: Int): Int {
            val calendar = Calendar.getInstance()
            calendar.set(year, month - 1, day)
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