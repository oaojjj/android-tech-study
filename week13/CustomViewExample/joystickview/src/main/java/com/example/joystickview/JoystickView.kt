package com.example.joystickview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.Nullable

class JoystickView : View {
    constructor(context: Context) : super(context)
    constructor(context: Context, @Nullable attrs: AttributeSet?) : super(context, attrs)
    constructor(
        context: Context,
        @Nullable attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr)

    private var mPosX: Float = 0.0f
    private var mPosY: Float = 0.0f

    private var mCenterX: Float = 0.0f
    private var mCenterY: Float = 0.0f

    private var stickRatio = 0.0f
    private var baseRatio = 0.0f

    private var stickRadius = 0.0f
    private var baseRadius = 0.0f

}

