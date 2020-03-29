package battleships.esa.ffhs.ch.ui.drawable

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import battleships.esa.ffhs.ch.R
import battleships.esa.ffhs.ch.ui.game.BoardGameFragment
import battleships.esa.ffhs.ch.ui.main.MainActivity
import battleships.esa.ffhs.ch.ui.main.MainActivity.Companion.activeGame
import battleships.esa.ffhs.ch.ui.viewmodel.BoardViewModel
import battleships.esa.ffhs.ch.ui.viewmodel.ShipViewModel

open class Board (
    context: Context, attributes: AttributeSet
) : View(context, attributes) {

    companion object {
        const val STROKE_WIDTH = 2f
        const val CLICK_LIMIT: Int = 6        // makes difference between click and move
    }

    var boardModel: BoardViewModel? = null
    var opponentBoardDrawable: Board? = null

    val shipPainter: ShipPainter
    val shotPainter: ShotPainter

    var clickCounter: Int = 0

    var paint: Paint
    var paintBackground: Paint
    var gridWidth = 0f


    init {
        shipPainter = ShipPainter(context, attributes)
        shotPainter = ShotPainter(context, attributes)
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
        boardModel!!.ships.forEach { shipViewModel ->
            shipPainter.draw(shipViewModel, canvas)
        }

        boardModel!!.shots.forEach { shot ->
            shotPainter.draw(shot, canvas)
        }

        // drawing grid over ships
        drawGrid(canvas)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)
    }

    // ----------------------------- end game check -----------------------------

    protected open fun endGameCheck(): Boolean {
        if (boardModel!!.endGameCheck()) {
            var gameResult = boardModel!!.getGameResult()
            CustomDialog().showEndGameDialog(context, gameResult)
            activeGame!!.state == GameState.ENDED
        }
        return false
    }

    // ----------------------------- create grid -----------------------------

    protected fun drawGrid(canvas: Canvas) {
        // painting two grids here to obtain gap between gridlines and drawn objects
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
            isAntiAlias = true
            isDither = true
        }
    }

    protected fun initBackgroundPaint(
    ): Paint {
        return Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.BLACK
            style = Paint.Style.STROKE
            strokeWidth = Companion.STROKE_WIDTH*6f
            isAntiAlias = true
            isDither = true
        }
    }
}
