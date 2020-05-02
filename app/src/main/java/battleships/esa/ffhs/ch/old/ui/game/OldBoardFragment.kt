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
//import battleships.esa.ffhs.ch.old.ui.game.GameFragment.Companion.currentGame
//import battleships.esa.ffhs.ch.old.ui.main.MainActivity
//import battleships.esa.ffhs.ch.old.ui.main.MainActivity.Companion.mainViewModel
//import battleships.esa.ffhs.ch.old.wrapper.Cell
//
//class OldBoardFragment : Fragment() {
//    lateinit var boardPainter: View
//    lateinit var v: View
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        v = inflater.inflate(R.layout.game_board_mine_fragment, container, false)
//        return v
//    }
//
//    // ----------------------------- set observers and click handler -----------------------------
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        boardPainter = (activity as MainActivity).findViewById<View>(R.id.game_board_mine)
//
//        setObservers()
//
//        v.setOnTouchListener(object : View.OnTouchListener {
//            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
//                if (!currentGame.isMyBoardVisible() && resources.configuration.orientation != Configuration.ORIENTATION_LANDSCAPE) {
//                    // if this fragment is displayed as small fragment (portrait mode), this click will trigger a board switch
//                    (parentFragment as GameActiveFragment).switchFragments()
//                    return false
//                } else {
//                    if (event == null || currentGame.equalsState(GameState.ENDED)) {
//                        return false
//                    }
//                    if (!currentGame.isMyBoardVisible() && resources.configuration.orientation != Configuration.ORIENTATION_LANDSCAPE) {
//                        return false
//                    }
//
//                    var handled: Boolean = false
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
//
////                    if (boardModel.repeatClickCheck(pointerPosition)) {
////                        return handled
////                    }
////
////                    handled = boardModel.clickAction(
////                        pointerPosition,
////                        activity as MainActivity
////                    )
//
//                    return true //v.onTouchEvent(event) ?: handled  // TODO: comment in if one shot per click should be made
//                }
//            }
//        })
//    }
//
//    private fun setObservers() {
//
//        mainViewModel.getMyShips().observe(viewLifecycleOwner, Observer {
//            println("------------- observing opponent ship change")
//            println("number of ships:1 " + it.size)
////            (boardPainter as BoardPainter).setShips(boardModel.getShips())
//        })
//
//        mainViewModel.getMyShots().observe(viewLifecycleOwner, Observer { shots ->
//            (boardPainter as BoardPainter).setShots(shots)
//            println("------------- opponent shot change observed")
//        })
//    }
//
//}
//
//
//
//