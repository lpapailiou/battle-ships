package battleships.esa.ffhs.ch.ui.drawable

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import battleships.esa.ffhs.ch.R
import battleships.esa.ffhs.ch.ui.viewmodel.ShipViewModel

class ShipPainter(
    context: Context, attributes: AttributeSet
) : View(context, attributes) {

    companion object {
        const val STROKE_WIDTH = 10f
    }

    val paint: Paint
    val errPaint: Paint
    val hiddenPaint: Paint

    init {
        paint = initPaint(R.color.colorAccent)
        errPaint = initPaint(R.color.colorComplementary)
        //hiddenPaint = initPaint(R.color.colorPrimaryDark)       // TODO: switch to black later. shlightly visible now for easier testing
        hiddenPaint = initPaint(R.color.colorBackground)
    }

    fun draw(shipViewModel: ShipViewModel, canvas: Canvas) {
        val gridWidth = canvas.width.toFloat() / BOARD_SIZE.toFloat()

        var startX = gridWidth * shipViewModel.bowCell.x
        var startY = gridWidth * shipViewModel.bowCell.y

        var endX: Float = 0f
        var endY: Float = 0f

        when (shipViewModel.direction) {
            Direction.UP -> {
                endY = startY + gridWidth
                startY = startY + gridWidth - (gridWidth * shipViewModel.size)
                endX = startX + gridWidth
            }
            Direction.DOWN -> {
                endY = startY + gridWidth * shipViewModel.size
                endX = startX + gridWidth
            }
            Direction.RIGHT -> {
                endX = startX + gridWidth * shipViewModel.size
                endY = startY + gridWidth
            }
            Direction.LEFT -> {
                endX = startX + gridWidth
                startX = startX + gridWidth - (gridWidth * shipViewModel.size)
                endY = startY + gridWidth
            }
            else -> {
                throw Exception("Direction does not exist!")
            }
        }

        var oval: RectF = RectF(startX, startY, endX, endY)
        val insetWith: Float = STROKE_WIDTH * (3 / 2)
        oval.inset(insetWith, insetWith)                 // resize ships to create padding effect

        if (shipViewModel.isHidden()) {
            canvas.drawOval(oval, hiddenPaint)
        } else if (shipViewModel.isPositionValid() && !shipViewModel.isSunken()) {
            canvas.drawOval(oval, paint)
        } else {
            canvas.drawOval(oval, errPaint)
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
}
