package ch.ffhs.esa.battleships.business.score

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.ffhs.esa.battleships.business.ship.DirectionLogic
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

    fun start(playerUid: String) = viewModelScope.launch {
        gameRepository.findAllGamesByPlayer(playerUid)

    }


}
