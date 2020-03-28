package battleships.esa.ffhs.ch.ui.drawable

import android.content.ClipData
import android.content.ClipDescription
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import battleships.esa.ffhs.ch.R
import battleships.esa.ffhs.ch.entity.Shot
import battleships.esa.ffhs.ch.ui.viewmodel.ShipViewModel

class ShotPainter(
    context: Context, attributes: AttributeSet
) : View(context, attributes) {

    companion object {
        const val STROKE_WIDTH = 10f
    }

    val waterPaint: Paint
    val shotInnerPaint: Paint
    val shotOuterPaint: Paint

    init {
        //waterPaint = initPaint(R.color.colorAccent)
        waterPaint = initLinePaint(R.color.colorAccent)
        shotInnerPaint = initPaint(R.color.colorComplementary)
        shotOuterPaint = initLinePaint(R.color.colorComplementary)
    }

    private fun initPaint(id: Int): Paint {
        return Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = ContextCompat.getColor(context, id)
            style = Paint.Style.FILL
            strokeWidth = STROKE_WIDTH
        }
    }

    private fun initLinePaint(id: Int): Paint {
        return Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = ContextCompat.getColor(context, id)
            style = Paint.Style.STROKE
            strokeWidth = STROKE_WIDTH/4
        }
    }

    fun draw(shot: Shot, canvas: Canvas) {
        val gridWidth = canvas.width.toFloat() / BOARD_SIZE.toFloat()

        var startX = gridWidth * shot.point.col
        var startY = gridWidth * shot.point.row

        var endX = startX + gridWidth
        var endY = startY + gridWidth

        var innerShotOval: RectF = RectF(startX, startY, endX, endY)
        var outerShotOval: RectF = RectF(startX, startY, endX, endY)
        val innerInsetWith: Float = STROKE_WIDTH*5
        val outerInsetWidth: Float = STROKE_WIDTH*2
        innerShotOval.inset(innerInsetWith,innerInsetWith)
        outerShotOval.inset(outerInsetWidth,outerInsetWidth)
        if (shot.isHit) {
            canvas.drawOval(innerShotOval, shotInnerPaint)
            canvas.drawOval(outerShotOval, shotOuterPaint)
        } else {
            drawWater(startX, endX, startY, endY, canvas)
        }
    }

    fun drawWater(startX: Float, endX: Float, startY: Float, endY: Float, canvas: Canvas) {
        var yList: MutableList<Float> = mutableListOf()
        var lineCount: Int = 3
        var yOffset = (endY - startY) / (lineCount+1)
        var y = startY + ((endY - startY)/lineCount)/1.3f
        for (i: Int in 0..lineCount-1) {
            yList.add(y)
            y += yOffset
        }
        for(i in yList) {
            canvas.drawLine(startX, i, endX, i, waterPaint)
        }
    }
}
