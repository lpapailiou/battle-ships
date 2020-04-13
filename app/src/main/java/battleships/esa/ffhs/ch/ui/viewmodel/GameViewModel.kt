package battleships.esa.ffhs.ch.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import battleships.esa.ffhs.ch.database.GameRepository
import battleships.esa.ffhs.ch.entity.BoardEntity
import battleships.esa.ffhs.ch.entity.GameEntity
import battleships.esa.ffhs.ch.model.GameState
import battleships.esa.ffhs.ch.model.WON_GAME_VALUE
import battleships.esa.ffhs.ch.ui.main.MainActivity.Companion.mainViewModel

class GameViewModel(gameEntity: LiveData<GameEntity>): ViewModel() {

    var data: MutableLiveData<GameEntity> = MutableLiveData<GameEntity>()
    private var opponentBoard: BoardOpponentViewModel
    private var myBoard: BoardMineViewModel



    init {
        data.value = gameEntity.value
        opponentBoard = BoardOpponentViewModel(this)
        myBoard = BoardMineViewModel(this)

    }

    fun getOpponentBoard(): BoardOpponentViewModel {
        return opponentBoard
    }
    fun getMyBoard(): BoardMineViewModel {
        return myBoard
    }

    fun equalsState(state: GameState): Boolean {
        return data.value!!.state == state
    }

    fun notEqualsState(state: GameState): Boolean {
        return !equalsState(state)
    }

    fun setState(state: GameState) {
        var updatedGame = data
        updatedGame.value!!.state = state
        data = updatedGame
    }

    fun isMyBoardVisible(): Boolean {
        return data.value!!.isMyBoardVisible
    }

    fun setMyBoardVisible(isMyBoard: Boolean) {
        var updatedGame = data
        updatedGame.value!!.isMyBoardVisible = isMyBoard
        data = updatedGame
    }

    fun setActive(active: Boolean) {
        var updatedGame = data
        updatedGame.value!!.isCurrentGame = active
        data = updatedGame
    }

    fun getObservableState(): LiveData<GameState> {
        val liveState = MutableLiveData<GameState>()
        liveState.value = data.value!!.state
        return liveState
    }

    fun getResult(): Int {
        return data.value!!.result
    }




}