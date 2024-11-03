package com.lucasaugustocastro.ballblast

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import kotlin.random.Random

class StartScene(private val screen: MainActivity.Screen): Scene {

    private val paint = Paint()
    private var controlTime = 0f

    init {
        paint.textSize = 70f
        paint.textAlign = Paint.Align.CENTER
        paint.isFakeBoldText = true
        paint.textSkewX = -0.3f
        paint.color = Color.rgb(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))

    }

    override fun update(et: Float) {
        controlTime += et
        if (controlTime > 300) {
            controlTime = 0f
            paint.color = Color.rgb(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))
        }
    }

    override fun render(canvas: Canvas) {
        val text = "Toque para iniciar..."
        canvas.drawText(text, screen.width/2f, screen.height/2f, paint)
    }

    override fun onTouch(e: MotionEvent): Boolean {
        return when (e.action) {
            MotionEvent.ACTION_DOWN -> {
                screen.scene = GameScene(screen)
                true
            }
            else -> false
        }
    }

}