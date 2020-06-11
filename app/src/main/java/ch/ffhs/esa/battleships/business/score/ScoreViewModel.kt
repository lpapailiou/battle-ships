package ch.ffhs.esa.battleships.business.score

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.ffhs.esa.battleships.data.DataResult
import ch.ffhs.esa.battleships.data.game.GameRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import javax.inject.Inject

class ScoreViewModel @Inject constructor(
    private val gameRepository: GameRepository
) : ViewModel() {

    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int> = _score

    fun start(playerUid: String) = viewModelScope.launch {
        try {
            Log.d("procedureLogger", "------------- >>>>>>> score start()")
            loadGames(playerUid)
        } catch (e: Exception) {
            println(e.stackTrace.toString())
        }
    }

    private suspend fun loadGames(playerUid: String) {
        Log.e("score VM", "before repo call")
        val result = gameRepository.findAllGamesByPlayer(playerUid)
        Log.e("score VM", "after repo call")
        if (result is DataResult.Success) {
            result.data
            val score = result.data.filter { it.winnerUid == playerUid }.size
            _score.value = score
        }
    }
}
