package com.lucasaugustocastro.ballblast

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent

class Cannon(x: Float, y: Float, private val screen: MainActivity.Screen): GameObject {

    private val paint = Paint()
    private var onMove = false
    private var rect: Rect? = null
    private var canonBitMap: Bitmap? = null
    private val projectiles = mutableListOf<ProjectTile>()
    private val handler = Handler(Looper.getMainLooper())
    private val fireRate = 75L
    private var isVisible = true
    private var isInvincible = false

    private val fireRunnable = object : Runnable {
        override fun run() {
            fireProjectile()
            handler.postDelayed(this, fireRate)
        }
    }

    private val blinkRunnable = object : Runnable {
        override fun run() {
            isVisible = !isVisible
            if (isInvincible) {
                handler.postDelayed(this, 100L)
            } else {
                isVisible = true
            }
        }
    }

    init {
        paint.color = Color.rgb(255, 255, 100)
        canonBitMap = loadBitmap("canon.png")
        val canon = canonBitMap!!
        val canonX = ((x - canon.width) / 2f).toInt()
        val canonY = ((y - canon.height - 300f)).toInt()
        val canonWidth = canon.width
        val canonHeight = canon.height
        rect = Rect(
            canonX,
            canonY,
            canonX + canonWidth,
            canonY + canonHeight
        )
    }

    override fun update(et: Float) {
        projectiles.forEach { it.update() }
        projectiles.removeAll { it.isOffScreen() }
    }

    override fun render(canvas: Canvas) {
        val canon = canonBitMap ?: return
        val rect = rect ?: return
        if (isVisible) {
            canvas.drawBitmap(canon, null, rect, paint)
        }
        projectiles.forEach { it.render(canvas) }
    }

    override fun onTouch(e: MotionEvent) {
        val rect = rect ?: return
        val canon = canonBitMap ?: return
        when (e.action) {
            MotionEvent.ACTION_DOWN -> {
                onMove = rect.contains(e.x.toInt(), e.y.toInt())
                if (onMove) {
                    handler.post(fireRunnable)
                }
            }
            MotionEvent.ACTION_UP -> {
                onMove = false
                handler.removeCallbacks(fireRunnable)
            }
            MotionEvent.ACTION_MOVE -> {
                if (onMove) {
                    rect.set(
                        (e.x - canon.width / 2f).toInt(),
                        rect.top,
                        (e.x + canon.width / 2f).toInt(),
                        rect.bottom
                    )
                }
            }
        }
    }

    private fun fireProjectile() {
        val rect = rect ?: return
        val projectile = ProjectTile(rect.centerX().toFloat(), rect.top.toFloat(), screen)
        projectiles.add(projectile)
        (screen.context as MainActivity).playShootSound()
    }

    private fun loadBitmap(file: String): Bitmap? {
        try {
            val inputStream = screen.context.assets.open(file)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()
            return bitmap
        } catch (e: Exception) {
            Log.d("App", e.message ?: "Algo ocorreu de errado ao carregar a imagem")
        }

        return null
    }

    fun getProjectiles(): MutableList<ProjectTile> {
        return projectiles
    }

    fun getRect(): Rect? {
        return rect
    }

    fun stopFiring() {
        handler.removeCallbacks(fireRunnable)
    }

    fun startBlinking() {
        isInvincible = true
        handler.post(blinkRunnable)
    }

    fun stopBlinking() {
        isInvincible = false
        handler.removeCallbacks(blinkRunnable)
        isVisible = true
    }
}