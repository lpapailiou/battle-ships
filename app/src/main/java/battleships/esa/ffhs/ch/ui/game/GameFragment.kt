package battleships.esa.ffhs.ch.ui.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import battleships.esa.ffhs.ch.R
import battleships.esa.ffhs.ch.model.GameState
import battleships.esa.ffhs.ch.ui.main.MainActivity
import battleships.esa.ffhs.ch.ui.main.MainActivity.Companion.activeGame
import battleships.esa.ffhs.ch.ui.viewmodel.BoardMineViewModel
import battleships.esa.ffhs.ch.ui.viewmodel.BoardOpponentViewModel


class GameFragment : Fragment() {

    lateinit var myBoard: BoardMineViewModel
    lateinit var opponentBoard: BoardOpponentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        opponentBoard = activeGame!!.opponentBoard
        myBoard = activeGame!!.myBoard
        return inflater.inflate(R.layout.game_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // initialize child fragments depending on 'do we know you already'
        initBoardFragment()
    }


    // check if the current game is ready to play or if the 'game preparation area' should be loaded
    private fun initBoardFragment() {
        if ((activity as MainActivity).findViewById<View>(R.id.game_fragment_container) != null) {
            if (activeGame!!.data.state != GameState.ACTIVE) {
                childFragmentManager.beginTransaction()
                    .replace(R.id.game_fragment_container, GamePreparationFragment(), "prep")
                    .commit()
            } else {
                switchToGameFragment()
            }
        }
    }

    // will be accessed as well from child fragment to start a game (GamePreparationFragment to GameActiveFragment)
    fun switchToGameFragment() {
        childFragmentManager.beginTransaction()
            .replace(R.id.game_fragment_container, GameActiveFragment(), "game").commit()
    }
}
