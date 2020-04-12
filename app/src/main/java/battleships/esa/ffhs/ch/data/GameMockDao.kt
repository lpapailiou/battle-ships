package battleships.esa.ffhs.ch.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import battleships.esa.ffhs.ch.model.GameState
import battleships.esa.ffhs.ch.model.WON_GAME_VALUE
import battleships.esa.ffhs.ch.ui.viewmodel.BoardMineViewModel
import battleships.esa.ffhs.ch.ui.viewmodel.BoardOpponentViewModel
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class GameMockDao { // Dao = Data Access Object

    private val id = MutableLiveData<Timestamp>()
    private var lastChange = MutableLiveData<String>()
    private var state = MutableLiveData<GameState>()
    private var result = MutableLiveData<Int>()
    private var opponentBoard = MutableLiveData<BoardOpponentViewModel>()
    private var myBoard = MutableLiveData<BoardMineViewModel>()

    private var isCurrentGame = MutableLiveData<Boolean>()
    private var isMyBoardVisible = MutableLiveData<Boolean>()
    private var isMyTurn = MutableLiveData<Boolean>()
    private var connectedGame = MutableLiveData<GameMockDao>()
    private var opponentName = MutableLiveData<String>()

    init {
        id.value = Timestamp(System.currentTimeMillis())
        state.value = GameState.PREPARATION
        result.value = 0          // 1 for win, 0 for unclear/loss

        opponentBoard.value = BoardOpponentViewModel(this)
        myBoard.value = BoardMineViewModel(this)
        isCurrentGame.value = true
        isMyBoardVisible.value = true
        opponentName.value = "Bot"

        updateModificationDate()
    }

    fun getId() = id as LiveData<Timestamp>
    fun getLastChange() = lastChange as LiveData<String>
    fun getState() = state as LiveData<GameState>
    fun getResult() = result as LiveData<Int>
    fun getOpponentBoard() = opponentBoard as LiveData<BoardOpponentViewModel>
    fun getMyBoard() = myBoard as LiveData<BoardMineViewModel>
    fun isActive() = isCurrentGame as LiveData<Boolean>
    fun isMyBoardVisible() = isMyBoardVisible as LiveData<Boolean>
    fun getOpponentName() = opponentName.value

    fun setState(newState: GameState) {
        state.value = newState
        updateModificationDate()
    }

    fun setResult(newResult: Int) {
        result.value = newResult
        updateModificationDate()
    }

    fun setActive(isNowActive: Boolean) {
        isCurrentGame.value = isNowActive
        updateModificationDate()
    }

    fun setMyBoardVisible(isMyBoard: Boolean) {
        isMyBoardVisible.value = isMyBoard
        updateModificationDate()
    }

    private fun updateModificationDate() {
        lastChange.value = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())
    }


    // TODO: move to gameViewModel

    fun printActive(): String {
        var gameState: String = ""
        if (state.value == GameState.PREPARATION) {
            gameState = ", preparing"
        }
        if (isCurrentGame.value!!) {
            gameState = gameState + " (CURRENT)"
        }
        return (print() + "" + gameState)
    }

    fun printScore(): String {
        return (print() + ": " + (result.value!! * WON_GAME_VALUE))
    }

    fun print(): String {
        return ("" + lastChange.value + ": vs. " + opponentName.value)
    }

}