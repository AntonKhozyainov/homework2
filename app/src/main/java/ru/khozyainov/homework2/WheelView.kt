package ru.khozyainov.homework2

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class WheelView(
    context: Context,
    attrs: AttributeSet?
) : View(context, attrs) {

    var wheelSize = DEFAULT_WHEEL_SIZE_VALUE
        set(value) {
            field = value
            invalidate()
        }

    private lateinit var paints: Array<Paint>
    private lateinit var paths: Array<Path>

    constructor(context: Context) : this(context, null)

    init {
        initPaints()
        initPaths()
        if (isInEditMode) {
            wheelSize = 90
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = width.toFloat()
        val height = height.toFloat()

        val radius = min(width, height) / 2f * (wheelSize.toFloat() / 100f)

        val centerX = width / 2f
        val centerY = height / 2f

        val angle = 360f / 7f

        for (i in 0 until 7) {
            val startAngle = i * angle - 90
            paths[i].moveTo(centerX, centerY)
            paths[i].lineTo(
                centerX + radius * cos(Math.toRadians(startAngle.toDouble())).toFloat(),
                centerY + radius * sin(Math.toRadians(startAngle.toDouble())).toFloat()
            )
            paths[i].arcTo(
                centerX - radius, centerY - radius, centerX + radius, centerY + radius,
                startAngle, angle, false
            )
            paths[i].lineTo(centerX, centerY)

            canvas.drawPath(paths[i], paints[i])
            paths[i].reset()
        }
    }

    private fun initPaints() {
        paints = arrayOf(
            getPaintWithColor(context.getColor(R.color.red)),
            getPaintWithColor(context.getColor(R.color.orange)),
            getPaintWithColor(context.getColor(R.color.yellow)),
            getPaintWithColor(context.getColor(R.color.green)),
            getPaintWithColor(context.getColor(R.color.cyan)),
            getPaintWithColor(context.getColor(R.color.blue)),
            getPaintWithColor(context.getColor(R.color.purple))
        )
    }

    private fun getPaintWithColor(@ColorInt argColor: Int): Paint =
        Paint(ANTI_ALIAS_FLAG).apply {
            color = argColor
            style = Paint.Style.FILL
        }

    private fun initPaths() {
        paths = (0..6).map {
            Path()
        }.toTypedArray()
    }

    companion object {
        private const val DEFAULT_WHEEL_SIZE_VALUE = 50
    }
}