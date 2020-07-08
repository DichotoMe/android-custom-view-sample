package com.dichotome.samplecustomview.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View

class CustomView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attributeSet, defStyle) {

    private val p = Paint()

    private val rect = Rect()

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas ?: return

        // заливка канвы цветом
        canvas.drawARGB(80, 102, 204, 255)

        // настройка кисти
        // красный цвет
        p.apply {
            color = Color.RED
            strokeWidth = 10f
        }
        // толщина линии = 10
        canvas.drawPoint(50f, 50f, p);

        // рисуем линию от (100,100) до (500,50)
        canvas.drawLine(100f, 100f, 500f, 50f, p);

        // рисуем круг с центром в (100,200), радиус = 50
        canvas.drawCircle(100f, 200f, 50f, p);

        // рисуем прямоугольник
        // левая верхняя точка (200,150), нижняя правая (400,200)
        canvas.drawRect(200f, 150f, 400f, 200f, p);

        // настройка объекта Rect
        // левая верхняя точка (250,300), нижняя правая (350,500)
        rect.set(250, 300, 350, 500);
        // рисуем прямоугольник из объекта rect
        canvas.drawRect(rect, p);
    }
}