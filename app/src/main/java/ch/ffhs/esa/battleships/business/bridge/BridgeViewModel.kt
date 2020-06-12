package ch.ffhs.esa.battleships.business.bridge

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.ffhs.esa.battleships.business.BOT_PLAYER_ID
import ch.ffhs.esa.battleships.business.OFFLINE_PLAYER_ID
import ch.ffhs.esa.battleships.data.DataResult
import ch.ffhs.esa.battleships.data.game.GameRepository
import ch.ffhs.esa.battleships.data.game.GameWithPlayerInfo
import kotlinx.coroutines.CancellationException
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
        try {
            Log.d("procedureLogger", "------------- >>>>>>> bridge loadActiveGamesFromPlayer()")
            val result = gameRepository.findActiveGamesFromPlayer(playerUid)

            if (result is DataResult.Success) {
                _activeGames.value = result.data
            }

            if (playerUid != OFFLINE_PLAYER_ID) {
                val localResult = gameRepository.findActiveGamesFromPlayer(OFFLINE_PLAYER_ID)
                if (localResult is DataResult.Success) {
                    if (_activeGames.value == null) {
                        _activeGames.value = localResult.data
                    } else {
                        _activeGames.value = _activeGames.value!! + localResult.data
                    }
                }
            }

        } catch (e: Exception) {
            println(e.stackTrace.toString())
        }
    }
}
