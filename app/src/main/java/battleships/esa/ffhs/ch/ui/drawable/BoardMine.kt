package battleships.esa.ffhs.ch.ui.drawable

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.fragment.app.findFragment
import battleships.esa.ffhs.ch.R
import battleships.esa.ffhs.ch.ui.game.BoardGameFragment
import battleships.esa.ffhs.ch.ui.main.MainActivity
import battleships.esa.ffhs.ch.ui.main.MainActivity.Companion.activeGame
import kotlinx.android.synthetic.main.board_game_fragment.view.*

class BoardMine(
    context: Context, attributes: AttributeSet
) : Board(context, attributes) {

    init {
        if (activeGame != null) {
            boardModel = activeGame!!.myBoard
            invalidate()
        }
    }

    // ----------------------------- ship handling -----------------------------

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null || activeGame!!.state == GameState.ENDED) {
            return false
        }
        if (!activeGame!!.isActivePlayerMe) {
            return false
        }

        var handled: Boolean = false

        var xTouch: Float = event!!.getX(0)
        var yTouch: Float = event!!.getY(0)

        var pointerPosition: Point = Point((xTouch/gridWidth).toInt(), (yTouch/gridWidth).toInt())

        if (boardModel!!.repeatClickCheck(pointerPosition)) {
            return handled
        }

        handled = boardModel!!.clickAction(pointerPosition)
        if (handled) {
            invalidate()
        }
        return super.onTouchEvent(event) || handled
    }

}
