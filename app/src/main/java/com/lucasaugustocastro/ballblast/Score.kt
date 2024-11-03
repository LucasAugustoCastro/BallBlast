package com.lucasaugustocastro.ballblast

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent

class Score(private val screen: MainActivity.Screen) : GameObject {

    private val paint = Paint()
    private var life = 5
    private var ballBroken = 0

    init {
        paint.color = Color.WHITE
        paint.textSize = 60f
        paint.textAlign = Paint.Align.CENTER
        paint.isFakeBoldText = true
    }

    override fun update(et: Float) {}

    override fun render(canvas: Canvas) {
        val text = "Vidas: $life - Bolas destruidas: $ballBroken"
        canvas.drawText(text, (screen.width/2).toFloat(), 150f, paint)
    }

    override fun onTouch(e: MotionEvent) {}

    fun incBroken() {
        ballBroken++
    }

    fun decLife(): Boolean {
        life--

        return life == 0
    }

    fun getBrokenBalls(): Int {
        return ballBroken
    }
}