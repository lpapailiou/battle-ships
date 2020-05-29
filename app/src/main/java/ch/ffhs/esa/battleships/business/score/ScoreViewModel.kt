package ch.ffhs.esa.battleships.business.score

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.ffhs.esa.battleships.business.board.BoardModel
import ch.ffhs.esa.battleships.business.ship.DirectionLogic
import ch.ffhs.esa.battleships.data.DataResult
import ch.ffhs.esa.battleships.data.board.BoardRepository
import ch.ffhs.esa.battleships.data.game.GameRepository
import ch.ffhs.esa.battleships.data.player.PlayerRepository
import ch.ffhs.esa.battleships.data.ship.ShipRepository
import ch.ffhs.esa.battleships.data.shot.ShotRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class ScoreViewModel @Inject constructor(

    private val gameRepository: GameRepository

) : ViewModel() {

    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int> = _score

    fun start(playerUid: String) = viewModelScope.launch {
        val result = gameRepository.findAllGamesByPlayer(playerUid)
        if (result is DataResult.Success){
            result.data
            val score = result.data.filter { it.winnerUid == playerUid }.size
            _score.value = score
        }
    }
}
