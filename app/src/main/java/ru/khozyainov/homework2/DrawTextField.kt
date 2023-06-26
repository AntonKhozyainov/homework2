package ru.khozyainov.homework2

import androidx.annotation.ColorInt
import kotlin.math.ceil

data class DrawTextField(
    val text: String,
    @ColorInt val color: Int,
    val rows: Int = if (text.length / 10 == 0) 1 else ceil(text.length / 10.0).toInt(),
    val columns: Int  = if(text.length > 10) 10 else text.length
)

