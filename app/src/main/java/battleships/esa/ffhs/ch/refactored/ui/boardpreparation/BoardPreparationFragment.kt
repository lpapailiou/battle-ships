package battleships.esa.ffhs.ch.refactored.ui.boardpreparation

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import battleships.esa.ffhs.ch.R
import battleships.esa.ffhs.ch.refactored.BattleShipsApplication
import battleships.esa.ffhs.ch.refactored.business.board.Cell
import battleships.esa.ffhs.ch.refactored.business.boardpreparation.BoardPreparationViewModel
import battleships.esa.ffhs.ch.refactored.business.ship.ShipModel
import battleships.esa.ffhs.ch.refactored.event.Event
import battleships.esa.ffhs.ch.refactored.event.EventObserver
import battleships.esa.ffhs.ch.refactored.ui.board.BoardView
import battleships.esa.ffhs.ch.refactored.ui.board.BoardView.Companion.CLICK_LIMIT
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.board_preparation_fragment.*
import javax.inject.Inject

class BoardPreparationFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val boardPreparationViewModel by viewModels<BoardPreparationViewModel> { viewModelFactory }

    private val args: BoardPreparationFragmentArgs by navArgs()

    lateinit var boardView: BoardView

    private var touchedShip: ShipModel? = null

    private var touchCounter = 0

    private var touchedOffsetY: Int = 0
    private var touchedOffsetX: Int = 0

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
        boardView = view.findViewById(R.id.preparation_board)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        boardPreparationViewModel.start(args.googlePlayerId)
        boardPreparationViewModel.ships.observe(viewLifecycleOwner, Observer { ships ->
            boardView.setShips(ships)
        })


        // TODO: refactor into custom KeyEvent in BoardView?
        boardView.setOnTouchListener(View.OnTouchListener { boardView, motionEvent ->

            boardView as BoardView
            val touchedCell = boardView.getCellAt(motionEvent.x, motionEvent.y)

            if (touchCounter == 0) {
                touchedShip = boardPreparationViewModel.getShipAt(touchedCell)
                if (touchedShip == null) {
                    return@OnTouchListener true
                }
                touchedOffsetX = touchedCell.x - touchedShip!!.x
                touchedOffsetY = touchedCell.y - touchedShip!!.y
            }

            if (touchedShip == null) {
                return@OnTouchListener true
            }
            touchCounter++

            when (motionEvent.actionMasked) {
                MotionEvent.ACTION_MOVE -> {
                    if (touchCounter > CLICK_LIMIT) {
                        boardPreparationViewModel.moveShipTo(
                            touchedShip!!,
                            Cell(touchedCell.x - touchedOffsetX, touchedCell.y - touchedOffsetY)
                        )
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
        })


        boardPreparationViewModel.gameReadyEvent.observe(
            viewLifecycleOwner,
            EventObserver {
                Log.e(
                    "Tag",
                    "NAVIGATED SASADSALKDADSAADDLKDSAJDSALKJLKJDALKJDSADSAKJDSALKDSAKJDKJDSALKJDALKJDSAKJDSAKLKDSLKJSALKDSADSAL"
                )
                val action =
                    BoardPreparationFragmentDirections.actionBoardPreparationFragmentToGameFragment(
                        boardPreparationViewModel.game.value!!.id,
                        boardPreparationViewModel.player.value!!.id,
                        boardPreparationViewModel.bot.value!!.id
                    )
                findNavController().navigate(action)
            })

        startgame_button.setOnClickListener {
            if (boardPreparationViewModel.isBoardInValidState()) {
                boardPreparationViewModel.startGame()
            } else {
                showSnackBar(it)
            }
        }
    }


    private fun showSnackBar(view: View) {
        val snackBar =
            Snackbar.make(view, "Some of your ships are still too close to each other!", 2000)
        snackBar.setBackgroundTint(ContextCompat.getColor(view.context, R.color.colorComplementary))
        snackBar.show()
    }
}