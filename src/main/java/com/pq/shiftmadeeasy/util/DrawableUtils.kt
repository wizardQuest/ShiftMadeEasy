package com.pq.shiftmadeeasy.util

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.GradientDrawable.OVAL
import android.graphics.drawable.LayerDrawable
import androidx.core.content.ContextCompat

fun getTextInsideCircleDrawable(context: Context, text: String, backgroundColor: Int): Drawable {
    val layer1 = GradientDrawable()
    val states = arrayOf(intArrayOf(android.R.attr.state_enabled))
    val colors = intArrayOf(backgroundColor)
    val colorStateList = ColorStateList(states, colors)
    layer1.apply {
        shape = OVAL
        setSize(64, 64)
        color = colorStateList

    }
    val textInsideDrawable = getDrawableText(context, text, null, android.R.color.white, 12)
    val layers = arrayOf(layer1, textInsideDrawable)
    return LayerDrawable(layers)
}

fun getDrawableText(
    context: Context,
    text: String,
    typeface: Typeface?,
    color: Int,
    size: Int
): Drawable? {
    val resources = context.resources
    val bitmap = Bitmap.createBitmap(48, 48, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    paint.typeface = typeface ?: Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    paint.color = ContextCompat.getColor(context, color)
    val scale = resources.displayMetrics.density
    paint.textSize = (size * scale)
    val bounds = Rect()
    paint.getTextBounds(text, 0, text.length, bounds)
    val x = (bitmap.width - bounds.width()) / 2
    val y = (bitmap.height + bounds.height()) / 2
    canvas.drawText(text, x.toFloat(), y.toFloat(), paint)
    return BitmapDrawable(context.resources, bitmap)
}