package battleships.esa.ffhs.ch.ui.drawable

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import battleships.esa.ffhs.ch.R
import battleships.esa.ffhs.ch.ui.game.BoardPreparationFragment
import battleships.esa.ffhs.ch.ui.viewmodel.ShipViewModel
import kotlinx.android.synthetic.main.board_preparation_fragment.view.*


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
    val CLICK_LIMIT: Int = 6        // makes difference between click and move

    var paint: Paint
    var paintBackground: Paint
    var gridWidth = 0f
    var clickCounter: Int = 0
    var offset = Point(0,0)

    init {
        ships = initShips()
        setShipsRandomly()          // set ships randomly on board
        currentShip = null
        shipPainter = ShipPainter(context, attributes)
        paint = initPaint(R.color.colorAccent)
        paintBackground = initBackgroundPaint()
    }

    // ----------------------------- basic view handling -----------------------------

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        // TODO: adjustment issue - board seems to be too large on real device. check if widht is actual fragment width
        gridWidth = (w.toFloat() / BOARD_SIZE)
    }

    override fun onDraw(canvas: Canvas) {
        // clear canvas
        canvas.drawColor(Color.BLACK)

        // drawing ships first
        ships.forEach { shipViewModel ->
            shipPainter.draw(shipViewModel, canvas)
        }

        // drawing grid over ships for shadow effect
        drawGrid(canvas)
    }


    fun validateStart(): Boolean {
        if (ships.filter{s -> !s.isPositionValid()}.count() > 0) {
            return false
        }
        return true
    }

    // ----------------------------- ship handling -----------------------------

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        var refresh: Boolean = false
        if (event == null) {
            return false
        }

        if (currentShip != null) {
            refresh = true
        }

        var xTouch: Float = event.getX(0)
        var yTouch: Float = event.getY(0)

        var pointerPosition: Point = Point((xTouch/gridWidth).toInt(), (yTouch/gridWidth).toInt())

        for (ship in ships) {
            if (ship.isHere(pointerPosition) && currentShip == null) {
                currentShip = ship.getShip(pointerPosition)
                offset = currentShip!!.getOffset(pointerPosition)
                refresh = true
            }
        }

        when (event.getActionMasked()) {
            MotionEvent.ACTION_MOVE -> {            // move ship around
                clickCounter++
                if (currentShip != null && clickCounter > CLICK_LIMIT) {
                    var oldPos = currentShip!!.getPoint()
                    currentShip!!.set(pointerPosition, offset)
                    if (!currentShip!!.isShipOnBoard()) {
                        currentShip!!.set(oldPos)
                    }
                }
                refreshView(refresh)
                return true
            }
            MotionEvent.ACTION_UP -> {              // rotate ship
                if (currentShip != null && clickCounter <= CLICK_LIMIT) {
                    currentShip!!.rotate(pointerPosition)
                }
                refreshView(refresh)
                clickCounter = 0
                offset = Point(0,0)
                currentShip = null
            }
        }
        return super.onTouchEvent(event) || refresh
    }

    private fun refreshView(refresh: Boolean) {     // refresh canvas if necessary
        if (shipInvalidPositionValidityCheck() || refresh) {
            invalidate()
        }
    }

    private fun shipInvalidPositionValidityCheck(): Boolean {
        var changed = false
        var overlapList = getOverlappingShips()
        for (ship in ships) {
            var isPosValid = true

            if (!ship.isShipOnBoard() || overlapList.contains(ship)) {
                isPosValid = false
            }

            if (isPosValid != ship.isPositionValid()) {
                ship.isPositionValid(isPosValid)
                changed = true
            }
        }
        return changed
    }

    fun getOverlappingShips(): List<ShipViewModel> {
        val overlapList = mutableListOf<ShipViewModel>()
        for (ship in ships) {
            val shipPoints = ship.getOverlapArea()
            var doesOverlap = false

            for (otherShip in ships) {
                if (ship != otherShip) {
                    val otherShipPoints = otherShip.getPoints()
                    for (shipPoint in shipPoints) {
                        for (otherShipPoint in otherShipPoints) {
                            if (shipPoint.equals(otherShipPoint)) {
                                doesOverlap = true
                                break
                            }
                        }
                    }
                }
            }

            if (doesOverlap) {
                overlapList.add(ship)
            }
        }
        return overlapList
    }

    fun setShipsRandomly() {
        ships.forEach{s -> s.setRandomly()}
        var overlapList = getOverlappingShips()
        while (!overlapList.isEmpty()) {
            overlapList.first().setRandomly()
            overlapList = getOverlappingShips()
        }
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

    private fun initPaint(id: Int): Paint {
        return Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = ContextCompat.getColor(context, id)
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
