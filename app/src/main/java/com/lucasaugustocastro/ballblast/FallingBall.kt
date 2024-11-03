package com.lucasaugustocastro.ballblast

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import kotlin.random.Random

class FallingBall(private val screen: MainActivity.Screen, private val cannon: Cannon) {

    private val paint = Paint()
    private val textPaint = Paint()
    private val radius = Random.nextInt(80, 200).toFloat()
    private var x = Random.nextInt(radius.toInt(), screen.width - radius.toInt()).toFloat()
    private var y = -radius
    private var speedY = .1f
    private var speedX = Random.nextInt(-5, 5).toFloat()
    private val dampingFactor = 0.95f
    private var hitsRemaining = Random.nextInt(5, 50)

    init {
        paint.color = Color.rgb(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))
        textPaint.color = Color.WHITE
        textPaint.textSize = 40f
        textPaint.textAlign = Paint.Align.CENTER
    }

    fun update() {
        y += speedY
        x += speedX

        if (x - radius < 0 || x + radius > screen.width) {
            speedX = -speedX * dampingFactor
        }
        if (y + radius > ((screen.height / 1f)-300f)) {
            speedY = -speedY * dampingFactor
            y = ((screen.height / 1f)- 300f) - radius
        } else {
            speedY += 0.1f
        }
    }

    fun render(canvas: Canvas) {
        canvas.drawCircle(x, y, radius, paint)
        canvas.drawText(hitsRemaining.toString(), x, y, textPaint)
    }

    fun isOffScreen(): Boolean {
        return y - radius > screen.height
    }

    fun getRect(): Rect {
        return Rect((x - radius).toInt(), (y - radius).toInt(), (x + radius).toInt(), (y + radius).toInt())
    }

    fun hit() {
        hitsRemaining--
    }

    fun isBroken(): Boolean {
        return hitsRemaining <= 0
    }
}