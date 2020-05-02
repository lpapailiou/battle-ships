//package battleships.esa.ffhs.ch.old.ui.game
//
//import android.content.res.Configuration
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.MotionEvent
//import android.view.View
//import android.view.ViewGroup
//import androidx.fragment.app.Fragment
//import androidx.lifecycle.Observer
//import battleships.esa.ffhs.ch.R
//import battleships.esa.ffhs.ch.old.model.GameState
//import battleships.esa.ffhs.ch.old.ui.drawable.BoardPainter
//import battleships.esa.ffhs.ch.old.ui.drawable.BoardPainter.Companion.CLICK_LIMIT
//import battleships.esa.ffhs.ch.old.ui.game.GameFragment.Companion.currentGame
//import battleships.esa.ffhs.ch.old.ui.main.MainActivity
//import battleships.esa.ffhs.ch.old.ui.main.MainActivity.Companion.mainViewModel
//import battleships.esa.ffhs.ch.old.wrapper.Cell
//
//class GameBoardOpponentFragment : Fragment() {
//
//    var boardPainter: View? = null
//    var observerSet = false
//    lateinit var boardModel: BoardViewModel
//    lateinit var v: View
//
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        boardModel = currentGame.getOpponentBoard()
//        v = inflater.inflate(R.layout.game_board_opponent_fragment, container, false)
//        boardPainter = v
//        if (currentGame.equalsState(GameState.PREPARATION)) {
//            setObserver()
//        }
//        return v
//    }
//
//    // ----------------------------- set observers and click handler -----------------------------
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        if (currentGame.notEqualsState(GameState.PREPARATION)) {
//            if (boardPainter == null) {
//                boardPainter =
//                    (activity as MainActivity).findViewById<View>(R.id.game_board_opponent)
//            }
//            if (!observerSet) {
//                setObserver()
//            }
//        }
//
//        v.setOnTouchListener(object : View.OnTouchListener {
//            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
//                if (currentGame.isMyBoardVisible() && resources.configuration.orientation != Configuration.ORIENTATION_LANDSCAPE && currentGame.notEqualsState(
//                        GameState.PREPARATION
//                    )
//                ) {
//                    // if this fragment is displayed as small fragment (portrait mode), this click will trigger a board switch
//                    (parentFragment as GameActiveFragment).switchFragments()
//                    return false
//                } else {
//                    if (event == null || currentGame.notEqualsState(GameState.PREPARATION)) {
//                        return false
//                    }
//                    if (currentGame.isMyBoardVisible() && currentGame.notEqualsState(GameState.PREPARATION)) {
//                        return false
//                    }
//
//                    val handled: Boolean
//
//                    val xTouch: Float = event.getX(0)
//                    val yTouch: Float = event.getY(0)
//
//                    (v as BoardPainter)
//
//                    val pointerPosition: Cell =
//                        Cell(
//                            (xTouch / v.gridWidth).toInt(),
//                            (yTouch / v.gridWidth).toInt()
//                        )
//                    handled = boardModel.identifyShip(pointerPosition)
//
//                    when (event.actionMasked) {
//                        MotionEvent.ACTION_MOVE -> {            // move ship around
//                            v.clickCounter++
//                            if (handled && v.clickCounter > CLICK_LIMIT) {
//                                boardModel.moveAction(pointerPosition)
//                            }
//                            return true
//                        }
//                        MotionEvent.ACTION_UP -> {              // rotate ship
//                            if (handled && v.clickCounter <= CLICK_LIMIT) {
//                                boardModel.clickAction(pointerPosition)
//                            } else {
//                                boardModel.releaseShip()
//                            }
//                            v.clickCounter = 0
//                        }
//                    }
//                    return handled
//                }
//            }
//        })
//    }
//
//    private fun setObserver() {     // TODO: this is doing way too much work
//
//        mainViewModel.getOpponentShips().observe(viewLifecycleOwner, Observer {
//            if (it.size == 10) {
//                val boardViewModel = currentGame.getOpponentBoard()
//                boardViewModel.updateShips()
//                (boardPainter as BoardPainter).setShips(boardViewModel.getShips())
//            }
//        })
//
//        mainViewModel.getOpponentShots().observe(viewLifecycleOwner, Observer { shots ->
//            (boardPainter as BoardPainter).setShots(shots)
//        })
//
//        observerSet = true
//    }
//
//}
