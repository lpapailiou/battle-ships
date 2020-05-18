package ch.ffhs.esa.battleships.business.bridge

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.ffhs.esa.battleships.data.DataResult
import ch.ffhs.esa.battleships.data.game.GameRepository
import ch.ffhs.esa.battleships.data.game.GameWithPlayerInfo
import kotlinx.coroutines.launch
import javax.inject.Inject

class BridgeViewModel @Inject constructor(
    private val gameRepository: GameRepository
) : ViewModel() {

    private val _activeGames = MutableLiveData<List<GameWithPlayerInfo>>()
    val activeGames: LiveData<List<GameWithPlayerInfo>> = _activeGames

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
