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
import battleships.esa.ffhs.ch.entity.Shot
import battleships.esa.ffhs.ch.ui.game.BoardPreparationFragment
import battleships.esa.ffhs.ch.ui.main.MainActivity.Companion.isGameFinished
import battleships.esa.ffhs.ch.ui.main.MainActivity.Companion.isGameStarted
import battleships.esa.ffhs.ch.ui.main.MainActivity.Companion.preparedShips
import battleships.esa.ffhs.ch.ui.viewmodel.ShipViewModel
import kotlinx.android.synthetic.main.board_preparation_fragment.view.*


open class Board (
    context: Context, attributes: AttributeSet
) : View(context, attributes) {

    companion object {
        const val STROKE_WIDTH = 2f
    }

    // TODO: outsource into viewmodel
    var ships: List<ShipViewModel> = listOf()
    var shots: MutableList<Shot> = mutableListOf()
    var currentShip: ShipViewModel? = null
    val shipSizes: IntArray = intArrayOf(4, 3, 3, 2, 2, 2, 1, 1, 1, 1)
    val shipPainter: ShipPainter
    val shotPainter: ShotPainter
    val CLICK_LIMIT: Int = 6        // makes difference between click and move

    var paint: Paint
    var paintBackground: Paint
    var gridWidth = 0f
    var clickCounter: Int = 0
    var offset = Point(0,0)

    init {
        shipPainter = ShipPainter(context, attributes)
        shotPainter = ShotPainter(context, attributes)
        paint = initPaint(R.color.colorAccent)
        paintBackground = initBackgroundPaint()
    }

    protected fun endGameCheck(): Boolean {
        var sunkenShips = ships.filter { s -> s.isSunk() }.count()
        if (sunkenShips == ships.size) {
            CustomDialog().showEndGameDialog(context, (ships != preparedShips))
            isGameFinished = true
        }
        return isGameFinished
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

        shots.forEach { shot ->
            shotPainter.draw(shot, canvas)
        }

        // drawing grid over ships for shadow effect
        drawGrid(canvas)
    }

    // ----------------------------- shot handling -----------------------------

    protected fun hit (shot: Shot) {
        hit(null, shot)
    }

    protected fun hit(ship: ShipViewModel?, shot: Shot) {
        if (ship != null) {
            shot.isHit(true)
            ship!!.hit(shot)
        }
        shots.add(shot)
        endGameCheck()
    }

    // ----------------------------- ship handling -----------------------------

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)
    }

    protected open fun refreshView(refresh: Boolean) {     // refresh canvas if necessary
        if (refresh) {
            invalidate()
        }
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

    protected fun initShips(): List<ShipViewModel> {
        return shipSizes.mapIndexed { index, size ->
            ShipViewModel(
                index,
                Point(0, index),
                size,
                Direction.RIGHT,
                mutableSetOf()
            )
        }.toList()
    }

    // ----------------------------- create grid -----------------------------

    protected fun drawGrid(canvas: Canvas) {
        // painting two grids here to obtain shadow effect
        drawGrid(canvas, paintBackground)       // wider strokes in black
        drawGrid(canvas, paint)                 // narrower strokes in radar green
    }

    protected fun drawGrid(canvas: Canvas, customPaint: Paint) {
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

    protected fun initPaint(id: Int): Paint {
        return Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = ContextCompat.getColor(context, id)
            style = Paint.Style.STROKE
            strokeWidth = Companion.STROKE_WIDTH
        }
    }

    protected fun initBackgroundPaint(
    ): Paint {
        return Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.BLACK
            style = Paint.Style.STROKE
            strokeWidth = Companion.STROKE_WIDTH*6f
        }
    }
}
