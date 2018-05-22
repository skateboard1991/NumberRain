package com.skateboard.numberrain

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class NumberRainItem(context: Context, attrs: AttributeSet?) : View(context, attrs)
{
    private lateinit var paint: Paint

    var textSize = 15 * resources.displayMetrics.density
        set(value)
        {
            field = value
            if (isAttachedToWindow)
            {
                postInvalidate()
            }
        }

    var normalColor: Int = Color.GREEN
        set(value)
        {
            field = value
            if (isAttachedToWindow)
            {
                postInvalidate()
            }
        }

    var hightLightColor: Int = Color.YELLOW
        set(value)
        {
            field = value
            if (isAttachedToWindow)
            {
                postInvalidate()
            }
        }

    private var nowHeight = 0f

    private var hightLightNumIndex = 0

    var startOffset = 0L
        set(value)
        {
            field = value
            if (isAttachedToWindow)
            {
                postInvalidate()
            }
        }

    constructor(context: Context) : this(context, null)

    init
    {
        if (attrs != null)
        {
            parseAttrs(attrs)
        }
        initPaint()
    }

    private fun parseAttrs(attrs: AttributeSet)
    {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.NumberRainItem)
        normalColor = typedArray.getColor(R.styleable.NumberRainItem_normalColor, normalColor)
        hightLightColor = typedArray.getColor(R.styleable.NumberRainItem_highLightColor, hightLightColor)
        startOffset = typedArray.getInt(R.styleable.NumberRainItem_startOffset, 0).toLong()
        textSize = typedArray.getDimension(R.styleable.NumberRainItem_textSize, textSize)
        typedArray.recycle()
    }

    private fun initPaint()
    {
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
    }

    override fun onDraw(canvas: Canvas?)
    {
        super.onDraw(canvas)
        configPaint()
        canvas?.let {

            if (isShowAllNumber())
            {
                drawTotalNumbers(it)
            } else
            {
                drawPartNumbers(it)
            }

        }
    }

    private fun configPaint()
    {
        paint.textSize = textSize
        paint.color = normalColor
    }

    private fun isShowAllNumber(): Boolean
    {
        return nowHeight >= height
    }

    private fun drawPartNumbers(canvas: Canvas)
    {
        val count = (nowHeight / textSize).toInt()
        nowHeight += textSize
        drawNumbers(canvas, count)

    }

    private fun drawTotalNumbers(canvas: Canvas)
    {
        val count = (height / textSize).toInt()
        drawNumbers(canvas, count)
    }

    private fun drawNumbers(canvas: Canvas, count: Int)
    {
        if (count == 0)
        {
            postInvalidateDelayed(startOffset)
        } else
        {
            var offset = 0f
            for (i in 0 until count)
            {
                val text = ((Math.random() * 9).toInt()).toString()

                if (hightLightNumIndex == i)
                {
                    paint.color = hightLightColor
                    paint.setShadowLayer(10f, 0f, 0f, hightLightColor)

                } else
                {
                    paint.color = normalColor
                    paint.setShadowLayer(10f, 0f, 0f, normalColor)
                }

                canvas.drawText(text, 0f, textSize + offset, paint)
                offset += textSize
            }

            if (!isShowAllNumber())
            {
                hightLightNumIndex++
            } else
            {
                hightLightNumIndex = (++hightLightNumIndex) % count
            }
            postInvalidateDelayed(100L)
        }
    }
}
