package battleships.esa.ffhs.ch.ui.drawable

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import battleships.esa.ffhs.ch.R
import battleships.esa.ffhs.ch.ui.viewmodel.ShipViewModel


class Board(
    context: Context, attributes: AttributeSet
) : View(context, attributes) {

    companion object {
        const val STROKE_WIDTH = 2f
    }

    // TODO: outsource into viewmodel
    var ships: List<ShipViewModel>
    var currentShip: ShipViewModel?
    val shipSizes: IntArray = intArrayOf(4, 3, 3, 2, 2, 2, 1, 1, 1, 1)
    val shipPainter: ShipPainter

    var paint: Paint
    var paintBackground: Paint
    var gridWidth = 0f

    init {

        ships = initShips()
        currentShip = null
        shipPainter = ShipPainter(context, attributes)
        paint = initPaint()
        paintBackground = initBackgroundPaint()
    }

    // ----------------------------- basic view handling -----------------------------

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        gridWidth = (w.toFloat() / BOARD_SIZE)
    }

    override fun onDraw(canvas: Canvas) {
        // clear canvas
        canvas.drawColor(Color.BLACK)

        println("===================== DRAWING ======================== ")
        // drawing ships first
        ships.forEach { shipViewModel ->
            shipPainter.draw(shipViewModel, canvas)
        }

        // drawing grid over ships for shadow effect
        drawGrid(canvas)
    }

    // ----------------------------- moving ships -----------------------------

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        var refresh: Boolean = false
        if (event == null) {
            return false
        }

        if (currentShip != null) {
            currentShip?.pickUp(false)
            currentShip = null
            refresh = true
        }

        var xTouch: Float
        var yTouch: Float

        xTouch = event.getX(0)
        yTouch = event.getY(0)

        var xCoord: Int = (xTouch/gridWidth).toInt()
        var yCoord: Int = (yTouch/gridWidth).toInt()

        for (ship in ships) {
            if (ship.isHere(xCoord, yCoord)) {
                println("===================== FOUND ======================== " + xCoord + ", " + yCoord)
                //currentShip = ship.getShip(xCoord, yCoord)
                ship.rotate(xCoord, yCoord)
                refresh = true
            }
        }

        if (refresh) {
            // refresh canvas
            invalidate()
        }
        return super.onTouchEvent(event)
    }

    // ----------------------------- initialization of ships -----------------------------

    private fun initShips(): List<ShipViewModel> {
        return shipSizes.mapIndexed { index, size ->

            ShipViewModel(
                index,
                Point(0, index),
                size,
                Direction.RIGHT,
                setOf()
            )
        }.toList()
    }

    // ----------------------------- create grid -----------------------------

    private fun drawGrid(canvas: Canvas) {
        // painting two grids here to obtain shadow effect
        drawGrid(canvas, paintBackground)       // wider strokes in black
        drawGrid(canvas, paint)                 // narrower strokes in radar green
    }

    private fun drawGrid(canvas: Canvas, customPaint: Paint) {
        // vertical lines
        for(i in 0..BOARD_SIZE) {
            val x = gridWidth * i
            canvas.drawLine(x, 0f, x, height.toFloat(), customPaint)
        }

        // horizontal lines
        for(i in 0..BOARD_SIZE) {
            val y = gridWidth * i
            canvas.drawLine(0f, y, width.toFloat(), y, customPaint)
        }
    }

    // ----------------------------- create paints -----------------------------

    private fun initPaint(
    ): Paint {
        return Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = ContextCompat.getColor(context, R.color.colorAccent)
            style = Paint.Style.STROKE
            strokeWidth = Companion.STROKE_WIDTH
        }
    }

    private fun initBackgroundPaint(
    ): Paint {
        return Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.BLACK
            style = Paint.Style.STROKE
            strokeWidth = Companion.STROKE_WIDTH*6f
        }
    }
}
