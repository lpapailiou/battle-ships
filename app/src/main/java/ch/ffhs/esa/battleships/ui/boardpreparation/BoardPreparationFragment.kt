package ch.ffhs.esa.battleships.ui.boardpreparation

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ch.ffhs.esa.battleships.BattleShipsApplication
import ch.ffhs.esa.battleships.R
import ch.ffhs.esa.battleships.business.board.Cell
import ch.ffhs.esa.battleships.business.boardpreparation.BoardPreparationViewModel
import ch.ffhs.esa.battleships.business.ship.ShipModel
import ch.ffhs.esa.battleships.data.game.Game
import ch.ffhs.esa.battleships.event.EventObserver
import ch.ffhs.esa.battleships.ui.board.BoardView
import ch.ffhs.esa.battleships.ui.board.BoardView.Companion.CLICK_LIMIT
import ch.ffhs.esa.battleships.ui.game.GameHostFragmentDirections
import ch.ffhs.esa.battleships.ui.main.MainActivity
import ch.ffhs.esa.battleships.ui.main.MainActivity.Companion.navEnemyId
import ch.ffhs.esa.battleships.ui.main.MainActivity.Companion.navGameId
import ch.ffhs.esa.battleships.ui.main.MainActivity.Companion.navIsBotGame
import ch.ffhs.esa.battleships.ui.main.MainActivity.Companion.navOwnPlayerId
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.board_preparation_fragment.*
import javax.inject.Inject

class BoardPreparationFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val boardPreparationViewModel by viewModels<BoardPreparationViewModel> { viewModelFactory }

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.board_preparation_fragment, container, false)
        boardView = view.findViewById(R.id.preparation_board)
        return view
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        boardPreparationViewModel.start(navOwnPlayerId, navIsBotGame)
        boardPreparationViewModel.board.observe(viewLifecycleOwner, Observer { boardModel ->
            boardView.boardModel = boardModel
        })

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

        startgame_button.setOnClickListener {
            if (boardPreparationViewModel.isBoardInValidState()) {
                startProgressBar()
                boardPreparationViewModel.startGame()
            } else {
                showSnackBar("Some of your ships are still too close to each other!", true)
            }
        }

        boardPreparationViewModel.gameReadyEvent.observe(
            viewLifecycleOwner,
            EventObserver {
                stopProgressBar()
                navigateToGame(it)
            })

        boardPreparationViewModel.waitForEnemyEvent.observe(
            viewLifecycleOwner,
            EventObserver {
                navigateToBridge()
            }
        )

    }

    private fun startProgressBar() {
        var progressBar = (activity as MainActivity).findViewById<View>(R.id.progress_Bar) as ProgressBar
        progressBar.visibility = View.VISIBLE
    }

    private fun stopProgressBar() {
        var progressBar = (activity as MainActivity).findViewById<View>(R.id.progress_Bar) as ProgressBar
        progressBar.visibility = View.GONE
    }

    private fun navigateToGame(game: Game) {
        val enemyPlayerId = if (game.attackerUid!! == navOwnPlayerId)
            game.defenderUid else game.attackerUid
        navEnemyId = enemyPlayerId!!
        navGameId.value = game.uid
    }

    private fun navigateToBridge() {
        showSnackBar("All canons ready! You'll get notified, when an enemy has been found!", false)
        val action = GameHostFragmentDirections.actionGameHostFragmentToMainFragment()
        findNavController().navigate(action)
    }

    private fun showSnackBar(message: String, isError: Boolean) {
        val snackBar =
            Snackbar.make(requireView(), message, 2000)
        if (isError) {
            snackBar.setBackgroundTint(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorComplementary
                )
            )
        } else {
            snackBar.setBackgroundTint(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorAccent
                )
            )
        }
        snackBar.show()
    }
}
