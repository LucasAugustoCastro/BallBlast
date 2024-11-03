package com.lucasaugustocastro.ballblast

import android.graphics.Canvas
import android.view.MotionEvent

interface GameObject {

    fun update(et: Float)
    fun render(canvas: Canvas)
    fun onTouch(e: MotionEvent)
}