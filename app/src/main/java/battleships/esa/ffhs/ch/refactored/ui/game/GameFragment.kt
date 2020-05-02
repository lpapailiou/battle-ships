package battleships.esa.ffhs.ch.refactored.ui.game

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import battleships.esa.ffhs.ch.R
import battleships.esa.ffhs.ch.refactored.BattleShipsApplication
import battleships.esa.ffhs.ch.refactored.business.game.GameViewModel
import battleships.esa.ffhs.ch.refactored.ui.board.BoardView
import javax.inject.Inject

class GameFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val gameViewModel by viewModels<GameViewModel> { viewModelFactory }

    private val args: GameFragmentArgs by navArgs()

    private lateinit var activeBoard: BoardView

    private lateinit var inactiveBoard: BoardView

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (requireActivity().application as BattleShipsApplication).appComponent.gameComponent()
            .create().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.game_fragment, container, false)
        activeBoard = view.findViewById(R.id.board_active)
        inactiveBoard = view.findViewById(R.id.board_inactive)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gameViewModel.start(args.gameId, args.currentPlayerId, args.enemyPlayerId)

        gameViewModel.activeBoard.observe(viewLifecycleOwner, Observer { boardModel ->
            if (boardModel == null) {
                return@Observer
            }
            activeBoard.setShips(boardModel.ships.value!!)
            activeBoard.setShots(boardModel.shots.value!!)
        })

        gameViewModel.inactiveBoard.observe(viewLifecycleOwner, Observer { boardModel ->
            if (boardModel == null) {
                return@Observer
            }
            inactiveBoard.setShips(boardModel.ships.value!!)
            inactiveBoard.setShots(boardModel.shots.value!!)
        })

        activeBoard.setOnTouchListener(View.OnTouchListener { boardView, motionEvent ->

            boardView as BoardView

            val touchedCell = boardView.getCellAt(motionEvent.x, motionEvent.y)


            if (motionEvent.action == MotionEvent.ACTION_UP) {
                gameViewModel.shootAt(touchedCell)
            }

            return@OnTouchListener true
        })

        gameViewModel.gameOverEvent.observe(viewLifecycleOwner, Observer { event ->
            event.getContentIfNotHandled()?.let {
                val dialog: AlertDialog.Builder =
                    AlertDialog.Builder(context, R.style.AppDialogTheme)
                dialog.setTitle("THE WAR IS OVER")
                if (gameViewModel.game.value!!.winnerId == args.currentPlayerId) {
                    dialog.setMessage("You won! You are the best general!")
                } else {
                    dialog.setMessage("You lost! Your wife will be very mad at you.")
                }
                dialog.show()
            }
        })
    }
}
