package com.dichotome.samplecustomview.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class CustomSurfaceView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) : SurfaceView(context, attributeSet, defStyle), SurfaceHolder.Callback, CoroutineScope {

    init {
        holder?.addCallback(this)
    }

    private var isRunning = false

    private val job = Job()

    override val coroutineContext: CoroutineContext = Dispatchers.IO + job

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {

    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        isRunning = false
        var retry = true
        while (retry) {
            launch {
                job.join()
            }
            retry = false
        }
        coroutineContext.cancel()
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        isRunning = true
        launch(block = onSurfaceCreated)
    }

    private val onSurfaceCreated: suspend CoroutineScope.() -> Unit = {
        var canvas: Canvas?
        var color = Color.GREEN
        while (isRunning) {
            canvas = null
            try {
                canvas = holder?.lockCanvas(null)
                canvas?.drawColor(color++) ?: continue
            } finally {
                canvas?.let {
                    holder?.unlockCanvasAndPost(it)
                }
            }
        }
    }
}