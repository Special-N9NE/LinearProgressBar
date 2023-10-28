package org.nine.linearprogressbar

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.Typeface
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import kotlin.math.roundToInt


class LinearProgressBar : View {

    private val textBound = Rect()

    private var mProgress = 0
    private var mBackground = Color.GRAY
    private var mProgressColor = Color.CYAN
    private var mGradient = false
    private var mGradientStartColor = Color.CYAN
    private var mGradientEndColor = Color.CYAN
    private var mTextColor = Color.WHITE
    private var mTextSize = 40f
    private var mProgressHeight = 25f
    private var mRadius = 20f
    private var mDuration = 2000
    private var mTextVisibility = VISIBLE
    private var mFontFamily = 0

    private var mWidth = 0
    private var progressRect = RectF()
    private var backgroundRect = RectF()

    private val paint = Paint()
    private val paintBackground = Paint()
    private val textPaint = Paint()

    constructor(context: Context) : super(context)

    @RequiresApi(Build.VERSION_CODES.O)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.LinearProgressBar)
        mProgress = a.getInt(R.styleable.LinearProgressBar_progressValue, 0)

        mProgressColor = a.getColor(R.styleable.LinearProgressBar_progressColor, mProgressColor)
        mBackground = a.getColor(R.styleable.LinearProgressBar_progressBackgroundColor, mBackground)
        mTextColor = a.getColor(R.styleable.LinearProgressBar_textColor, mTextColor)
        mTextSize =
            a.getDimensionPixelSize(R.styleable.LinearProgressBar_textSize, mTextSize.toInt())
                .toFloat()
        mProgressHeight = a.getDimensionPixelSize(
            R.styleable.LinearProgressBar_progressHeight, mProgressHeight.toInt()
        ).toFloat()
        mRadius = a.getDimensionPixelSize(
            R.styleable.LinearProgressBar_radius, mRadius.toInt()
        ).toFloat()
        mDuration = a.getInt(R.styleable.LinearProgressBar_animationDuration, mDuration)
        mTextVisibility = a.getInt(R.styleable.LinearProgressBar_textVisibility, mTextVisibility)
        mFontFamily = a.getResourceId(R.styleable.LinearProgressBar_font, 0)
        mGradient = a.getBoolean(R.styleable.LinearProgressBar_gradient, false)
        mGradientStartColor =
            a.getColor(R.styleable.LinearProgressBar_gradientStartColor, mGradientStartColor)
        mGradientEndColor =
            a.getColor(R.styleable.LinearProgressBar_gradientEndColor, mGradientEndColor)

        a.recycle()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {
        val a =
            context.obtainStyledAttributes(attrs, R.styleable.LinearProgressBar, defStyleAttr, 0)

        mProgressColor = a.getColor(R.styleable.LinearProgressBar_progressColor, mProgressColor)
        mBackground = a.getColor(R.styleable.LinearProgressBar_progressBackgroundColor, mBackground)
        mTextColor = a.getColor(R.styleable.LinearProgressBar_textColor, mTextColor)
        mTextSize =
            a.getDimensionPixelSize(R.styleable.LinearProgressBar_textSize, mTextSize.toInt())
                .toFloat()
        mProgressHeight = a.getDimensionPixelSize(
            R.styleable.LinearProgressBar_progressHeight, mProgressHeight.toInt()
        ).toFloat()
        mRadius = a.getDimensionPixelSize(
            R.styleable.LinearProgressBar_radius, mRadius.toInt()
        ).toFloat()
        mDuration = a.getInt(R.styleable.LinearProgressBar_animationDuration, mDuration)
        mTextVisibility = a.getInt(R.styleable.LinearProgressBar_textVisibility, mTextVisibility)
        mFontFamily = a.getResourceId(R.styleable.LinearProgressBar_font, 0)
        mGradient = a.getBoolean(R.styleable.LinearProgressBar_gradient, false)
        mGradientStartColor =
            a.getColor(R.styleable.LinearProgressBar_gradientStartColor, mGradientStartColor)
        mGradientEndColor =
            a.getColor(R.styleable.LinearProgressBar_gradientEndColor, mGradientEndColor)

        a.recycle()
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

        paintBackground.apply {
            color = mBackground
            style = Paint.Style.FILL
        }

        textPaint.apply {
            color = mTextColor
            textSize = mTextSize
            if (mFontFamily != 0) {
                try {
                    val tp = ResourcesCompat.getFont(context, mFontFamily)
                    this.typeface = Typeface.create(tp, Typeface.NORMAL)
                } catch (e: Exception) {
                    Log.e("LinearProgressBar", e.message.toString())
                    e.printStackTrace()
                    throw e
                }
            }
        }

        progressRect = RectF(0f, 0f, (mProgress * mWidth) / 100f, mProgressHeight)
        backgroundRect = RectF(0f, 0f, mWidth.toFloat(), mProgressHeight)

        setProgress(mProgress)
    }

    fun setProgress(progress: Int) {
        mProgress = progress
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

    fun setProgressBackgroundColor(color: Int) {
        mBackground = color
        init()
    }

    fun setProgressColor(color: Int) {
        mProgressColor = color
        init()
    }

    fun setTextColor(color: Int) {
        mTextColor = color
        init()
    }

    fun setAnimationDuration(duration: Int) {
        mDuration = duration
    }

    fun setTextVisibility(visibility: Int) {
        if (visibility in listOf(VISIBLE, INVISIBLE, GONE)) mTextVisibility = visibility
    }

    fun setFont(resourceId: Int) {
        try {
            ResourcesCompat.getFont(context, resourceId)
            mFontFamily = resourceId
            init()
        } catch (e: Exception) {
            Log.e("LinearProgressBar", e.message.toString())
            e.printStackTrace()
            throw e
        }
    }

    fun getProgress(): Int {
        return mProgress
    }

    fun getProgressBackgroundColor(): Int {
        return mBackground
    }

    fun getProgressColor(): Int {
        return mProgressColor
    }

    fun getTextColor(): Int {
        return mTextColor
    }

    fun getProgressHeight(): Float {
        return mProgressHeight
    }

    fun getRadius(): Float {
        return mRadius
    }

    fun getAnimationDuration(): Int {
        return mDuration
    }

    fun getTextVisibility(): Int {
        return mTextVisibility
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