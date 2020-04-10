package battleships.esa.ffhs.ch.ui.game

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import battleships.esa.ffhs.ch.R
import battleships.esa.ffhs.ch.entity.Cell
import battleships.esa.ffhs.ch.entity.InjectorUtils
import battleships.esa.ffhs.ch.model.GameState
import battleships.esa.ffhs.ch.ui.drawable.BoardPainter
import battleships.esa.ffhs.ch.ui.drawable.BoardPainter.Companion.CLICK_LIMIT
import battleships.esa.ffhs.ch.ui.game.GameFragment.Companion.currentGame
import battleships.esa.ffhs.ch.ui.main.MainActivity
import battleships.esa.ffhs.ch.ui.viewmodel.BoardViewModel
import battleships.esa.ffhs.ch.ui.viewmodel.GameViewModel

class GameBoardOpponentFragment : Fragment() {

    var boardPainter: View? = null
    lateinit var boardModel: BoardViewModel
    lateinit var v: View


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        boardModel = currentGame.opponentBoard
        v = inflater.inflate(R.layout.game_board_opponent_fragment, container, false)
        if (currentGame.data.state == GameState.PREPARATION) {
            boardPainter = v
            (boardPainter as BoardPainter).setBoardViewModel(boardModel)
        }
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (currentGame.data.state != GameState.PREPARATION) {
            boardPainter = (activity as MainActivity).findViewById<View>(R.id.game_board_opponent)
            (boardPainter as BoardPainter).setBoardViewModel(boardModel)
            currentGame.opponentBoardDrawable = (boardPainter as BoardPainter)
        }

        v.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if (currentGame.isActivePlayerMe && resources.configuration.orientation != Configuration.ORIENTATION_LANDSCAPE && currentGame.data.state != GameState.PREPARATION) {
                    (parentFragment as GameActiveFragment).switchFragments()
                    return true
                } else {
                    if (event == null || currentGame.data.state != GameState.PREPARATION) {
                        return false
                    }
                    if (currentGame.isActivePlayerMe && currentGame.data.state != GameState.PREPARATION) {
                        return false
                    }

                    val handled: Boolean

                    val xTouch: Float = event.getX(0)
                    val yTouch: Float = event.getY(0)

                    val pointerPosition: Cell =
                        Cell(
                            (xTouch / (v as BoardPainter).gridWidth).toInt(),
                            (yTouch / (v as BoardPainter).gridWidth).toInt()
                        )
                    handled = boardModel.identifyShip(pointerPosition)

                    when (event.actionMasked) {
                        MotionEvent.ACTION_MOVE -> {            // move ship around
                            v.clickCounter++
                            if (handled && v.clickCounter > CLICK_LIMIT) {
                                boardModel.moveAction(pointerPosition)
                            }
                            v.invalidate()
                            return true
                        }
                        MotionEvent.ACTION_UP -> {              // rotate ship
                            if (handled && (v as BoardPainter).clickCounter <= CLICK_LIMIT) {
                                boardModel.clickAction(pointerPosition)
                            } else {
                                boardModel.releaseShip()
                            }
                            v.invalidate()
                            v.clickCounter = 0
                        }
                    }
                    return handled //v?.onTouchEvent(event) ?: handled
                }
                return false
            }
        })
    }

}
