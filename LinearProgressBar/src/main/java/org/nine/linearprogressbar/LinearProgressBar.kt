package org.nine.linearprogressbar

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.os.Build
import android.util.AttributeSet
import androidx.annotation.RequiresApi
import kotlin.math.roundToInt


class LinearProgressBar : BaseProgressBar {

    private var mWidth = 0

    @RequiresApi(Build.VERSION_CODES.O)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    @RequiresApi(Build.VERSION_CODES.O)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    )

    override fun callInit() {
        super.callInit()
        init()
    }

    private fun init() {
        if (mGradient) {
            paint.apply {
                shader = LinearGradient(
                    0f,
                    0f,
                    mWidth.toFloat(),
                    0f,
                    mGradientStartColor,
                    mGradientEndColor,
                    Shader.TileMode.CLAMP
                )
            }
        } else {
            paint.apply {
                color = mProgressColor
                style = Paint.Style.FILL
            }
        }

        progressRect = RectF(0f, 0f, (mProgress * mWidth) / 100f, mThickness)
        backgroundRect = RectF(0f, 0f, mWidth.toFloat(), mThickness)

        setProgress(mProgress)
    }

    override fun setProgress(progress: Int) {
        super.setProgress(progress)
        val to = (mProgress * mWidth) / 100f
        val animator = ValueAnimator.ofFloat(progressRect.right, to)
        animator.duration = mDuration.toLong()
        animator.addUpdateListener {
            progressRect = RectF(
                progressRect.left,
                progressRect.top,
                it.animatedValue.toString().toFloat(),
                progressRect.bottom
            )
            invalidate()
        }
        animator.start()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawRoundRect(
            backgroundRect, mRadius, mRadius, paintBackground
        )
        canvas.drawRoundRect(
            progressRect, mRadius, mRadius, paint
        )

        var text = (progressRect.right * 100 / mWidth).toString()
        text = text.toFloat().roundToInt().toString()

        textPaint.getTextBounds(text, 0, text.length, textBound)
        textBound.height()

        val x = if (progressRect.right < textBound.width()) {
            (textBound.width() / 10)
        } else if (progressRect.right > mWidth - textBound.width()) {
            mWidth - textBound.width() - (textBound.width() / 10)
        } else {
            progressRect.right - (textBound.width() / 2)
        }
        if (mTextVisibility == VISIBLE) {
            canvas.drawText(
                text.toInt().toString(),
                x.toFloat(),
                (backgroundRect.bottom.toInt()) + textBound.height() * 2f,
                textPaint
            )
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)


        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        textPaint.getTextBounds("H", 0, 1, textBound)

        val boundHeight = if (mTextVisibility == GONE) 0
        else textBound.height()

        val desiredHeight = (backgroundRect.bottom.toInt()) + boundHeight * 3
        val desiredWidth = backgroundRect.right.toInt()

        mWidth = when (widthMode) {
            MeasureSpec.EXACTLY -> {
                widthSize
            }

            MeasureSpec.AT_MOST -> {
                widthSize
            }

            else -> {
                desiredWidth
            }
        }
        val height = when (heightMode) {
            MeasureSpec.EXACTLY -> {
                heightSize
            }

            MeasureSpec.AT_MOST -> {
                desiredHeight.coerceAtMost(heightSize)
            }

            else -> {
                desiredHeight
            }
        }

        init()
        setMeasuredDimension(mWidth, height)

    }
}