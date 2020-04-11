package battleships.esa.ffhs.ch.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import battleships.esa.ffhs.ch.entity.GameDao
import battleships.esa.ffhs.ch.model.GameState

class GameViewModel(gameDao: MutableLiveData<GameDao>): ViewModel() {

    private var data: MutableLiveData<GameDao>

    init {
        data = gameDao
    }

    fun getOpponentBoard(): BoardOpponentViewModel {
        return data.value!!.getOpponentBoard().value!!
    }
    fun getMyBoard(): BoardMineViewModel {
        return data.value!!.getMyBoard().value!!
    }

    fun equalsState(state: GameState): Boolean {
        return data.value!!.getState().value!! == state
    }

    fun notEqualsState(state: GameState): Boolean {
        return !equalsState(state)
    }

    fun setState(state: GameState) {
        data.value!!.setState(state)
    }

    fun isMyTurn(): Boolean {
        return data.value!!.isMyTurn().value!!
    }

    fun setMyTurn(isMyTurnNow: Boolean) {
        data.value!!.setMyTurn(isMyTurnNow)
    }

    fun setActive(active: Boolean) {
        data.value!!.setActive(active)
    }

    fun isActive(): Boolean {
        return data.value!!.isActive().value!!
    }

    fun getObservableState(): LiveData<GameState> {
        return data.value!!.getState()
    }

    fun getResult(): Int {
        return data.value!!.getResult().value!!
    }

}