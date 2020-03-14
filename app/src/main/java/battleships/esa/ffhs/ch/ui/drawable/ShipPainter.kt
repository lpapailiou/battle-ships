package battleships.esa.ffhs.ch.ui.drawable

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import battleships.esa.ffhs.ch.ui.viewmodel.ShipViewModel

class ShipPainter() {

    companion object {
        const val STROKE_WIDTH = 10f
    }

    val paint: Paint

    init {
        paint = initPaint()
    }

    private fun initPaint(
    ): Paint {
        return Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.BLUE
            style = Paint.Style.FILL
            strokeWidth = STROKE_WIDTH
        }
    }

    fun draw(shipViewModel: ShipViewModel, canvas: Canvas) {
        val gridWidth = canvas.width.toFloat() / BOARD_SIZE.toFloat()

        val startX = gridWidth * shipViewModel.position.col
        val startY = gridWidth * shipViewModel.position.row

        val endX: Float
        val endY: Float

        when(shipViewModel.direction) {
            Direction.DOWN, Direction.UP -> {
                endY = startY + gridWidth * shipViewModel.size
                endX = startX + gridWidth
            }
            Direction.LEFT, Direction.RIGHT -> {
                endX = startX + gridWidth * shipViewModel.size
                endY = startY + gridWidth
            }
            else ->{
                throw Exception("Direction does not exist!")
            }
        }


        canvas.drawOval(startX, startY, endX, endY, paint)
    }
}
