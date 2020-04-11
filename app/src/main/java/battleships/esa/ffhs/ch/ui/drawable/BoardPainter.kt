package battleships.esa.ffhs.ch.ui.drawable

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import battleships.esa.ffhs.ch.R
import battleships.esa.ffhs.ch.entity.InjectorUtils
import battleships.esa.ffhs.ch.entity.Shot
import battleships.esa.ffhs.ch.model.BOARD_SIZE
import battleships.esa.ffhs.ch.ui.viewmodel.BoardViewModel
import battleships.esa.ffhs.ch.ui.viewmodel.GameListViewModel
import battleships.esa.ffhs.ch.ui.viewmodel.ShipViewModel

open class BoardPainter(
    context: Context, attributes: AttributeSet
) : View(context, attributes) {

    companion object {
        const val STROKE_WIDTH = 2f
        const val CLICK_LIMIT: Int = 6        // makes difference between click and move
    }

    private var ships: MutableList<ShipViewModel> = mutableListOf()
    private var shots: MutableList<Shot> = mutableListOf()

    var boardModel: BoardViewModel? = null

    var gridWidth: Float = 0f
    var clickCounter: Int = 0

    val shipPainter: ShipPainter = ShipPainter(context, attributes)
    val shotPainter: ShotPainter = ShotPainter(context, attributes)

    var paint: Paint
    var paintBackground: Paint

    init {
        paint = initPaint(R.color.colorAccent)
        paintBackground = initBackgroundPaint()
    }

    fun setShips(updatedShips: List<ShipViewModel>) {
        println("ships set")
        ships = updatedShips as MutableList<ShipViewModel>
        invalidate()
    }

    fun setShots(updatedShots: List<Shot>) {
        println("shots set")
        shots = updatedShots as MutableList<Shot>
        invalidate()
    }

    // ----------------------------- basic view handling -----------------------------

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        gridWidth = (w.toFloat() / BOARD_SIZE)
    }

    override fun onDraw(canvas: Canvas) {
        // clear canvas
        canvas.drawColor(Color.BLACK)

        // drawing ships first
        ships.forEach { shipViewModel ->
            shipPainter.draw(shipViewModel, canvas)
        }
        // drawing shots on ships
        shots.forEach { shot ->
            shotPainter.draw(shot, canvas)
        }

        // drawing grid over ships
        drawGrid(canvas)
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

    // ----------------------------- create paints -----------------------------

    protected fun initPaint(id: Int): Paint {
        return Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = ContextCompat.getColor(context, id)
            style = Paint.Style.STROKE
            strokeWidth = Companion.STROKE_WIDTH
            isAntiAlias = true
            isDither = true
        }
    }

    protected fun initBackgroundPaint(
    ): Paint {
        return Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.BLACK
            style = Paint.Style.STROKE
            strokeWidth = Companion.STROKE_WIDTH * 6f
            isAntiAlias = true
            isDither = true
        }
    }
}
