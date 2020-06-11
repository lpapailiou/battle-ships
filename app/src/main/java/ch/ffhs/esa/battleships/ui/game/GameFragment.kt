package ch.ffhs.esa.battleships.ui.game

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ch.ffhs.esa.battleships.BattleShipsApplication
import ch.ffhs.esa.battleships.R
import ch.ffhs.esa.battleships.business.BOT_PLAYER_ID
import ch.ffhs.esa.battleships.business.OFFLINE_PLAYER_ID
import ch.ffhs.esa.battleships.business.game.GameViewModel
import ch.ffhs.esa.battleships.ui.board.BoardView
import ch.ffhs.esa.battleships.ui.main.MainActivity.Companion.navEnemyId
import ch.ffhs.esa.battleships.ui.main.MainActivity.Companion.navGameId
import ch.ffhs.esa.battleships.ui.main.MainActivity.Companion.navOwnPlayerId
import javax.inject.Inject

class GameFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val gameViewModel by viewModels<GameViewModel> { viewModelFactory }

    private lateinit var activeBoard: BoardView

    private lateinit var inactiveBoard: BoardView

    private lateinit var vibrator: Vibrator

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

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (navEnemyId == BOT_PLAYER_ID) {
            navOwnPlayerId = OFFLINE_PLAYER_ID
        }
        gameViewModel.start(navGameId.value!!, navOwnPlayerId, navEnemyId)
        gameViewModel.enemyBoard.observe(viewLifecycleOwner, Observer { boardModel ->
            if (boardModel == null) {
                return@Observer
            }

            if (activeBoard.boardModel.playerUid == null || activeBoard.boardModel.playerUid == boardModel.playerUid) {
                activeBoard.boardModel = boardModel
            } else {
                inactiveBoard.boardModel = boardModel
            }
        })

        gameViewModel.ownBoard.observe(viewLifecycleOwner, Observer { boardModel ->
            if (boardModel == null) {
                return@Observer
            }

            if (activeBoard.boardModel.playerUid == boardModel.playerUid) {
                activeBoard.boardModel = boardModel
            } else {
                inactiveBoard.boardModel = boardModel
            }
        })

        activeBoard.setOnTouchListener(View.OnTouchListener { boardView, motionEvent ->
            boardView as BoardView

            if (boardView.boardModel.playerUid == gameViewModel.ownBoard.value?.playerUid) {
                return@OnTouchListener true
            }

            val touchedCell = boardView.getCellAt(motionEvent.x, motionEvent.y)
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                gameViewModel.shootAt(touchedCell)
            }

            return@OnTouchListener true
        })

        gameViewModel.gameOverEvent.observe(viewLifecycleOwner, Observer { event ->
            event.getContentIfNotHandled()?.let {
                displayGameOverDialog()
            }
        })

        enableVibration()

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return
        }

        inactiveBoard.setOnTouchListener(View.OnTouchListener { boardView, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                swapBoards()
            }
            return@OnTouchListener true
        })

        navGameId.value = navGameId.value
    }

    private fun displayGameOverDialog() {
        val dialog: AlertDialog.Builder =
            AlertDialog.Builder(context, R.style.AppDialogTheme)
        dialog.setTitle("THE WAR IS OVER")
        if (gameViewModel.game.value!!.winnerUid == gameViewModel.ownBoard.value!!.playerUid) {
            dialog.setMessage("You won! You are the best general!")
        } else {
            dialog.setMessage("You lost! Your wife will be very mad at you.")
        }
        dialog.show()
    }


    private fun enableVibration() {
        vibrator = requireContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (!vibrator.hasVibrator()) {
            return
        }

        gameViewModel.shipHitEvent.observe(viewLifecycleOwner, Observer {
            vibrate()
        })

    }

    private fun vibrate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(
                    200,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        } else {
            vibrator.vibrate(200)
        }
    }

    private fun swapBoards() {
        val tmpBoardModel = activeBoard.boardModel
        activeBoard.boardModel = inactiveBoard.boardModel
        inactiveBoard.boardModel = tmpBoardModel
    }
}
