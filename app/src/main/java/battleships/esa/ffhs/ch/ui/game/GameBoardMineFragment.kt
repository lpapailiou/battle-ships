package battleships.esa.ffhs.ch.ui.game

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import battleships.esa.ffhs.ch.R
import battleships.esa.ffhs.ch.entity.Cell
import battleships.esa.ffhs.ch.model.GameState
import battleships.esa.ffhs.ch.ui.drawable.BoardPainter
import battleships.esa.ffhs.ch.ui.main.MainActivity
import battleships.esa.ffhs.ch.ui.main.MainActivity.Companion.activeGame
import battleships.esa.ffhs.ch.ui.viewmodel.BoardViewModel

class GameBoardMineFragment : Fragment() {

    var boardPainter: View? = null
    lateinit var boardModel: BoardViewModel
    lateinit var v: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        boardModel = activeGame!!.myBoard
        v = inflater.inflate(R.layout.game_board_mine_fragment, container, false)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        boardPainter = (activity as MainActivity).findViewById<View>(R.id.game_board_mine)
        (boardPainter as BoardPainter).setBoardViewModel(boardModel)

        v.setOnTouchListener(object : View.OnTouchListener {    // TODO: bugfix - extra click on 0,0 when board switch happens
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if (!activeGame!!.isActivePlayerMe && resources.configuration.orientation != Configuration.ORIENTATION_LANDSCAPE) {
                    (parentFragment as GameActiveFragment).switchFragments()
                    return true
                } else {
                    if (event == null || activeGame!!.data.state == GameState.ENDED) {
                        return false
                    }
                    if (!activeGame!!.isActivePlayerMe && resources.configuration.orientation != Configuration.ORIENTATION_LANDSCAPE) {
                        return false
                    }

                    var handled: Boolean = false

                    var xTouch: Float = event.getX(0)
                    var yTouch: Float = event.getY(0)

                    var pointerPosition: Cell =
                        Cell(
                            (xTouch / (v as BoardPainter).gridWidth).toInt(),
                            (yTouch / (v as BoardPainter).gridWidth).toInt()
                        )

                    if (boardModel != null && boardModel!!.repeatClickCheck(pointerPosition)) {
                        return handled
                    }
                    if (boardModel != null) {
                        handled = boardModel!!.clickAction(
                            pointerPosition,
                            (boardPainter as BoardPainter)
                        )
                    }
                    if (handled) {
                        v?.invalidate()
                    }
                    //endGameCheck()    TODO: implement
                    return v?.onTouchEvent(event) ?: handled
                }
                return false
            }
        })
    }

}




