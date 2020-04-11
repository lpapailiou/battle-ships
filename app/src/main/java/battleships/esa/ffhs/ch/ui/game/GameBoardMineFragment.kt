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
import battleships.esa.ffhs.ch.ui.game.GameFragment.Companion.currentGame
import battleships.esa.ffhs.ch.ui.main.MainActivity
import battleships.esa.ffhs.ch.ui.viewmodel.BoardViewModel

class GameBoardMineFragment : Fragment() {


    var boardPainter: View? = null
    lateinit var boardModel: BoardViewModel
    lateinit var v: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        boardModel = currentGame.myBoard
        v = inflater.inflate(R.layout.game_board_mine_fragment, container, false)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        boardPainter = (activity as MainActivity).findViewById<View>(R.id.game_board_mine)
        (boardPainter as BoardPainter).setBoardViewModel(boardModel)

        v.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if (!currentGame.isActivePlayerMe && resources.configuration.orientation != Configuration.ORIENTATION_LANDSCAPE) {
                    (parentFragment as GameActiveFragment).switchFragments()
                    return false
                } else {
                    if (event == null || currentGame.data.state == GameState.ENDED) {
                        return false
                    }
                    if (!currentGame.isActivePlayerMe && resources.configuration.orientation != Configuration.ORIENTATION_LANDSCAPE) {
                        return false
                    }

                    var handled: Boolean = false

                    val xTouch: Float = event.getX(0)
                    val yTouch: Float = event.getY(0)

                    val pointerPosition: Cell =
                        Cell(
                            (xTouch / (v as BoardPainter).gridWidth).toInt(),
                            (yTouch / v.gridWidth).toInt()
                        )

                    if (boardModel.repeatClickCheck(pointerPosition)) {
                        return handled
                    }

                    handled = boardModel.clickAction(
                        pointerPosition,
                        activity as MainActivity
                    )
                    if (handled) {
                        v.invalidate()
                    }
                    //endGameCheck()    TODO: implement
                    return true //v.onTouchEvent(event) ?: handled  // TODO: comment in if one shot per click should be made
                }
            }
        })
    }

}




