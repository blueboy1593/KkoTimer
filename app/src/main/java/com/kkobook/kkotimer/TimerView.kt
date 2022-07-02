package com.kkobook.kkotimer

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.RequiresApi
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2

class TimerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    lateinit var viewModel: TimerViewModel

    private lateinit var center: PointF
    private lateinit var angleCenter: PointF
    private lateinit var rect: RectF
    private var sweepAngle: Float = 0f
    private val radius = 400f
    private lateinit var kkobookBitmap: Bitmap

    private val circlePaint =
        Paint().apply {
            isAntiAlias = true
            color = Color.GRAY
            style = Paint.Style.STROKE
            strokeWidth = 60f
        }

    @RequiresApi(Build.VERSION_CODES.M)
    private val progressPaint =
        Paint().apply {
            isAntiAlias = true
            color = context.resources.getColor(R.color.kkobook, null)
            style = Paint.Style.STROKE
            strokeWidth = 60f
            strokeCap = Paint.Cap.ROUND
        }

    @JvmName("setViewModel1")
    fun setViewModel(viewModel: TimerViewModel) {
        this.viewModel = viewModel
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        center = PointF(w/2f, h/2f)
        val coordinate = IntArray(2)
        getLocationOnScreen(coordinate)
        angleCenter = PointF(coordinate[0] + center.x, coordinate[1] + center.y)
        rect = RectF(
            center.x - radius,
            center.y - radius,
            center.x + radius,
            center.y + radius
        )
        kkobookBitmap = BitmapFactory.decodeResource(resources, R.drawable.kkobook_eye_open)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawArc(rect, 0f, 360f, true, circlePaint)
        canvas?.drawArc(rect, -90f, sweepAngle, false, progressPaint)

        canvas?.drawBitmap(kkobookBitmap, null, rect, null)
    }

    fun drawEyeOpen() {
        kkobookBitmap = BitmapFactory.decodeResource(resources, R.drawable.kkobook_eye_open)
        invalidate()
    }

    fun drawEyeClose() {
        kkobookBitmap = BitmapFactory.decodeResource(resources, R.drawable.kkobook_eye_close)
        invalidate()
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
        val timeInDegree = computeAngle(touchCoordinate, false)
        viewModel.setTime(timeInDegree)
    }

    private fun onActionMove(touchCoordinate: PointF) {
        val timeInDegree = computeAngle(touchCoordinate, true)
        viewModel.setTime(timeInDegree)
    }

    private fun computeAngle(touchCoordinate: PointF, isActionMove: Boolean): Float {
        val xGap = touchCoordinate.x - angleCenter.x
        val yGap = angleCenter.y - touchCoordinate.y
        val radian = atan2(xGap, yGap)
        val degree = (radian * 180 / PI).toFloat()

        val currentAngle = if (degree >= 0) {
            degree
        } else {
            degree + 360f
        }
        if (isActionMove && abs(sweepAngle - currentAngle) > 50) {
            // 0에서 360 / 360에서 0. 12시를 드래그로 넘기려고 하는 케이스. 스킵해준다.
        } else {
            sweepAngle = currentAngle
        }

        return sweepAngle
    }

    fun resetTimer() {
        sweepAngle = 0f
        invalidate()
    }
}