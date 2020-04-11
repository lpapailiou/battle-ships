package battleships.esa.ffhs.ch.ui.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import battleships.esa.ffhs.ch.R
import battleships.esa.ffhs.ch.entity.GameDao
import battleships.esa.ffhs.ch.entity.InjectorUtils
import battleships.esa.ffhs.ch.model.GameState
import battleships.esa.ffhs.ch.ui.drawable.CustomDialog
import battleships.esa.ffhs.ch.ui.main.MainActivity
import battleships.esa.ffhs.ch.ui.viewmodel.BoardMineViewModel
import battleships.esa.ffhs.ch.ui.viewmodel.BoardOpponentViewModel
import battleships.esa.ffhs.ch.ui.viewmodel.GameListViewModel


class GameFragment : Fragment() {

    companion object {
        lateinit var currentGame: GameDao
    }

    lateinit var myBoard: BoardMineViewModel
    lateinit var opponentBoard: BoardOpponentViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val factory = InjectorUtils.provideGameViewModelFactory()
        val viewModel = ViewModelProviders.of(this, factory).get(GameListViewModel::class.java)
        // TODO: move to function
        if (viewModel.getActiveGame().value == null) {
            currentGame = GameDao()
            viewModel.addGame(currentGame)
        } else {
            currentGame = viewModel.getActiveGame().value!!
        }
        viewModel.getActiveGame().observe(viewLifecycleOwner, Observer { game ->
            // is not working because game state is not live data and nested, so no notification on changes
            if (game.getState() == GameState.ENDED) {
                println("game ENDS ================================================")
                (activity as MainActivity).vibrate()
                CustomDialog().showEndGameDialog(context!!, currentGame.getResult().value!!)
            }
        })
        /* TODO: make work
        val observedGame: MutableLiveData<GameInstance> = currentGame as MutableLiveData<GameInstance>
        observedGame.observe(viewLifecycleOwner, Observer { game ->
            if (currentGame.data.state == GameState.ENDED) {
                println("game ENDS ================================================")
                (activity as MainActivity).vibrate()
                CustomDialog().showEndGameDialog(context!!, currentGame.data.result)
            }
        })*/


        opponentBoard = currentGame.getOpponentBoard().value!!
        myBoard = currentGame.getMyBoard().value!!
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
            if (currentGame.getState() != GameState.ACTIVE) {
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
