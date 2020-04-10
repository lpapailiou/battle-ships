package battleships.esa.ffhs.ch.ui.drawable

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import battleships.esa.ffhs.ch.R
import battleships.esa.ffhs.ch.entity.BOARD_SIZE
import battleships.esa.ffhs.ch.model.GameState
import battleships.esa.ffhs.ch.ui.main.MainActivity.Companion.activeGame
import battleships.esa.ffhs.ch.ui.viewmodel.BoardViewModel

open class BoardPainter(
    context: Context, attributes: AttributeSet
) : View(context, attributes) {

    companion object {
        const val STROKE_WIDTH = 2f
        const val CLICK_LIMIT: Int = 6        // makes difference between click and move
        //var gridWidth = 0f
    }

    var boardModel: BoardViewModel? = null
    var gridWidth: Float = 0f
    val shipPainter: ShipPainter
    val shotPainter: ShotPainter

    var clickCounter: Int = 0

    var paint: Paint
    var paintBackground: Paint

    init {
        shipPainter = ShipPainter(context, attributes)
        shotPainter = ShotPainter(context, attributes)
        paint = initPaint(R.color.colorAccent)
        paintBackground = initBackgroundPaint()
    }

    open fun setBoardViewModel(viewModel: BoardViewModel) {
        boardModel = viewModel
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
        if (boardModel != null) {
            // drawing ships first
            boardModel!!.ships.forEach { shipViewModel ->
                shipPainter.draw(shipViewModel, canvas)
            }

            boardModel!!.shots.forEach { shot ->
                shotPainter.draw(shot, canvas)
            }
        }

        // drawing grid over ships
        drawGrid(canvas)
    }

    // ----------------------------- enable vibrations -----------------------------

    open fun vibrate() {
        val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (vibrator.hasVibrator()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        200,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            } else {
                vibrator.vibrate(200)
            }
        }
    }

    // ----------------------------- end game check -----------------------------

    open fun endGameCheck(): Boolean {
        if (boardModel != null && boardModel!!.endGameCheck() && activeGame!!.state != GameState.ENDED) {
            var gameResult = boardModel!!.getGameResult()
            CustomDialog().showEndGameDialog(context, gameResult)
            activeGame!!.state = GameState.ENDED
            vibrate()
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
