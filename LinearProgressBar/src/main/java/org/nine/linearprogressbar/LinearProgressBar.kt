package org.nine.linearprogressbar

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import kotlin.math.roundToInt


class LinearProgressBar : View {

    private val textBound = Rect()

    private var mProgress = 0
    private var mBackground = Color.GRAY
    private var mProgressColor = Color.CYAN
    private var mTextColor = Color.WHITE
    private var mTextSize = 40f

    private var mWidth = 0
    private var progressRect = RectF()
    private var backgroundRect = RectF()

    private val paint = Paint().apply {
        color = mProgressColor
        style = Paint.Style.FILL
    }
    private val paintBackground = Paint().apply {
        color = mBackground
        style = Paint.Style.FILL
    }
    private val textPaint = Paint().apply {
        color = mTextColor
        textSize = 40f
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.LinearProgressBar)
        mProgress = a.getInt(R.styleable.LinearProgressBar_progressValue, 0)

        mProgressColor = a.getColor(R.styleable.LinearProgressBar_progressColor, mProgressColor)
        mBackground = a.getColor(R.styleable.LinearProgressBar_progressBackgroundColor, mBackground)
        mTextColor = a.getColor(R.styleable.LinearProgressBar_textColor, mTextColor)
        mTextSize =
            a.getDimensionPixelSize(R.styleable.LinearProgressBar_textSize, mTextSize.toInt())
                .toFloat()

        a.recycle()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        val a =
            context.obtainStyledAttributes(attrs, R.styleable.LinearProgressBar, defStyleAttr, 0)

        mProgressColor = a.getColor(R.styleable.LinearProgressBar_progressColor, mProgressColor)
        mBackground = a.getColor(R.styleable.LinearProgressBar_progressBackgroundColor, mBackground)
        mTextColor = a.getColor(R.styleable.LinearProgressBar_textColor, mTextColor)
        mTextSize =
            a.getDimensionPixelSize(R.styleable.LinearProgressBar_textSize, mTextSize.toInt())
                .toFloat()

        a.recycle()
    }

    private fun init() {
        paint.color = mProgressColor
        paintBackground.color = mBackground
        textPaint.apply {
            color = mTextColor
            textSize = mTextSize
        }

        progressRect = RectF(0f, 0f, (mProgress * mWidth) / 100f, 25f)
        backgroundRect = RectF(0f, 0f, mWidth.toFloat(), 25f)

        setProgress(mProgress)
    }

    fun setProgress(progress: Int) {
        mProgress = progress
        val to = (mProgress * mWidth) / 100f
        val animator = ValueAnimator.ofFloat(progressRect.right, to)
        animator.duration = 2000
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

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRoundRect(
            backgroundRect, 20f, 20f, paintBackground
        )
        canvas.drawRoundRect(
            progressRect, 20f, 20f, paint
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
        canvas.drawText(
            text.toInt().toString(),
            x.toFloat(),
            (backgroundRect.bottom.toInt()) + textBound.height() * 2f,
            textPaint
        )
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)


        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        textPaint.getTextBounds("H", 0, 1, textBound)

        val desiredHeight =
            (backgroundRect.bottom.toInt()) + textBound.height() * 3
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