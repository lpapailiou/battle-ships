package battleships.esa.ffhs.ch.entity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import battleships.esa.ffhs.ch.model.Game
import battleships.esa.ffhs.ch.model.GameState
import battleships.esa.ffhs.ch.ui.viewmodel.BoardMineViewModel
import battleships.esa.ffhs.ch.ui.viewmodel.BoardOpponentViewModel
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class GameDao {

    private val id = MutableLiveData<Timestamp>()
    private var lastChange = MutableLiveData<String>()
    private var state = MutableLiveData<GameState>()
    private var result = MutableLiveData<Int>()

    private var isCurrentGame = MutableLiveData<Boolean>()
    private var opponentBoard: MutableLiveData<BoardOpponentViewModel>()
    private var myBoard: MutableLiveData<BoardMineViewModel>()
    private var active = MutableLiveData<Boolean>()

    private var connectedGame = MutableLiveData<GameDao>()
    private var opponentName = MutableLiveData<String>()

    init {
        id.value = Timestamp(System.currentTimeMillis())
        state.value = GameState.PREPARATION
        result.value = 0          // 1 for win, 0 for unclear/loss

        isCurrentGame.value = true

        // opponentBoard.value = BoardOpponentViewModel(this) TODO: refactor to use gameDao
        // myBoard.value = BoardMineViewModel(this)
        active.value = true
        opponentName.value = "Bot"

        updateModificationDate()
    }

    fun getId() = id as LiveData<Timestamp>
    fun getLastChange() = lastChange as LiveData<String>
    fun getState() = state as LiveData<GameState>
    fun getResult() = result as LiveData<Int>
    fun isActive() = active as LiveData<Boolean>


    fun setState(newState: GameState) {
        state.value = newState
        updateModificationDate()
    }

    fun setResult(newResult: Int) {
        result.value = newResult
        updateModificationDate()
    }

    fun setActive(isNowActive: Boolean) {
        active.value = isNowActive
        updateModificationDate()
    }

    private fun updateModificationDate() {
        lastChange.value = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())
    }

}