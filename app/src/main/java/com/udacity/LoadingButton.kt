package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates


class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var firstColor = 0
    private var loadingColor = 0
    private var buttonColor = 0
    private var heightSize = 0
    private var width = 0F
    private var withAngle = 0F
    private var txtButtonLabel = ""
    private var valueAnimator = ValueAnimator()

    private val paint = Paint()
    private val paintText = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL_AND_STROKE
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD)
    }


    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { _, _, new ->
        when (new) {
            ButtonState.Loading -> {

                Log.i("LoadingButton","Button state changed from complete to loading")

                txtButtonLabel = context.getString(R.string.button_name)
                // Show loading text (access string resource)
                valueAnimator = ValueAnimator.ofFloat(0F,widthSize).apply {
                    duration = 2200

                    addUpdateListener { animation ->
                        width = animation.animatedValue as Float
                        withAngle = animation.animatedValue as Float
                        buttonColor = loadingColor
                        invalidate()
                    }
                    start()
                }
            }
            ButtonState.Clicked -> {
                txtButtonLabel = context.getString(R.string.button_loading)

                valueAnimator = ValueAnimator.ofFloat(0F, measuredWidth.toFloat()).apply {
                    duration = 2200

                    addUpdateListener { animation ->
                        width = animation.animatedValue as Float
                        withAngle = animation.animatedValue as Float
                        buttonColor = loadingColor
                        invalidate()
                    }
                    start()
                }
            }

            ButtonState.Completed -> {
                txtButtonLabel = context.getString(R.string.button_name)
                valueAnimator.removeAllListeners()
                valueAnimator.end()

                invalidate()

                buttonColor = firstColor
                withAngle = 0f
            }
        }
    }
    private var textSize = 0F
    private var circleColor = 0
    private var textColor = 0

    init {
        isClickable = true

        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            firstColor = getColor(R.styleable.LoadingButton_Color1, 0)
            loadingColor = getColor(R.styleable.LoadingButton_Color2, 0)
            circleColor = getColor(R.styleable.LoadingButton_arcColor, 0)
            textSize = getFloat(R.styleable.LoadingButton_textSize, 0F)
            textColor = getColor(R.styleable.LoadingButton_textColor, 0)
        }
    }

    override fun performClick(): Boolean {
        if (super.performClick()) return true

        invalidate()
        return true
    }


    fun setBtnState(state: ButtonState) {
        buttonState = state
    }

    private var widthSize = 0f
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.drawColor(firstColor)
        paint.color = buttonColor
        canvas?.drawRect(
            0f,
            0f,
            width,
            measuredHeight.toFloat(),
            paint
        )

        paintText.color = textColor
        paintText.textSize = textSize
        val textHeight: Float = paint.descent() - paintText.ascent()
        val textOffset: Float = textHeight / 2 - paintText.descent()
        canvas?.drawText(
            txtButtonLabel,
            (widthSize / 2).toFloat(),
            heightSize.toFloat() / 2 + textOffset,
            paintText
        )
        // Draw the circle
        paint.color = circleColor
        val arcBounds = RectF(
            (width * 0.90 - 20).toFloat(),
            (height / 2 - 20).toFloat(),
            (width * 0.90 + 20).toFloat(),
            (height / 2 + 20).toFloat()
        )
        canvas?.drawArc(arcBounds, 0f, withAngle, true, paint)

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w.toFloat()
        heightSize = h
        setMeasuredDimension(w, h)
    }


}