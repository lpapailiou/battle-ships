package battleships.esa.ffhs.ch.ui.drawable

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import battleships.esa.ffhs.ch.R

class Board(
    context: Context, attributes: AttributeSet
) : View(context, attributes) {

    // TODO: outsource to view attribute?
    val BOARD_SIZE = 10
    val STROKE_WIDTH = 2f

    // TODO: outsource into viewmodel
    var ships: List<Ship>
    val shipSizes: IntArray = intArrayOf(4, 3, 3, 2, 2, 2, 1, 1, 1, 1)

    var paint: Paint

    init {
        ships = initShips()
        paint = initPaint()
    }

    private fun initShips(): List<Ship> {
        return shipSizes.mapIndexed { index, size ->
            Ship(
                index,
                null,
                size,
                null,
                setOf()
            )
        }.toList()
    }

    private fun initPaint(
    ): Paint {
        return Paint(Paint.ANTI_ALIAS_FLAG).apply {
            this.color = ContextCompat.getColor(context, R.color.colorAccent)
            this.style = Paint.Style.STROKE
            strokeWidth = STROKE_WIDTH
        }
    }

    override fun onDraw(canvas: Canvas) {
        // vertical lines
        for(i in 0..BOARD_SIZE) {
            val x = (width.toFloat() / BOARD_SIZE) * i
            canvas.drawLine(x, 0f, x, height.toFloat(), paint)
        }

        // horizontal lines
        for(i in 0..BOARD_SIZE) {
            val y = (height.toFloat() / BOARD_SIZE) * i
            canvas.drawLine(0f, y, width.toFloat(), y, paint)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)


    }
}


