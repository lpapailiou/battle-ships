package battleships.esa.ffhs.ch.ui.drawable

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import battleships.esa.ffhs.ch.R

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
        waterPaint = initLinePaint(R.color.colorAccent)
        shotInnerPaint = initPaint(R.color.colorComplementary)
        shotOuterPaint = initLinePaint(R.color.colorComplementary)
    }

    fun draw(shot: Shot, canvas: Canvas) {
        val gridWidth = canvas.width.toFloat() / BOARD_SIZE.toFloat()

        var startX = gridWidth * shot.cell.x
        var startY = gridWidth * shot.cell.y

        var endX = startX + gridWidth
        var endY = startY + gridWidth

        var with = endX - startX

        var innerShotOval: RectF = RectF(startX, startY, endX, endY)
        var outerShotOval: RectF = RectF(startX, startY, endX, endY)
        val innerInsetWith: Float = with * 0.3f
        val outerInsetWidth: Float = with * 0.15f
        innerShotOval.inset(innerInsetWith, innerInsetWith)
        outerShotOval.inset(outerInsetWidth, outerInsetWidth)

        if (shot.isHit) {
            if (shot.isDrawable()) {
                canvas.drawOval(innerShotOval, shotInnerPaint)
                canvas.drawOval(outerShotOval, shotOuterPaint)
            }
        } else {
            drawWater(startX, endX, startY, endY, canvas)
        }
    }

    fun drawWater(startX: Float, endX: Float, startY: Float, endY: Float, canvas: Canvas) {
        var yList: MutableList<Float> = mutableListOf()
        var lineCount: Int = 3
        var yOffset = (endY - startY) / (lineCount + 1)
        var y = startY + ((endY - startY) / lineCount) / 1.3f
        for (i: Int in 0..lineCount - 1) {
            yList.add(y)
            y += yOffset
        }
        for (i in yList) {
            canvas.drawLine(startX, i, endX, i, waterPaint)
        }
    }

    // ----------------------------- create paints -----------------------------

    private fun initPaint(id: Int): Paint {
        return Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = ContextCompat.getColor(context, id)
            style = Paint.Style.FILL
            strokeWidth = STROKE_WIDTH
            isAntiAlias = true
            isDither = true
        }
    }

    private fun initLinePaint(id: Int): Paint {
        return Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = ContextCompat.getColor(context, id)
            style = Paint.Style.STROKE
            strokeWidth = STROKE_WIDTH / 4
            isAntiAlias = true
            isDither = true
        }
    }
}
