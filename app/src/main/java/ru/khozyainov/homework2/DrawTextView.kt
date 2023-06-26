package ru.khozyainov.homework2

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import kotlin.math.max
import kotlin.math.min

class DrawTextView(
    context: Context,
    attrs: AttributeSet?
): View(context, attrs) {

    var field: DrawTextField = DrawTextField(text = "", color = Color.BLACK)
        set(value) {
            field = value

            updateViewSizes()
            requestLayout()
            invalidate()
        }

    private val fieldRect = RectF(0f, 0f, 0f, 0f)
    private var cellSize: Float = 0f
    private var cellPadding: Float = 0f

    private val cellRect = RectF()

    private lateinit var gridPaint: Paint
    private lateinit var charPaint: Paint

    constructor(context: Context) : this(context, null)

    init {

        if (isInEditMode) {
//            drawText = "красный"
//            drawText = "желтый"
//            drawText = "фиолетовый"
            field = DrawTextField(text ="голубой", color = Color.BLUE)
        }
        initPaints()
    }

    private fun initPaints() {
        gridPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.BLACK
            style = Paint.Style.STROKE
            strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1f, resources.displayMetrics)
        }

        charPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.GREEN
            style = Paint.Style.STROKE
            strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2f, resources.displayMetrics)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val minWidth = suggestedMinimumWidth + paddingLeft + paddingRight
        val minHeight = suggestedMinimumHeight + paddingTop + paddingBottom

        val desiredCharSizeInPixels = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, DESIRED_CHAR_SIZE,
            resources.displayMetrics).toInt()

        val desiredWith = max(minWidth, field.columns * desiredCharSizeInPixels + paddingLeft + paddingRight)
        val desiredHeight = max(minHeight, field.rows * desiredCharSizeInPixels + paddingTop + paddingBottom)

        setMeasuredDimension(
            resolveSize(desiredWith, widthMeasureSpec),
            resolveSize(desiredHeight, heightMeasureSpec)
        )
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        updateViewSizes()
    }

    private fun updateViewSizes() {

        val safeWidth = width - paddingLeft - paddingRight
        val safeHeight = height - paddingTop - paddingBottom

        val cellWidth = safeWidth / field.columns.toFloat()
        val cellHeight = safeHeight / field.rows.toFloat()

        cellSize = min(cellWidth, cellHeight)
        cellPadding = cellSize * 0.2f

        val fieldWidth = cellSize * field.columns
        val fieldHeight = cellSize * field.rows

        fieldRect.left = paddingLeft + (safeWidth - fieldWidth) / 2
        fieldRect.top = paddingTop + (safeHeight - fieldHeight) / 2
        fieldRect.right = fieldRect.left + fieldWidth
        fieldRect.bottom = fieldRect.top + fieldHeight
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (field.text.isEmpty()) return

        charPaint.color = field.color

        drawGrid(canvas)
        drawCells(canvas)
    }

    private fun drawGrid(canvas: Canvas) {

        val xStart = fieldRect.left
        val xEnd = fieldRect.right
        for (i in 0..field.rows) {
            val y = fieldRect.top + cellSize * i
            canvas.drawLine(xStart, y, xEnd, y, gridPaint)
        }

        val yStart = fieldRect.top
        val yEnd = fieldRect.bottom
        for (i in 0..field.columns) {
            val x = fieldRect.left + cellSize * i
            canvas.drawLine(x, yStart, x, yEnd, gridPaint)
        }
    }

    private fun drawCells(canvas: Canvas) {
        var charIndex = 0
        for (row in 0 until field.rows) {
            for (column in 0 until field.columns) {
                drawChar(field.text[charIndex++], canvas, row, column)
            }
        }
    }

    private fun drawChar(char: Char, canvas: Canvas, row: Int, column: Int){
        val cellRect = getCellRect(row, column)

        when(char){
            'к' -> {
                canvas.drawLine(cellRect.left, cellRect.top, cellRect.left, cellRect.bottom, charPaint)
                canvas.drawLine(cellRect.left, cellRect.centerY(), cellRect.right, cellRect.top, charPaint)
                canvas.drawLine(cellRect.left, cellRect.centerY(), cellRect.right, cellRect.bottom, charPaint)
            }
            'р' -> {
                canvas.drawLine(cellRect.left, cellRect.top, cellRect.left, cellRect.bottom, charPaint)
                canvas.drawLine(cellRect.left, cellRect.top, cellRect.right, cellRect.centerY(), charPaint)
                canvas.drawLine(cellRect.left, cellRect.centerY(), cellRect.right, cellRect.centerY(), charPaint)
            }
            'а' -> {
                canvas.drawLine(cellRect.left, cellRect.bottom, cellRect.centerX(), cellRect.top, charPaint)
                canvas.drawLine(cellRect.centerX(), cellRect.top, cellRect.right, cellRect.bottom, charPaint)
                canvas.drawLine(cellRect.left, cellRect.centerY(), cellRect.right, cellRect.centerY(), charPaint)
            }
            'с' -> {
                canvas.drawLine(cellRect.left, cellRect.top, cellRect.right, cellRect.top, charPaint)
                canvas.drawLine(cellRect.left, cellRect.top, cellRect.left, cellRect.bottom, charPaint)
                canvas.drawLine(cellRect.left, cellRect.bottom, cellRect.right, cellRect.bottom, charPaint)
            }
            'н' -> {
                canvas.drawLine(cellRect.left, cellRect.top, cellRect.left, cellRect.bottom, charPaint)
                canvas.drawLine(cellRect.left, cellRect.centerY(), cellRect.right, cellRect.centerY(), charPaint)
                canvas.drawLine(cellRect.right, cellRect.top, cellRect.right, cellRect.bottom, charPaint)
            }
            'ы' -> {
                canvas.drawLine(cellRect.left, cellRect.top, cellRect.left, cellRect.bottom, charPaint)
                canvas.drawLine(cellRect.left, cellRect.bottom, cellRect.centerX(), cellRect.bottom, charPaint)
                canvas.drawLine(cellRect.centerX(), cellRect.bottom, cellRect.left, cellRect.centerY(), charPaint)
                canvas.drawLine(cellRect.right, cellRect.top, cellRect.right, cellRect.bottom, charPaint)
            }
            'й' -> {
                canvas.drawLine(cellRect.left, cellRect.top, cellRect.left, cellRect.bottom, charPaint)
                canvas.drawLine(cellRect.left, cellRect.bottom, cellRect.right, cellRect.top, charPaint)
                canvas.drawLine(cellRect.right, cellRect.top, cellRect.right, cellRect.bottom, charPaint)
                canvas.drawLine(cellRect.centerX(), cellRect.top, cellRect.centerX(), cellRect.centerY() - 60, charPaint)
            }
            'ж' -> {
                canvas.drawLine(cellRect.left, cellRect.top, cellRect.right, cellRect.bottom, charPaint)
                canvas.drawLine(cellRect.right, cellRect.top, cellRect.left, cellRect.bottom, charPaint)
                canvas.drawLine(cellRect.centerX(), cellRect.top, cellRect.centerX(), cellRect.bottom, charPaint)
            }
            'е' -> {
                canvas.drawLine(cellRect.left, cellRect.top, cellRect.right, cellRect.top, charPaint)
                canvas.drawLine(cellRect.left, cellRect.centerY(), cellRect.right, cellRect.centerY(), charPaint)
                canvas.drawLine(cellRect.left, cellRect.top, cellRect.left, cellRect.bottom, charPaint)
                canvas.drawLine(cellRect.left, cellRect.bottom, cellRect.right, cellRect.bottom, charPaint)
            }
            'л' -> {
                canvas.drawLine(cellRect.left, cellRect.bottom, cellRect.centerX(), cellRect.top, charPaint)
                canvas.drawLine(cellRect.centerX(), cellRect.top, cellRect.right, cellRect.bottom, charPaint)
            }
            'т' -> {
                canvas.drawLine(cellRect.left, cellRect.top, cellRect.right, cellRect.top, charPaint)
                canvas.drawLine(cellRect.centerX(), cellRect.top, cellRect.centerX(), cellRect.bottom, charPaint)
            }
            'г' -> {
                canvas.drawLine(cellRect.left, cellRect.top, cellRect.right, cellRect.top, charPaint)
                canvas.drawLine(cellRect.left, cellRect.top, cellRect.left, cellRect.bottom, charPaint)
            }
            'о' -> {
                canvas.drawLine(cellRect.left, cellRect.top, cellRect.right, cellRect.top, charPaint)
                canvas.drawLine(cellRect.left, cellRect.top, cellRect.left, cellRect.bottom, charPaint)
                canvas.drawLine(cellRect.left, cellRect.bottom, cellRect.right, cellRect.bottom, charPaint)
                canvas.drawLine(cellRect.right, cellRect.top, cellRect.right, cellRect.bottom, charPaint)
            }
            'у' -> {
                canvas.drawLine(cellRect.left, cellRect.top, cellRect.centerX(), cellRect.centerY(), charPaint)
                canvas.drawLine(cellRect.centerX(), cellRect.centerY(), cellRect.right, cellRect.top, charPaint)
                canvas.drawLine(cellRect.centerX(), cellRect.centerY(), cellRect.centerX(), cellRect.bottom, charPaint)
            }
            'б' -> {
                canvas.drawLine(cellRect.left, cellRect.top, cellRect.left, cellRect.bottom, charPaint)
                canvas.drawLine(cellRect.left, cellRect.top, cellRect.right, cellRect.top, charPaint)
                canvas.drawLine(cellRect.left, cellRect.centerY(), cellRect.right, cellRect.bottom, charPaint)
                canvas.drawLine(cellRect.left, cellRect.bottom, cellRect.right, cellRect.bottom, charPaint)
            }
            'ф' -> {
                canvas.drawLine(cellRect.right, cellRect.centerY(), cellRect.centerX(), cellRect.top, charPaint)
                canvas.drawLine(cellRect.right, cellRect.centerY(), cellRect.centerX(), cellRect.bottom, charPaint)
                canvas.drawLine(cellRect.centerX(), cellRect.top, cellRect.left, cellRect.centerY(), charPaint)
                canvas.drawLine(cellRect.centerX(), cellRect.bottom, cellRect.left, cellRect.centerY(), charPaint)
                canvas.drawLine(cellRect.centerX(), cellRect.top, cellRect.centerX(), cellRect.bottom, charPaint)
            }
            'и' -> {
                canvas.drawLine(cellRect.left, cellRect.top, cellRect.left, cellRect.bottom, charPaint)
                canvas.drawLine(cellRect.left, cellRect.bottom, cellRect.right, cellRect.top, charPaint)
                canvas.drawLine(cellRect.right, cellRect.top, cellRect.right, cellRect.bottom, charPaint)
            }
            'в' -> {
                canvas.drawLine(cellRect.left, cellRect.top, cellRect.left, cellRect.bottom, charPaint)
                canvas.drawLine(cellRect.left, cellRect.top, cellRect.right, cellRect.top, charPaint)
                canvas.drawLine(cellRect.left, cellRect.centerY(), cellRect.right, cellRect.top, charPaint)
                canvas.drawLine(cellRect.left, cellRect.centerY(), cellRect.right, cellRect.bottom, charPaint)
                canvas.drawLine(cellRect.left, cellRect.bottom, cellRect.right, cellRect.bottom, charPaint)
            }
        }
    }

    private fun getCellRect(row: Int, column: Int): RectF {
        cellRect.left = fieldRect.left + column * cellSize + cellPadding
        cellRect.top = fieldRect.top + row * cellSize + cellPadding
        cellRect.right = cellRect.left + cellSize - cellPadding * 2
        cellRect.bottom = cellRect.top + cellSize - cellPadding * 2
        return cellRect
    }

    companion object{
        const val DESIRED_CHAR_SIZE = 50f
    }
}

