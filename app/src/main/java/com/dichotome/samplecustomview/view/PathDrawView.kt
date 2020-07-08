package com.dichotome.samplecustomview.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.math.cos
import kotlin.math.sin

class PathDrawView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attributeSet, defStyle) {

    private val numbers = arrayOf(20, 1, 18, 4, 13, 6, 10, 15, 2, 17, 3, 19, 7, 16, 8, 11, 14, 9, 12, 5)

    private val colorRingStep = 30f
    private val firstRowStep = 150f
    private val secondRowStep = 70f
    private val borderStep = 70f

    private val segmentInDegrees = 18f

    private val center = PointF()

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.WHITE; textSize = 24f }
    private val textRect = Rect()
    private val path = Path()

    private val point = PointF()

    private val centerPointRect by lazy {
        center.run { RectF(x, y, x, y) }
    }

    private val innerCenterRing by lazy {
        centerPointRect.run {
            RectF(
                left - colorRingStep,
                top - colorRingStep,
                right + colorRingStep,
                bottom + colorRingStep
            )
        }
    }

    private val outerCenterRing by lazy {
        innerCenterRing.run {
            RectF(
                left - colorRingStep,
                top - colorRingStep,
                right + colorRingStep,
                bottom + colorRingStep
            )
        }
    }

    private val firstRowRect by lazy {
        outerCenterRing.run {
            RectF(
                left - firstRowStep,
                top - firstRowStep,
                right + firstRowStep,
                bottom + firstRowStep
            )
        }
    }

    private val firstRingRect by lazy {
        firstRowRect.run {
            RectF(
                left - colorRingStep,
                top - colorRingStep,
                right + colorRingStep,
                bottom + colorRingStep
            )
        }
    }

    private val secondRowRect by lazy {
        firstRingRect.run {
            RectF(
                left - secondRowStep,
                top - secondRowStep,
                right + secondRowStep,
                bottom + secondRowStep
            )
        }
    }

    private val secondRingRect by lazy {
        secondRowRect.run {
            RectF(
                left - colorRingStep,
                top - colorRingStep,
                right + colorRingStep,
                bottom + colorRingStep
            )
        }
    }

    private val borderRect by lazy {
        secondRingRect.run {
            RectF(
                left - borderStep,
                top - borderStep,
                right + borderStep,
                bottom + borderStep
            )
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas ?: return

        canvas.drawColor(Color.CYAN)

        center.run {
            x = width / 2f
            y = height / 2f
        }

        paint.style = Paint.Style.FILL_AND_STROKE
        paint.color = Color.RED
        canvas.drawCircle(center.x, center.y, colorRingStep, paint)

        canvas.save()

        canvas.rotate(-segmentInDegrees / 2, center.x, center.y)

        for (i in 0 until 20) {
            val isEven = i % 2 == 0
            val whiteOrBlack = if (isEven) Color.WHITE else Color.BLACK
            val greenOrRed = if (isEven) Color.GREEN else Color.RED


            canvas.run {
                drawRingSegment(innerCenterRing, outerCenterRing, Color.GREEN)
                drawRingSegment(outerCenterRing, firstRowRect, whiteOrBlack)
                drawRingSegment(firstRowRect, firstRingRect, greenOrRed)
                drawRingSegment(firstRingRect, secondRowRect, whiteOrBlack)
                drawRingSegment(secondRowRect, secondRingRect, greenOrRed)
                drawRingSegment(secondRingRect, borderRect, Color.BLACK)

                rotate(segmentInDegrees, center.x, center.y)
            }
        }

        point.x = borderRect.right - borderStep / 2
        point.y = center.y

        canvas.restore()

        for (element in numbers) {
            val text = element.toString()
            textPaint.getTextBounds(text, 0, text.length, textRect)
            canvas.drawText(text, point.x - textRect.width() / 2, point.y + textRect.height() / 2, textPaint)
            rotatePoint(center.x, center.y, point.x, point.y, segmentInDegrees, point)
        }
    }

    private fun Canvas.drawRingSegment(
        innerOval: RectF,
        outerOval: RectF,
        color: Int,
        degrees: Float = segmentInDegrees
    ) {
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.strokeWidth = 1f
        paint.color = color
        path.run {
            arcTo(outerOval, degrees, -degrees, true)
            lineTo(innerOval.right, center.y)
            arcTo(innerOval, 0f, degrees)
            close()
        }

        this.drawPath(path, paint)
        path.reset()
        paint.reset()
    }

    private fun rotatePoint(cx: Float, cy: Float, x: Float, y: Float, degrees: Float, res: PointF) {
        val rads = degrees / 180 * Math.PI
        val s = sin(rads).toFloat()
        val c = cos(rads).toFloat()

        val ox = x - cx
        val oy = y - cy

        val nx = ox * c - oy * s
        val ny = ox * s + oy * c

        res.x = cx + nx
        res.y = cy + ny
    }
}