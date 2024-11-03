package com.lucasaugustocastro.ballblast

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    private lateinit var soundPool: SoundPool
    private var shootSoundId: Int = 0
    private var isSoundLoaded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val screen = Screen(this)
        screen.setOnTouchListener(screen)
        setContentView(screen)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()

            soundPool = SoundPool.Builder()
                .setMaxStreams(6)
                .setAudioAttributes(audioAttributes)
                .build()
        } else {
            soundPool = SoundPool(6, AudioManager.STREAM_MUSIC, 0)
        }

        soundPool.setOnLoadCompleteListener { _, sampleId, status ->
            if (status == 0) {
                isSoundLoaded = true
                Log.d("MainActivity", "Sound loaded with ID: $sampleId")
            } else {
                Log.e("MainActivity", "Failed to load sound with ID: $sampleId")
            }
        }

        shootSoundId = soundPool.load(this, R.raw.shoot, 1)
    }

    override fun onDestroy() {
        super.onDestroy()
        soundPool.release()
    }

    fun playShootSound() {
        if (isSoundLoaded) {
            soundPool.play(shootSoundId, 1f, 1f, 1, 0, 1f)
        } else {
            Log.w("MainActivity", "Sound not loaded yet")
        }
    }

    class Screen(private val context: Context): View(context), OnTouchListener {

        private var startTime = System.nanoTime()
        var scene: Scene = StartScene(this)

        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)

            val elapsedTime = (System.nanoTime() - startTime) / 1000000f
            startTime = System.nanoTime()

            canvas.drawColor(Color.BLACK)
            this.update(elapsedTime)
            this.render(canvas)

            invalidate()
        }

        private fun update(et: Float) {
            scene.update(et)
        }

        private fun render(canvas: Canvas) {
            scene.render(canvas)
        }

        override fun onTouch(view: View?, motionEvent: MotionEvent?): Boolean {
            val e = motionEvent ?: return false

            return scene.onTouch(e)
        }
    }
}