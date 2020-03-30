package battleships.esa.ffhs.ch.ui.drawable

import android.content.Context
import android.content.res.Configuration
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.fragment.app.Fragment
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
        if (!activeGame!!.isActivePlayerMe && resources.configuration.orientation != Configuration.ORIENTATION_LANDSCAPE) {
            return false
        }

        var handled: Boolean = false

        var xTouch: Float = event!!.getX(0)
        var yTouch: Float = event!!.getY(0)

        var pointerPosition: Point = Point((xTouch/gridWidth).toInt(), (yTouch/gridWidth).toInt())

        if (boardModel!!.repeatClickCheck(pointerPosition)) {
            return handled
        }

        handled = boardModel!!.clickAction(pointerPosition, this)
        if (handled) {
            invalidate()
        }
        endGameCheck()
        return super.onTouchEvent(event) || handled
    }

}
