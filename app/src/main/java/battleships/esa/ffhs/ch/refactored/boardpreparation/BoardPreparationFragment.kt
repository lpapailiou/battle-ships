package battleships.esa.ffhs.ch.refactored.boardpreparation

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import battleships.esa.ffhs.ch.R
import battleships.esa.ffhs.ch.old.ui.drawable.BoardView
import battleships.esa.ffhs.ch.old.ui.drawable.BoardView.Companion.CLICK_LIMIT
import battleships.esa.ffhs.ch.refactored.BattleShipsApplication
import battleships.esa.ffhs.ch.refactored.data.ship.Ship
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.board_preparation_fragment.*
import javax.inject.Inject

class BoardPreparationFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val boardPreparationViewModel by viewModels<BoardPreparationViewModel> { viewModelFactory }

    private val args: BoardPreparationFragmentArgs by navArgs()

    lateinit var boardView: BoardView

    private var touchCounter = 0

    private var touchedShip: Ship? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (requireActivity().application as BattleShipsApplication).appComponent.boardPreparationComponent()
            .create().inject(this)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.board_preparation_fragment, container, false)

        boardPreparationViewModel.start(args.googlePlayerId)

        boardView = view.findViewById(R.id.preparation_board)
        boardView.setShips(boardPreparationViewModel.ships)
        boardView.setOnTouchListener(View.OnTouchListener { boardView, motionEvent ->

            boardView as BoardView
            val touchedCell = boardView.getCellAt(motionEvent.x, motionEvent.y)

            if (touchCounter == 0) {
                touchedShip = boardPreparationViewModel.determineShipAt(touchedCell)
                if (touchedShip == null) {
                    return@OnTouchListener true
                }
            }

            touchCounter++

            when (motionEvent.actionMasked) {
                MotionEvent.ACTION_MOVE -> {
                    if (touchCounter > CLICK_LIMIT) {
                        boardPreparationViewModel.moveShip(touchedShip!!, touchedCell)
                    }
                }
                MotionEvent.ACTION_UP -> {
                    if (touchCounter <= CLICK_LIMIT) {
                        boardPreparationViewModel.rotateAround(touchedShip!!, touchedCell)
                    }
                    touchCounter = 0
                }
            }


            return@OnTouchListener true
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
//        }
        })


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        startgame_button.setOnClickListener {
//            if (currentGame.getOpponentBoard().validateStart()) {
//                currentGame.setState(GameState.ACTIVE)
//                (parentFragment as GameFragment).switchToGameFragment()
//            } else {
//                showSnackBar(it)
//            }
        }
    }


    private fun showSnackBar(view: View) {
        val snackBar =
            Snackbar.make(view, "Some of your ships are still too close to each other!", 1000)
        snackBar.setBackgroundTint(ContextCompat.getColor(view.context, R.color.colorComplementary))
        snackBar.show()
    }
}
