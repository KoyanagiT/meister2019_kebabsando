package com.websarva.wings.andrroid.sudotest

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView

@SuppressLint("ViewConstructor")
class VisualizerSurfaceView// 線の太さ、アンチエイリアス、色、とか

// この2つを書いてフォーカスを当てないとSurfaceViewが動かない？
//        isFocusable = true
//        requestFocus()
    (context: Context, surface: SurfaceView) : SurfaceView(context), SurfaceHolder.Callback, Runnable {

    private val _paint = Paint()
    private var _buffer: ShortArray = ShortArray(0)
    private var _holder: SurfaceHolder = surface.holder
    private var _thread: Thread? = null

    override fun run() {
        while (_thread != null) {
            doDraw(_holder)
        }
    }


    init {
        _holder.addCallback(this)
        _paint.strokeWidth = 2f
        _paint.isAntiAlias = true
        _paint.color = Color.WHITE
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        if (holder != null) {
            val canvas = holder.lockCanvas()

            holder.unlockCanvasAndPost(canvas)
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        _thread = Thread(this)
        _thread?.start()
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        _thread = null
    }

    fun update(buffer: ShortArray, size: Int) {
        _buffer = buffer.copyOf(size)
//      postInvalidate()
    }

    private fun doDraw(holder: SurfaceHolder) {
        if (_buffer.isEmpty()) {
            return
        }

        try {
            val canvas: Canvas = holder.lockCanvas()

            canvas.drawColor(Color.BLACK)

            val baseLine: Float = canvas.height / 2f
            var oldX = 0f
            var oldY: Float = baseLine

            for ((index, _) in _buffer.withIndex()) {
                val x: Float = canvas.width.toFloat() / _buffer.size.toFloat() * index.toFloat()
                val y: Float = _buffer[index] / 128 + baseLine

                canvas.drawLine(oldX, oldY, x, y, _paint)

                oldX = x
                oldY = y
            }

            _buffer = ShortArray(0)

            holder.unlockCanvasAndPost(canvas)
        } catch (e: Exception) {
            Log.e(this.javaClass.name, "doDraw", e)
        }
    }
}