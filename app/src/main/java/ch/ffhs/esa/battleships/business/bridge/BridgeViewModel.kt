package ch.ffhs.esa.battleships.business.bridge

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.ffhs.esa.battleships.data.game.GameRepository
import ch.ffhs.esa.battleships.data.game.GameWithPlayerInfo
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch
import javax.inject.Inject

class BridgeViewModel @Inject constructor(
    private val gameRepository: GameRepository
) : ViewModel() {

    private val _activeGames =
        MutableLiveData<List<GameWithPlayerInfo>>().apply { value = emptyList() }
    val activeGames: LiveData<List<GameWithPlayerInfo>> = _activeGames

    private var job: Job? = null

    fun start(playerUid: String) {
        if (job != null) {
            return
        }

        job = observeActiveGamesFromPlayer(playerUid)
    }

    @OptIn(InternalCoroutinesApi::class)
    private fun observeActiveGamesFromPlayer(playerUid: String) = viewModelScope.launch {
        val flow = gameRepository.observeActiveGamesFromPlayer(playerUid)
        flow.collect(object : FlowCollector<List<GameWithPlayerInfo>> {
            override suspend fun emit(value: List<GameWithPlayerInfo>) {
                _activeGames.value = value
            }
        })
    }

    override fun onCleared() {
        super.onCleared()

        job?.cancel()
    }
}
