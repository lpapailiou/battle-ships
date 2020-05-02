package battleships.esa.ffhs.ch.refactored.business.bridge

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import battleships.esa.ffhs.ch.refactored.data.DataResult
import battleships.esa.ffhs.ch.refactored.data.game.Game
import battleships.esa.ffhs.ch.refactored.data.game.GameRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class BridgeViewModel @Inject constructor(
    private val gameRepository: GameRepository
) : ViewModel() {

    private val _activeGames = MutableLiveData<List<Game>>()
    val activeGames: LiveData<List<Game>> = _activeGames

    fun start() {
        loadGames()
    }

    private fun loadGames() = viewModelScope.launch {
        val result = gameRepository.findActiveGames()
        if (result is DataResult.Success) {
            _activeGames.value = result.data
        }
    }
}
