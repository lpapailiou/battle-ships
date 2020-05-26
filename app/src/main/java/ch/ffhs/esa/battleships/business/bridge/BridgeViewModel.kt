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

    private val _activeGames =
        MutableLiveData<List<GameWithPlayerInfo>>().apply { value = emptyList() }
    val activeGames: LiveData<List<GameWithPlayerInfo>> = _activeGames

    fun start(playerUid: String) {
        loadActiveGamesFromPlayer(playerUid)
    }

    private fun loadActiveGamesFromPlayer(playerUid: String) = viewModelScope.launch {
        val result = gameRepository.findActiveGamesFromPlayer(playerUid)
        if (result is DataResult.Success) {
            _activeGames.value = result.data
        }
    }

    fun resumeGame(game: GameWithPlayerInfo) {

    }
}
