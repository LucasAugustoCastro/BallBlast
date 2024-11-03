package com.lucasaugustocastro.ballblast

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect

class ProjectTile(private var x: Float, private var y: Float, private val screen: MainActivity.Screen) {

    private val paint = Paint()
    private val radius = 10f
    private val speed = 50f
    private var markedForRemoval = false

    init {
        paint.color = Color.YELLOW
    }

    fun update() {
        y -= speed
    }

    fun render(canvas: Canvas) {
        canvas.drawCircle(x, y, radius, paint)
    }

    fun isOffScreen(): Boolean {
        return y + radius < 0
    }

    fun getRect(): Rect {
        return Rect((x - radius).toInt(), (y - radius).toInt(), (x + radius).toInt(), (y + radius).toInt())
    }

    fun markForRemoval() {
        markedForRemoval = true
    }

    fun isMarkedForRemoval(): Boolean {
        return markedForRemoval
    }
}