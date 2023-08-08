package com.example.app_inf

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class CustomCircleView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val circlePaint: Paint = Paint()
    private var centerX = 0f
    private var centerY = 0f
    private var radius = 0f
    private var circleColor = Color.BLACK

    init {
        circlePaint.style = Paint.Style.STROKE
        circlePaint.strokeWidth = 1f  // Grosor del contorno
    }

    // Función para establecer la ubicación y el color del círculo
    fun setCircleLocationAndColor(x: Float, y: Float, color: Int) {
        centerX = x
        centerY = y
        circleColor = color
        invalidate() // Redibujar la vista
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Establecer el color del contorno
        circlePaint.color = circleColor

        // Dibujar el círculo en la ubicación con el contorno y color establecidos
        canvas.drawCircle(centerX, centerY, radius, circlePaint)

        // Dibujar un punto rojo en el centro del círculo
        val pointPaint = Paint()
        pointPaint.color = Color.RED
        canvas.drawPoint(centerX, centerY, pointPaint)
    }


    // Llamado cuando las dimensiones de la vista cambian
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        // Calcular el radio basado en el tamaño de la vista
        radius = Math.min(w, h) * 0.015f // Cambia el factor según tu preferencia
    }
}

