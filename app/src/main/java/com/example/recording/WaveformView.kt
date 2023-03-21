package com.example.recording

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class WaveformView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val ampList = mutableListOf<Float>() // 계속해서 녹음된 데이터 리스트 - 원시 데이터
    private val rectList = mutableListOf<RectF>() // 계속해서 그려질 데이터 리스트
    private val rectWidth = 15f
    private var tick = 0
    private val redPaint = Paint().apply {
        color = Color.RED
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        for (rectF in rectList) {
            canvas?.drawRect(rectF, redPaint)
        }
    }

    fun addAmplitude(maxAmplitude: Float) {
        val amplitude = (maxAmplitude / Short.MAX_VALUE) * this.height // 0~1사이 값으로 변경 * 높이
        ampList.add(amplitude)
        rectList.clear()

        val maxRect = (this.width / rectWidth).toInt() // 전체 가로 길이에서 몇 개의 rect가 들어갈 수 있는지
        val amps = ampList.takeLast(maxRect) // 화면에 나올 수 있는 amplist = 최근에 추가된 amplist

        for ((i, amp) in amps.withIndex()) {
            val rectF = RectF()
            rectF.top = this.height / 2 - amp / 2
            rectF.bottom = rectF.top + amp
            rectF.left = i * rectWidth
            rectF.right = rectF.left + (rectWidth - 5f)
            rectList.add(rectF)
        }
        invalidate()
    }

    fun replayAmplitude() {
        rectList.clear()
        val maxRect = (this.width / rectWidth).toInt()
        val amps = ampList.take(tick).takeLast(maxRect) // amplist가 이미 쌓여있는 상태이므로 take로 가져온다.

        for ((i, amp) in amps.withIndex()) {
            val rectF = RectF()
            rectF.top = this.height / 2 - amp / 2
            rectF.bottom = rectF.top + amp
            rectF.left = i * rectWidth
            rectF.right = rectF.left + (rectWidth - 5f)
            rectList.add(rectF)
        }
        tick += 1
        invalidate()
    }

    fun clearData() {
        ampList.clear()
    }

    fun clearWave() {
        rectList.clear()
        tick = 0
        invalidate()
    }
}