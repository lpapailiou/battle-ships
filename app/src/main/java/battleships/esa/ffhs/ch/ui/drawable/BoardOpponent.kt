package battleships.esa.ffhs.ch.ui.drawable

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import battleships.esa.ffhs.ch.model.GameState
import battleships.esa.ffhs.ch.entity.Cell
import battleships.esa.ffhs.ch.ui.main.MainActivity.Companion.activeGame

class BoardOpponent(
    context: Context, attributes: AttributeSet
) : BoardPainter(context, attributes) {


    init {
        if (activeGame != null) {
            boardModel = activeGame!!.opponentBoard
            activeGame!!.opponentBoardDrawable = this
            invalidate()
        }
    }

    // ----------------------------- jump to game validation -----------------------------

    fun validateStart(): Boolean {
        return boardModel!!.validateStart()
    }

    // ----------------------------- ship positioning handling -----------------------------

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null || activeGame!!.state != GameState.PREPARATION) {
            return false
        }
        if (activeGame!!.isActivePlayerMe && activeGame!!.state != GameState.PREPARATION) {
            return false
        }

        var handled: Boolean

        var xTouch: Float = event.getX(0)
        var yTouch: Float = event.getY(0)

        var pointerPosition: Cell =
            Cell(
                (xTouch / gridWidth).toInt(),
                (yTouch / gridWidth).toInt()
            )

        handled = boardModel!!.identifyShip(pointerPosition)

        when (event.actionMasked) {
            MotionEvent.ACTION_MOVE -> {            // move ship around
                clickCounter++
                if (handled && clickCounter > CLICK_LIMIT) {
                    boardModel!!.moveAction(pointerPosition)
                }
                invalidate()
                return true
            }
            MotionEvent.ACTION_UP -> {              // rotate ship
                if (handled && clickCounter <= CLICK_LIMIT) {
                    boardModel!!.clickAction(pointerPosition)
                } else {
                    boardModel!!.releaseShip()
                }
                invalidate()
                clickCounter = 0
            }
        }
        return super.onTouchEvent(event) || handled
    }

}
