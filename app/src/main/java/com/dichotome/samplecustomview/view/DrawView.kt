package com.dichotome.samplecustomview.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View

class DrawView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attributeSet, defStyle) {

    val rect = Rect(100,200,200,300)
    val paint = Paint().apply {
        color = Color.BLUE
        strokeWidth = 20f
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas ?: return

        canvas.drawColor(Color.CYAN)

        paint.style = Paint.Style.FILL
        rect.offset(150, 0)
        canvas.drawRect(rect, paint)

        paint.style = Paint.Style.STROKE
        rect.offset(150, 0)
        canvas.drawRect(rect, paint)

        paint.style = Paint.Style.FILL_AND_STROKE
        rect.offset(150, 0)
        canvas.drawRect(rect, paint)
    }
}