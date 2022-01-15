package com.example.joystickview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.Nullable

/**
 * https://github.com/monotics/joystick/blob/master/joystick/src/main/java/com/monotics/view/joystick/JoystickView.kt
 * Study CustomView with example
 */
class JoystickView : View, Runnable {
    /**
     * constructors
     */
    constructor(context: Context) : super(context)
    constructor(context: Context, @Nullable attrs: AttributeSet?) : super(context, attrs)
    constructor(
        context: Context,
        @Nullable attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr)

    /**
     *  properties
     */
    private val mBackgroundPaint: Paint = Paint()
    private val mStickPaint: Paint = Paint()

    private var mBackgroundColor: Int = 0
    private var mStickColor: Int = 0

    private var mPosX: Float = 0.0f
    private var mPosY: Float = 0.0f

    private var mCenterX: Float = 0.0f
    private var mCenterY: Float = 0.0f

    private var mStickRatio = 0.0f
    private var mBackgroundRatio = 0.0f

    private var mStickRadius = 0.0f
    private var mBackgroundRadius = 0.0f

    private var moveListener: OnMoveListener? = null

    private var useSpring = true

    private var mThread = Thread(this)

    private var moveUpdateInterval = DEFAULT_UPDATE_INTERVAL


    override fun onDraw(canvas: Canvas?) {
        with(canvas) {
            this?.drawCircle(width / 2f, width / 2f, mBackgroundRadius, mBackgroundPaint)
            this?.drawCircle(mPosX, mPosY, mStickRadius, mStickPaint)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // 측정된 값을 설정하여 뷰의 크기를 특정 너비와 높이로 조정
        val d = measure(widthMeasureSpec).coerceAtMost(measure(heightMeasureSpec))
        setMeasuredDimension(d, d)
    }

    private fun measure(measureSpec: Int): Int {
        return if (MeasureSpec.getMode(measureSpec) == MeasureSpec.UNSPECIFIED) {
            // 경계가 지정되지 않은 경우 기본 크기(200)를 반환
            DEFAULT_SIZE
        } else {
            // 사용 가능한 공간을 채우고 싶을 때, 항상 사용 가능한 전체 범위를 반환
            MeasureSpec.getSize(measureSpec)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        mPosX = event!!.x
        mPosY = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {}
            MotionEvent.ACTION_UP -> {}
        }
        return true
    }

    override fun run() {
        TODO("Not yet implemented")
    }

    interface OnMoveListener {
        fun onMove(x: Int, y: Int)
    }

    companion object {
        private val TAG = JoystickView::class.java.name
        private const val DEFAULT_SIZE = 240
        private const val DEFAULT_UPDATE_INTERVAL = 50
    }
}

