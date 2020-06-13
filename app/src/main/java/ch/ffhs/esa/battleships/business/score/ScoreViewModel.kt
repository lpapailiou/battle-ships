package ch.ffhs.esa.battleships.business.score

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.ffhs.esa.battleships.business.OFFLINE_PLAYER_ID
import ch.ffhs.esa.battleships.business.WINNING_SCORE_MULTIPLIER
import ch.ffhs.esa.battleships.data.DataResult
import ch.ffhs.esa.battleships.data.game.GameRepository
import ch.ffhs.esa.battleships.data.game.GameWithPlayerInfo
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import javax.inject.Inject

class ScoreViewModel @Inject constructor(
    private val gameRepository: GameRepository
) : ViewModel() {

    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int> = _score
    private val _botScore = MutableLiveData<Int>()
    val botScore: LiveData<Int> = _botScore
    private val _closedGames =
        MutableLiveData<List<GameWithPlayerInfo>>().apply { value = emptyList() }
    val closedGames: LiveData<List<GameWithPlayerInfo>> = _closedGames

    fun start(playerUid: String) = viewModelScope.launch {
        try {
            Log.d("procedureLogger", "------------- >>>>>>> score start()")
            loadClosedGamesFromPlayer(playerUid)
        } catch (e: Exception) {
            println(e.stackTrace.toString())
        }
    }

    private fun loadClosedGamesFromPlayer(playerUid: String) = viewModelScope.launch {
        try {
            Log.d("procedureLogger", "------------- >>>>>>> bridge loadActiveGamesFromPlayer()")
            val result = gameRepository.findClosedGamesFromPlayer(playerUid)

            if (result is DataResult.Success) {
                println("player id is --------------------------------------" + playerUid)
                if (playerUid == OFFLINE_PLAYER_ID) {
                    _botScore.value = (result.data.filter { it.winnerUid == playerUid }.size ?: 0) * WINNING_SCORE_MULTIPLIER
                } else {
                    _score.value = (result.data.filter { it.winnerUid == playerUid }.size ?: 0) * WINNING_SCORE_MULTIPLIER
                }
                _closedGames.value = result.data
            } else {
                _botScore.value = 0
                _score.value = 0
            }

            if (playerUid != OFFLINE_PLAYER_ID) {
                val localResult = gameRepository.findClosedGamesFromPlayer(OFFLINE_PLAYER_ID)

                if (localResult is DataResult.Success) {
                    println("....................................................." + localResult.data.size)
                    _botScore.value = (localResult.data.filter { it.winnerUid == OFFLINE_PLAYER_ID }.size ?: 0) * WINNING_SCORE_MULTIPLIER
                    println("....................................................." + (localResult.data.filter { it.winnerUid == OFFLINE_PLAYER_ID }.size))
                    if (_closedGames.value == null) {
                        _closedGames.value = localResult.data
                    } else {
                        _closedGames.value = _closedGames.value!! + localResult.data
                    }
                } else {
                    println("------------------------------------------------------------------ NO SUCCESS")
                    _botScore.value = 0
                }
            }

        } catch (e: Exception) {
            println(e.stackTrace.toString())
        }
    }
}
