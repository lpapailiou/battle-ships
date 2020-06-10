package ch.ffhs.esa.battleships.ui.shot

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.toRect
import ch.ffhs.esa.battleships.R
import ch.ffhs.esa.battleships.business.BOARD_SIZE
import ch.ffhs.esa.battleships.business.shot.ShotModel

class ShotPainter(
    context: Context, attributes: AttributeSet
) : View(context, attributes) {

    private val shipShotImage: Drawable? = context.getDrawable(R.drawable.ship_shot);
    private val waterShotImage: Drawable? = context.getDrawable(R.drawable.water_shot);

    private val STROKE_WIDTH = 10f
    val waterPaint: Paint
    val shotInnerPaint: Paint
    val shotOuterPaint: Paint

    init {
        waterPaint = initLinePaint(R.color.colorAccent)
        shotInnerPaint = initPaint(R.color.colorComplementary)
        shotOuterPaint = initLinePaint(R.color.colorComplementary)
    }

    fun draw(canvas: Canvas, shot: ShotModel) {

        val gridWidth = canvas.width.toFloat() / BOARD_SIZE.toFloat()

        val startX = gridWidth * shot.x
        val startY = gridWidth * shot.y

        val endX = startX + gridWidth
        val endY = startY + gridWidth

        var with = endX - startX

        var innerShotOval: RectF = RectF(startX, startY, endX, endY)
        var outerShotOval: RectF = RectF(startX, startY, endX, endY)
        val innerInsetWith: Float = with * 0.3f
        val outerInsetWidth: Float = with * 0.15f
        innerShotOval.inset(innerInsetWith, innerInsetWith)
        outerShotOval.inset(outerInsetWidth, outerInsetWidth)

        if (shot.isHit) {
            if (shot.isVisible) {
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

    /*
    private fun drawShipHit(
        startX: Float,
        endX: Float,
        startY: Float,
        endY: Float,
        canvas: Canvas
    ) {
        val imageBounds = RectF(startX, startY, endX, endY)

        shipShotImage!!.bounds = imageBounds.toRect()
        shipShotImage.draw(canvas)
    }

    private fun drawWaterHit(
        startX: Float,
        endX: Float,
        startY: Float,
        endY: Float,
        canvas: Canvas
    ) {
        val imageBounds = RectF(startX, startY, endX, endY)

        waterShotImage!!.bounds = imageBounds.toRect()
        waterShotImage.draw(canvas)
    }*/
}
