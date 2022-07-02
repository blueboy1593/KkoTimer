package com.kkobook.kkotimer

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import kotlin.math.PI
import kotlin.math.atan2

class TimerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    lateinit var viewModel: TimerViewModel

    private lateinit var center: PointF
    private lateinit var rect: RectF
    private var sweepAngle: Float = 0f
    private val radius = 400f

    private val circlePaint =
        Paint().apply {
            isAntiAlias = true
            color = Color.GRAY
            style = Paint.Style.STROKE
            strokeWidth = 60f
        }

    private val progressPaint =
        Paint().apply {
            isAntiAlias = true
            color = Color.BLUE
            style = Paint.Style.STROKE
            strokeWidth = 60f
            strokeCap = Paint.Cap.ROUND
        }

    private val samplePaint =
        Paint().apply {
            isAntiAlias = true
            color = Color.RED
            style = Paint.Style.FILL
        }

    @JvmName("setViewModel1")
    fun setViewModel(viewModel: TimerViewModel) {
        this.viewModel = viewModel
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        center = PointF(w/2f, h/2f)
        rect = RectF(
            center.x - radius,
            center.y - radius,
            center.x + radius,
            center.y + radius
        )
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawArc(rect, 0f, 360f, true, circlePaint)
        canvas?.drawArc(rect, -90f, sweepAngle, false, progressPaint)

        // 운이 좋게 의도하게 나온 코드. 기억해두기
        val kkobookEyeOpen = BitmapFactory.decodeResource(resources, R.drawable.kkobook_eye_open)
        canvas?.drawBitmap(kkobookEyeOpen, null, rect, null)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            val touchCoordinate = PointF(it.rawX, it.rawY)
            when (it.action) {
                MotionEvent.ACTION_DOWN -> onActionDown(touchCoordinate)
                MotionEvent.ACTION_MOVE -> onActionMove(touchCoordinate)
            }
            invalidate()
        }
        return true
    }

    private fun onActionDown(touchCoordinate: PointF) {
        val timeInDegree = computeAngle(touchCoordinate)
        viewModel.setTime(timeInDegree)
    }

    private fun onActionMove(touchCoordinate: PointF) {
        val timeInDegree = computeAngle(touchCoordinate)
        viewModel.setTime(timeInDegree)
    }

    private fun computeAngle(touchCoordinate: PointF): Float {
        Log.d("kkog", "touchCoordinate.x : " + touchCoordinate.x + " center.x : " + center.x)
        Log.d("kkog", "touchCoordinate.y : " + touchCoordinate.y + " center.y : " + center.y)
        val xGap = touchCoordinate.x - center.x
        val yGap = center.y - touchCoordinate.y
        val radian = atan2(xGap, yGap)
        val degree = (radian * 180 / PI).toFloat()
        Log.d("kkog", "degree : $degree")

        sweepAngle = if (degree >= 0) {
            degree
        } else {
            degree + 360f
        }
        Log.d("kkog", "degree : $sweepAngle")
        return sweepAngle
    }
}