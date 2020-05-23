package ch.ffhs.esa.battleships.ui.ship

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import ch.ffhs.esa.battleships.R
import ch.ffhs.esa.battleships.business.BOARD_SIZE
import ch.ffhs.esa.battleships.business.ship.ShipModel
import ch.ffhs.esa.battleships.data.ship.Direction
import javax.inject.Inject

class ShipPainter @Inject constructor(
    context: Context,
    attributes: AttributeSet
) : View(context, attributes) {

    companion object {
        const val STROKE_WIDTH = 10f
    }

    private val paint: Paint
    private val errPaint: Paint
    private val hiddenPaint: Paint

    init {
        paint = initPaint(R.color.colorAccent)
        errPaint = initPaint(R.color.colorComplementary)
        hiddenPaint = initPaint(R.color.colorBackground)
    }

    // TODO: Move heavy work out of draw loop
    fun draw(canvas: Canvas, ship: ShipModel) {
        if (!ship.isVisible) {
            return
        }

        val gridWidth = canvas.width.toFloat() / BOARD_SIZE.toFloat()

        var startX = gridWidth * ship.x
        var startY = gridWidth * ship.y

        val endX: Float
        val endY: Float

        when (ship.direction) {
            Direction.UP -> {
                endY = startY + gridWidth
                startY = startY + gridWidth - (gridWidth * ship.size)
                endX = startX + gridWidth
            }
            Direction.DOWN -> {
                endY = startY + gridWidth * ship.size
                endX = startX + gridWidth
            }
            Direction.RIGHT -> {
                endX = startX + gridWidth * ship.size
                endY = startY + gridWidth
            }
            Direction.LEFT -> {
                endX = startX + gridWidth
                startX = startX + gridWidth - (gridWidth * ship.size)
                endY = startY + gridWidth
            }
            else -> {
                throw Exception("Direction does not exist!")
            }
        }

        val oval = RectF(startX, startY, endX, endY)
        val insetWith: Float = STROKE_WIDTH * (3 / 2)
        oval.inset(insetWith, insetWith)                 // resize ships to create padding effect


        if (ship.isPositionValid) {
            canvas.drawOval(oval, paint)
            return
        }

        canvas.drawOval(oval, errPaint)
    }

    private fun initPaint(id: Int): Paint {
        return Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = ContextCompat.getColor(context, id)
            style = Paint.Style.FILL
            strokeWidth =
                STROKE_WIDTH
            isAntiAlias = true
            isDither = true
        }
    }
}
