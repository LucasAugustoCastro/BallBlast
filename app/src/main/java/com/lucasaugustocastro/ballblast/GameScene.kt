package com.lucasaugustocastro.ballblast

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent

class GameScene(private val screen: MainActivity.Screen): Scene {

    private val cannon = Cannon(300f, screen.height / 1f, screen)
    private val paint = Paint()
    private val score = Score(screen)
    private var endGame = false
    private var winGame = false
    private val fallingBalls = mutableListOf<FallingBall>()
    private val handler = Handler(Looper.getMainLooper())
    private var canDecreaseLife = true
    private val lifeDecreaseInterval = 4000L


    init {
        paint.color = Color.RED
        paint.textSize = 60f
        paint.textAlign = Paint.Align.CENTER
        paint.isFakeBoldText = true
        for (i in 1..2) {
            fallingBalls.add(FallingBall(screen, cannon))
        }
    }

    override fun update(et: Float) {
        if (!endGame) {
            cannon.update(et)
            fallingBalls.forEach { it.update() }
            fallingBalls.removeAll {
                if(it.isOffScreen() || it.isBroken()){
                    score.incBroken()
                    true
                } else {
                    false
                }
            }

            val projectiles = cannon.getProjectiles()
            for (ball in fallingBalls) {

                for (projectile in projectiles) {
                    if (Rect.intersects(ball.getRect(), projectile.getRect())) {
                        ball.hit()
                        projectile.markForRemoval()
                    }
                }

                if (Rect.intersects(ball.getRect(), cannon.getRect()!!)) {
                    if (canDecreaseLife) {
                        canDecreaseLife = false
                        cannon.startBlinking()
                        handler.postDelayed({
                            canDecreaseLife = true
                            cannon.stopBlinking()
                        }, lifeDecreaseInterval)
                        if (score.decLife()) {
                            endGame = true
                            cannon.stopFiring()
                        }
                    }
                    return
                }
                if (score.getBrokenBalls() > 5) {
                    winGame = true
                    endGame = true
                    cannon.stopFiring()
                }
            }
            projectiles.removeAll { it.isOffScreen() || it.isMarkedForRemoval() }

            if (fallingBalls.size < 2) {
                fallingBalls.add(FallingBall(screen, cannon))
            }
        }
    }

    override fun render(canvas: Canvas) {
        if (!endGame) {

            cannon.render(canvas)
            fallingBalls.forEach { it.render(canvas) }
        }
        else {
            val text = if (winGame) "Você venceu!!!" else "Fim de Jogo!!!"
            canvas.drawText(text, (screen.width/2).toFloat(),
                (screen.height/2).toFloat(), paint)
        }
        score.render(canvas)
    }

    override fun onTouch(e: MotionEvent): Boolean {

        if (!endGame) {
            cannon.onTouch(e)
        }
        else {
            screen.scene = StartScene(screen)
        }

        return true
    }

}