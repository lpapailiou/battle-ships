package battleships.esa.ffhs.ch.ui.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import battleships.esa.ffhs.ch.R
import battleships.esa.ffhs.ch.entity.GameEntity
import battleships.esa.ffhs.ch.model.GameState
import battleships.esa.ffhs.ch.ui.drawable.CustomDialog
import battleships.esa.ffhs.ch.ui.main.MainActivity
import battleships.esa.ffhs.ch.ui.main.MainActivity.Companion.gameListViewModel
import battleships.esa.ffhs.ch.ui.viewmodel.BoardMineViewModel
import battleships.esa.ffhs.ch.ui.viewmodel.BoardOpponentViewModel
import battleships.esa.ffhs.ch.ui.viewmodel.GameListViewModel
import battleships.esa.ffhs.ch.ui.viewmodel.GameViewModel


class GameFragment : Fragment() {

    companion object {
        lateinit var currentGame: GameViewModel
    }

    private var updateDatabase: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    var gameToUpdate: GameEntity? = null
    lateinit var myBoard: BoardMineViewModel
    lateinit var opponentBoard: BoardOpponentViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        currentGame = gameListViewModel.getCurrentGameAsViewModel()
        gameToUpdate = currentGame.data.value
        var updateNow = MutableLiveData<Boolean>()
        updateNow.value = true
        updateDatabase = updateNow
        currentGame.getObservableState().observe(viewLifecycleOwner, Observer { state ->
            if (currentGame.equalsState(GameState.ENDED)) {
                currentGame.setActive(false)
                (activity as MainActivity).vibrate()
                CustomDialog().showEndGameDialog(context!!, currentGame.getResult())
            }
        })

        opponentBoard = currentGame.getOpponentBoard()
        myBoard = currentGame.getMyBoard()
        return inflater.inflate(R.layout.game_fragment, container, false)
    }

    suspend fun updateRepo(game: GameEntity?) {
        if (game != null) {
            gameListViewModel.repository.insert(game)
            updateDatabase.value = false
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // initialize child fragments depending on 'are you already logged in'
        initBoardFragment()
    }


    // check if the current game is ready to play or if the 'game preparation area' should be loaded
    private fun initBoardFragment() {
        if ((activity as MainActivity).findViewById<View>(R.id.game_fragment_container) != null) {
            if (currentGame.notEqualsState(GameState.ACTIVE)) {
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
