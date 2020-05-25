package ch.ffhs.esa.battleships.ui.board

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import ch.ffhs.esa.battleships.R
import ch.ffhs.esa.battleships.business.BOARD_SIZE
import ch.ffhs.esa.battleships.business.board.BoardModel
import ch.ffhs.esa.battleships.business.board.Cell
import ch.ffhs.esa.battleships.ui.ship.ShipPainter
import ch.ffhs.esa.battleships.ui.shot.ShotPainter

// TODO: maybe split into each use cases: preparation, attacking and defending boards?
open class BoardView(
    context: Context,
    attributes: AttributeSet
) : View(context, attributes) {

    companion object {
        const val STROKE_WIDTH = 2f
        const val CLICK_LIMIT: Int = 6        // makes difference between click and move
    }

    var boardModel = BoardModel(null, null, null)
        set(value) {
            field = value
            invalidate()
        }


    var gridWidth: Float = 0f

    private val shotPainter: ShotPainter =
        ShotPainter(
            context,
            attributes
        )
    private val shipPainter: ShipPainter =
        ShipPainter(
            context,
            attributes
        )

    private var paint: Paint
    private var paintBackground: Paint

    init {
        paint = initPaint(R.color.colorAccent)
        paintBackground = initBackgroundPaint()
    }

    // ----------------------------- basic view handling -----------------------------

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        gridWidth = (w.toFloat() / BOARD_SIZE)
    }

    override fun onDraw(canvas: Canvas) {
        clearCanvas(canvas)

        boardModel.ships.value!!.forEach { ship ->
            shipPainter.draw(canvas, ship)
        }

        boardModel.shots.value!!.forEach { shot ->
            shotPainter.draw(canvas, shot)
        }

        drawGrid(canvas)
    }

    private fun clearCanvas(canvas: Canvas) {
        canvas.drawColor(Color.BLACK)
    }

    // ----------------------------- create grid -----------------------------

    protected fun drawGrid(canvas: Canvas) {
        // painting two grids here to obtain gap between gridlines and drawn objects
        drawGrid(canvas, paintBackground)       // wider strokes in black
        drawGrid(canvas, paint)                 // narrower strokes in radar green
    }

    protected fun drawGrid(canvas: Canvas, customPaint: Paint) {
        // vertical lines
        for (i in 0..BOARD_SIZE) {
            val x = gridWidth * i
            canvas.drawLine(x, 0f, x, height.toFloat(), customPaint)
        }

        // horizontal lines
        for (i in 0..BOARD_SIZE) {
            val y = gridWidth * i
            canvas.drawLine(0f, y, width.toFloat(), y, customPaint)
        }
    }

    protected fun initPaint(id: Int): Paint {
        return Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = ContextCompat.getColor(context, id)
            style = Paint.Style.STROKE
            strokeWidth =
                STROKE_WIDTH
            isAntiAlias = true
            isDither = true
        }
    }

    protected fun initBackgroundPaint(
    ): Paint {
        return Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.BLACK
            style = Paint.Style.STROKE
            strokeWidth = STROKE_WIDTH * 6f
            isAntiAlias = true
            isDither = true
        }
    }

    fun getCellAt(x: Float, y: Float): Cell {
        return Cell(
            (x / gridWidth).toInt(),
            (y / gridWidth).toInt()
        )
    }
}
